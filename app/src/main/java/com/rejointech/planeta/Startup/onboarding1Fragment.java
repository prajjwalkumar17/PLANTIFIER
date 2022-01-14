package com.rejointech.planeta.Startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;

public class onboarding1Fragment extends Fragment {
    AppCompatButton onboard_nextbot2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_onboarding3, container, false);
        onboard_nextbot2 = root.findViewById(R.id.onboard_nextbot2);
        onboard_nextbot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new onboarding2Fragment()).addToBackStack(null).commit();
            }
        });

        return root;
    }
}