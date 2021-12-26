package com.rejointech.planeta.Fragments;

import android.graphics.RenderEffect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rejointech.planeta.R;


public class CameraFragment extends Fragment {
ImageView cameraback;
ImageView camera_cameraopen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_camera, container, false);
        cameraback=root.findViewById(R.id.cameraback);
        camera_cameraopen=root.findViewById(R.id.camera_cameraopen);

        return root;
    }
}