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

import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class registerotpvalidationFragment extends Fragment {
    ImageView confrmotp_gobckbot;
    AppCompatButton confrmotp_verify, confrmotp_resend;
    AppCompatEditText confrmotp_otpedittext;
    SharedPreferences sharedPreferences;
    Context thiscontext;
    TextView textView8;
    String email;


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
                if (otp.length() == 0) {
                    CommonMethods.DisplayShortTOAST(thiscontext, "Please enter a valid OTP");
                } else {
                    verifyOTP(otp);
                }
            }
        });

//        confrmotp_resend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                verifyOTP();
//            }
//        });

        confrmotp_gobckbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void verifyOTP(String otp) {
        String url = Constants.verifyotpurl;
        APICall.okhttpmaster().newCall(APICall.post4ootpverificfation(
                APICall.urlbuilderforhttp(url),
                APICall.buildreq4otpverification(email, otp)
        )).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonMethods.DisplayShortTOAST(thiscontext, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String Myresponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responsez = new JSONObject(Myresponse);
                            String status = responsez.optString("status");
                            String message = responsez.optString("message");
                            CommonMethods.DisplayShortTOAST(thiscontext, message);
                            if (status.equals("")) {
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerconformationFragment()).commit();
                            } else {
                                CommonMethods.DisplayShortTOAST(thiscontext, "Try again Validation Failed!!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    private void InitViews(View root) {
        sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(Constants.prefregisteremail, "No data found!!!");
        confrmotp_gobckbot = root.findViewById(R.id.confrmotp_gobckbot);
        confrmotp_resend = root.findViewById(R.id.confrmotp_resend);
        confrmotp_otpedittext = root.findViewById(R.id.confrmotp_otpedittext);
        confrmotp_verify = root.findViewById(R.id.confrmotp_verify);
        textView8 = root.findViewById(R.id.textView8);
        CommonMethods.LOGthesite(Constants.LOG, email);
        String text = "Please confirm your 4 digit OTP. which is sent on " + email;
        textView8.setText(text);

    }
}