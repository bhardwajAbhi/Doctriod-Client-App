package apps.abhibhardwaj.com.doctriod.patient.prescription;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class PrescriptionListAdapter extends RecyclerView.Adapter<PrescriptionListAdapter.ViewHolder> {

  Context context;
  ArrayList<Prescription> prescriptionList;

  public PrescriptionListAdapter(Context context,
      ArrayList<Prescription> prescriptionList) {
    this.context = context;
    this.prescriptionList = prescriptionList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(context).inflate(R.layout.layout_prescription_list_single_item, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder vH, final int i) {

    vH.tvDocName.setText(prescriptionList.get(i).getDoctorName());
    vH.tvDocSpec.setText(prescriptionList.get(i).getDoctorSpecialization());
    vH.tvDocAdd.setText(prescriptionList.get(i).getDoctorAddres());
    vH.tvDescription.setText(prescriptionList.get(i).getDescription());

    Picasso.get().load(prescriptionList.get(i).getImageURL()).into(vH.ivImage);

    vH.ivImage.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent viewImageFullScreen = new Intent();
        viewImageFullScreen.setAction(Intent.ACTION_VIEW);
        viewImageFullScreen.setDataAndType(Uri.parse(prescriptionList.get(i).getImageURL()), "image/*");
        context.startActivity(viewImageFullScreen);

      }
    });


  }

  @Override
  public int getItemCount() {
    return prescriptionList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder{

    TextView tvDocName, tvDocSpec, tvDocAdd, tvDescription;
    ImageView ivImage;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      tvDocName = itemView.findViewById(R.id.pres_doc_name);
      tvDocSpec = itemView.findViewById(R.id.pres_doc_specialization);
      tvDocAdd = itemView.findViewById(R.id.pres_doc_location);
      tvDescription = itemView.findViewById(R.id.pres_desc);

      ivImage = itemView.findViewById(R.id.pres_image);


    }
  }
}
