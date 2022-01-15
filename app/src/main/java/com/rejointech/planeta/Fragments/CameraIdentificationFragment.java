package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.Adapters.AdapterSearchresultsfromupload;
import com.rejointech.planeta.Decoration.DecorationForRecyclerView;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CameraIdentificationFragment extends Fragment {
    Context thiscontext;
    String token;
    String encoded_pic;
    ImageView cameraresult_mypic;
    RecyclerView cameraresult_recyclerview;
    AdapterSearchresultsfromupload adapterSearchresultsfromupload;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_camera_identification, container, false);
        Init_views(root);
        Button_Clicks();


        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    private void Button_Clicks() {
    }

    private void Init_views(View root) {
        cameraresult_mypic = root.findViewById(R.id.cameraresult_mypic);
        cameraresult_recyclerview = root.findViewById(R.id.cameraresult_recyclerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(thiscontext, 1, GridLayoutManager.VERTICAL, false);
        cameraresult_recyclerview.setLayoutManager(gridLayoutManager);
        cameraresult_recyclerview.addItemDecoration(new DecorationForRecyclerView(thiscontext, R.dimen.dp_2));


        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.token, "No data found!!!");
        SharedPreferences sharedPreferencess = thiscontext.getSharedPreferences(Constants.CAMERAPREFS, Context.MODE_PRIVATE);
        encoded_pic = sharedPreferencess.getString(Constants.prefcamerapicencoded, "No data found!!!");
        byte[] imageAsBytes = Base64.decode(encoded_pic.getBytes(), Base64.DEFAULT);
        cameraresult_mypic.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        Postpicfromcamera(encoded_pic);
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
                            adapterSearchresultsfromupload = new AdapterSearchresultsfromupload(myResponsez);
                            cameraresult_recyclerview.setAdapter(adapterSearchresultsfromupload);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}