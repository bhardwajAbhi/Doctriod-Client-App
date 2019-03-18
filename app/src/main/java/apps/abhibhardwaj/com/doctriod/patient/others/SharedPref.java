package apps.abhibhardwaj.com.doctriod.patient.others;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPref {

  Context context;
  String key = "isFirstTime";
  SharedPreferences preferences;
  Editor editor;

  public SharedPref()
  {

  }

  public SharedPref(Context context) {
    this.context = context;
    preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
  }

  public void setAppLaunchedFirstTime(Boolean stats)
  {
    editor = preferences.edit();
    editor.putBoolean(key, stats);
    editor.apply();

  }

  public boolean isAppLaunchedFirstTime()
  {
    return preferences.getBoolean(key, true);
  }



}
