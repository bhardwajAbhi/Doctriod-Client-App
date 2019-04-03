package apps.abhibhardwaj.com.doctriod.patient.emergency;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class EmergencyActivity extends AppCompatActivity implements OnClickListener {


  private static String firstN, secondN;
  private int flag = 1;
  private EditText edtFirstNumber, edtSecondNumber;
  private Button btnEdit, btnSave;
  ImageButton btnTrack;
  private TextView txtStatus;

  private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 121;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_emergency);

    if(ContextCompat.checkSelfPermission(EmergencyActivity.this,permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(EmergencyActivity.this, permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
    {
      // request for permission
      ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
    }
    else
    {
      initViews();
      initActionListeners();
      loadEmergencyContacts();
    }



  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode)
    {
      case REQUEST_PERMISSION_EXTERNAL_STORAGE:
      {
        if (grantResults.length > 0
              && grantResults[0] == PackageManager.PERMISSION_GRANTED
              && grantResults[1] == PackageManager.PERMISSION_GRANTED)
        {
          initViews();
          initActionListeners();
          loadEmergencyContacts();
        }
        else
        {
          Utils.makeToast(EmergencyActivity.this, "Please grant permission to continue");
          finish();
        }
      }
    }

  }

  private void initActionListeners() {
    //action listeners
    btnEdit.setOnClickListener(this);
    btnTrack.setOnClickListener(this);

    if (btnSave != null) {
      btnSave.setOnClickListener(this);
    }
  }

  private void initViews() {
    edtFirstNumber = findViewById(R.id.emergency_edt_contact_one);
    edtSecondNumber = findViewById(R.id.emergency_edt_contact_two);

    btnEdit = findViewById(R.id.emergency_btn_edit_contact);
    btnSave = findViewById(R.id.emergency_btn_save_contact);

    btnTrack = findViewById(R.id.emergency_fab_on_off);
    txtStatus = findViewById(R.id.emergency_tv_status);
  }


  @Override
  public void onClick(View v) {

    if (v == btnTrack) {
      toggleTrackingService();
    }

    if (v == btnSave) {
      saveEmergencyContacts();
    }

    if (v == btnEdit) {
      editEmergencyContacts();
    }

  }


  private void toggleTrackingService() {

    if (flag == 1) {

      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        startForegroundService(new Intent(EmergencyActivity.this, ShakeService.class));
        Toast.makeText(this, "Foreground Service Activated!", Toast.LENGTH_SHORT).show();
      }
      else
      {
        startService(new Intent(EmergencyActivity.this, ShakeService.class));
        Toast.makeText(this, "Service Activated!", Toast.LENGTH_SHORT).show();
      }
      flag = 0;
      txtStatus.setText("ACTIVATED");
      txtStatus.setTextColor(Color.parseColor("#1FC61F"));
      btnTrack.setBackgroundColor(Color.parseColor("#1FC61F"));




    } else {
      Toast.makeText(this, "Service Deactivated!", Toast.LENGTH_SHORT).show();
      stopService(new Intent(EmergencyActivity.this, ShakeService.class));
      flag = 1;
      txtStatus.setText("DE-ACTIVATED");
      txtStatus.setTextColor(Color.parseColor("#E3110A"));
      btnTrack.setBackgroundColor(Color.parseColor("#E3110A"));
    }
  }


  private void saveEmergencyContacts() {

    firstN = edtFirstNumber.getText().toString().trim();
    secondN = edtSecondNumber.getText().toString().trim();

    //validation check
    if (TextUtils.isEmpty(firstN) || firstN.length() <= 9) {
      edtFirstNumber.setError("Enter Contact Number");
      return;
    }
    if (TextUtils.isEmpty(secondN) || secondN.length() <= 9) {
      edtSecondNumber.setError("Enter Contact Number");
      return;
    }

    try {

      File myFile = new File("/sdcard/.emergencyNumbersDoctroid.txt");
      myFile.createNewFile();

      FileOutputStream fOut = new FileOutputStream(myFile);

      OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

      myOutWriter.append(firstN);
      myOutWriter.append("\n");
      myOutWriter.append(secondN);

      myOutWriter.close();
      fOut.close();

      Toast.makeText(this, "Emergency Contacts have been saved", Toast.LENGTH_SHORT).show();

    } catch (Exception e) {
      Toast.makeText(EmergencyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    edtFirstNumber.setEnabled(false);
    edtSecondNumber.setEnabled(false);

  }

  private void editEmergencyContacts() {

    edtFirstNumber.setEnabled(true);
    edtSecondNumber.setEnabled(true);
  }

  private void loadEmergencyContacts() {

    edtFirstNumber.setEnabled(false);
    edtSecondNumber.setEnabled(false);

    try {
      File myFile = new File("/sdcard/.emergencyNumbersDoctroid.txt");
      FileInputStream fIn = new FileInputStream(myFile);
      BufferedReader myReader = new BufferedReader(
          new InputStreamReader(fIn));
      firstN = myReader.readLine();
      secondN = myReader.readLine();
      myReader.close();

      edtFirstNumber.setText(firstN);
      edtSecondNumber.setText(secondN);
    } catch (Exception e) {
      Toast.makeText(EmergencyActivity.this, e.getMessage(),
          Toast.LENGTH_SHORT).show();
    }
  }
}
