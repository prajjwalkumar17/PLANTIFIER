package com.rejointech.planeta.Startup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.rejointech.planeta.Fragments.DashboardFragment;
import com.rejointech.planeta.R;

public class Startup_container extends AppCompatActivity {
    FrameLayout startupviewcontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_container);
        startupviewcontainer=findViewById(R.id.startupviewcontainer);
        addfragmentforfirst();
    }

    private void addfragmentforfirst() {
        getSupportFragmentManager().beginTransaction().add(R.id.startupviewcontainer,new Onboarding1Fragment()).commit();
    }
}