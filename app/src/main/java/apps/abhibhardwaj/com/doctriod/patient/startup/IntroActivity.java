package apps.abhibhardwaj.com.doctriod.patient.startup;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.HomeActivity;
import apps.abhibhardwaj.com.doctriod.patient.R;

public class IntroActivity extends AppCompatActivity implements OnClickListener, OnPageChangeListener {

  private ViewPager viewPager;
  private LinearLayout linearLayout;

  private IntroAdapter adapter;

  //an array of text view for dots indicator
  private TextView[] dotsIndicator;

  private ImageButton btnNext, btnPrev;
  private Button btnStart;

  private int currentPage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);

    viewPager = findViewById(R.id.view_pager);
    linearLayout = findViewById(R.id.linear_layout);

    btnNext = findViewById(R.id.btn_next);
    btnPrev = findViewById(R.id.btn_prev);
    btnStart = findViewById(R.id.btn_start);

    btnNext.setOnClickListener(this);
    btnPrev.setOnClickListener(this);
    btnStart.setOnClickListener(this);



    adapter = new IntroAdapter(IntroActivity.this);
    viewPager.setAdapter(adapter);
    viewPager.setOnPageChangeListener(this);

    addDotsIndicator(0);

  }

  public void addDotsIndicator(int position)
  {
    dotsIndicator = new TextView[3];
    linearLayout.removeAllViews();
    for (int i = 0; i<dotsIndicator.length; i++)
    {
      dotsIndicator[i] = new TextView(IntroActivity.this);
      dotsIndicator[i].setText("-");
      dotsIndicator[i].setTextSize(45);
      dotsIndicator[i].setPadding(8,0,0,0);
      dotsIndicator[i].setTextColor(getResources().getColor(R.color.colorGrey));
      linearLayout.addView(dotsIndicator[i]);
    }

    if(dotsIndicator.length > 0)
    {
      dotsIndicator[position].setTextColor(getResources().getColor(R.color.colorBlue));
    }

  }


  @Override
  public void onPageScrolled(int i, float v, int i1) {

  }

  @Override
  public void onPageSelected(int i) {
    addDotsIndicator(i);

    currentPage = i;

    if(i == 0)
    {
      btnNext.setEnabled(true);

      btnPrev.setEnabled(false);
      btnPrev.setVisibility(View.INVISIBLE);

      btnStart.setEnabled(false);
      btnStart.setVisibility(View.INVISIBLE);
    }
    else if(i == dotsIndicator.length - 1)
    {
      btnNext.setEnabled(false);
      btnNext.setVisibility(View.INVISIBLE);

      btnPrev.setEnabled(false);
      btnPrev.setVisibility(View.INVISIBLE);

      btnStart.setEnabled(true);
      btnStart.setVisibility(View.VISIBLE);
    }
    else
    {
      btnNext.setEnabled(true);
      btnNext.setVisibility(View.VISIBLE);

      btnPrev.setEnabled(true);
      btnPrev.setVisibility(View.VISIBLE);

      btnStart.setEnabled(false);
      btnStart.setVisibility(View.INVISIBLE);
    }
  }

  @Override
  public void onPageScrollStateChanged(int i) {

  }

  @Override
  public void onClick(View v) {

    switch (v.getId())
    {
      case R.id.btn_next:
      {
        viewPager.setCurrentItem(currentPage + 1);
        break;
      }
      case R.id.btn_prev:
      {
        viewPager.setCurrentItem(currentPage - 1);
        break;
      }
      case R.id.btn_start:
      {
        startActivity(new Intent(IntroActivity.this, HomeActivity.class));
        finish();
        break;
      }
    }


  }
}
