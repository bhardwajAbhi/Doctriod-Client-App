package apps.abhibhardwaj.com.doctriod.patient.notifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = "mFirebaseIIDService";
  private static final String SUBSCRIBE_TO = "userABC";

  @Override
  public void onTokenRefresh() {

    String token = FirebaseInstanceId.getInstance().getToken();

    // Once the token is generated, subscribe to topic with the userId
    FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
    Log.i(TAG, "onTokenRefresh completed with token: " + token);
    Utils.makeToast(getApplicationContext(), token);
  }
}
