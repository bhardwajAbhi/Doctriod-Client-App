package apps.abhibhardwaj.com.doctriod.patient.recognizemeds;

import android.Manifest;
import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector.Detections;
import com.google.android.gms.vision.Detector.Processor;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;

public class RecognizeMedsActivity extends AppCompatActivity implements OnClickListener {

  private static final String TAG = "RegMedActivity";
  private SurfaceView cameraView;
  private TextView textView;
  private CameraSource cameraSource;
  private Button btnSearch;

  private Toolbar toolbar;
  private ImageView ivBack;
  private TextView tvToolbarTitle;

  private static final int REQUEST_CAMERA = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recognize_meds);

    initToolbar();
    iniViews();
    startCameraSource();
  }

  private void initToolbar() {
    toolbar = findViewById(R.id.reg_meds_tool_bar);
    tvToolbarTitle = findViewById(R.id.reg_meds_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Recognize Medicine");
    getSupportActionBar().setDisplayShowTitleEnabled(false);

  }

  private void iniViews() {
    cameraView = findViewById(R.id.surface_view);
    textView = findViewById(R.id.text_view);
    ivBack = findViewById(R.id.reg_meds_iv_back_icon);
    btnSearch = findViewById(R.id.reg_meds_btn_search);

    ivBack.setOnClickListener(this);
    btnSearch.setOnClickListener(this);

  }



  private void startCameraSource() {
    // creating text recognizer
    final TextRecognizer textRecognizer = new TextRecognizer.Builder(RecognizeMedsActivity.this).build();

    if (!textRecognizer.isOperational())
    {
      Log.d(TAG, "Detector dependencies not loaded yet");
    }
    else
    {
      //initialize camera source
      cameraSource = new CameraSource.Builder(RecognizeMedsActivity.this, textRecognizer)
          .setFacing(CameraSource.CAMERA_FACING_BACK)
          .setRequestedPreviewSize(1280, 1024)
          .setAutoFocusEnabled(true)
          .setRequestedFps(2.0f)
          .build();
    }

    /*
     * adding a call back to surfaceView and checking if camera permission is granted
     * if permission is granted, we can start our cameraSource and pass it to surfaceView
     * */

    cameraView.getHolder().addCallback(new Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        try{
          if(ActivityCompat.checkSelfPermission(RecognizeMedsActivity.this, permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
          {
            ActivityCompat.requestPermissions(RecognizeMedsActivity.this, new String[] {permission.CAMERA}, REQUEST_CAMERA);
            return;
          }
          cameraSource.start(cameraView.getHolder());
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {
        cameraSource.stop();
      }
    });

    textRecognizer.setProcessor(new Processor<TextBlock>() {
      @Override
      public void release() {
        //empty
      }


      /*
       * Detect all the text from camera using TextBlock and the values into a stringBuilder which will then be set to the textView
       * */
      @Override
      public void receiveDetections(Detections<TextBlock> detections) {
        final SparseArray<TextBlock> items = detections.getDetectedItems();

        if (items.size() != 0)
        {
          textView.post(new Runnable() {
            @Override
            public void run() {
              StringBuilder builder = new StringBuilder();

              for (int i = 0; i < items.size(); i ++)
              {
                TextBlock item = items.valueAt(i);
                builder.append(item.getValue());
                builder.append("  ");
              }
              textView.setText(builder.toString());
            }
          });
        }
      }
    });
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if(requestCode != REQUEST_CAMERA){
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      return;
    }

    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
    {
      try {
        if (ActivityCompat.checkSelfPermission(RecognizeMedsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          return;
        }
        cameraSource.start(cameraView.getHolder());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  @Override
  public void onClick(View v) {

    switch (v.getId())
    {

      case R.id.reg_meds_iv_back_icon:
      {
        startActivity(new Intent(RecognizeMedsActivity.this, SearchMedicineActivity.class));
        finish();
        break;
      }


      case R.id.reg_meds_btn_search:
      {
        getRecognizedTextAndSendBack();
      }
    }
  }

  private void getRecognizedTextAndSendBack() {
    String recognizedText = textView.getText().toString().toLowerCase().trim();

    if (!recognizedText.isEmpty())
    {
      Intent intent = new Intent(RecognizeMedsActivity.this, SearchMedicineActivity.class);
      intent.putExtra("recognizedText", recognizedText);
      startActivity(intent);
      finish();
    }
    else
    {
      Utils.makeToast(RecognizeMedsActivity.this, "No Text Recognized");
    }
  }
}
