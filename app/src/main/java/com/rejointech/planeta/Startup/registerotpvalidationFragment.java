package com.rejointech.planeta.Startup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

public class registerotpvalidationFragment extends Fragment {
    ImageView confrmotp_gobckbot;
    AppCompatButton confrmotp_verify;
    AppCompatEditText confrmotp_otpedittext;
    SharedPreferences sharedPreferences;
    Context thiscontext;
    TextView textView8;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_registerotpvalidation, container, false);
        InitViews(root);
        ButtonClicks();
        return root;
    }

    private void ButtonClicks() {
        confrmotp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer,new registerconformationFragment()).addToBackStack(null).commit();
                String otp = confrmotp_otpedittext.getText().toString();
            }
        });

        confrmotp_gobckbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void InitViews(View root) {
        sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Constants.prefregisteremail, "No data found!!!");
        confrmotp_gobckbot = root.findViewById(R.id.confrmotp_gobckbot);
        confrmotp_otpedittext = root.findViewById(R.id.confrmotp_otpedittext);
        confrmotp_verify = root.findViewById(R.id.confrmotp_verify);
        textView8 = root.findViewById(R.id.textView8);
        CommonMethods.LOGthesite(Constants.LOG, email);
        String text = "Please confirm your 4 digit OTP. which is sent on " + email;
        textView8.setText(text);

    }
}