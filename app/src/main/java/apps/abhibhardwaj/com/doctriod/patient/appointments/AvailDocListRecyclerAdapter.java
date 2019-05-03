package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
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
import java.util.List;

public class AvailDocListRecyclerAdapter extends RecyclerView.Adapter<AvailDocListRecyclerAdapter.ViewHolder> {

  private Context context;
  private List<Doctor> availDocList;
  List<CardView> cardViewList;
  LocalBroadcastManager localBroadcastManager;

  public AvailDocListRecyclerAdapter(Context context,
      List<Doctor> availDocList) {
    this.context = context;
    this.availDocList = availDocList;
    cardViewList = new ArrayList<>();
    localBroadcastManager = LocalBroadcastManager.getInstance(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(context).inflate(R.layout.layout_avail_doc_list_single_item, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

    viewHolder.docName.setText(availDocList.get(i).getName());
    viewHolder.docSpec.setText(availDocList.get(i).getSpecialization());
    viewHolder.docAdd.setText(availDocList.get(i).getAddress());
    viewHolder.docRating.setText(String.valueOf(availDocList.get(i).getRating()));
    viewHolder.docLoc.setText(availDocList.get(i).getLocation());

    Picasso.get().load(availDocList.get(i).getProfileURL()).into(viewHolder.docImage);

    if (!cardViewList.contains(viewHolder.cardDoctor))
    {
      cardViewList.add(viewHolder.cardDoctor);
    }

    viewHolder.setItemSelectedListener(new RecyclerItemSelectedListener() {
      @Override
      public void onItemSelectedListener(View view, int position) {
        // set background for all item
        for (CardView cardView : cardViewList)
        {
          cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        //set color for selected card
        viewHolder.cardDoctor.setCardBackgroundColor(context.getResources().getColor(R.color.colorGreen));

        //send local broadcast
        Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
        intent.putExtra(Common.KEY_DOCTOR_SELECTED, availDocList.get(position));
        intent.putExtra(Common.KEY_STEP, 2);
        localBroadcastManager.sendBroadcast(intent);

      }
    });



  }

  @Override
  public int getItemCount() {
    return availDocList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    ImageView docImage;
    TextView docName, docSpec, docAdd, docRating, docLoc;
    CardView cardDoctor;

    RecyclerItemSelectedListener itemSelectedListener;

    public void setItemSelectedListener(
        RecyclerItemSelectedListener itemSelectedListener) {
      this.itemSelectedListener = itemSelectedListener;
    }

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      docImage = itemView.findViewById(R.id.doc_list_image);
      docName = itemView.findViewById(R.id.doc_list_name);
      docSpec = itemView.findViewById(R.id.doc_list_specialization);
      docAdd = itemView.findViewById(R.id.doc_list_address);
      docRating = itemView.findViewById(R.id.doc_list_rating);
      docLoc = itemView.findViewById(R.id.doc_list_location);
      cardDoctor = itemView.findViewById(R.id.card_doctor);

      itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
    }
  }
}
