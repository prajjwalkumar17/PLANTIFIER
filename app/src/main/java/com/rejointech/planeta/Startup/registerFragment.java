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
import com.google.firebase.auth.PhoneAuthCredential;
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
                String Name = register_nameedittext.getText().toString();
                String Email = register_emailedittext.getText().toString();
                String Phone = register_phoneedittext.getText().toString();
                String PasswordConfirmed = register_passwordconfirmedittext.getText().toString();
                String Password = register_passwordedittext.getText().toString();


                if (Name.length() == 0 ||
                        Email.length() == 0 ||
                        Phone.length() != 10 ||
                        Password.length() == 0 ||
                        PasswordConfirmed.length() == 0 ||
                        !Password.equals(PasswordConfirmed)) {
                    CommonMethods.DisplayShortTOAST(thiscontext, "Check the filled details Properly");

                } else {
                    RegisterUser(Name,
                            Email,
                            Phone,
                            PasswordConfirmed,
                            Password);
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
                            Savedatatoprefs(token, name, email, phone, role);
                            if (status.equals("success")) {
//                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerotpvalidationFragment()).commit();
//                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerconformationFragment()).commit();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerotpvalidationFragment()).commit();
                                sendotptophone(phone);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

    }

    private void sendotptophone(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phone,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        CommonMethods.DisplayShortTOAST(thiscontext, e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        editor = preferences.edit();
                        editor.putString(Constants.prerrfbackendotp, backendotp);
                        editor.apply();

                    }
                }
        );
    }

    private void Savedatatoprefs(String token, String name, String email, String phone, String role) {
        preferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(Constants.token, token);
        editor.putString(Constants.prefregistername, name);
        editor.putString(Constants.prefregisteremail, email);
        editor.putString(Constants.prefregisterphone, phone);
        editor.putString(Constants.prefregisterrole, role);
        editor.apply();
    }

}