package apps.abhibhardwaj.com.doctriod.patient.appointments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.abhibhardwaj.com.doctriod.patient.R;
import java.util.ArrayList;

public class BookAppointment2Fragment extends Fragment {

  public static BookAppointment2Fragment instance;

  private RecyclerView recyclerView;

  LocalBroadcastManager localBroadcastManager;

  private BroadcastReceiver availaleDocReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      ArrayList<Doctor> availDocList = intent.getParcelableArrayListExtra(Common.KEY_AVAILABLE_DOC_LIST_LOAD_DONE);
      AvailDocListRecyclerAdapter adapter = new AvailDocListRecyclerAdapter(getActivity(), availDocList);
      recyclerView.addItemDecoration(new SpaceItemDecoration(8));
      recyclerView.setAdapter(adapter);
    }
  };

  public static BookAppointment2Fragment getInstance() {
    if (instance == null)
    {
      instance = new BookAppointment2Fragment();
    }
    return instance;
  }


  public BookAppointment2Fragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_book_appointment2, container, false);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
    localBroadcastManager.registerReceiver(availaleDocReceiver, new IntentFilter(Common.KEY_AVAILABLE_DOC_LIST_LOAD_DONE));

    recyclerView = view.findViewById(R.id.apt_step2_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
  }

  @Override
  public void onDestroyView() {
    localBroadcastManager.unregisterReceiver(availaleDocReceiver);
    super.onDestroyView();
  }
}
