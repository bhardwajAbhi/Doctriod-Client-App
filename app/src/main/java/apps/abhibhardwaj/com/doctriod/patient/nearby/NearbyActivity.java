package apps.abhibhardwaj.com.doctriod.patient.nearby;


import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.models.NearbyPlacesModel;
import apps.abhibhardwaj.com.doctriod.patient.models.Result;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyActivity extends AppCompatActivity implements OnClickListener,
    OnMapReadyCallback {

  private Toolbar toolbar;
  private ImageView ivBack;
  private TextView tvToolbarTitle;


  private GoogleMap gMap;
  private Location currentLocation;
  private FusedLocationProviderClient fusedLocationProviderClient;
  private SupportMapFragment mapFragment;
  private RecyclerView recyclerView;
  private RecyclerViewAdapter recyclerViewAdapter;
  private static final int LOCATION_REQUEST_CODE = 101;

  private BottomSheetBehavior bottomSheetBehavior;
  private ImageButton btnToggleBottomSheet;
  private TextView tvSelectedStatus;
  private FloatingActionButton fabToggleMapList;
  private LinearLayout btnHospital, btnDentist, btnDoctor, btnPharmacy;

  private static int TOGGLE_FAB = 1;
  private static final String API_KEY = "AIzaSyB0mzGO2Yn9fl8RCzWFFCdp8_6zfz9Rerc";
  private NearPlaceInterface service;
  private ProgressDialog dialog;
  private ArrayList<Result> placesList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nearby);

    initToolbar();
    initBottomSheet();
    initViews();
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    if (ActivityCompat
        .checkSelfPermission(NearbyActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat
        .checkSelfPermission(NearbyActivity.this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat
          .requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
              LOCATION_REQUEST_CODE);
      return;
    }
    fetchLastLocation();
  }

  private void initViews() {
    ivBack = findViewById(R.id.nearby_toolbar_iv_back);
    ivBack.setOnClickListener(this);
    fabToggleMapList = findViewById(R.id.nearby_fab_map_list);
    fabToggleMapList.setOnClickListener(this);
    dialog = new ProgressDialog(this);
    placesList = new ArrayList<Result>();
    recyclerView = findViewById(R.id.nearby_recycler_view);
  }

  private void initBottomSheet() {
    View bottomSheetView = findViewById(R.id.nearby_bottom_sheet);
    bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    bottomSheetBehavior.setHideable(false);

    btnDoctor = bottomSheetView.findViewById(R.id.btm_list_iv_doctor);
    btnDentist = bottomSheetView.findViewById(R.id.btm_list_iv_dentist);
    btnHospital = bottomSheetView.findViewById(R.id.btm_list_iv_hospital);
    btnPharmacy = bottomSheetView.findViewById(R.id.btm_list_iv_pharmacy);
    btnToggleBottomSheet = bottomSheetView.findViewById(R.id.btm_sheet_btn_toggle);
    tvSelectedStatus = bottomSheetView.findViewById(R.id.btm_sheet_tv_status);

    btnDoctor.setOnClickListener(this);
    btnDentist.setOnClickListener(this);
    btnHospital.setOnClickListener(this);
    btnPharmacy.setOnClickListener(this);
    btnToggleBottomSheet.setOnClickListener(this);
  }

  private void initToolbar() {
    toolbar = findViewById(R.id.nearby_tool_bar);
    tvToolbarTitle = findViewById(R.id.nearby_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("Nearby");
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.nearby_toolbar_iv_back: {
        startActivity(new Intent(NearbyActivity.this, HomeActivity.class));
        finish();
        break;
      }
      case R.id.btm_list_iv_doctor: {
        showNearbyDoctors();
        break;
      }
      case R.id.btm_list_iv_dentist: {
        showNearbyDentists();
        break;
      }
      case R.id.btm_list_iv_hospital: {
        showNearbyHospitals();
        break;
      }
      case R.id.btm_list_iv_pharmacy: {
        showNearbyPharmacy();
        break;
      }
      case R.id.nearby_fab_map_list:
        toggleListOrMap();

    }

  }


  private void fetchLastLocation() {
    @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient
        .getLastLocation();
    task.addOnSuccessListener(new OnSuccessListener<Location>() {
      @Override
      public void onSuccess(Location location) {
        if (location != null) {
          currentLocation = location;
          Toast.makeText(NearbyActivity.this,
              currentLocation.getLatitude() + " " + currentLocation.getLongitude(),
              Toast.LENGTH_SHORT).show();
          mapFragment = (SupportMapFragment) getSupportFragmentManager()
              .findFragmentById(R.id.frag_map);
          mapFragment.getMapAsync(NearbyActivity.this);
        } else {
          Toast.makeText(NearbyActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    gMap = googleMap;
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    gMap.getUiSettings().setZoomControlsEnabled(true);
    gMap.setBuildingsEnabled(true);
    gMap.setMyLocationEnabled(true);

    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    showMarker(latLng, "Current Location");
  }

  private void showMarker(LatLng latLng, String title) {
    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
    gMap.addMarker(markerOptions);
    gMap.animateCamera(CameraUpdateFactory
        .newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
            14));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
    switch (requestCode) {
      case LOCATION_REQUEST_CODE:
        if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
          fetchLastLocation();
        } else {
          Toast.makeText(NearbyActivity.this, "Location permission missing", Toast.LENGTH_SHORT)
              .show();
        }
        break;
    }
  }


  private void showNearbyPharmacy() {
    tvSelectedStatus.setText("Showing Nearby Pharmacies");
    fetchPlacesFromAPI("pharmacy");
  }

  private void showNearbyHospitals() {
    tvSelectedStatus.setText("Showing Nearby Hospitals");
    fetchPlacesFromAPI("hospital");
  }

  private void showNearbyDentists() {
    tvSelectedStatus.setText("Showing Nearby Dentists");
    fetchPlacesFromAPI("dentist");
  }

  private void showNearbyDoctors() {
    tvSelectedStatus.setText("Showing Nearby Doctors");
    fetchPlacesFromAPI("doctor");
  }

  private void toggleListOrMap() {

    if (TOGGLE_FAB == 0) {
      // then show map with markers
      TOGGLE_FAB = 1;
      fabToggleMapList.setImageResource(R.drawable.ic_show_list);
      loadMapWithPlaceMarkers();

    } else {
      // show recycler view
      TOGGLE_FAB = 0;
      fabToggleMapList.setImageResource(R.drawable.ic_show_map);
      loadRecyclerViewWithPlaceDetails();
    }
  }

  private void loadMapWithPlaceMarkers() {
    recyclerView.setVisibility(View.GONE);
    mapFragment.getView().setVisibility(View.VISIBLE);

    gMap.clear();
    if (!placesList.isEmpty()) {
      for (int i = 0; i < placesList.size(); i++) {
        Result result = placesList.get(i);
        LatLng latLng = new LatLng(result.getGeometry().getLocation().getLat(),
            result.getGeometry().getLocation().getLng());
        showMarker(latLng, result.getName(), result.getPlaceId());
      }
    }
  }

  private void showMarker(LatLng latLng, String title, final String placeId) {
    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
    gMap.addMarker(markerOptions);
    gMap.animateCamera(CameraUpdateFactory
        .newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
            14));

    gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
      @Override
      public void onInfoWindowClick(Marker marker) {
        Intent lauchPlaceDetailsActivity = new Intent(NearbyActivity.this, PlaceDetailsActivity.class);
        lauchPlaceDetailsActivity.putExtra("placeID", placeId);
        startActivity(lauchPlaceDetailsActivity);
      }
    });
  }

  private void loadRecyclerViewWithPlaceDetails() {
    mapFragment.getView().setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
    recyclerViewAdapter = new RecyclerViewAdapter(NearbyActivity.this, currentLocation, placesList);
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        LinearLayout.VERTICAL);
    recyclerView.addItemDecoration(dividerItemDecoration);
    recyclerView.setAdapter(recyclerViewAdapter);
  }

  private void fetchPlacesFromAPI(String type) {
    dialog.setMessage("Loading...");
    dialog.show();

    if (!placesList.isEmpty()) {
      placesList.clear();
    }

    service = ApiClient.getRetrofitInstance().create(NearPlaceInterface.class);

    String location = currentLocation.getLatitude() + ", " + currentLocation.getLongitude();

    Call<NearbyPlacesModel> call = service.getNearbyPlace(location, 5000, type, API_KEY);

    call.enqueue(new Callback<NearbyPlacesModel>() {
      @Override
      public void onResponse(Call<NearbyPlacesModel> call, Response<NearbyPlacesModel> response) {
        List<Result> results = response.body().getResults();
        placesList.addAll(results);
        loadMapWithPlaceMarkers();
        dialog.dismiss();

      }

      @Override
      public void onFailure(Call<NearbyPlacesModel> call, Throwable t) {
        dialog.dismiss();
        Utils.makeToast(NearbyActivity.this, t.getMessage());
      }
    });


  }


}