package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;
import dmax.dialog.SpotsDialog;
import java.util.ArrayList;
import java.util.List;

public class AddAppointmentActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private TextView tvToolbarTitle;
  private ImageView ivBack;


  private StepView stepView;
  private NonSwipeViewPager viewPager;
  private Button btnPrev, btnNext;

  private AlertDialog dialog;
  CollectionReference availableDocRef;



  LocalBroadcastManager localBroadcastManager;

  private BroadcastReceiver btnNextReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {

      int step = intent.getIntExtra(Common.KEY_STEP, 0);
      if (step == 1)
        Common.currentDocType = intent.getParcelableExtra(Common.KEY_DOC_TYPE);
      else if (step == 2)
        Common.currentDoctor = intent.getParcelableExtra(Common.KEY_DOCTOR_SELECTED);
      else if (step == 3)
        Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);

      btnNext.setEnabled(true);
      setButtonColor();

    }
  };


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_appointment);


    initToolbar();
    initFirebase();
    initViews();
    initActionListeners();
    initStepView();
    initViewPager();
    setButtonColor();

    localBroadcastManager = LocalBroadcastManager.getInstance(this);
    localBroadcastManager.registerReceiver(btnNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));


  }

  @Override
  protected void onDestroy() {
    localBroadcastManager.unregisterReceiver(btnNextReceiver);
    super.onDestroy();
  }

  private void initToolbar() {
    toolbar = findViewById(R.id.add_appointment_tool_bar);
    tvToolbarTitle = findViewById(R.id.add_appointment_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Add Appointment");
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void initFirebase() {

  }

  private void initViews() {

    dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

    stepView = findViewById(R.id.add_appointment_step_view);
    viewPager = findViewById(R.id.add_appointment_view_pager);
    btnPrev = findViewById(R.id.add_appointment_btn_prev);
    btnNext = findViewById(R.id.add_appointment_btn_next);

    ivBack = findViewById(R.id.add_appointment_toolbar_iv_back);


  }

  private void initActionListeners() {
    btnPrev.setOnClickListener(this);
    btnNext.setOnClickListener(this);
    ivBack.setOnClickListener(this);
  }

  private void initStepView() {
    List<String> stepList = new ArrayList<>();
    stepList.add("Location");
    stepList.add("Doctor");
    stepList.add("Time");
    stepList.add("Confirm");
    stepView.setSteps(stepList);
  }

  private void initViewPager() {
    AppointmentPagerAdapter adapter = new AppointmentPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(4);
    viewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int i, float v, int i1) {

      }

      @Override
      public void onPageSelected(int i) {

        stepView.go(i, true);

          if (i == 0)
          {
            btnPrev.setEnabled(false);
          }
          else
          {
            btnPrev.setEnabled(true);
          }
          btnNext.setEnabled(false);
          setButtonColor();
      }

      @Override
      public void onPageScrollStateChanged(int i) {

      }
    });

  }

  private void setButtonColor() {
      if (btnNext.isEnabled())
      {
        btnNext.setBackgroundResource(R.color.colorBlue);
      }
      else
      {
        btnNext.setBackgroundResource(R.color.colorDarkGrey);
      }

    if (btnPrev.isEnabled())
    {
      btnPrev.setBackgroundResource(R.color.colorBlue);
    }
    else
    {
      btnPrev.setBackgroundResource(R.color.colorDarkGrey);
    }

  }

  @Override
  public void onClick(View v) {
      switch (v.getId())
      {
        case R.id.add_appointment_btn_prev:
        {
          onPrevButtonClicked();
          break;
        }

        case R.id.add_appointment_btn_next:
        {
          onNextButtonClicked();
          break;
        }

        case R.id.add_appointment_toolbar_iv_back:
        {
          startActivity(new Intent(AddAppointmentActivity.this, AppointmentsActivity.class));
          finish();
          break;
        }
      }
  }

  private void onPrevButtonClicked() {
    if (Common.step == 3 ||  Common.step > 0)
    {
       Common.step--;
       viewPager.setCurrentItem(Common.step);
       if (Common.step < 3)
       {
         btnNext.setEnabled(true);
         setButtonColor();
       }
    }
  }

  private void onNextButtonClicked() {

    if (Common.step < 3 || Common.step == 0)
    {
      Common.step++;
      if (Common.step == 1)   //after choosing doc type
      {
          if (Common.currentDocType != null)
          {
              loadDoctorsByType(Common.currentDocType.getId());
          }

      }
      else if (Common.step == 2)    // pick time slot
      {
        if (Common.currentDoctor != null)
        {
          loadTimeSlotOfDoctor(Common.currentDoctor.getId());
        }
      }

      else if (Common.step == 3)    // confirm appointment
      {
        if (Common.currentTimeSlot != -1)
        {
          confirmAppointmentBooking();
        }
      }

      viewPager.setCurrentItem(Common.step);
    }

  }

  private void confirmAppointmentBooking() {
    Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
    localBroadcastManager.sendBroadcast(intent);
  }

  private void loadTimeSlotOfDoctor(String docID) {
    // Send local broadcast to fragment 3
    Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
    localBroadcastManager.sendBroadcast(intent);
  }

  private void loadDoctorsByType(String id) {
    dialog.show();

    if (!TextUtils.isEmpty(Common.city))
    {
      availableDocRef = FirebaseFirestore.getInstance()
          .collection("AllDoctors")
          .document(Common.city).collection("DocType")
          .document(id).collection("AvailableDoctors");


      availableDocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful())
          {
              ArrayList<Doctor> availableDocList = new ArrayList<>();
              for (QueryDocumentSnapshot documentSnapshot:task.getResult())
              {
                Doctor doctor = documentSnapshot.toObject(Doctor.class);
                doctor.setId(documentSnapshot.getId());
                doctor.setPassword("");     // remove password as this is client side app
                availableDocList.add(doctor);
              }

              // send broadcast to bookAppoint2 Fragment
            Intent intent = new Intent(Common.KEY_AVAILABLE_DOC_LIST_LOAD_DONE);
            intent.putParcelableArrayListExtra(Common.KEY_AVAILABLE_DOC_LIST_LOAD_DONE, availableDocList);
            localBroadcastManager.sendBroadcast(intent);
            dialog.dismiss();

          }


        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          dialog.dismiss();
          Utils.makeToast(AddAppointmentActivity.this, e.getMessage());
        }
      });

    }





  }
}
