package apps.abhibhardwaj.com.doctriod.patient.others;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.BuildConfig;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.home.HomeActivity;

public class AboutAppActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private TextView tvToolbarTitle;
  private ImageView ivBack;
  private ImageView twitter, playstore, instagram, linkdin, facebook, github;
  private TextView tvVersion;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about_app);

    initToolbar();
    initViews();
    initListeners();

  }

  private void initToolbar() {
    toolbar = findViewById(R.id.about_tool_bar);
    tvToolbarTitle = findViewById(R.id.about_toolbar_tv_title);
    setSupportActionBar(toolbar);
    tvToolbarTitle.setText("About Developer");
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void initViews() {
    ivBack = findViewById(R.id.about_toolbar_iv_back);
    twitter = findViewById(R.id.iv_twitter);
    playstore = findViewById(R.id.iv_playstore);
    instagram = findViewById(R.id.iv_instagram);
    linkdin = findViewById(R.id.iv_linkdin);
    facebook = findViewById(R.id.iv_facebook);
    github = findViewById(R.id.iv_github);
    tvVersion = findViewById(R.id.tv_version);


    tvVersion.setText("Version : " + BuildConfig.VERSION_NAME);

  }

  private void initListeners() {
    ivBack.setOnClickListener(this);
    twitter.setOnClickListener(this);
    playstore.setOnClickListener(this);
    instagram.setOnClickListener(this);
    linkdin.setOnClickListener(this);
    facebook.setOnClickListener(this);
    github.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {

    if (view == ivBack)
    {
      startActivity(new Intent(this, HomeActivity.class));
      finish();
    }

    if (view == twitter)
    {
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_BROWSABLE);
      intent.setData(Uri.parse("https://twitter.com/_AbhiBhardwaj"));
      startActivity(intent);
    }

    if (view == playstore)
    {
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_BROWSABLE);
      intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id=8676220947948748539"));
      startActivity(intent);
    }

    if (view == instagram)
    {
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_BROWSABLE);
      intent.setData(Uri.parse("https://www.instagram.com/_abhishek___bhardwaj/"));
      startActivity(intent);
    }

    if (view == linkdin)
    {
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_BROWSABLE);
      intent.setData(Uri.parse("https://www.linkedin.com/in/abhishek-bhardwaj-0459974a/"));
      startActivity(intent);
    }

    if (view == facebook)
    {
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_BROWSABLE);
      intent.setData(Uri.parse("https://www.facebook.com/abhishekbhardwaj100"));
      startActivity(intent);
    }

    if (view == github)
    {
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_BROWSABLE);
      intent.setData(Uri.parse("https://bhardwajabhi.github.io/"));
      startActivity(intent);
    }
  }
}
