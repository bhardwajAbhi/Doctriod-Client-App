package apps.abhibhardwaj.com.doctriod.patient.startup;

import android.Manifest.permission;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.models.User;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import de.hdodenhof.circleimageview.CircleImageView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements OnClickListener {

  private static final int GALLERY_REQUEST = 1;
  private static final int CAMERA_REQUEST = 2;
  private CircleImageView ivProfile;
  private ImageView ivBack;
  private EditText edtFullName, edtEmail, edtPhone, edtPass, edtConfirmPass;
  private TextView tvDOB;
  private Spinner spGender, spBloodGroup;
  private Button btnSignUp;
  private ProgressDialog progressDialog;

  private Uri capImageURI;

  private FirebaseAuth auth;
  private DatabaseReference databaseReference;
  private StorageReference storageReference;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    
    auth = FirebaseAuth.getInstance();
    databaseReference = FirebaseDatabase.getInstance().getReference().child("Database").child("Users");
    storageReference = FirebaseStorage.getInstance().getReference().child("Database").child("Users");
    initializeViews();
    addClickListeners();


  }


  private void initializeViews() {
    ivProfile = findViewById(R.id.signup_iv_profile_image);
    ivBack = findViewById(R.id.signup_iv_back);
    edtFullName = findViewById(R.id.signup_edt_full_name);
    edtEmail = findViewById(R.id.signup_edt_mail);
    edtPhone = findViewById(R.id.signup_edt_phone);
    edtPass = findViewById(R.id.signup_edt_password);
    edtConfirmPass = findViewById(R.id.signup_edt_confirm_pass);
    tvDOB = findViewById(R.id.signup_tv_dob);
    spGender = findViewById(R.id.signup_spin_gender);
    spBloodGroup = findViewById(R.id.signup_spin_blood);
    btnSignUp = findViewById(R.id.signup_btn_signUp);
    progressDialog = new ProgressDialog(SignUpActivity.this);
  }

  private void addClickListeners() {
    ivProfile.setOnClickListener(this);
    ivBack.setOnClickListener(this);
    btnSignUp.setOnClickListener(this);
    tvDOB.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.signup_iv_profile_image: {
        showAddPhotoDialog();
        break;
      }
      case R.id.signup_iv_back: {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();

        break;
      }
      case R.id.signup_btn_signUp: {
        registerUser();
        break;
      }
      case R.id.signup_tv_dob: {
        selectDOB();
        break;
      }
    }

  }


  private void showAddPhotoDialog() {
    final CharSequence[] items = {"Take a new photo", "Choose from gallery", "Cancel"};

    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
    builder.setTitle("Add Photo");
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int i) {

        if (items[i].equals("Take a new photo")) {
          //request permission start camera intent
          requestCameraPermission();

        } else if (items[i].equals("Choose from gallery")) {
          //request gallery permission and start gallery intent
          requestGalleryPermission();

        } else if (items[i].equals("Cancel")) {
          //dismiss the alert dialog
          dialog.dismiss();
        }
      }
    });
    builder.show();
  }

  private void requestGalleryPermission() {
    int result = ContextCompat
        .checkSelfPermission(SignUpActivity.this, permission.READ_EXTERNAL_STORAGE);
    if (result == PackageManager.PERMISSION_GRANTED) {
      openGallery();
    } else {
      ActivityCompat
          .requestPermissions(SignUpActivity.this, new String[]{permission.READ_EXTERNAL_STORAGE},
              GALLERY_REQUEST);
    }
  }

  private void requestCameraPermission() {
    int result = ContextCompat.checkSelfPermission(SignUpActivity.this, permission.CAMERA);
    if (result == PackageManager.PERMISSION_GRANTED) {
      startCamera();
    } else {
      ActivityCompat.requestPermissions(SignUpActivity.this,
          new String[]{permission.CAMERA, permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch (requestCode) {
      case CAMERA_REQUEST: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          startCamera();
        } else {
          Utils.makeToast(SignUpActivity.this, "Permission denied to open Camera");
        }
        break;
      }

      case GALLERY_REQUEST: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          openGallery();
        } else {
          Utils.makeToast(SignUpActivity.this, "Permission denied to open Gallery");
        }
        break;
      }
    }


  }

  private void openGallery() {
    Intent pickPhoto = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(pickPhoto, GALLERY_REQUEST);
  }

  private void startCamera() {
    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePicture.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(takePicture, CAMERA_REQUEST);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case GALLERY_REQUEST: {
        if (resultCode == RESULT_OK && data.getData() != null) {
          capImageURI = data.getData();
          setProfileImage(data.getData());
        } else {
          Utils.makeToast(SignUpActivity.this, "No Image Selected! Try Again");
        }
        break;
      }
      case CAMERA_REQUEST: {
        if (resultCode == RESULT_OK && data.getData() != null) {
          capImageURI = data.getData();
          setProfileImage(data.getData());
        } else {
          Utils.makeToast(SignUpActivity.this, "No Image Captured! Try Again");
        }
        break;
      }
    }


  }

  private void setProfileImage(Uri imageURI) {
    ivProfile.setImageURI(imageURI);
  }


  private void selectDOB() {
    final Calendar cldr = Calendar.getInstance();
    int day = cldr.get(Calendar.DAY_OF_MONTH);
    int month = cldr.get(Calendar.MONTH);
    int year = cldr.get(Calendar.YEAR);
    // date picker dialog
    DatePickerDialog picker = new DatePickerDialog(SignUpActivity.this,
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


  private void registerUser() {
    final String fullName = edtFullName.getText().toString().trim();
    final String email = edtEmail.getText().toString().trim();
    final String phone = edtPhone.getText().toString().trim();
    String password = edtPass.getText().toString().trim();
    String confirmPass = edtConfirmPass.getText().toString().trim();
    final String DoB = tvDOB.getText().toString().trim();
    final String gender = spGender.getSelectedItem().toString();
    final String bloodGrp = spBloodGroup.getSelectedItem().toString();

    if (fullName.isEmpty()) {
      edtFullName.setError("Enter a valid Name");
      return;
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      edtEmail.setError("Invalid E-mail");
      return;
    }

    if (!Patterns.PHONE.matcher(phone).matches()) {
      edtPhone.setError("Invalid Number");
      return;
    }

    if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,}$")) {
      edtPass.setError("Must Contain one Digit, Upper & a Lowercase letter [Min length - 6 ch]");
      return;
    }

    if (!confirmPass.equals(password)) {
      edtConfirmPass.setError("Password did not match");
      return;
    }

    if (DoB.isEmpty()) {
      Utils.makeToast(SignUpActivity.this, "Please add Date of Birth");
      return;
    }

    if (capImageURI == null)
    {
      Utils.makeToast(SignUpActivity.this, "Please select a profile image to continue");
      return;
    }

    //if everything is Ok
    progressDialog.setMessage("Signing Up, Please Wait...");
    progressDialog.show();



    //create user
    auth.createUserWithEmailAndPassword(email, confirmPass).addOnCompleteListener(
        new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            
            if (task.isSuccessful())
            {
              final StorageReference filePath = storageReference.child(auth.getCurrentUser().getUid()).child("ProfilePic").child(capImageURI.getLastPathSegment());
              filePath.putFile(capImageURI).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(TaskSnapshot taskSnapshot) {
                  filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                      User user = new User();
                      user.setFullName(fullName);
                      user.setEmail(email);
                      user.setPhone(phone);
                      user.setDoB(DoB);
                      user.setGender(gender);
                      user.setBloodGrp(bloodGrp);
                      user.setProfileImageURL(uri.toString());
                      user.setProfileImageName(capImageURI.getLastPathSegment());

                      databaseReference.child(auth.getCurrentUser().getUid()).child("BasicDetails").setValue(user);


                      progressDialog.dismiss();
                      Utils.makeToast(SignUpActivity.this, "Registered Successfully !!");
                      startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                      finish();
                    }
                  });
                }
              });
            }
            
            
          }
        }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        progressDialog.dismiss();
        Utils.makeToast(SignUpActivity.this, e.getMessage());
      }
    });


  }


}
