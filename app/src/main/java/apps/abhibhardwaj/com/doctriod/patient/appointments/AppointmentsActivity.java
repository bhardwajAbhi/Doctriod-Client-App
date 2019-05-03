package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.models.User;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import dmax.dialog.SpotsDialog;
import java.util.ArrayList;

public class AppointmentsActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private TextView tvToolbarTitle;
  private ImageView ivBack;

  private AlertDialog dialog;
  private Button btnAddAppointment;
  private RecyclerView recyclerView;
  private LinearLayout placeHolder;
  private ArrayList<AppointmentInfo> appointmentList;

 private AppointmentListRecyclerAdapter adapter;

  private FirebaseAuth auth;
  private FirebaseUser currentUser;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_appointments);

    initFirebase();
    initToolbar();
    initViews();
    fetchAppointments();

  }

  private void initFirebase() {
    auth = FirebaseAuth.getInstance();
    currentUser = auth.getCurrentUser();
  }

  private void initToolbar() {
    toolbar = findViewById(R.id.appointment_tool_bar);
    tvToolbarTitle = findViewById(R.id.appointment_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Appointments");
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void initViews() {
    appointmentList = new ArrayList<>();

    dialog = new SpotsDialog.Builder().setContext(this).setCancelable(true).build();
    placeHolder = findViewById(R.id.layout_linear_appointment_empty_view);
    recyclerView = findViewById(R.id.appointment_recycler_view);

    ivBack = findViewById(R.id.appointment_toolbar_iv_back);
    ivBack.setOnClickListener(this);

    btnAddAppointment = findViewById(R.id.appointment_btn_add_appointment);
    btnAddAppointment.setOnClickListener(this);
  }

  private void fetchAppointments() {
    CollectionReference appointmentDate = FirebaseFirestore.getInstance()
        .collection("users")
        .document(currentUser.getUid())
        .collection("appointments");

    appointmentDate.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful())
        {
          for (QueryDocumentSnapshot documentSnapshot : task.getResult())
          {
            AppointmentInfo info = documentSnapshot.toObject(AppointmentInfo.class);
            appointmentList.add(info);
          }
          loadAppointmentList();
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Utils.makeToast(AppointmentsActivity.this, e.getMessage());
      }
    });

  }

  private void loadAppointmentList() {
    if (appointmentList.isEmpty())
    {
      placeHolder.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    }
    else
    {
      placeHolder.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);

      adapter = new AppointmentListRecyclerAdapter(AppointmentsActivity.this, appointmentList, currentUser.getUid());
      recyclerView.setAdapter(adapter);
      DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
          LinearLayout.VERTICAL);
      recyclerView.addItemDecoration(dividerItemDecoration);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    loadAppointmentList();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId())
    {
      case R.id.appointment_btn_add_appointment:
      {
        launchAddAppointmentActivity();
        break;
      }
      case R.id.appointment_toolbar_iv_back:
      {
        startActivity(new Intent(AppointmentsActivity.this, HomeActivity.class));
        finish();
        break;
      }
    }
  }

  private void launchAddAppointmentActivity() {
    startActivity(new Intent(AppointmentsActivity.this, AddAppointmentActivity.class));
  }
}
