package com.rejointech.planeta.Startup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class registerFragment extends Fragment {

    AppCompatButton register_loginbot;
    AppCompatButton register_registerbot;
    AppCompatEditText register_nameedittext, register_emailedittext, register_phoneedittext, register_passwordedittext, register_passwordconfirmedittext;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private Context thiscontext;
    String Name, Email, Phone, Password, PasswordConfirmed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        InitViews(root);
        ButtonClicks(root);
        return root;
    }

    private void InitViews(View root) {
        register_loginbot = root.findViewById(R.id.register_loginbot);
        register_registerbot = root.findViewById(R.id.register_registerbot);
        register_nameedittext = root.findViewById(R.id.register_nameedittext);
        register_emailedittext = root.findViewById(R.id.register_emailedittext);
        register_phoneedittext = root.findViewById(R.id.register_phoneedittext);
        register_passwordedittext = root.findViewById(R.id.register_passwordedittext);
        register_passwordconfirmedittext = root.findViewById(R.id.register_passwordconfirmedittext);
    }


    private void ButtonClicks(View root) {
        register_registerbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = register_nameedittext.getText().toString();
                Email = register_emailedittext.getText().toString();
                Phone = register_phoneedittext.getText().toString();
                PasswordConfirmed = register_passwordconfirmedittext.getText().toString();
                Password = register_passwordedittext.getText().toString();


                if (Name.length() == 0 ||
                        Email.length() == 0 ||
                        Phone.length() != 10 ||
                        Password.length() == 0 ||
                        PasswordConfirmed.length() == 0 ||
                        !Password.equals(PasswordConfirmed)) {
                    CommonMethods.DisplayShortTOAST(thiscontext, "Check the filled details Properly");

                } else {
                    CommonMethods.DisplayLongTOAST(thiscontext, "wait for some time don't Press anything \nVerification still in progress");
                    sendotptophone(Phone);
                }
            }
        });

        register_loginbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new loginFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void RegisterUser(String name, String email, String phone, String passwordConfirmed, String password) {
        String url = Constants.signupurl;
        APICall.okhttpmaster().newCall(
                APICall.post4signup(APICall.urlbuilderforhttp(url),
                        APICall.buildrequestbody4signup(name,
                                email,
                                phone,
                                passwordConfirmed,
                                password))

        ).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonMethods.DisplayLongTOAST(thiscontext, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responsez = new JSONObject(myResponse);
                            JSONObject Signup = responsez.getJSONObject(getString(R.string.Registger_Maindetails));
                            String token = responsez.optString(getString(R.string.Register_Token));
                            String status = responsez.optString("status");
                            String name = Signup.optString("name");
                            String email = Signup.optString("email");
                            String phone = Signup.optString("phone");
                            String role = Signup.optString("role");
                            String id = Signup.optString("_id");
                            Savedatatoprefs(token, name, email, phone, role, id);
                            if (status.equals("success")) {
//                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerotpvalidationFragment()).commit();
//                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerconformationFragment()).commit();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

    }


    private void Savedatatoprefs(String token, String name, String email, String phone, String role, String id) {
        preferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(Constants.token, token);
        editor.putString(Constants.prefregistername, name);
        editor.putString(Constants.prefregisteremail, email);
        editor.putString(Constants.prefregisterphone, phone);
        editor.putString(Constants.prefregisterid, id);
        editor.putString(Constants.prefregisterrole, role);
        editor.apply();
    }


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
            public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                preferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString(Constants.prerrfbackendotp, backendotp);
                editor.apply();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerotpvalidationFragment()).commit();
                RegisterUser(Name,
                        Email,
                        Phone,
                        PasswordConfirmed,
                        Password);
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
}