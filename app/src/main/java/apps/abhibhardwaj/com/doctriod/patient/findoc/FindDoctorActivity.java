package apps.abhibhardwaj.com.doctriod.patient.findoc;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.models.NearbyPlacesModel;
import apps.abhibhardwaj.com.doctriod.patient.models.Result;
import apps.abhibhardwaj.com.doctriod.patient.nearby.ApiClient;
import apps.abhibhardwaj.com.doctriod.patient.nearby.NearPlaceInterface;
import apps.abhibhardwaj.com.doctriod.patient.nearby.RecyclerViewAdapter;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import dmax.dialog.SpotsDialog;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindDoctorActivity extends AppCompatActivity implements OnClickListener{


  private Toolbar toolbar;
  private ImageView ivBack;
  private TextView tvToolbarTitle;

  private RecyclerView recyclerView;
  private RecyclerViewAdapter nearbyAdapter;

  private AlertDialog dialog;
  private Location currentLocation;
  private FusedLocationProviderClient fusedLocationProviderClient;

  private static final String API_KEY = "AIzaSyB0mzGO2Yn9fl8RCzWFFCdp8_6zfz9Rerc";
  private static final int LOCATION_REQUEST_CODE = 111;
  private ArrayList<Result> doctorList;
  private NearPlaceInterface service;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_doctor);

    initToolbar();
    initViews();

    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    if (ActivityCompat.checkSelfPermission(FindDoctorActivity.this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FindDoctorActivity.this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    {
      ActivityCompat.requestPermissions(this, new String[] {permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
      return;
    }

    fetchLastLocation();
    dialog.show();

  }



  private void initToolbar() {
    toolbar = findViewById(R.id.findoc_tool_bar);
    tvToolbarTitle = findViewById(R.id.findoc_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Find Doctors");
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }


  private void initViews() {
      dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
      recyclerView = findViewById(R.id.findoc_recycler_view);
      doctorList = new ArrayList<>();

  }


  private void fetchLastLocation() {

        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
          @Override
          public void onSuccess(Location location) {
            if (location != null)
            {
              currentLocation = location;
              Utils.makeToast(FindDoctorActivity.this, currentLocation.getLatitude() + " " + currentLocation.getLongitude());


              if (currentLocation != null)
                fetchNearbyDoctorsFromAPI();
              else
                Utils.makeToast(FindDoctorActivity.this, "No Doctors Available. Try Again Later!");

            }
            else
            {
              dialog.dismiss();
              Utils.makeToast(FindDoctorActivity.this, "No Location Recorded");
            }
          }
        });

  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch (requestCode)
    {
      case LOCATION_REQUEST_CODE:
      {
        if (grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED)
        {
          fetchLastLocation();
        }
        else
        {
          Utils.makeToast(FindDoctorActivity.this, "Location permission missing");
        }
        break;
      }
    }
  }

  @Override
  public void onClick(View v) {

  }


  private void fetchNearbyDoctorsFromAPI() {

    if (!doctorList.isEmpty())
      doctorList.clear();

    service = ApiClient.getRetrofitInstance().create(NearPlaceInterface.class);

    String location  = currentLocation.getLatitude() + ", " + currentLocation.getLongitude();

    Call<NearbyPlacesModel> call = service.getNearbyPlace(location, 5000, "doctor", API_KEY);

    call.enqueue(new Callback<NearbyPlacesModel>() {
      @Override
      public void onResponse(Call<NearbyPlacesModel> call, Response<NearbyPlacesModel> response) {
        List<Result> results = response.body().getResults();
        doctorList.addAll(results);
        loadRecyclerViewWithData();
        dialog.dismiss();

      }


      @Override
      public void onFailure(Call<NearbyPlacesModel> call, Throwable t) {
        dialog.dismiss();
        Utils.makeToast(FindDoctorActivity.this, "No Doctors Available. Try Again Later!");
      }
    });

  }

  private void loadRecyclerViewWithData() {
    nearbyAdapter = new RecyclerViewAdapter(FindDoctorActivity.this, currentLocation, doctorList);

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(FindDoctorActivity.this,
        LinearLayout.VERTICAL);
    recyclerView.addItemDecoration(dividerItemDecoration);
    recyclerView.setAdapter(nearbyAdapter);

  }

  //     https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Doctor%20Vinod&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours&geometrylocationbias=circle:2000@30.697865,%2076.720263&key=AIzaSyB0mzGO2Yn9fl8RCzWFFCdp8_6zfz9Rerc

}
