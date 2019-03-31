package apps.abhibhardwaj.com.doctriod.patient.nearby;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.models.PlaceDetailsModel;
import apps.abhibhardwaj.com.doctriod.patient.models.Result;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailsActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private ImageView ivBack;
  private TextView tvTitle;
  private Button btnCall, btnWeb, btnDirection;

  private TabLayout tabLayout;
  private ViewPager viewPager;


  private ProgressDialog dialog;
  private static String PLACE_ID = null;
  private static final String API_KEY = "AIzaSyB0mzGO2Yn9fl8RCzWFFCdp8_6zfz9Rerc";
  private PlaceDetailsInterface service;
  private Result pladeDetails;


  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_place_details);

    loadPlaceDetails();
  }

  private void loadPlaceDetails() {
    dialog = new ProgressDialog(this);
    dialog.setMessage("Loading Details ...");
    dialog.show();
    PLACE_ID = getIntent().getStringExtra("placeID");

    service = ApiClient.getRetrofitInstance().create(PlaceDetailsInterface.class);

    Call<PlaceDetailsModel> call = service.getPlaceDetails(PLACE_ID, API_KEY);

    call.enqueue(new Callback<PlaceDetailsModel>() {
      @Override
      public void onResponse(Call<PlaceDetailsModel> call, Response<PlaceDetailsModel> response) {
        pladeDetails = response.body().getResult();
        if (pladeDetails != null)
        {
          initToolBar();
          initViews();
          initAdapters();
        }
        dialog.dismiss();
      }

      @Override
      public void onFailure(Call<PlaceDetailsModel> call, Throwable t) {
        Utils.makeToast(PlaceDetailsActivity.this, t.getMessage());
      }
    });


  }


  private void initToolBar() {
    toolbar = findViewById(R.id.placedetails_tool_bar);
    setSupportActionBar(toolbar);
    tvTitle = findViewById(R.id.placedetails_toolbar_tv_title);
    tvTitle.setText(pladeDetails.getName());
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void initViews() {
    tabLayout = findViewById(R.id.placedetails_tab_layout);
    viewPager = findViewById(R.id.placedetails_view_pager);
    ivBack = findViewById(R.id.placedetails_toolbar_iv_back);
    btnCall = findViewById(R.id.placedetails_button_call);
    btnWeb = findViewById(R.id.placedetails_button_web);
    btnDirection = findViewById(R.id.placedetails_button_direction);

    ivBack.setOnClickListener(this);
    btnCall.setOnClickListener(this);
    btnWeb.setOnClickListener(this);
    btnDirection.setOnClickListener(this);
  }

  private void initAdapters() {
      ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), pladeDetails);
      viewPager.setAdapter(adapter);
      tabLayout.setupWithViewPager(viewPager);
  }

  @Override
  public void onClick(View v) {
      switch (v.getId())
      {
        case R.id.placedetails_toolbar_iv_back:
        {
          startActivity(new Intent(PlaceDetailsActivity.this, NearbyActivity.class));
          finish();
          break;
        }

        case R.id.placedetails_button_call:
        {
          makePhoneCall();
          break;
        }
        case R.id.placedetails_button_web:
        {
          openWebsite();
          break;
        }
        case R.id.placedetails_button_direction:
        {
          openGoogleMaps();
          break;
        }
      }
  }

  private void makePhoneCall() {
    String phone = pladeDetails.getInternationalPhoneNumber();
    Intent makeCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
    startActivity(makeCall);
  }

  private void openWebsite() {
    String url = pladeDetails.getWebsite();
    Intent openWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(openWebsite);
  }

  private void openGoogleMaps() {
    String mapUrl = pladeDetails.getUrl();
    Intent openMap = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
    startActivity(openMap);

  }


}
