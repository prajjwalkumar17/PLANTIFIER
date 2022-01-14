package com.rejointech.planeta.Startup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.Constants;


public class splashFragment extends Fragment {

    Context thiscontext;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseuser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

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
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        String loginstatus = sharedPreferences.getString(Constants.LOGGEDIN, "No data found!!!");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseuser = firebaseAuth.getCurrentUser();

        Handler mainLooperHandler = new Handler(Looper.getMainLooper());

        mainLooperHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    if (!loginstatus.equals("loggedin") && firebaseuser == null) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.startupviewcontainer, new onboarding1Fragment(), "FRAGMENTB")
                                .commit();
                    } else {
                        Intent intent = new Intent(getActivity(), HomeActivityContainer.class);
                        startActivity(intent);
                    }
                }
            }
        }, Constants.SPLASH_TIMEOUT);
        return root;
    }
}