package apps.abhibhardwaj.com.doctriod.patient.reminder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import java.util.ArrayList;
import okhttp3.internal.Util;

public class ReminderListAdapter extends BaseAdapter {

  Context context;
  ArrayList<PillReminder> reminderList;
  DatabaseHelper helper;

  public ReminderListAdapter(Context context, ArrayList<PillReminder> reminderList) {
    this.context = context;
    this.reminderList = reminderList;
    helper = new DatabaseHelper(context);
  }

  @Override
  public int getCount() {
    return reminderList.size();
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    convertView = LayoutInflater.from(context).inflate(R.layout.layout_reminder_list_single_item, parent, false);

    final PillReminder reminder = reminderList.get(position);

    TextView name, dosage, time, day, msg;
    ImageView ivEdit, ivDelete;

    name = convertView.findViewById(R.id.rem_name);
    dosage = convertView.findViewById(R.id.rem_dosage);
    time = convertView.findViewById(R.id.rem_time);
    day = convertView.findViewById(R.id.rem_day);
    msg = convertView.findViewById(R.id.rem_msg);

    ivEdit = convertView.findViewById(R.id.rem_edit);
    ivDelete = convertView.findViewById(R.id.rem_delete);

    String dose = reminder.getDosage() + " Tablet";

    name.setText(reminder.getName());
    dosage.setText(dose);
    day.setText(reminder.getDays());
    time.setText(reminder.getTime());
    msg.setText(reminder.getMessage());

    ivEdit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(context, AddReminderActivity.class);
        i.putExtra("medID", String.valueOf(reminder.getId()));
        context.startActivity(i);
      }
    });

    ivDelete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        AlarmService a = new AlarmService(context, reminder.getId());
        a.cancel(reminder.getId());
        helper.deleteReminder(reminder.getId());
        Utils.makeToast(context, "Reminder deleted");
      }
    });

    return convertView;
  }
}
