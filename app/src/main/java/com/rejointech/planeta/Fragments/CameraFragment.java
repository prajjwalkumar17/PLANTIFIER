package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.Intent;
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
    Context thiscontext;
    private static final int REQUEST_STORAGE = 112;


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
                Bitmap image = (Bitmap) data.getExtras().get("data");
                //TODO Adding code for getiing path
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = data.getData();
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
        String encoded_pic = Base64.encodeToString(imageinByte, Base64.DEFAULT);
        Postpicfromcamera(encoded_pic);
    }


  /*  public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (thiscontext.getContentResolver() != null) {
            Cursor cursor = thiscontext.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }*/

    private void InitViews(View root) {
        cameraback = root.findViewById(R.id.cameraback);
        camera_cameraopen = root.findViewById(R.id.camera_cameraopen);
        camera_imgselect = root.findViewById(R.id.camera_imgselect);
    }


    private void Postpicfromcamera(String encoded_string) {
        APICall.okhttpmaster().newCall(
                APICall.post4imageupload(APICall.urlbuilderforhttp(Constants.camerauploaderurl),
                        APICall.buildrequstbody4imageupload(encoded_string))).enqueue(new Callback() {
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