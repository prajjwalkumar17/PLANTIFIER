package com.rejointech.planeta.Startup;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejointech.planeta.R;


public class Onboarding1Fragment extends Fragment {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_onboarding1, container, false);



        Handler mainLooperHandler = new Handler(Looper.getMainLooper());

        mainLooperHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getActivity()!=null){

                    Fragment mFragmentB = new Onboarding2Fragment();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.startupviewcontainer, mFragmentB, "FRAGMENTB")
                            .commit();
                }
            }
        }, 2000);

        return root;
    }
}