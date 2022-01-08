package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AccountsFragment extends Fragment {

    AppCompatEditText account_nameedittext, account_emaileditext, account_phoneeditext;
    AppCompatButton account_logoutbot, account_submitbot;
    TextView account_updatedetailbot;
    SharedPreferences sharedPreferences;
    String token;
    Context thiscontext;

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
        View root = inflater.inflate(R.layout.fragment_accounts, container, false);
        InitViews(root);
        GetprofileData();
        ButtonClicks();
        return root;
    }

    private void GetprofileData() {
        String url = Constants.profileurl;
        APICall.okhttpmaster().newCall(APICall.get4profiledata(APICall.urlbuilderforhttp(url), token)).enqueue(new Callback() {
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
                final String myresponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responsez = new JSONObject(myresponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }

    private void ButtonClicks() {
        account_logoutbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        account_updatedetailbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittextinput(true);
                account_submitbot.setEnabled(true);
                String name = account_nameedittext.getText().toString();
                String phone = account_phoneeditext.getText().toString();
                updatedetails(name, phone);
            }
        });
    }

    private void updatedetails(String name, String phone) {
    }

    private void InitViews(View root) {
        account_nameedittext = root.findViewById(R.id.account_nameedittext);
        account_emaileditext = root.findViewById(R.id.account_emaileditext);
        account_phoneeditext = root.findViewById(R.id.account_phoneeditext);
        edittextinput(false);
        account_logoutbot = root.findViewById(R.id.account_logoutbot);
        account_submitbot = root.findViewById(R.id.account_submitbot);
        account_updatedetailbot = root.findViewById(R.id.account_updatedetailbot);
        sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.token, "No data found!!!");
    }

    private void edittextinput(Boolean value) {
        account_nameedittext.setEnabled(value);
        account_emaileditext.setEnabled(false);
        account_phoneeditext.setEnabled(value);

    }
}