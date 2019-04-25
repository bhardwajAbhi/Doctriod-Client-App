package apps.abhibhardwaj.com.doctriod.patient.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private TextView tvToolbarTitle;
  private ImageView ivBack;

  private CircleImageView ivUserImage;
  private TextView tvFullName, tvDOB, tvBloodType, tvGender, tvHeight, tvWeight, tvStatus, tvEmail, tvPhone, tvAddress;
  private Button btnEdit;
  private ProgressDialog progressDialog;

  private FirebaseAuth auth;
  private FirebaseUser currentUser;
  private DatabaseReference databaseReference;
  private User user = null;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    initToolbar();
    initFireBase();
    intViews();
    initActionListeners();
    fetchUserDetails();


  }




  private void initToolbar() {
    toolbar = findViewById(R.id.profile_toolbar);
    tvToolbarTitle = findViewById(R.id.profile_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Profile");
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void initFireBase() {
    auth = FirebaseAuth.getInstance();
    currentUser = auth.getCurrentUser();
    databaseReference = FirebaseDatabase.getInstance().getReference().child("Database").child("Users").child(currentUser.getUid());
  }

  private void intViews() {
    ivUserImage = findViewById(R.id.profile_image);
    tvFullName = findViewById(R.id.profile_user_name);
    tvDOB = findViewById(R.id.profile_user_dob);
    tvBloodType = findViewById(R.id.profile_user_blood);
    tvGender = findViewById(R.id.profile_user_gender);
    tvHeight = findViewById(R.id.profile_user_height);
    tvWeight = findViewById(R.id.profile_user_weight);
    tvStatus = findViewById(R.id.profile_user_status);
    tvEmail = findViewById(R.id.profile_user_email);
    tvPhone = findViewById(R.id.profile_user_phone);
    tvAddress = findViewById(R.id.profile_user_address);

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Loading...");

    ivBack = findViewById(R.id.profile_toolbar_iv_back);
    btnEdit = findViewById(R.id.profile_btn_edit);
  }

  private void initActionListeners() {
    ivBack.setOnClickListener(this);
    btnEdit.setOnClickListener(this);
  }

  private void fetchUserDetails() {
    progressDialog.show();
    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
          user = ds.getValue(User.class);
          loadUserDetails(user);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void loadUserDetails(User user) {
    Picasso.get().load(user.getProfileImageURL()).into(ivUserImage);
    tvFullName.setText(user.getFullName());
    tvDOB.setText(user.getDoB());
    tvBloodType.setText(user.getBloodGrp());
    tvGender.setText(user.getGender());
    tvHeight.setText(user.getHeight());
    tvWeight.setText(user.getWeight());
    tvStatus.setText(user.getStatus());
    tvEmail.setText(user.getEmail());
    tvPhone.setText(user.getPhone());
    tvAddress.setText(user.getAddress());

    progressDialog.dismiss();

  }

  @Override
  public void onClick(View v) {

    switch (v.getId())
    {
      case R.id.profile_toolbar_iv_back:
      {
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        finish();
        break;
      }
      case R.id.profile_btn_edit:
      {
        // start edit details activity
        startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        break;
      }

    }



  }
}
