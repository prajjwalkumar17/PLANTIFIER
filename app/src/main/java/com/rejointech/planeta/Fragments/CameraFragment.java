package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class CameraFragment extends Fragment {
    ImageView cameraback, camera_cameraopen, camera_imgselect;
    private static final int REQUEST_STORAGE = 112;
    Context thiscontext;
    String token;
    String encoded_pic;
    Bitmap image;


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
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        InitViews(root);
        ButtonClicks();
        return root;
    }


    private void ButtonClicks() {
        cameraback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.startupviewcontainer, new DashboardFragment()).commit();
            }
        });

        camera_cameraopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Constants.CAMERA_PIC_REQUEST);


            }
        });
        camera_imgselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, Constants.CAMERA_PICK_PHOTO_FOR_AVATAR);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_PIC_REQUEST) {
            if (data != null) {
                image = (Bitmap) data.getExtras().get("data");
                uploadtheseledimage(image);
            }
        } else if (requestCode == Constants.CAMERA_PICK_PHOTO_FOR_AVATAR) {
            if (data == null) {
                CommonMethods.LOGthesite(Constants.LOG, "We have an Error");
                return;
            }
            Uri path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(thiscontext.getContentResolver(), path);
                uploadtheseledimage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadtheseledimage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] imageinByte = byteArrayOutputStream.toByteArray();
        encoded_pic = Base64.encodeToString(imageinByte, Base64.DEFAULT);

        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.CAMERAPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.prefcamerapicencoded, encoded_pic);
        editor.apply();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new CameraIdentificationFragment()).commit();
    }


    private void InitViews(View root) {
        cameraback = root.findViewById(R.id.cameraback);
        camera_cameraopen = root.findViewById(R.id.camera_cameraopen);
        camera_imgselect = root.findViewById(R.id.camera_imgselect);
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.token, "No data found!!!");
    }

    private void Postpicfromcamera(String encoded_string) {
        APICall.okhttpmaster().newCall(
                APICall.post4imageupload(APICall.urlbuilderforhttp(Constants.camerauploaderurl)
                        , token
                        , APICall.buildrequstbody4imageupload(encoded_string))).enqueue(new Callback() {
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
                final String rResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject myResponsez = new JSONObject(rResponse);
                            String status = myResponsez.optString("status");
                            String results = myResponsez.optString("results");
                            JSONArray data = myResponsez.optJSONArray("data");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}