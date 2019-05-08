package apps.abhibhardwaj.com.doctriod.patient.recognizemeds;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import dmax.dialog.SpotsDialog;
import java.util.ArrayList;

public class SearchMedicineActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private ImageView ivBack;
  private TextView tvToolbarTitle;


  private AutoCompleteTextView tvInput;
  private ImageView ivSearchBtn;
  private ImageButton ivToggle;
  private FloatingActionButton fab;
  private TextView tvMedName, tvMedUse, tvMedManufacturer, tvMedPrice, tvMedQuantity, tvMedContains, tvMedSchedule;
  private Button btnUse, btnSideEffects, btnPrecautions, btnHowWork, btnDiet, btnSubstitute;
  private LinearLayout medInfoContainerLayout, emptyView;

  private BottomSheetBehavior bottomSheetBehavior;
  private TextView tvMedInformationContainer;

  private CollectionReference allMedRef;
  private AlertDialog dialog;
  private ArrayList<String> allMedNameList;
  private Medicine medicine = null;





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_medicine);

    initToolbar();
    initBottomSheet();
    initViews();
    initListeners();
    initFireStore();

    lookForRecognizedText();


  }

  private void lookForRecognizedText() {
    String recognizedText = getIntent().getStringExtra("recognizedText");
    tvInput.setText(recognizedText);
  }

  private void initFireStore() {
    dialog.show();
    allMedRef = FirebaseFirestore.getInstance().collection("allMedicine");
    allMedRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful())
          {
             for (QueryDocumentSnapshot documentSnapshot : task.getResult())
             {
               allMedNameList.add(documentSnapshot.getId());
             }
            initAutoCompleteTextView();
          }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        dialog.dismiss();
        Utils.makeToast(SearchMedicineActivity.this, e.getMessage());
      }
    });
  }

  private void initAutoCompleteTextView() {
    String[] language ={"C","C++","Java",".NET","iPhone","Android","ASP.NET","PHP"};
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        this, android.R.layout.simple_dropdown_item_1line, allMedNameList);
    Log.d("SearchMed", "initAutoCompleteTextView: " + allMedNameList);
    tvInput.setAdapter(adapter);
    tvInput.setThreshold(1);
    dialog.dismiss();

  }

  private void initToolbar() {
    toolbar = findViewById(R.id.searchmed_tool_bar);
    tvToolbarTitle = findViewById(R.id.searchmed_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Search Medicine");
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void initBottomSheet() {
    View bottomSheetView = findViewById(R.id.search_med_bottom_sheet);
    bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    bottomSheetBehavior.setHideable(false);

    tvMedInformationContainer = bottomSheetView.findViewById(R.id.btm_sheet_tv_med_information_holder);
    ivToggle = bottomSheetView.findViewById(R.id.btm_sheet_btn_toggle);


  }

  private void initViews() {

    allMedNameList = new ArrayList<>();

    fab = findViewById(R.id.search_med_fab);

    tvInput = findViewById(R.id.tv_input);
    ivSearchBtn = findViewById(R.id.searchmed_iv_search);
    ivBack = findViewById(R.id.searchmed_toolbar_iv_back);

    tvMedName = findViewById(R.id.searchmed_tv_medname);
    tvMedUse = findViewById(R.id.searchmed_tv_meduse);
    tvMedManufacturer = findViewById(R.id.searchmed_tv_medmanufacturer);
    tvMedPrice = findViewById(R.id.searchmed_tv_medprice);
    tvMedQuantity = findViewById(R.id.searchmed_tv_medquantity);
    tvMedSchedule = findViewById(R.id.searchmed_tv_medschedule);
    tvMedContains = findViewById(R.id.searchmed_tv_medcontains);

    btnUse = findViewById(R.id.searchmed_btn_med_use);
    btnSideEffects = findViewById(R.id.searchmed_btn_med_side_effect);
    btnPrecautions = findViewById(R.id.searchmed_btn_med_precaution);
    btnHowWork = findViewById(R.id.searchmed_btn_med_work);
    btnDiet = findViewById(R.id.searchmed_btn_med_diet);
    btnSubstitute = findViewById(R.id.searchmed_btn_med_substitute);

    medInfoContainerLayout = findViewById(R.id.searchmed_info_holder_main);
    emptyView = findViewById(R.id.search_med_empty_view);

    dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

  }

  private void initListeners() {
    ivSearchBtn.setOnClickListener(this);
    btnUse.setOnClickListener(this);
    btnSideEffects.setOnClickListener(this);
    btnPrecautions.setOnClickListener(this);
    btnHowWork.setOnClickListener(this);
    btnDiet.setOnClickListener(this);
    btnSubstitute.setOnClickListener(this);
    ivBack.setOnClickListener(this);
    fab.setOnClickListener(this);

    bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View view, int newState) {
          if (newState == BottomSheetBehavior.STATE_EXPANDED)
            ivToggle.setImageResource(R.drawable.ic_arrow_down);
          else
            ivToggle.setImageResource(R.drawable.ic_arrow_up);
      }

      @Override
      public void onSlide(@NonNull View view, float v) {

      }
    });

  }


  @Override
  public void onClick(View v) {

    switch (v.getId())
    {

      case R.id.searchmed_toolbar_iv_back:
      {
        startActivity(new Intent(SearchMedicineActivity.this, HomeActivity.class));
        finish();
        break;
      }

      case R.id.search_med_fab:
      {
        startActivity(new Intent(SearchMedicineActivity.this, RecognizeMedsActivity.class));
        break;
      }



      case R.id.searchmed_iv_search:
      {
          getInputAndLoadResult();
          break;
      }

      case R.id.searchmed_btn_med_use:
      {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tvMedInformationContainer.setText(medicine.getUses());
        break;
      }

      case R.id.searchmed_btn_med_side_effect:
      {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tvMedInformationContainer.setText(medicine.getSideffect());
        break;
      }

      case R.id.searchmed_btn_med_precaution:
      {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tvMedInformationContainer.setText(medicine.getPrecautions());
        break;
      }

      case R.id.searchmed_btn_med_work:
      {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tvMedInformationContainer.setText(medicine.getWork());
        break;
      }

      case R.id.searchmed_btn_med_diet:
      {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tvMedInformationContainer.setText(medicine.getDiet());
        break;
      }

      case R.id.searchmed_btn_med_substitute:
      {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tvMedInformationContainer.setText("No information available");
        break;
      }


    }


  }

  private void getInputAndLoadResult() {
    String input = tvInput.getText().toString().toLowerCase().trim();

    if (input.isEmpty())
    {
      tvInput.setError("Please enter a valid name");
      return;
    }

    dialog.show();




    allMedRef.document(input).get().addOnCompleteListener(
        new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            if (task.isSuccessful())
            {
              DocumentSnapshot documentSnapshot = task.getResult();

              if (documentSnapshot.exists())
              {
                medicine = documentSnapshot.toObject(Medicine.class);
                emptyView.setVisibility(View.GONE);
                medInfoContainerLayout.setVisibility(View.VISIBLE);
                loadMedicineDetails();
                dialog.dismiss();
              }
              else
              {
                dialog.dismiss();
                Utils.makeToast(SearchMedicineActivity.this, "Medicine not found");
                emptyView.setVisibility(View.VISIBLE);
                medInfoContainerLayout.setVisibility(View.GONE);
              }
            }
          }
        }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        dialog.dismiss();
        Utils.makeToast(SearchMedicineActivity.this, e.getMessage());
      }
    });

  }

  private void loadMedicineDetails() {
    tvMedName.setText(medicine.getName());
    tvMedUse.setText(medicine.getUsedfor());
    tvMedManufacturer.setText(medicine.getManufacturer());
    tvMedPrice.setText("₹ " + String.valueOf(medicine.getPrice()));
    tvMedQuantity.setText(String.valueOf(medicine.getQuantity()) + " Tablets");
    tvMedSchedule.setText(medicine.getSchedule());
    tvMedContains.setText(medicine.getContains());
  }
}
