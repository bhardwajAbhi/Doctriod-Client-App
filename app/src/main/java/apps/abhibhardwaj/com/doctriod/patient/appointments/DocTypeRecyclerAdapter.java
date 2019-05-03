package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.app.AlertDialog;
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
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import java.util.ArrayList;
import java.util.List;

public class DocTypeRecyclerAdapter extends RecyclerView.Adapter<DocTypeRecyclerAdapter.ViewHolder> {

  private Context context;
  private List<DocType> doctorTypeList;
  List<CardView> cardViewList;
  LocalBroadcastManager localBroadcastManager;

  public DocTypeRecyclerAdapter(Context context, List<DocType> doctorTypeList) {
    this.context = context;
    this.doctorTypeList = doctorTypeList;
    cardViewList = new ArrayList<>();
    localBroadcastManager = LocalBroadcastManager.getInstance(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    View view = LayoutInflater.from(context).inflate(R.layout.layout_doc_type_single_item, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

    viewHolder.tvDocType.setText(doctorTypeList.get(i).getDocType());

    if (!cardViewList.contains(viewHolder.cardView))
      cardViewList.add(viewHolder.cardView);

    viewHolder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
      @Override
      public void onItemSelectedListener(View view, int position) {

        // set white background for all cards that are not selected
        for (CardView cardView : cardViewList)
          cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));


        // set selected cardView background color
        viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorGreen));

        // send broadcast to tell Appointment activity to enable  NEXT button
        Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
        intent.putExtra(Common.KEY_DOC_TYPE, doctorTypeList.get(position));
        intent.putExtra(Common.KEY_STEP, 1);
        localBroadcastManager.sendBroadcast(intent);



      }
    });
  }

  @Override
  public int getItemCount() {
    return doctorTypeList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    TextView tvDocType;
    CardView cardView;

    RecyclerItemSelectedListener recyclerItemSelectedListener;

    public void setRecyclerItemSelectedListener(
        RecyclerItemSelectedListener recyclerItemSelectedListener) {
      this.recyclerItemSelectedListener = recyclerItemSelectedListener;
    }

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvDocType = itemView.findViewById(R.id.tv_docType);
      cardView = itemView.findViewById(R.id.card_view_doc_type);

      itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
      recyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
    }


  }
}
