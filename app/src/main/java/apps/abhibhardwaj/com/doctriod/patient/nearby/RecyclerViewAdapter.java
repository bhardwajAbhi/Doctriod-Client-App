package apps.abhibhardwaj.com.doctriod.patient.nearby;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.models.Result;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  Context context;
  private Location currentLocation;
  private ArrayList<Result> placesList;

  public RecyclerViewAdapter(Context context, Location currentLocation,
      ArrayList<Result> placesList) {
    this.context = context;
    this.currentLocation = currentLocation;
    this.placesList = placesList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_nearyby_recycler_view_single_item, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    final Result place = placesList.get(i);
    float[] results = new float[1];
    Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                                place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng(), results);
    Double distance = results[0] *  0.001;
    viewHolder.placeName.setText(place.getName());
    viewHolder.placeAddress.setText(place.getVicinity());
    viewHolder.placeDistance.setText(String.format("%.2f", distance) + " km");

    Picasso.get().load(place.getIcon()).into(viewHolder.placeImage);

  }

  @Override
  public int getItemCount() {
    return placesList.size();
  }


  public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView placeImage;
    TextView placeName, placeAddress, placeDistance;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      placeImage = itemView.findViewById(R.id.iv_place_image);
      placeName = itemView.findViewById(R.id.tv_place_name);
      placeAddress = itemView.findViewById(R.id.tv_place_address);
      placeDistance = itemView.findViewById(R.id.tv_place_distance);


    }
  }
}
