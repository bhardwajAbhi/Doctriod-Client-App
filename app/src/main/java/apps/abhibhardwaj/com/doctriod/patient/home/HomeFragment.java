package apps.abhibhardwaj.com.doctriod.patient.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

  GridView gridView;
  HomeGridAdapter adapter;


  public HomeFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    gridView = view.findViewById(R.id.home_grid_view);
    adapter = new HomeGridAdapter(getActivity());
    gridView.setAdapter(adapter);

    gridView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
          case 0:
          {
            Utils.makeToast(getActivity(), "you clicked item " + position);
            break;
          }
          case 1:
          {
            Utils.makeToast(getActivity(), "you clicked item " + position);
            break;
          }
          case 2:
          {
            Utils.makeToast(getActivity(), "you clicked item " + position);
            break;
          }
          case 3:
          {
            Utils.makeToast(getActivity(), "you clicked item " + position);
            break;
          }
          case 4:
          {
            Utils.makeToast(getActivity(), "you clicked item " + position);
            break;
          }
          case 5:
          {
            Utils.makeToast(getActivity(), "you clicked item " + position);
            break;
          }
          case 6:
          {
            Utils.makeToast(getActivity(), "you clicked item " + position);
            break;
          }
          case 7:
          {
            Utils.makeToast(getActivity(), "you clicked item " + position);
            break;
          }
        }
      }
    });
  }
}
