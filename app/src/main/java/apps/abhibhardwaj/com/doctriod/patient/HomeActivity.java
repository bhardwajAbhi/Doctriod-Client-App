package apps.abhibhardwaj.com.doctriod.patient;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private TextView tvTitle;
  private ImageView ivMenu, ivClose;
  private DrawerLayout drawerLayout;
  private NavigationView navigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    initToolBar();

    ivMenu = findViewById(R.id.iv_menu);
    drawerLayout = findViewById(R.id.drawer_layout);
    navigationView = findViewById(R.id.navigation_view);
    ivClose = navigationView.getHeaderView(0).findViewById(R.id.iv_btn_close);

    ivClose.setOnClickListener(this);
    ivMenu.setOnClickListener(this);

  }

  private void initToolBar() {
    toolbar = findViewById(R.id.tool_bar);
    tvTitle = findViewById(R.id.tv_title);
    setSupportActionBar(toolbar);
    tvTitle.setText(toolbar.getTitle());
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_btn_close: {
        closeNavDrawer();
        break;
      }
      case R.id.iv_menu: {
        toggleNavDrawer();
        break;
      }
    }
  }

  private void closeNavDrawer() {
    drawerLayout.closeDrawer(Gravity.START);
  }

  private void toggleNavDrawer() {
    if (drawerLayout.isDrawerOpen(Gravity.START)) {
      drawerLayout.closeDrawer(Gravity.START);
    } else {
      drawerLayout.openDrawer(Gravity.START);
    }
  }
}
