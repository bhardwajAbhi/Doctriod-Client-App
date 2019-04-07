package apps.abhibhardwaj.com.doctriod.patient.reminder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import apps.abhibhardwaj.com.doctriod.patient.R;

public class SelectDaysDialog extends Dialog implements OnClickListener, OnCheckedChangeListener {

  private Context context;
  private String result;
  private Button btnConfirm;
  private RadioButton rdMon, rdTue, rdWed, rdThu, rdFri, rdSat, rdSun;
  private int mon, tue, wed, thu, fri, sat, sun;

  public SelectDaysDialog(Context context) {
    super(context);
    this.context = context;
  }


  public interface EditDayDialogListener {
    void onFinishEditDialog(String inputText);
  }



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_select_days);
    setTitle("Select Days");

    mon = tue = wed = thu = fri = sat = sun = 0;

    btnConfirm = findViewById(R.id.btn_okay);

    rdMon = findViewById(R.id.rdmon);
    rdTue = findViewById(R.id.rdtue);
    rdWed = findViewById(R.id.rdwed);
    rdThu = findViewById(R.id.rdthu);
    rdFri = findViewById(R.id.rdfri);
    rdSat = findViewById(R.id.rdsat);
    rdSun = findViewById(R.id.rdsun);

    rdMon.setOnCheckedChangeListener(this);
    rdTue.setOnCheckedChangeListener(this);
    rdWed.setOnCheckedChangeListener(this);
    rdThu.setOnCheckedChangeListener(this);
    rdFri.setOnCheckedChangeListener(this);
    rdSat.setOnCheckedChangeListener(this);
    rdSun.setOnCheckedChangeListener(this);

    btnConfirm.setOnClickListener(this);


  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_okay)
    {
      AddReminderActivity reminderActivity = (AddReminderActivity) context;
      createResultString();
      reminderActivity.onFinishEditDialog(result);
      this.dismiss();
    }
  }

  private void createResultString() {
    result = " ";
    if (mon == 1)
      result += "Monday ";
    if (tue == 1)
      result += "Tuesday ";
    if (wed == 1)
      result += "Wednesday ";
    if (thu == 1)
      result += "Thursday ";
    if (fri == 1)
      result += "Friday ";
    if (sat == 1)
      result += "Saturday ";
    if (sun == 1)
      result += "Sunday ";
  }


  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (rdMon.isChecked())
      mon = 1;
    else mon = 0;
    if (rdTue.isChecked())
      tue = 1;
    else tue = 0;
    if (rdWed.isChecked())
      wed = 1;
    else wed = 0;
    if (rdThu.isChecked())
      thu = 1;
    else thu = 0;
    if (rdFri.isChecked())
      fri = 1;
    else fri = 0;
    if (rdSat.isChecked())
      sat = 1;
    else sat = 0;
    if (rdSun.isChecked())
      sun = 1;
    else sun = 0;
  }
}
