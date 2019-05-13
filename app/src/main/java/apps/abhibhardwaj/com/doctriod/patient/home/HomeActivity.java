package apps.abhibhardwaj.com.doctriod.patient.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.appointments.AppointmentsActivity;
import apps.abhibhardwaj.com.doctriod.patient.emergency.EmergencyActivity;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.findoc.FindDoctorActivity;
import apps.abhibhardwaj.com.doctriod.patient.models.User;
import apps.abhibhardwaj.com.doctriod.patient.nearby.NearbyActivity;
import apps.abhibhardwaj.com.doctriod.patient.notifications.MyFirebaseInstanceIDService;
import apps.abhibhardwaj.com.doctriod.patient.notifications.MyFirebaseMessagingService;
import apps.abhibhardwaj.com.doctriod.patient.notifications.NotificationsActivity;
import apps.abhibhardwaj.com.doctriod.patient.others.AboutAppActivity;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import apps.abhibhardwaj.com.doctriod.patient.prescription.MyPrescription;
import apps.abhibhardwaj.com.doctriod.patient.profile.ProfileActivity;
import apps.abhibhardwaj.com.doctriod.patient.recognizemeds.RecognizeMedsActivity;
import apps.abhibhardwaj.com.doctriod.patient.recognizemeds.SearchMedicineActivity;
import apps.abhibhardwaj.com.doctriod.patient.reminder.PillReminder;
import apps.abhibhardwaj.com.doctriod.patient.reminder.PillReminderActivity;
import apps.abhibhardwaj.com.doctriod.patient.startup.LoginActivity;
import apps.abhibhardwaj.com.doctriod.patient.vdoctor.VDoctorActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements OnClickListener,
    OnNavigationItemSelectedListener {

  private Toolbar toolbar;
  private TextView tvTitle, tvUserName, tvUserEmail;
  private ImageView ivMenu, ivClose;
  private CircleImageView ivUserProfile;
  private DrawerLayout drawerLayout;
  private NavigationView navigationView;
  private GridView gridView;
  private HomeGridAdapter adapter;

  private FirebaseAuth auth;
  private FirebaseUser currentUser;
  private FirebaseFirestore db;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    initFireBase();
    initToolBar();
    initViews();
    addClickListeners();
    initNavHeader();
    initGridView();
    // startNotificationServices();
  }

  private void startNotificationServices() {
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      startForegroundService(new Intent(HomeActivity.this, MyFirebaseInstanceIDService.class));
      startForegroundService(new Intent(HomeActivity.this, MyFirebaseMessagingService.class));
      Utils.makeToast(this, "Foreground services started");
    }
    else
    {
      startService(new Intent(HomeActivity.this, MyFirebaseInstanceIDService.class));
      startService(new Intent(HomeActivity.this, MyFirebaseMessagingService.class));
      Utils.makeToast(this, " services started");
    }

  }

  private void initGridView() {
    gridView = findViewById(R.id.home_grid_view);
    adapter = new HomeGridAdapter(HomeActivity.this);
    gridView.setAdapter(adapter);
    gridView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
          case 0:
          {
            startActivity(new Intent(HomeActivity.this, FindDoctorActivity.class));
            break;
          }
          case 1:
          {
            startActivity(new Intent(HomeActivity.this, AppointmentsActivity.class));
            break;
          }
          case 2:
          {
            startActivity(new Intent(HomeActivity.this, NearbyActivity.class));
            break;
          }
          case 3:
          {
            startActivity(new Intent(HomeActivity.this, MyPrescription.class));
            break;
          }
          case 4:
          {
            startActivity(new Intent(HomeActivity.this, PillReminderActivity.class));
            break;
          }
          case 5:
          {
            startActivity(new Intent(HomeActivity.this, EmergencyActivity.class));
            break;
          }
          case 6:
          {
            startActivity(new Intent(HomeActivity.this, SearchMedicineActivity.class));
            break;
          }
          case 7:
          {
            startActivity(new Intent(HomeActivity.this, VDoctorActivity.class));
            break;
          }
        }
      }
    });
  }

  private void initFireBase() {
    auth = FirebaseAuth.getInstance();
    currentUser = auth.getCurrentUser();
    db = FirebaseFirestore.getInstance();
  }

  private void addClickListeners() {
    ivClose.setOnClickListener(this);
    ivMenu.setOnClickListener(this);
    navigationView.setNavigationItemSelectedListener(this);
  }

  private void initViews() {
    ivMenu = findViewById(R.id.iv_menu);
    drawerLayout = findViewById(R.id.drawer_layout);

    navigationView = findViewById(R.id.navigation_view);
    ivClose = navigationView.getHeaderView(0).findViewById(R.id.iv_btn_close);
    ivUserProfile = navigationView.getHeaderView(0).findViewById(R.id.nav_profile_image);
    tvUserName = navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
    tvUserEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);
  }

  private void initNavHeader() {
    db.collection("users").document(currentUser.getUid()).get().addOnSuccessListener(
        new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
            User user = documentSnapshot.toObject(User.class);
            tvUserName.setText(user.getFullName());
            tvUserEmail.setText(user.getEmail());

            Picasso.get().load(user.getProfileImageURL()).into(ivUserProfile);

          }
        }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Utils.makeToast(HomeActivity.this, e.getMessage());
      }
    });
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

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(Gravity.START)) {
      drawerLayout.closeDrawer(Gravity.START);
    } else {
      if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
        new AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                HomeActivity.super.onBackPressed();
              }
            })
            .setNegativeButton("No", null)
            .show();
      } else {
        getSupportFragmentManager().popBackStack();
      }
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // getMenuInflater().inflate(R.menu.options_menu, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId())
    {

      case R.id.item_notification:
      {
        startActivity(new Intent(HomeActivity.this, NotificationsActivity.class));
        return true;
      }

      default:
        return super.onOptionsItemSelected(item);
    }

  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

    switch (menuItem.getItemId())
    {
      case R.id.nav_item_profile:
      {
        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        break;
      }

      case R.id.nav_item_logout:
      {
        auth.signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
        break;
      }

      case R.id.nav_item_appoint:
      {
        startActivity(new Intent(HomeActivity.this, AppointmentsActivity.class));
        break;
      }

      case R.id.nav_item_nearby:
      {
        startActivity(new Intent(HomeActivity.this, NearbyActivity.class));
        break;
      }

      case R.id.nav_item_remind:
      {
        startActivity(new Intent(HomeActivity.this, PillReminderActivity.class));
        break;
      }

      case R.id.nav_item_about:
      {
        startActivity(new Intent(HomeActivity.this, AboutAppActivity.class));
        break;
      }

      default:
      {
        return false;
      }
    }
    return true;
  }
}
