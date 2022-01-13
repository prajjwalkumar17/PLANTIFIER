package com.rejointech.planeta.Startup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.Constants;


public class splashFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_onboarding1, container, false);


        //TODO
        // it will open everytime

        Handler mainLooperHandler = new Handler(Looper.getMainLooper());

        mainLooperHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.startupviewcontainer, new onboarding1Fragment(), "FRAGMENTB")
                            .commit();
                }
            }
        }, Constants.SPLASH_TIMEOUT);

        return root;
    }
}