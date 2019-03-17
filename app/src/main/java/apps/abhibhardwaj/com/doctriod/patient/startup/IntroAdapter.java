package apps.abhibhardwaj.com.doctriod.patient.startup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;

public class IntroAdapter extends PagerAdapter {

  Context context;
  LayoutInflater layoutInflater;

  private int[] slideImages = { R.drawable.img_intro_one,
      R.drawable.img_intro_two,
      R.drawable.img_intro_three  };


  private String[] headings = { "DOCTORS", "MEDICINE", "APPOINTMENTS"};

  private String[] descriptions = { "Find expert doctors for particular problem on one tap",
      "Alopathic, Ayurvedic and all types of medicines can be bought from here",
      "Book appointments and get best treatment on one tap"  };



  public IntroAdapter(Context context) {
    this.context = context;
  }

  @Override
  public int getCount() {
    return headings.length;
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
    return view == o;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {

    View view = LayoutInflater.from(context).inflate(R.layout.layout_intro_screen, container, false);

    ImageView image = view.findViewById(R.id.iv_image);
    TextView heading = view.findViewById(R.id.tv_heading);
    TextView description = view.findViewById(R.id.tv_description);

    image.setImageResource(slideImages[position]);
    heading.setText(headings[position]);
    description.setText(descriptions[position]);

    container.addView(view);

    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView( (LinearLayout) object);
  }
}
