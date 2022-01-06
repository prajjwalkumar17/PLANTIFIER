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

import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
                String Name = register_nameedittext.getText().toString();
                String Email = register_emailedittext.getText().toString();
                String Phone = register_phoneedittext.getText().toString();
                String PasswordConfirmed = register_passwordconfirmedittext.getText().toString();
                String Password = register_passwordedittext.getText().toString();


                if (Name.length() == 0 ||
                        Email.length() == 0 ||
                        Phone.length() == 0 ||
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
                        CommonMethods.LOGthesite(Constants.LOG, e.getMessage().toString());
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
                            String name = Signup.optString("name");
                            String email = Signup.optString("email");
                            String phone = Signup.optString("phone");
                            String role = Signup.optString("role");
                            Savedatatoprefs(token, name, email, phone, role);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerotpvalidationFragment()).addToBackStack(null).commit();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

    }

    private void Savedatatoprefs(String token, String name, String email, String phone, String role) {
        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.token, token);
        editor.putString(Constants.prefregistername, name);
        editor.putString(Constants.prefregisteremail, email);
        editor.putString(Constants.prefregisterphone, phone);
        editor.putString(Constants.prefregisterrole, role);
        editor.apply();
    }

}