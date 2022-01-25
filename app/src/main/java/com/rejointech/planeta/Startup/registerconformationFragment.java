package com.rejointech.planeta.Startup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.Constants;


public class registerconformationFragment extends Fragment {
    AppCompatButton regconfor_gobckbot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_registerconformation, container, false);
        regconfor_gobckbot=root.findViewById(R.id.regconfor_gobckbot);
        regconfor_gobckbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.signupsucessfulladdemail, "1");
                editor.apply();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new loginFragment()).commit();
            }
        });

        return root;
    }
}