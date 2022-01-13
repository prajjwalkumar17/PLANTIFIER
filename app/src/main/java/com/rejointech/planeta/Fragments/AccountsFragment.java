package com.rejointech.planeta.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Startup.Startup_container;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AccountsFragment extends Fragment {

    AppCompatEditText account_nameedittext, account_emaileditext, account_phoneeditext;
    AppCompatButton account_logoutbot, account_submitbot;
    TextView account_updatedetailbot, account_updatedpbot;
    SharedPreferences sharedPreferences;
    String token;
    Context thiscontext;
    SharedPreferences.Editor editor;
    CircleImageView circleImageView;


    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    Uri imageUri;
    String myUri = "";
    StorageTask uploadTask;
    StorageReference storageProfilePicsRef;

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
        getuserinfo();
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
                            String status = responsez.optString("status");
                            JSONObject data = responsez.optJSONObject("data");
                            String name = data.optString("name");
                            String email = data.optString("email");
                            String phone = data.optString("phone");

                            if (status.equals("success")) {
                                account_nameedittext.setText(name);
                                account_emaileditext.setText(email);
                                account_phoneeditext.setText(phone);
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
                CommonMethods.DisplayShortTOAST(thiscontext, "You are going to be Logged out");
                sharedPreferences = requireActivity().getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString(Constants.LOGGEDIN, "notloggedin");
                editor.apply();
                Intent intent = new Intent(getActivity(), Startup_container.class);
                startActivity(intent);
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
                edittextinput(true);
                account_submitbot.setEnabled(true);
                String name = account_nameedittext.getText().toString();
                String phone = account_phoneeditext.getText().toString();
                updatedetails(name, phone);
            }
        });
    }

    private void selectimageforprofilepic() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), this);
        getuserinfo();
    }

    private void getuserinfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (snapshot.hasChild("image")) {
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            circleImageView.setImageURI(imageUri);
            uploadimagetofirebase();
        } else {
            CommonMethods.DisplayLongTOAST(thiscontext, "Error Try again after sometime");
        }
    }

    private void uploadimagetofirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(thiscontext);
        progressDialog.setTitle("Set your Profile Pic");
        progressDialog.setMessage("Please enjoy the Natural beauty while we are setting your Profile Pic");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileref = storageProfilePicsRef
                    .child(mAuth.getCurrentUser().getUid() + ".jpg");
            uploadTask = fileref.getFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloaduri = task.getResult();
                        myUri = downloaduri.toString();
                        HashMap<String, Object> usrMap = new HashMap<>();
                        usrMap.put("image", myUri);
                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(usrMap);
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    CommonMethods.LOGthesite(Constants.LOG, e.getMessage());
                }
            });
        } else {
            progressDialog.dismiss();
            CommonMethods.DisplayLongTOAST(thiscontext, "Image not selected");
        }
    }

    private void updatedetails(String name, String phone) {
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

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
    }

    private void edittextinput(Boolean value) {
        account_nameedittext.setEnabled(value);
        account_emaileditext.setEnabled(false);
        account_phoneeditext.setEnabled(value);
    }
}