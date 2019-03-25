package apps.abhibhardwaj.com.doctriod.patient.recognizemeds;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector.Detections;
import com.google.android.gms.vision.Detector.Processor;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;

public class RecognizeMedsFragment extends Fragment {

  private static final String TAG = "RegMedFrag";
  private SurfaceView cameraView;
  private TextView textView;
  private CameraSource cameraSource;

  private static final int REQUEST_CAMERA = 10;


  public RecognizeMedsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_recognize_meds, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    cameraView = view.findViewById(R.id.surface_view);
    textView = view.findViewById(R.id.text_view);

    startCameraSource();


  }

  private void startCameraSource() {
    // creating text recognizer
    final TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();

    if (!textRecognizer.isOperational())
    {
      Log.d(TAG, "Detector dependencies not loaded yet");
    }
    else
    {
      //initialize camera source
      cameraSource = new CameraSource.Builder(getContext(), textRecognizer)
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
          if(ActivityCompat.checkSelfPermission(getContext(), permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
          {
            ActivityCompat.requestPermissions(getActivity(), new String[] {permission.CAMERA}, REQUEST_CAMERA);
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          return;
        }
        cameraSource.start(cameraView.getHolder());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
