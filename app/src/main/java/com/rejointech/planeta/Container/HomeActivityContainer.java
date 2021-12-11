package com.rejointech.planeta.Container;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.rejointech.planeta.CommonInterfaces.botnavController;
import com.rejointech.planeta.Fragments.DashboardFragment;
import com.rejointech.planeta.Fragments.NotesFragment;
import com.rejointech.planeta.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivityContainer extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        botnavController.botVisibilityController {
    NavigationView nav_view;
    DrawerLayout drawer;
    ImageView navBotimg;
    View tool;
    NavController navController;
    Animation rotate;
    BottomNavigationView botnav;
    BottomAppBar completebotnav;
    TextView nav_name, nav_email, nav_filledandtotalentries;
    ProgressBar nav_progress;
    CircleImageView nav_dp;
    String usrtoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_container);
        defaultfragmentonstartup(savedInstanceState);
        muticals();
    }

    private void defaultfragmentonstartup(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new DashboardFragment()).commit();
        }
    }

    private void muticals() {
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);
        navBotimg = findViewById(R.id.toolwithbackbotheadbot);
        tool = findViewById(R.id.tool);
        navController = Navigation.findNavController(this, R.id.maincontainerview);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        botnav = findViewById(R.id.botnav);
        completebotnav = findViewById(R.id.completebotnav);


        NavigationUI.setupWithNavController(nav_view, navController);
        NavigationUI.setupWithNavController(botnav, navController);
        nav_view.setNavigationItemSelectedListener(this);
        manageBottomNavigation(botnav);
        botnav.getMenu().findItem(R.id.botnav_menu_dashboard).setChecked(true);
    }


    private void manageBottomNavigation(BottomNavigationView botnav) {
        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.botnav_menu_notes:
                        getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new NotesFragment()  ).addToBackStack(null).commit();

                        break;
                    case R.id.botnav_menu_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new DashboardFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.botnav_menu_account:
//                        SharedPreferences sharedPreferences = HomeActivityContainer.this.getSharedPreferences(Constants.METAPTEF, MODE_PRIVATE);
////                      getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new contactusFragment()).addToBackStack(null).commit();
//                        CustomTabsIntent.Builder customtabintent = new CustomTabsIntent.Builder();
////                      customtabintent.setToolbarColor(Color.parseColor("#0080000"));
//                        opencustomtabyy(HomeActivity.this, customtabintent.build(), Uri.parse(sharedPreferences.getString(Constants.contactUs, "data not found")));
                        break;
                }
                return true;
            }
        });
    }


    public void clickMenu(View view) {
        openDrawer(drawer);
        navBotimg.setAnimation(rotate);
    }

    private void openDrawer(DrawerLayout drawer) {
        drawer.openDrawer(GravityCompat.START);
    }

    private void closeDrawer(DrawerLayout drawer) {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer(drawer);
        } else {
            super.onBackPressed();
            botnav.getMenu().findItem(R.id.botnav_menu_dashboard).setChecked(true);

        }
    }


    /**
     * Interface Methods implemented
     **/

    @Override
    public void setDrawerLocked() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    @Override
    public void setDrawerunLocked() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    @Override
    public void setToolbarInvisible() {
        tool.setVisibility(View.INVISIBLE);

    }

    @Override
    public void setToolbarVisible() {
        tool.setVisibility(View.VISIBLE);
    }


    @Override
    public void setbotInvisible() {
        botnav.setVisibility(View.INVISIBLE);
        completebotnav.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setbotVisible() {
        botnav.setVisibility(View.VISIBLE);
        completebotnav.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}