package apps.abhibhardwaj.com.doctriod.patient.appointments;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
  private int space;

  public SpaceItemDecoration(int space) {
    this.space = space;
  }


  @Override
  public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
      @NonNull RecyclerView parent, @NonNull State state) {
    super.getItemOffsets(outRect, view, parent, state);

    outRect.left = outRect.top = outRect.right = outRect.bottom = space;

  }
}
