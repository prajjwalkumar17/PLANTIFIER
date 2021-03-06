package com.rejointech.planeta.Startup;

import android.content.Context;
import android.content.Intent;
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
import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class loginFragment extends Fragment {
    AppCompatButton login_loginbot, login_registerbot, login_forgotpassbot;
    AppCompatEditText login_accounidedittext, login_passwordedittext;
    Context thiscontext;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String signupcode;

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
        getdetails();
        ButtonClicks();
        return root;
    }

    private void getdetails() {
        SharedPreferences sharedPreferences1 = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        signupcode = sharedPreferences1.getString(Constants.signupsucessfulladdemail, "0");
        if (signupcode.equals("1")) {
            CommonMethods.DisplayShortTOAST(thiscontext, "Login with password");
            String email = sharedPreferences1.getString(Constants.prefregisteremail, "no value found!!!");
            login_accounidedittext.setEnabled(false);
            login_accounidedittext.setText(email);
            loginwithoutemail(email);
            SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.signupsucessfulladdemail, "1");
            editor.apply();
        } else {
            loginwithemail();
        }
    }

    private void loginwithoutemail(String email) {
        login_loginbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.DisplayShortTOAST(thiscontext, "Checking");
                String userpassword = login_passwordedittext.getText().toString();
                if (userpassword.length() != 0) {
                    Signinuser(email, userpassword);
                } else {
                    CommonMethods.DisplayShortTOAST(thiscontext, "Check the filled details Properly");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    private void ButtonClicks() {
        login_forgotpassbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.DisplayLongTOAST(thiscontext, "Contact Distributor for Assistance\nSend us Email to:\nplantifier2022@gmail.com");
            }
        });
        login_registerbot.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new registerFragment()).addToBackStack(null).commit();
        });


    }

    public void loginwithemail() {
        login_loginbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.DisplayShortTOAST(thiscontext, "Checking");
                String useremail = login_accounidedittext.getText().toString();
                String userpassword = login_passwordedittext.getText().toString();
                if (useremail.length() != 0 && userpassword.length() != 0) {
                    Signinuser(useremail, userpassword);
                } else {
                    CommonMethods.DisplayShortTOAST(thiscontext, "Check the filled details Properly");
                }
            }
        });

    }

    private void Signinuser(String useremail, String userpassword) {
        String url = Constants.signinurl;
        APICall.okhttpmaster().newCall(
                APICall.post4signup(APICall.urlbuilderforhttp(url),
                        APICall.buildrequestbody4signin(useremail, userpassword)))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommonMethods.LOGthesite(Constants.LOG, e.getMessage());
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
                                    JSONObject responsee = new JSONObject(myResponse);
                                    String statuscode = responsee.optString("status");
                                    String token = responsee.optString("token");
                                    Addatatoprefs(token);
                                    if (statuscode.equals("200")) {
                                        preferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
                                        editor = preferences.edit();
                                        editor.putString(Constants.LOGGEDIN, "loggedin");
                                        editor.apply();
                                        Intent intent = new Intent(getActivity(), HomeActivityContainer.class);
                                        startActivity(intent);
                                    } else {
                                        CommonMethods.DisplayLongTOAST(thiscontext, "User not registered or wrong password/Email");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                    }
                });
    }

    private void Addatatoprefs(String token) {
        preferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(Constants.token, token);
        editor.apply();
    }

    private void InitViews(View root) {
        login_loginbot = root.findViewById(R.id.login_loginbot);
        login_registerbot = root.findViewById(R.id.login_registerbot);
        login_accounidedittext = root.findViewById(R.id.login_accounidedittext);
        login_forgotpassbot = root.findViewById(R.id.login_forgotpassbot);
        login_passwordedittext = root.findViewById(R.id.login_passwordedittext);
    }
}