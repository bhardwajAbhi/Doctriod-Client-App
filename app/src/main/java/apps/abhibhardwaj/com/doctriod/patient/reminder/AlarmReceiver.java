package apps.abhibhardwaj.com.doctriod.patient.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import apps.abhibhardwaj.com.doctriod.patient.R;

public class AlarmReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Toast.makeText(context, "Pill reminder broadcast", Toast.LENGTH_SHORT).show();

    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    int notificationId = 104;
    String channelId = "pill_reminder_channel";
    String channelName = "Pill Reminder";
    int importance = NotificationManager.IMPORTANCE_HIGH;

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
          channelId, channelName, importance);
      notificationManager.createNotificationChannel(channel);
    }


    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, PillReminderActivity.class), 0);


    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.img_pill_notification)
        .setContentTitle("Pill reminder")
        .setContentText("Time to take Medicine")
        .setContentIntent(contentIntent);


    notificationManager.notify(notificationId, builder.build());


  }
}


