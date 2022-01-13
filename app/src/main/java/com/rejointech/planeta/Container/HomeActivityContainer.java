package com.rejointech.planeta.Container;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.CommonInterfaces.botnavController;
import com.rejointech.planeta.Fragments.AccountsFragment;
import com.rejointech.planeta.Fragments.CameraFragment;
import com.rejointech.planeta.Fragments.DashboardFragment;
import com.rejointech.planeta.Fragments.NotesFragment;
import com.rejointech.planeta.Fragments.QuizFragment;
import com.rejointech.planeta.Fragments.ShareFragment;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeActivityContainer extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        botnavController.botVisibilityController {
    NavigationView nav_view;
    DrawerLayout drawer;
    ImageView navBotimg;
    View tool;
    NavController navController;
    FloatingActionButton home_fab;
    Animation rotate;
    BottomNavigationView botnav;
    BottomAppBar completebotnav;
    TextView nav_name, nav_email;
    CircleImageView nav_dp;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_container);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.token, "No data found!!!");
        GetprofileData();

        defaultfragmentonstartup(savedInstanceState);
        muticals();
    }

    private void defaultfragmentonstartup(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new DashboardFragment()).commit();
        }
    }

    private void GetprofileData() {
        String url = Constants.profileurl;
        APICall.okhttpmaster().newCall(APICall.get4profiledata(APICall.urlbuilderforhttp(url), token)).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonMethods.DisplayShortTOAST(HomeActivityContainer.this, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myresponse = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responsez = new JSONObject(myresponse);
                            String status = responsez.optString("status");
                            JSONObject data = responsez.optJSONObject("data");
                            String name = data.optString("name");
                            String email = data.optString("email");
                            String phone = data.optString("phone");

                            if (status.equals("success")) {
                                nav_name.setText(name);
                                nav_email.setText(email);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private void muticals() {
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);
        navBotimg = findViewById(R.id.toolwithbackbotheadbot);
        tool = findViewById(R.id.tool);
        navController = Navigation.findNavController(this, R.id.maincontainerview);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        botnav = findViewById(R.id.botnav);
        home_fab = findViewById(R.id.home_fab);
        botnav.setBackground(null);
        botnav.getMenu().getItem(2).setEnabled(false);
        completebotnav = findViewById(R.id.completebotnav);

        View headerView = nav_view.getHeaderView(0);
        nav_name = headerView.findViewById(R.id.nav_name);
        nav_email = headerView.findViewById(R.id.nav_email);

        NavigationUI.setupWithNavController(nav_view, navController);
        NavigationUI.setupWithNavController(botnav, navController);
        nav_view.setNavigationItemSelectedListener(this);
        manageBottomNavigation(botnav);
        botnav.getMenu().findItem(R.id.botnav_menu_dashboard).setChecked(true);

        home_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new CameraFragment()).addToBackStack(null).commit();

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void manageBottomNavigation(BottomNavigationView botnav) {
        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.botnav_menu_notes:
                        getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new NotesFragment()).addToBackStack(null).commit();

                        break;
                    case R.id.botnav_menu_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new DashboardFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.botnav_menu_share:
                        getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new ShareFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.botnav_menu_account:
//                        SharedPreferences sharedPreferences = HomeActivityContainer.this.getSharedPreferences(Constants.METAPTEF, MODE_PRIVATE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new AccountsFragment()).addToBackStack(null).commit();
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
        switch (item.getItemId()) {
            case R.id.nav_menu_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new AccountsFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_menu_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new DashboardFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_menu_notes:
                getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new NotesFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_menu_quiz:
                getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new QuizFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_menu_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new ShareFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_menu_settings:
                break;
            case R.id.nav_menu_tutorial:
                break;
            case R.id.nav_menu_telegram:
                break;
            case R.id.nav_menu_Instagram:
                break;
            case R.id.nav_menu_Youtube:
                break;
        }
        closeDrawer(drawer);
        return true;
    }


}