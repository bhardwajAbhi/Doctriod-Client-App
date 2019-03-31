package apps.abhibhardwaj.com.doctriod.patient.nearby;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.abhibhardwaj.com.doctriod.patient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceMapFragment extends Fragment implements OnMapReadyCallback {

  private GoogleMap gMap;
  private MapView mapView;
  private Double lat, lng;

  public PlaceMapFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_place_map, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Bundle bundle = getArguments();
    lat = Double.valueOf(bundle.getString("Lat"));
    lng = Double.valueOf(bundle.getString("Lng"));


    mapView = view.findViewById(R.id.frag_map_mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    gMap = googleMap;
    LatLng placeLocation = new LatLng(lat, lng);

    gMap.addMarker(new MarkerOptions().position(placeLocation));
    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 14));
  }


  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}
