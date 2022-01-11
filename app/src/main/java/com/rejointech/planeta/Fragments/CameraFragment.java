package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class CameraFragment extends Fragment {
    ImageView cameraback, camera_cameraopen, camera_imgselect;
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
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
//        Intent intent = new Intent(getActivity(), MainActivity2.class);
//        startActivity(intent);
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
                startActivityForResult(intent, Constants.CAMERA_PICK_PHOTO_FOR_AVATAR);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            CommonMethods.LOGthesite(Constants.LOG, "The Image bitmap is   " + image.toString());
//            imageview.setImageBitmap(image);
        } else if (requestCode == Constants.CAMERA_PICK_PHOTO_FOR_AVATAR) {
            if (data == null) {
                //Display an error
                CommonMethods.LOGthesite(Constants.LOG, "We have an Error");
                return;
            }
            try {
                InputStream inputStream = thiscontext.getContentResolver().openInputStream(data.getData());
                CommonMethods.LOGthesite(Constants.LOG, inputStream.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

    private void InitViews(View root) {
        cameraback = root.findViewById(R.id.cameraback);
        camera_cameraopen = root.findViewById(R.id.camera_cameraopen);
        camera_imgselect = root.findViewById(R.id.camera_imgselect);
    }
}