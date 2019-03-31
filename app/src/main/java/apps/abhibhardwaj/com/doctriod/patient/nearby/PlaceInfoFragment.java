package apps.abhibhardwaj.com.doctriod.patient.nearby;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.models.Result;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceInfoFragment extends Fragment {

  TextView tvTitle, tvAddess;


  public PlaceInfoFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_place_info, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Bundle bundle = getArguments();
    String placeName = bundle.getString("placeName");
    String placeAdd = bundle.getString("placeAddress");

    tvTitle = view.findViewById(R.id.frag_info_place_title);
    tvAddess = view.findViewById(R.id.frag_info_place_address);

    tvTitle.setText(placeName);
    tvAddess.setText(placeAdd);



  }
}
