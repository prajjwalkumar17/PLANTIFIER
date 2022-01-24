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

import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class CameraFragment extends Fragment {
    ImageView cameraback, camera_cameraopen, camera_imgselect;
    private static final int REQUEST_STORAGE = 112;
    Context thiscontext;
    String token;
    String encoded_pic;
    Bitmap image;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        initlayout();
        InitViews(root);
        ButtonClicks();
        SharedPreferences sharedPreferences8 = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor7 = sharedPreferences8.edit();

        editor7.putString(Constants.prefdashboard_fromcameraidentification, "0");
        return root;
    }

    private void initlayout() {
        ((HomeActivityContainer) getActivity()).setToolbarInvisible();
        ((HomeActivityContainer) getActivity()).setDrawerLocked();
        ((HomeActivityContainer) getActivity()).setbotVisible();
        ((HomeActivityContainer) getActivity()).setfabinvisible();
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
                CommonMethods.DisplayLongTOAST(thiscontext, "This Feature will be available after sometime \nTry Clicking Pictures");
                /*Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, Constants.CAMERA_PICK_PHOTO_FOR_AVATAR);*/

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageinByte = byteArrayOutputStream.toByteArray();
        encoded_pic = Base64.encodeToString(imageinByte, Base64.DEFAULT);
        CommonMethods.LOGthesite(Constants.LOG, encoded_pic);

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


}