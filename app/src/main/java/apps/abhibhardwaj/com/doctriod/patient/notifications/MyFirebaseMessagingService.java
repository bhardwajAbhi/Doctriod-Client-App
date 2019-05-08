package apps.abhibhardwaj.com.doctriod.patient.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import androidx.annotation.RequiresApi;
import apps.abhibhardwaj.com.doctriod.patient.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

  private final String ADMIN_CHANNELID = "admin_channel";

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);


    final Intent intent = new Intent(this, NotificationsActivity.class);
    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    int notificationID = new Random().nextInt(3000);

    
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      setupChannels(notificationManager);
    }

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.img_splash_image);

    Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNELID)
        .setSmallIcon(R.drawable.img_splash_image)
        .setLargeIcon(largeIcon)
        .setContentTitle(remoteMessage.getData().get("title"))
        .setContentText(remoteMessage.getData().get("message"))
        .setAutoCancel(true)
        .setSound(notificationSoundUri)
        .setContentIntent(pendingIntent);


    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP)
    {
      notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    notificationManager.notify(notificationID, notificationBuilder.build());
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void setupChannels(NotificationManager notificationManager) {
    CharSequence adminChannelName = "New Notification";
    String adminChannelDescrption = "Device to Device Notification";

    NotificationChannel adminChannel;

    adminChannel = new NotificationChannel(ADMIN_CHANNELID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
    adminChannel.setDescription(adminChannelDescrption);
    adminChannel.enableLights(true);
    adminChannel.setLightColor(Color.RED);
    adminChannel.enableVibration(true);

    if (notificationManager != null)
      notificationManager.createNotificationChannel(adminChannel);

  }
}
