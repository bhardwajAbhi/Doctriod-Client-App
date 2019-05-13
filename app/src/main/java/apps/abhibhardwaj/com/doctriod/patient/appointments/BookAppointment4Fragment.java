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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.models.User;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookAppointment4Fragment extends Fragment implements OnClickListener {

  SimpleDateFormat simpleDateFormat;
  LocalBroadcastManager localBroadcastManager;

  TextView tvDocName, tvSpecialization, tvLocation, tvAppointDate, tvContact, tvAddress;
  Button btnConfirmApt;
  CircleImageView ivImage;

  public static BookAppointment4Fragment instance;

  private FirebaseAuth auth;
  private FirebaseUser currentUser;
  private FirebaseFirestore db;
  private User user = null;

  private AlertDialog dialog;


  BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        setData();
    }
  };

  private void setData() {
    tvDocName.setText(Common.currentDoctor.getName());
    tvSpecialization.setText(Common.currentDoctor.getSpecialization());
    tvLocation.setText(Common.currentDoctor.getLocation());
    tvContact.setText(Common.currentDoctor.getPhone());
    tvAddress.setText(Common.currentDoctor.getAddress());
    tvAppointDate.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot)).append(" at ").append(simpleDateFormat.format(Common.currentDate.getTime())));
    Picasso.get().load(Common.currentDoctor.getProfileURL()).into(ivImage);
  }


  public static BookAppointment4Fragment getInstance() {
    if (instance == null)
    {
      instance = new BookAppointment4Fragment();
    }
    return instance;
  }


  public BookAppointment4Fragment() {
    // Required empty public constructor
  }




  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_book_appointment4, container, false);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initFirebase();

    simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    localBroadcastManager = LocalBroadcastManager.getInstance(getContext());


    dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();

    tvDocName = view.findViewById(R.id.confirm_apt_doc_name);
    tvSpecialization = view.findViewById(R.id.confirm_apt_doc_specialization);
    tvLocation = view.findViewById(R.id.confirm_apt_doc_location);
    tvAppointDate = view.findViewById(R.id.confirm_apt_timing);
    tvContact = view.findViewById(R.id.confirm_apt_doc_phone);
    tvAddress = view.findViewById(R.id.confirm_apt_doc_address);
    btnConfirmApt = view.findViewById(R.id.apt_btn_confirm_appoint);
    ivImage = view.findViewById(R.id.confirm_apt_doc_img_view);
    btnConfirmApt.setOnClickListener(this);

    localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));




  }

  private void initFirebase() {

    auth = FirebaseAuth.getInstance();
    currentUser = auth.getCurrentUser();
    db = FirebaseFirestore.getInstance();

    db.collection("users").document(currentUser.getUid()).get().addOnSuccessListener(
        new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
             user = documentSnapshot.toObject(User.class);
          }
        });
  }

  @Override
  public void onDestroyView() {
    localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
    super.onDestroyView();
  }

  @Override
  public void onClick(View v) {
    
    switch (v.getId())
    {
      case R.id.apt_btn_confirm_appoint:
      {
        confirmBooking();
        break;
      }
    }

  }

  private void confirmBooking() {
    dialog.show();
    final AppointmentInfo appointmentInfo = new AppointmentInfo();

    appointmentInfo.setDoctorID(Common.currentDoctor.getId());
    appointmentInfo.setDoctorName(Common.currentDoctor.getName());
    appointmentInfo.setCustomerName(user.getFullName());
    appointmentInfo.setCustomerPhone(user.getPhone());
    appointmentInfo.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot)).append(" at ").append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
    appointmentInfo.setSlot(Long.valueOf(Common.currentTimeSlot));
    appointmentInfo.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());

    DocumentReference appointmentDate = FirebaseFirestore.getInstance()
        .collection("AllDoctors")
        .document(Common.city)
        .collection("DocType")
        .document(Common.currentDocType.getId())
        .collection("AvailableDoctors")
        .document(Common.currentDoctor.getId())
        .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
        .document(String.valueOf(Common.currentTimeSlot));


    appointmentDate.set(appointmentInfo)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
              addBookingDetailsToUserProfile(appointmentInfo);
          }
        }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Utils.makeToast(getActivity(), ""+e.getMessage());
      }
    });



  }

  private void addBookingDetailsToUserProfile(AppointmentInfo appointmentInfo) {

    appointmentInfo.setDocSpec(Common.currentDoctor.getSpecialization());
    appointmentInfo.setDocAddess(Common.currentDoctor.getAddress());
    appointmentInfo.setDocPhone(Common.currentDoctor.getPhone());
    appointmentInfo.setAppointmentID(String.valueOf(System.currentTimeMillis()));

    DocumentReference appointmentDate = FirebaseFirestore.getInstance()
        .collection("users")
        .document(currentUser.getUid())
        .collection("appointments")
        .document(String.valueOf(System.currentTimeMillis()));

    appointmentDate.set(appointmentInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        resetStaticData();
        getActivity().finish();
        Utils.makeToast(getActivity(), "Appointment Confirmed !!");
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Utils.makeToast(getActivity(), ""+e.getMessage());
      }
    });

  }

  private void resetStaticData() {
    Common.step = 0;
    Common.currentTimeSlot = -1;
    Common.currentDocType = null;
    Common.currentDoctor = null;
    Common.currentDate.add(Calendar.DATE, 0);
    dialog.dismiss();
  }
}
