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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import java.util.concurrent.TimeUnit;

public class registerotpvalidationFragment extends Fragment {
    ImageView confrmotp_gobckbot;
    AppCompatButton confrmotp_verify, confrmotp_resend;
    AppCompatEditText confrmotp_otpedittext;
    SharedPreferences sharedPreferences;
    Context thiscontext;
    TextView textView8;
    String phone, backendotp, otp;


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
        View root = inflater.inflate(R.layout.fragment_registerotpvalidation, container, false);
        InitViews(root);
        ButtonClicks();
        return root;
    }

    private void ButtonClicks() {
        confrmotp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = confrmotp_otpedittext.getText().toString().trim();
                if (otp.length() == 0) {
                    CommonMethods.DisplayShortTOAST(thiscontext, "Please enter a valid OTP");
                } else {
                    verifyphoneotp(otp, backendotp);
                }
            }
        });
        confrmotp_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendotptophone(phone);
            }
        });
        confrmotp_gobckbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void verifyphoneotp(String otp, String backendotp) {
        if (backendotp != null && otp != null) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(backendotp, otp);

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                CommonMethods.LOGthesite(Constants.LOG, "signInWithCredential:success");
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerconformationFragment()).commit();
                                FirebaseUser user = task.getResult().getUser();
                            } else {
                                // Sign in failed, display a message and update the UI
                                CommonMethods.DisplayShortTOAST(thiscontext, "Check Internet Connection - Sign in Failed");
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                    CommonMethods.DisplayShortTOAST(thiscontext, "Invalid OTP");
                                }
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    CommonMethods.DisplayShortTOAST(thiscontext, e.getMessage());
                    CommonMethods.LOGthesite(Constants.LOG, e.getMessage());
                }
            });
        } else {
            CommonMethods.DisplayShortTOAST(thiscontext, "Check Internet Connection");
        }
    }


 /*   private void verifyOTP(String otp) {
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
*/

    private void sendotptophone(String phone) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verifyphoneotp(otp, newbackendotp);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void InitViews(View root) {
        sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
//        email = sharedPreferences.getString(Constants.prefregisteremail, "No data found!!!");
        phone = sharedPreferences.getString(Constants.prefregisterphone.toString(), "xxxx-xxxx-xx");
        backendotp = sharedPreferences.getString(Constants.prerrfbackendotp, "No data found!!!");
        confrmotp_gobckbot = root.findViewById(R.id.confrmotp_gobckbot);
        confrmotp_resend = root.findViewById(R.id.confrmotp_resend);
        confrmotp_otpedittext = root.findViewById(R.id.confrmotp_otpedittext);
        confrmotp_verify = root.findViewById(R.id.confrmotp_verify);
        textView8 = root.findViewById(R.id.textView8);
        String text = "Please confirm your 4 digit OTP. which is sent on " + "+91" + phone;
        textView8.setText(text);

    }
}