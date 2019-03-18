package apps.abhibhardwaj.com.doctriod.patient.others;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

public class Utils {

  public static void makeToast(Context context, String text){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
  }
}
