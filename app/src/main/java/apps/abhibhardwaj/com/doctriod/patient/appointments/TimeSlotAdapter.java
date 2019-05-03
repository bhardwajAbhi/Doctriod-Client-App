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
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import java.util.ArrayList;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

  Context context;
  List<TimeSlot> timeSlotList;
  List<CardView> cardViewList;
  LocalBroadcastManager localBroadcastManager;

  public TimeSlotAdapter(Context context) {
    this.context = context;
    this.timeSlotList = new ArrayList<>();
    this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
    cardViewList = new ArrayList<>();
  }

  public TimeSlotAdapter(Context context,
      List<TimeSlot> timeSlotList) {
    this.context = context;
    this.timeSlotList = timeSlotList;
    this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
    cardViewList = new ArrayList<>();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(context)
        .inflate(R.layout.layout_time_slot_single_item, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

    viewHolder.tvTime.setText(new StringBuilder(Common.convertTimeSlotToString(i)));
    if (timeSlotList.size() == 0) {
      viewHolder.cardTimeSlot
          .setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
      viewHolder.tvDesc.setText("Available");
      viewHolder.tvDesc.setTextColor(context.getResources().getColor(R.color.colorGreen));
      viewHolder.tvTime.setTextColor(context.getResources().getColor(R.color.colorBlue));
    } else {
      for (TimeSlot slotValue : timeSlotList) {
        int slot = Integer.parseInt(slotValue.getSlot().toString());
        if (slot == i) {
          viewHolder.cardTimeSlot.setTag(Common.DISABLE_TAG);
          viewHolder.cardTimeSlot
              .setCardBackgroundColor(context.getResources().getColor(R.color.colorGrey));
          viewHolder.tvDesc.setText("Full");
          viewHolder.tvTime.setTextColor(context.getResources().getColor(R.color.colorWhite));
          viewHolder.tvDesc.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
      }
    }

    if (!cardViewList.contains(viewHolder.cardTimeSlot))
      cardViewList.add(viewHolder.cardTimeSlot);

    if (!timeSlotList.contains(i))
    {

      viewHolder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
        @Override
        public void onItemSelectedListener(View view, int position) {
          for (CardView cardView : cardViewList)
          {
            if (cardView.getTag() == null)
              cardView.setCardBackgroundColor(context.getResources().getColor((R.color.colorWhite)));
          }
          viewHolder.cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(R.color.colorGreen));

          Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
          intent.putExtra(Common.KEY_TIME_SLOT, i);
          intent.putExtra(Common.KEY_STEP, 3);
          localBroadcastManager.sendBroadcast(intent);
        }
      });
    }

  }

  @Override
  public int getItemCount() {
    return Common.TIME_SLOT_TOTAL;
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    CardView cardTimeSlot;
    TextView tvTime, tvDesc;

    RecyclerItemSelectedListener recyclerItemSelectedListener;

    public void setRecyclerItemSelectedListener(
        RecyclerItemSelectedListener recyclerItemSelectedListener) {
      this.recyclerItemSelectedListener = recyclerItemSelectedListener;
    }

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      cardTimeSlot = itemView.findViewById(R.id.card_time);
      tvTime = itemView.findViewById(R.id.time_slot_time);
      tvDesc = itemView.findViewById(R.id.time_slot_desc);

      itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
      recyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
    }
  }
}
