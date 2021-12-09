package com.rejointech.planeta.Startup;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejointech.planeta.R;


public class Onboarding1Fragment extends Fragment {
    AppCompatButton forward_bot;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_onboarding1, container, false);
        forward_bot=root.findViewById(R.id.forward_bot);
        forward_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new Onboarding2Fragment()).addToBackStack(null).commit();
            }
        });
        return root;
    }
}