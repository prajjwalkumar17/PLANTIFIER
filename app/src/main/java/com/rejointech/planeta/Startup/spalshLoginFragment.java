package com.rejointech.planeta.Startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;


public class spalshLoginFragment extends Fragment {
    AppCompatButton onboarding2_loginbot;
    AppCompatButton onboarding2_skipfornowbot;
    LinearLayout onboarding2_Registerbot;


//TODO it will open only first time

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

        onboarding2_loginbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new loginFragment()).addToBackStack(null).commit();
            }
        });
        return root;
    }
}