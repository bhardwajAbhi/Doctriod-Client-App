package apps.abhibhardwaj.com.doctriod.patient.reminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import apps.abhibhardwaj.com.doctriod.patient.reminder.SelectDaysDialog.EditDayDialogListener;
import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity implements OnClickListener,
    OnCheckedChangeListener, EditDayDialogListener {

  private Toolbar toolbar;
  private TextView tvToolbarTitle;
  private ImageView ivBack;

  private EditText edtMedName, edtMsg;
  private RadioButton rbEveryday, rbSpecificDay;
  private TextView tvSelectedDays;
  private NumberPicker numberPicker;
  private TimePicker timePicker;
  private Button btnAddRem;

  private PillReminder reminder;
  private DatabaseHelper databaseHelper;
  private String remID = null;




  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_reminder);

    initToolbar();
    initViews();

    if (getIntent().getStringExtra("medID") != null)
    {
      remID = getIntent().getStringExtra("medID");
    }


  }



  private void initToolbar() {
    toolbar = findViewById(R.id.add_rem_tool_bar);
    tvToolbarTitle = findViewById(R.id.add_rem_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Add Reminder");
    getSupportActionBar().setDisplayShowTitleEnabled(false);

  }

  private void initViews() {

    reminder = new PillReminder();
    databaseHelper = new DatabaseHelper(this);

    tvSelectedDays = findViewById(R.id.add_rem_tv_selected_stats);
    edtMedName = findViewById(R.id.add_rem_edt_med_name);
    edtMsg = findViewById(R.id.add_rem_edt_msg);

    rbEveryday = findViewById(R.id.rdo_everyday);
    rbSpecificDay = findViewById(R.id.rdo_specific_days);

    numberPicker = findViewById(R.id.add_rem_num_picker);
    numberPicker.setMaxValue(20);
    numberPicker.setMinValue(1);
    numberPicker.setWrapSelectorWheel(true);

    timePicker = findViewById(R.id.add_rem_time_picker);

    rbEveryday.setOnCheckedChangeListener(this);
    rbSpecificDay.setOnCheckedChangeListener(this);

    ivBack = findViewById(R.id.add_rem__toolbar_iv_back);
    ivBack.setOnClickListener(this);

    btnAddRem = findViewById(R.id.add_rem_btn_add);
    btnAddRem.setOnClickListener(this);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    switch (buttonView.getId())
    {
      case R.id.rdo_everyday:
      {
        if (rbEveryday.isChecked())
        {
          rbSpecificDay.setChecked(false);
          tvSelectedDays.setText("Everyday");
        }
        break;
      }
      case R.id.rdo_specific_days:
      {
        if (rbSpecificDay.isChecked())
        {
          rbEveryday.setChecked(false);
          SelectDaysDialog daysDialog = new SelectDaysDialog(AddReminderActivity.this);
          daysDialog.show();
        }
        break;
      }
    }
  }

  @Override
  public void onFinishEditDialog(String inputText) {
    tvSelectedDays.setText(inputText);
    reminder.setDays(inputText);
  }

  @Override
  public void onClick(View v) {

    if (v == ivBack)
    {
      // do something
      launchPrevActivity();
    }

    if (v == btnAddRem)
    {
      saveReminderDetails();
    }


  }

  private void launchPrevActivity() {
    new AlertDialog.Builder(this)
        .setTitle("Are you Sure?")
        .setMessage("By Going back you will lose all the details you added for this reminder")
        .setCancelable(false)
        .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            startActivity(new Intent(AddReminderActivity.this, PillReminderActivity.class));
            finish();
          }
        })
        .setNegativeButton("Cancel", null)
        .show();
  }

  private void saveReminderDetails() {

    if (edtMedName.getText().toString().trim().isEmpty())
    {
      edtMedName.setError("Enter name");
      return;
    }
    else
    {
      reminder.setName(edtMedName.getText().toString().trim());
    }

    reminder.setDosage(String.valueOf(numberPicker.getValue()));

    reminder.setDays(tvSelectedDays.getText().toString());

    reminder.setTime(timePicker.getCurrentHour().toString() + " : " + timePicker.getCurrentMinute().toString());

    if (edtMsg.getText().toString().trim().isEmpty())
    {
      edtMsg.setError("Enter message");
      return;
    }
    else
    {
      reminder.setMessage(edtMsg.getText().toString().trim());
    }


    long rowCount;

    if (remID == null)
    {
      // create a new reminder

      rowCount = databaseHelper.saveReminder(reminder);

      if (rowCount > 0)
      {
        Utils.makeToast(AddReminderActivity.this, "Reminder Saved");
      }
      else
      {
        Utils.makeToast(AddReminderActivity.this, "Please Try Again");
      }
    }
    else
    {
      // update reminder
      rowCount = Long.parseLong(remID);

      databaseHelper.updateReminder(rowCount, reminder);
      databaseHelper.closeDB();
      Utils.makeToast(AddReminderActivity.this, "Reminder Updated!");
    }

    // create alarm
    Long time =  System.currentTimeMillis();
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);

    int cYear = cal.get(Calendar.YEAR);
    int cMonth = cal.get(Calendar.MONTH);
    int cDay = cal.get(Calendar.DAY_OF_MONTH);

    AlarmService alarmService = new AlarmService(getApplicationContext(), rowCount);
    cal.set(cYear, cMonth, cDay, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    alarmService.startAlarm(cal, tvSelectedDays.getText().toString());

  }


}
