package apps.abhibhardwaj.com.doctriod.patient.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.nearby.NearbyActivity;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import apps.abhibhardwaj.com.doctriod.patient.recognizemeds.RecognizeMedsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements OnClickListener {

  private Toolbar toolbar;
  private TextView tvTitle, tvUserName, tvUserEmail;
  private ImageView ivMenu, ivClose;
  private CircleImageView ivUserProfile;
  private DrawerLayout drawerLayout;
  private NavigationView navigationView;
  GridView gridView;
  HomeGridAdapter adapter;

  FirebaseUser currentUser;
  DatabaseReference databaseReference;

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
            Utils.makeToast(HomeActivity.this, "you clicked item " + position);
            break;
          }
          case 1:
          {
            Utils.makeToast(HomeActivity.this, "you clicked item " + position);
            break;
          }
          case 2:
          {
            startActivity(new Intent(HomeActivity.this, NearbyActivity.class));
            break;
          }
          case 3:
          {
            Utils.makeToast(HomeActivity.this, "you clicked item " + position);
            break;
          }
          case 4:
          {
            Utils.makeToast(HomeActivity.this, "you clicked item " + position);
            break;
          }
          case 5:
          {
            Utils.makeToast(HomeActivity.this, "you clicked item " + position);
            break;
          }
          case 6:
          {
            startActivity(new Intent(HomeActivity.this, RecognizeMedsActivity.class));
            break;
          }
          case 7:
          {
            Utils.makeToast(HomeActivity.this, "you clicked item " + position);
            break;
          }
        }
      }
    });
  }

  private void initFireBase() {
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    databaseReference = FirebaseDatabase.getInstance().getReference().child("Database")
        .child("Users");
  }

  private void addClickListeners() {
    ivClose.setOnClickListener(this);
    ivMenu.setOnClickListener(this);
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
    databaseReference.child(currentUser.getUid()).child("BasicDetails").addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            tvUserName.setText(dataSnapshot.child("fullName").getValue().toString());
            tvUserEmail.setText(dataSnapshot.child("email").getValue().toString());
            Picasso.get()
                .load(Uri.parse(dataSnapshot.child("profileImageURL").getValue().toString()))
                .into(ivUserProfile);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

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
}
