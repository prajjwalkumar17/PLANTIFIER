package com.rejointech.planeta.Startup;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rejointech.planeta.R;

public class registerotpvalidationFragment extends Fragment {
    ImageView confrmotp_gobckbot;
    AppCompatButton confrmotp_verify;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_registerotpvalidation, container, false);

        confrmotp_gobckbot=root.findViewById(R.id.confrmotp_gobckbot);
        confrmotp_verify=root.findViewById(R.id.confrmotp_verify);
        confrmotp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer,new registerconformationFragment()).addToBackStack(null).commit();
            }
        });

        confrmotp_gobckbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer,new registerFragment()).addToBackStack(null).commit();
            }
        });
        return root;
    }
}