package apps.abhibhardwaj.com.doctriod.patient.appointments;


import android.app.AlertDialog;
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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendar.Mode;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookAppointment3Fragment extends Fragment implements TimeSlotLoadListener {

  public static BookAppointment3Fragment instance;

  private DocumentReference doctorRef;
  TimeSlotLoadListener timeSlotLoadListener;
  AlertDialog dialog;

  HorizontalCalendarView calendarView;
  RecyclerView recyclerView;

  SimpleDateFormat simpleDateFormat;
  LocalBroadcastManager localBroadcastManager;


  BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Calendar date = Calendar.getInstance();
      date.add(Calendar.DATE, 0);
      loadAvailableTimeSlotOfDoctor(Common.currentDoctor.getId(),
          simpleDateFormat.format(date.getTime()));
    }
  };

  private void loadAvailableTimeSlotOfDoctor(String id, final String appointmentDate) {
      dialog.show();

      // /AllDoctors/Chandigarh/DocType/Eye/AvailableDoctors/HUMQPHLSQ0y3vVppTaBl

    doctorRef = FirebaseFirestore.getInstance()
        .collection("AllDoctors")
        .document(Common.city)
        .collection("DocType")
        .document(Common.currentDocType.getId())
        .collection("AvailableDoctors")
        .document(Common.currentDoctor.getId());


    doctorRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful())
        {
          DocumentSnapshot documentSnapshot = task.getResult();
          if (documentSnapshot.exists())
          {
            CollectionReference date = FirebaseFirestore.getInstance()
                .collection("AllDoctors")
                .document(Common.city)
                .collection("DocType")
                .document(Common.currentDocType.getId())
                .collection("AvailableDoctors")
                .document(Common.currentDoctor.getId())
                .collection(appointmentDate);


            date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {
                   QuerySnapshot querySnapshot = task.getResult();

                   if (querySnapshot.isEmpty())
                   {
                     timeSlotLoadListener.onTimeSlotLoadEmpty();
                   }
                   else
                   {
                     List<TimeSlot> timeSlots = new ArrayList<>();
                     for (QueryDocumentSnapshot document : task.getResult())
                       timeSlots.add(document.toObject(TimeSlot.class));
                     timeSlotLoadListener.onTimeSlotLoadSucess(timeSlots);
                   }
                }

              }
            }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                timeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
              }
            });
          }
        }
      }
    });



  }


  public static BookAppointment3Fragment getInstance() {
    if (instance == null) {
      instance = new BookAppointment3Fragment();
    }
    return instance;
  }


  public BookAppointment3Fragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_book_appointment3, container, false);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    timeSlotLoadListener = this;
    localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
    localBroadcastManager
        .registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));
    simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
    dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();



  }

  @Override
  public void onDestroyView() {
    localBroadcastManager.unregisterReceiver(displayTimeSlot);
    super.onDestroyView();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    calendarView = view.findViewById(R.id.apt_calendar_view);
    recyclerView = view.findViewById(R.id.apt_step3_recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    recyclerView.addItemDecoration(new SpaceItemDecoration(8));

    //calendar
    Calendar startDate = Calendar.getInstance();
    startDate.add(Calendar.DATE, 0);
    Calendar endDate = Calendar.getInstance();
    endDate.add(Calendar.DATE, 2);    // 2 day left

    HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view,
        R.id.apt_calendar_view)
        .range(startDate, endDate)
        .datesNumberOnScreen(1)
        .mode(Mode.DAYS)
        .defaultSelectedDate(startDate)
        .build();

    horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
      @Override
      public void onDateSelected(Calendar date, int position) {
        if (Common.currentDate.getTimeInMillis() != date.getTimeInMillis())
        {
          Common.currentDate = date;
          loadAvailableTimeSlotOfDoctor(Common.currentDoctor.getId(), simpleDateFormat.format(date.getTime()));
        }
      }
    });
  }

  @Override
  public void onTimeSlotLoadSucess(List<TimeSlot> timeSlotList) {
    TimeSlotAdapter adapter = new TimeSlotAdapter(getContext(), timeSlotList);
    recyclerView.setAdapter(adapter);
    dialog.dismiss();
  }

  @Override
  public void onTimeSlotLoadFailed(String message) {
    dialog.dismiss();
    Utils.makeToast(getActivity(), message);
  }

  @Override
  public void onTimeSlotLoadEmpty() {
      TimeSlotAdapter adapter = new TimeSlotAdapter(getContext());
      recyclerView.setAdapter(adapter);
      dialog.dismiss();
  }
}
