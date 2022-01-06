package com.rejointech.planeta.Startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;

public class loginFragment extends Fragment {
    AppCompatButton login_loginbot, login_registerbot;
    AppCompatEditText login_accounidedittext, login_passwordedittext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        InitViews(root);
        ButtonClicks();
        return root;
    }

    private void ButtonClicks() {
        login_registerbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerFragment()).addToBackStack(null).commit();


            }
        });

        login_loginbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivityContainer.class);
                startActivity(intent);
            }
        });
    }

    private void InitViews(View root) {
        login_loginbot = root.findViewById(R.id.login_loginbot);
        login_registerbot = root.findViewById(R.id.login_registerbot);
        login_accounidedittext = root.findViewById(R.id.login_accounidedittext);
        login_passwordedittext = root.findViewById(R.id.login_passwordedittext);
    }
}