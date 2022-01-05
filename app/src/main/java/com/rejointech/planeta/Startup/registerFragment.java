package com.rejointech.planeta.Startup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class registerFragment extends Fragment {

    AppCompatButton register_loginbot;
    AppCompatButton register_registerbot;
    AppCompatEditText register_nameedittext;
    AppCompatEditText register_emailedittext;
    AppCompatEditText register_phoneedittext;
    AppCompatEditText register_passwordedittext;
    AppCompatEditText register_passwordconfirmedittext;
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
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerotpvalidationFragment()).addToBackStack(null).commit();
                String Name = Objects.requireNonNull(register_nameedittext.getText()).toString();
                String Email = Objects.requireNonNull(register_emailedittext.getText()).toString();
                String Phone = Objects.requireNonNull(register_phoneedittext.getText()).toString();
                String PasswordConfirmed = Objects.requireNonNull(register_passwordconfirmedittext.getText()).toString();
                String Password = Objects.requireNonNull(register_passwordedittext.getText()).toString();

                if(register_nameedittext.getText() != null &&
                        register_emailedittext.getText() != null &&
                        register_phoneedittext.getText() != null &&
                        register_passwordconfirmedittext != null &&
                        register_passwordedittext !=null
                && Password.equals(PasswordConfirmed)){
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
        String url =Constants.signupurl;
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
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responsez = new JSONObject(myResponse);
                            CommonMethods.LOGthesite(Constants.LOG,responsez.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

    }

}