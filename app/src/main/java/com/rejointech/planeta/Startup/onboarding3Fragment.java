package com.rejointech.planeta.Startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;

public class onboarding3Fragment extends Fragment {
    AppCompatButton onboard_nextbot3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding5, container, false);
        onboard_nextbot3 = root.findViewById(R.id.onboard_nextbot3);
        onboard_nextbot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new spalshLoginFragment()).commit();

            }
        });
        return root;
    }
}