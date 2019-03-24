package apps.abhibhardwaj.com.doctriod.patient.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

  private EditText edtMail, edtPassword;
  private TextView tvForgotPass, tvSignUp;
  private Button btnSignIn;

  private FirebaseAuth auth;
  ProgressDialog dialog;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);


    // initialize views
    auth = FirebaseAuth.getInstance();
    dialog = new ProgressDialog(this);
    edtMail = findViewById(R.id.login_edt_email);
    edtPassword = findViewById(R.id.login_edt_password);
    tvForgotPass = findViewById(R.id.login_tv_forgot_password);
    tvSignUp = findViewById(R.id.login_tv_create_account);
    btnSignIn = findViewById(R.id.login_btn_signIn);

    btnSignIn.setOnClickListener(this);
    tvForgotPass.setOnClickListener(this);
    tvSignUp.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
      switch (v.getId())
      {
        case R.id.login_btn_signIn:
        {
          loginUser();
          break;
        }
        case R.id.login_tv_forgot_password:
        {
          forgetPassword();
          break;
        }
        case R.id.login_tv_create_account:
        {
          signUpUser();
          break;
        }
      }
  }

  private void signUpUser() {
    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    finish();
  }

  private void forgetPassword() {
    startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class));
  }

  private void loginUser() {

    dialog.setMessage("Logging In...");

    final String email = edtMail.getText().toString().trim();
    final String password = edtPassword.getText().toString().trim();

    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
    {
      edtMail.setError("Invalid E-mail");
      return;
    }
    if (password.isEmpty() || password.length() < 6)
    {
      edtPassword.setError("Invalid Password");
      return;
    }

    dialog.show();

    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
        new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful())
            {
              dialog.dismiss();
              Snackbar.make(findViewById(android.R.id.content),  "Welcome " + email, Snackbar.LENGTH_LONG).show();
              startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
          }
        }).addOnFailureListener(this, new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        dialog.dismiss();
        Snackbar.make(findViewById(android.R.id.content),  e.getMessage(), Snackbar.LENGTH_LONG).show();
      }
    });






  }
}
