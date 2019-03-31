package apps.abhibhardwaj.com.doctriod.patient.nearby;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.abhibhardwaj.com.doctriod.patient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceReviewFragment extends Fragment {


  public PlaceReviewFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_place_review, container, false);
  }

}
