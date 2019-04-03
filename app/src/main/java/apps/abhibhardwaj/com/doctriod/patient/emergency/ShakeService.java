package apps.abhibhardwaj.com.doctriod.patient.emergency;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ShakeService extends Service implements ShakeListener.OnShakeListener {
  private ShakeListener mShaker;
  private SensorManager mSensorManager;
  private Sensor mAccelerometer;
  public int check;

  private static final String TAG = "ShakeServiceTag";

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }




  public void onCreate() {
    super.onCreate();

    Log.d(TAG, "inside on create of service");

    if (Build.VERSION.SDK_INT >= 26) {
      Log.d(TAG, "initializing notification channel");

      String CHANNEL_ID = "my_channel_01";
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
          "Accident Detection Service is running",
          NotificationManager.IMPORTANCE_HIGH);
      Log.d(TAG, "notification channel created");
      ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

      Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
          .setContentTitle("")
          .setContentText("").build();
      Log.d(TAG, "notification created");
      startForeground(1, notification);
      Log.d(TAG, " successfully **started foreground service");

    }

    this.mSensorManager = ((SensorManager)getSystemService(Context.SENSOR_SERVICE));
    this.mAccelerometer = this.mSensorManager.getDefaultSensor(1);
    mShaker = new ShakeListener(this);
    mShaker.setOnShakeListener(this);
    Toast.makeText(ShakeService.this, "Service is Running in Background!",Toast.LENGTH_LONG).show();
    Log.d(getPackageName(), "Created the Service!");
    check=1;
  }
  @Override
  public void onShake() {
    if(check==1) {
      Toast.makeText(ShakeService.this, "SHAKEN!", Toast.LENGTH_LONG).show();
      final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
      vib.vibrate(500);
      Intent i = new Intent();
      i.setClass(this, CheckCertainity.class);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(i);
    }

  }
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);

  }
  public void onDestroy(){
    super.onDestroy();
    check=0;
    Log.d(getPackageName(),"Service Destroyed.");
  }
}
