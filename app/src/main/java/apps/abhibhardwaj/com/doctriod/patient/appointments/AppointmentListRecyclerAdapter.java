package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class AppointmentListRecyclerAdapter extends RecyclerView.Adapter<AppointmentListRecyclerAdapter.ViewHolder> {

  Context context;
  String uid;
  private ArrayList<AppointmentInfo> appointmentList;

  public AppointmentListRecyclerAdapter(Context context,
      ArrayList<AppointmentInfo> appointmentList, String uid) {
    this.context = context;
    this.appointmentList = appointmentList;
    this.uid = uid;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    View view = LayoutInflater.from(context).inflate(R.layout.layout_appointment_list_single_item, viewGroup, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    final AppointmentInfo appointmentInfo = appointmentList.get(i);

    viewHolder.tvDocName.setText(appointmentInfo.getDoctorName());
    viewHolder.tvSpecialization.setText(appointmentInfo.getDocSpec());
    viewHolder.tvAddress.setText(appointmentInfo.getDocAddess());
    viewHolder.tvContact.setText(appointmentInfo.getDocPhone());
    viewHolder.tvAppointDate.setText(appointmentInfo.getTime());


    viewHolder.setItemSelectedListener(new RecyclerItemSelectedListener() {
      @Override
      public void onItemSelectedListener(View view, int position) {
        DocumentReference currentAppointemnt = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("appointments")
            .document(appointmentInfo.getAppointmentID());


        currentAppointemnt.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Utils.makeToast(context, "Appointment Deleted Successfully !!");
          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
           Utils.makeToast(context, e.getMessage());
          }
        });


      }
    });

  }

  @Override
  public int getItemCount() {
    return appointmentList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements OnLongClickListener {


    TextView tvDocName, tvSpecialization, tvLocation, tvAppointDate, tvContact, tvAddress;

    RecyclerItemSelectedListener itemSelectedListener;

    public void setItemSelectedListener(
        RecyclerItemSelectedListener itemSelectedListener) {
      this.itemSelectedListener = itemSelectedListener;
    }


    public ViewHolder(@NonNull View view) {
      super(view);

      tvDocName = view.findViewById(R.id.apt_doc_name);
      tvSpecialization = view.findViewById(R.id.apt_doc_specialization);
      tvLocation = view.findViewById(R.id.apt_doc_location);
      tvAppointDate = view.findViewById(R.id.apt_timing);
      tvContact = view.findViewById(R.id.apt_doc_phone);
      tvAddress = view.findViewById(R.id.apt_doc_address);

      itemView.setOnLongClickListener(this);

    }

    @Override
    public boolean onLongClick(View v) {
      itemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
      return true;
    }
  }
}
