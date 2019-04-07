package apps.abhibhardwaj.com.doctriod.patient.reminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import java.util.ArrayList;

public class PillReminderActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private TextView tvToolbarTitle;
  private ImageView ivBack;

  private ListView reminderListView;
  private LinearLayout placeHolder;
  private Button btnAddReminder;
  private ArrayList<PillReminder> reminderList;
  private DatabaseHelper helper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pill_reminder);

    initToolbar();
    initViews();
    loadReminderList();


  }

  private void loadReminderList() {
    reminderList = helper.fetchAllReminders();

    if (reminderList.isEmpty())
    {
      placeHolder.setVisibility(View.VISIBLE);
      reminderListView.setVisibility(View.GONE);
    }
    else
    {
      placeHolder.setVisibility(View.GONE);
      reminderListView.setVisibility(View.VISIBLE);
      ReminderListAdapter adapter = new ReminderListAdapter(PillReminderActivity.this, reminderList);
      reminderListView.setAdapter(adapter);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    loadReminderList();
  }

  private void initToolbar() {
    toolbar = findViewById(R.id.reminder_tool_bar);
    tvToolbarTitle = findViewById(R.id.reminder_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Pill Reminder");
    getSupportActionBar().setDisplayShowTitleEnabled(false);

  }

  private void initViews() {
    reminderList = new ArrayList<PillReminder>();
    helper = new DatabaseHelper(PillReminderActivity.this);

    placeHolder = findViewById(R.id.layout_linear_reminder_empty_view);
    reminderListView = findViewById(R.id.reminder_list_view);

    ivBack = findViewById(R.id.reminder_toolbar_iv_back);
    btnAddReminder = findViewById(R.id.reminder_btn_add_reminder);

    ivBack.setOnClickListener(this);
    btnAddReminder.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {

    if (v == ivBack)
    {
      startActivity(new Intent(PillReminderActivity.this, HomeActivity.class));
      finish();
    }

    if (v == btnAddReminder)
    {
      startActivity(new Intent(PillReminderActivity.this, AddReminderActivity.class));
    }


  }
}
