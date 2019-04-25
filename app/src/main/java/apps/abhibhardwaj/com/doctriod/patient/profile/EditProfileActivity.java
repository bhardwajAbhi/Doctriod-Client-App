package apps.abhibhardwaj.com.doctriod.patient.profile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.models.User;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import apps.abhibhardwaj.com.doctriod.patient.reminder.AddReminderActivity;
import apps.abhibhardwaj.com.doctriod.patient.reminder.PillReminderActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity implements OnClickListener {

  private ImageButton btnClose, btnSave;
  private TextView tvDOB;
  private EditText edtName, edtPhone, edtMail, edtAddress, edtWeight, edtHeight;
  private Spinner spGender, spBloodGrp, spStatus;


  private FirebaseAuth auth;
  private DatabaseReference databaseReference;
  private StorageReference storageReference;

  private ProgressDialog progressDialog;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_profile);

    initFireBase();
    intViews();
    initActionListeners();

  }

  private void initFireBase() {
    auth = FirebaseAuth.getInstance();
    databaseReference = FirebaseDatabase.getInstance().getReference().child("Database").child("Users");
    storageReference = FirebaseStorage.getInstance().getReference().child("Database").child("Users");
  }

  private void intViews() {


    progressDialog = new ProgressDialog(EditProfileActivity.this);

    edtName = findViewById(R.id.edit_profile_full_name);
    edtPhone = findViewById(R.id.edit_profile_phone);
    edtMail = findViewById(R.id.edit_profile_email);
    edtAddress = findViewById(R.id.edit_profile_address);
    tvDOB = findViewById(R.id.edit_profile_dob);
    edtWeight = findViewById(R.id.edit_profile_weight);
    edtHeight = findViewById(R.id.edit_profile_height);
    spGender = findViewById(R.id.edit_profile_gender);
    spBloodGrp = findViewById(R.id.edt_profile_blood_grp);
    spStatus = findViewById(R.id.edit_profile_status);

    btnSave = findViewById(R.id.edit_profile_btn_save);
    btnClose = findViewById(R.id.edit_profile_btn_close);


  }

  private void initActionListeners() {

    btnClose.setOnClickListener(this);
    btnSave.setOnClickListener(this);
    tvDOB.setOnClickListener(this);

  }

  @Override
  public void onBackPressed() {
    closeActivity();
  }

  @Override
  public void onClick(View v) {

    switch (v.getId())
    {

      case R.id.edit_profile_btn_save:
      {
        saveUserDetails();
        break;
      }

      case R.id.edit_profile_btn_close:
      {
        closeActivity();
        break;
      }

      case R.id.edit_profile_dob:
      {
        selectDOB();
        break;
      }
    }

  }

  private void selectDOB() {
    final Calendar cldr = Calendar.getInstance();
    int day = cldr.get(Calendar.DAY_OF_MONTH);
    int month = cldr.get(Calendar.MONTH);
    int year = cldr.get(Calendar.YEAR);
    // date picker dialog
    DatePickerDialog picker = new DatePickerDialog(EditProfileActivity.this,
        new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            //tvDOB.setText(sdf.format(cldr.getTime()));
            tvDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
          }
        }, year, month, day);
    picker.show();
  }



  private void closeActivity() {
    new AlertDialog.Builder(this)
        .setTitle("Are you Sure?")
        .setMessage("By Going back you will lose all the details you added")
        .setCancelable(false)
        .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            finish();
          }
        })
        .setNegativeButton("Cancel", null)
        .show();
  }


  private void saveUserDetails() {

    final String fullName = edtName.getText().toString().trim();
    final String email = edtMail.getText().toString().trim();
    final String phone = edtPhone.getText().toString().trim();
    final String address = edtAddress.getText().toString().trim();
    final String weight = edtWeight.getText().toString().trim();
    final String height = edtHeight.getText().toString().trim();
    final String DoB = tvDOB.getText().toString().trim();
    final String status = spStatus.getSelectedItem().toString();
    final String gender = spGender.getSelectedItem().toString();
    final String bloodGrp = spBloodGrp.getSelectedItem().toString();


    if (fullName.isEmpty()) {
      edtName.setError("Enter a valid Name");
      return;
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      edtMail.setError("Invalid E-mail");
      return;
    }

    if (!Patterns.PHONE.matcher(phone).matches()) {
      edtPhone.setError("Invalid Number");
      return;
    }

    if (DoB.isEmpty()) {
      Utils.makeToast(EditProfileActivity.this, "Please add Date of Birth");
      return;
    }

    if (address.isEmpty()) {
      edtAddress.setError("Enter a valid Address");
      return;
    }

    if (weight.isEmpty()) {
      edtWeight.setError("Enter valid Weight");
      return;
    }

    if (height.isEmpty()) {
      edtHeight.setError("Enter valid Height");
      return;
    }



    //if everything is Ok
    progressDialog.setMessage("Saving details, Please Wait...");
    progressDialog.show();

/*    User user = new User();
    user.setFullName(fullName);
    user.setEmail(email);
    user.setPhone(phone);
    user.setDoB(DoB);
    user.setGender(gender);
    user.setBloodGrp(bloodGrp);
    user.setHeight(height);
    user.setWeight(weight);
    user.setAddress(address);*/

    String userID = auth.getCurrentUser().getUid();

    databaseReference.child(userID).child("BasicDetails").child("fullName").setValue(fullName);

    databaseReference.child(userID).child("BasicDetails").child("phone").setValue(phone);

    databaseReference.child(userID).child("BasicDetails").child("doB").setValue(DoB);

    databaseReference.child(userID).child("BasicDetails").child("gender").setValue(gender);

    databaseReference.child(userID).child("BasicDetails").child("bloodGrp").setValue(bloodGrp);

    databaseReference.child(userID).child("BasicDetails").child("height").setValue(height);

    databaseReference.child(userID).child("BasicDetails").child("weight").setValue(weight);

    databaseReference.child(userID).child("BasicDetails").child("address").setValue(address);

    databaseReference.child(userID).child("BasicDetails").child("status").setValue(status);



    progressDialog.dismiss();
    Utils.makeToast(EditProfileActivity.this, "Details Saved Successfully !!");


    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
    finish();


  }
}
