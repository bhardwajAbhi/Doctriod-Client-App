package apps.abhibhardwaj.com.doctriod.patient.startup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.others.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPref pref = new SharedPref(SplashActivity.this);
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();





    if(currentUser == null && pref.isAppLaunchedFirstTime())
    {
      startActivity(new Intent(SplashActivity.this, IntroActivity.class));
      finish();
    }
    else if(currentUser == null && !pref.isAppLaunchedFirstTime())
    {
      startActivity(new Intent(SplashActivity.this, LoginActivity.class));
      finish();
    }

    else {
      startActivity(new Intent(SplashActivity.this, HomeActivity.class));
      finish();
    }

  }
}
