package apps.abhibhardwaj.com.doctriod.patient.prescription;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import android.support.v7.widget.Toolbar;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class MyPrescriptionActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private TextView tvToolbarTitle;
  private ImageView ivBack;

  private RecyclerView recyclerView;
  private LinearLayout placeHolder;
  private ArrayList<Prescription> prescriptionList;
  private PrescriptionListAdapter adapter;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_prescription);

    intToolbar();
    initViews();
    intListeners();
    fetchPrescriptions();


  }



  private void intToolbar() {
    toolbar = findViewById(R.id.prescription_tool_bar);
    tvToolbarTitle = findViewById(R.id.prescription_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("My Prescription");
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void initViews() {
    prescriptionList = new ArrayList<>();

    placeHolder = findViewById(R.id.layout_linear_prescription_empty_view);
    recyclerView = findViewById(R.id.prescription_recycler_view);

    ivBack = findViewById(R.id.prescription_toolbar_iv_back);

  }

  private void intListeners() {
    ivBack.setOnClickListener(this);
  }


  private void fetchPrescriptions() {

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .collection("prescriptions")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {

            if (task.isSuccessful())
            {
              for (QueryDocumentSnapshot prescSnapshot : task.getResult())
              {
                Prescription prescription = prescSnapshot.toObject(Prescription.class);
                prescriptionList.add(prescription);
              }
              loadPrescriptions();
            }


          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Utils.makeToast(MyPrescriptionActivity.this, e.getMessage());
          }
        });

  }

  private void loadPrescriptions() {
    if (prescriptionList.isEmpty())
    {
      placeHolder.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    }
    else
    {
      placeHolder.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
      recyclerView.setLayoutManager(new LinearLayoutManager(MyPrescriptionActivity.this, LinearLayoutManager.VERTICAL, false));

      adapter = new PrescriptionListAdapter(MyPrescriptionActivity.this, prescriptionList);
      recyclerView.setAdapter(adapter);
      DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
          LinearLayout.VERTICAL);
      recyclerView.addItemDecoration(dividerItemDecoration);
    }


  }

  @Override
  public void onClick(View v) {

    if (v == ivBack)
    {
      startActivity(new Intent(MyPrescriptionActivity.this, HomeActivity.class));
      finish();
    }

  }
}
