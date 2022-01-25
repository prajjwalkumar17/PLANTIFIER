package com.rejointech.planeta.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Startup.Startup_container;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AccountsFragment extends Fragment {

    AppCompatEditText account_nameedittext, account_emaileditext, account_phoneeditext;
    AppCompatButton account_logoutbot, account_submitbot;
    TextView account_updatedetailbot, account_updatedpbot;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    Context thiscontext;
    CircleImageView circleImageView;
    Uri imageUri;
    private String id;
    ConstraintLayout profilelayout;
    private ShimmerFrameLayout accountshimmmer;
    TextView toolwithbackbothead;


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
        initlayout();
        shimmersetup(root);
        InitViews(root);
        ButtonClicks();
        GetprofileData();
        return root;
    }

    private void initlayout() {
        ((HomeActivityContainer) getActivity()).setToolbarInvisible();
        ((HomeActivityContainer) getActivity()).setbotVisible();
        ((HomeActivityContainer) getActivity()).setfabinvisible();

    }

    private void shimmersetup(View root) {
        profilelayout = root.findViewById(R.id.profilelayout);
        accountshimmmer = root.findViewById(R.id.accountshimmmer);
        toolwithbackbothead = root.findViewById(R.id.toolwithbackbothead);
        toolwithbackbothead.setText("My Profile");
        profilelayout.setVisibility(View.GONE);
        accountshimmmer.setVisibility(View.VISIBLE);
        accountshimmmer.startShimmer();
    }

    private void stopshimmer() {
        profilelayout.setVisibility(View.VISIBLE);
        accountshimmmer.setVisibility(View.GONE);
        accountshimmmer.stopShimmer();

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
                            String status = responsez.optString("status");
                            JSONObject data = responsez.optJSONObject("data");
                            String name = data.optString("name");
                            String email = data.optString("email");
                            String phone = data.optString("phone");

                            if (status.equals("success")) {
                                account_nameedittext.setText(name);
                                account_emaileditext.setText(email);
                                account_phoneeditext.setText(phone);
                                stopshimmer();
                            }
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

                AlertDialog alertDialog = new AlertDialog.Builder(thiscontext).create();
                alertDialog.setTitle("Logout Confirmation");
                alertDialog.setMessage("Are you sure you want to Logout?");
                alertDialog.setIcon(R.drawable.icon_pic_error);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        CommonMethods.DisplayShortTOAST(thiscontext, "You are going to be Logged out");
                        logout();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        account_updatedpbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimageforprofilepic();
            }
        });

        account_updatedetailbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.DisplayLongTOAST(thiscontext, "Edit your name and Email  ");
                edittextinput(true);
                account_submitbot.setVisibility(View.VISIBLE);
            }
        });
        account_submitbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = account_nameedittext.getText().toString();
                String email = account_emaileditext.getText().toString();
                updatedetails(name, email);
                account_submitbot.setVisibility(View.GONE);
            }
        });

    }

    private void logout() {
        sharedPreferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Constants.LOGGEDIN, "notloggedin");
        editor.putString(Constants.token, null);
        editor.apply();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent intent = new Intent(getActivity(), Startup_container.class);
        startActivity(intent);
    }


    private void selectimageforprofilepic() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            circleImageView.setImageURI(imageUri);
            final ProgressDialog progressDialog = new ProgressDialog(thiscontext);
            progressDialog.setTitle("Set your Profile Pic");
            progressDialog.setMessage("Please enjoy the Natural beauty while we are setting your Profile Pic");
            progressDialog.show();
            sharedPreferences = requireActivity().getSharedPreferences(Constants.ACOOUNTSPREF, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString(Constants.prefprofilepic, imageUri.toString());
            editor.apply();
            progressDialog.dismiss();
        } else {
            CommonMethods.DisplayLongTOAST(thiscontext, "Error Try again after sometime");
        }
    }

    private void InitViews(View root) {
        account_nameedittext = root.findViewById(R.id.account_nameedittext);
        account_emaileditext = root.findViewById(R.id.account_emaileditext);
        account_phoneeditext = root.findViewById(R.id.account_phoneeditext);
        account_updatedpbot = root.findViewById(R.id.account_updatedpbot);
        edittextinput(false);
        account_logoutbot = root.findViewById(R.id.account_logoutbot);
        account_submitbot = root.findViewById(R.id.account_submitbot);
        account_updatedetailbot = root.findViewById(R.id.account_updatedetailbot);
        circleImageView = root.findViewById(R.id.circleImageView);
        sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.token, "No data found!!!");
        id = sharedPreferences.getString(Constants.prefregisterid, "No data found!!!");
        sharedPreferences = thiscontext.getSharedPreferences(Constants.ACOOUNTSPREF, Context.MODE_PRIVATE);
        String imageuri = sharedPreferences.getString(Constants.prefprofilepic, "No data found!!!");
        if (!imageuri.equals("No data found!!!")) {
            circleImageView.setImageURI(Uri.parse(imageuri));
        }
    }

    private void updatedetails(String name, String email) {
        APICall.okhttpmaster().newCall(APICall.patch4updateprofile(
                APICall.urlbuilderforhttp(Constants.updateprofileurl)
                , token
                , APICall.buildrequest4updatingprofile(name, email)
        )).enqueue(new Callback() {
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
                        CommonMethods.DisplayShortTOAST(thiscontext, "Details Updated");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new AccountsFragment()).commit();
                    }
                });
            }
        });

    }

    private void edittextinput(Boolean value) {
        account_nameedittext.setEnabled(value);
        account_emaileditext.setEnabled(value);
        account_phoneeditext.setEnabled(false);
    }
}