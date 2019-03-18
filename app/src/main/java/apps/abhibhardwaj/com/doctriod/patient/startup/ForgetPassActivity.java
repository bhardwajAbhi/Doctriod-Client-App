
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
import android.widget.ImageView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgetPassActivity extends AppCompatActivity implements OnClickListener {

  private ImageView btnBack;
  private EditText edtEmail;
  private Button btnReset;

  FirebaseAuth auth;

  ProgressDialog dialog;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forget_pass);

    auth = FirebaseAuth.getInstance();

    btnBack = findViewById(R.id.frgtpass_iv_back);
    edtEmail = findViewById(R.id.frgtpass_edt_mail);
    btnReset = findViewById(R.id.frgtpass_btn_reset);
    dialog = new ProgressDialog(this);


    btnReset.setOnClickListener(this);
    btnBack.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {

    switch (v.getId())
    {
      case R.id.frgtpass_btn_reset:
      {
        resetPassword();
        break;
      }

      case R.id.frgtpass_iv_back:
      {
        goBack();
        break;
      }

    }

  }

  private void goBack() {
    startActivity(new Intent(ForgetPassActivity.this, LoginActivity.class));
    finish();
  }

  private void resetPassword() {
    final String email = edtEmail.getText().toString().trim();

    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
    {
      edtEmail.setError("Invalid E-mail");
      return;
    }

    dialog.setMessage("Sending Email...");
    dialog.show();

    auth.sendPasswordResetEmail(email).addOnCompleteListener(ForgetPassActivity.this,
        new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful())
            {
              dialog.dismiss();
              Snackbar.make(findViewById(android.R.id.content),  "Email sent successfully!", Snackbar.LENGTH_LONG).show();
            }
          }
        }).addOnFailureListener(ForgetPassActivity.this, new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        dialog.dismiss();
        Snackbar.make(findViewById(android.R.id.content),  e.getMessage(), Snackbar.LENGTH_LONG).show();
      }
    });

  }
}
