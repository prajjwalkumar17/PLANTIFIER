package com.rejointech.planeta.Startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;

public class loginFragment extends Fragment {
    AppCompatButton login_loginbot;
    AppCompatButton login_registerbot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_login, container, false);
        login_loginbot=root.findViewById(R.id.login_loginbot);
        login_registerbot=root.findViewById(R.id.login_registerbot);
        login_registerbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerFragment()).addToBackStack(null).commit();
            }
        });

        login_loginbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), HomeActivityContainer.class);
                startActivity(intent);
            }
        });
        return root;
    }
}