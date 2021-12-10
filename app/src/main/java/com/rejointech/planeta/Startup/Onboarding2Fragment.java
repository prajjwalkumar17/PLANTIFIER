package com.rejointech.planeta.Startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;


public class Onboarding2Fragment extends Fragment {
    AppCompatButton onboarding2_loginbot;
    AppCompatButton onboarding2_skipfornowbot;
    LinearLayout onboarding2_Registerbot;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_onboarding2, container, false);
        onboarding2_loginbot =root.findViewById(R.id.onboarding2_loginbot);
        onboarding2_skipfornowbot =root.findViewById(R.id.onboarding2_skipfornowbot);
        onboarding2_Registerbot =root.findViewById(R.id.onboarding2_Registerbot);
        onboarding2_Registerbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerFragment()).addToBackStack(null).commit();

            }
        });

        onboarding2_skipfornowbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), HomeActivityContainer.class);
                startActivity(intent);
            }
        });
        onboarding2_loginbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new loginFragment()).addToBackStack(null).commit();
            }
        });
        return root;
    }
}