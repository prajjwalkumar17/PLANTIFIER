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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.Adapters.AdapterSearchresultsfromupload;
import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.Decoration.DecorationForRecyclerView;
import com.rejointech.planeta.R;
import com.rejointech.planeta.RecyclerClickInterface.RecyclerSearchresultsInterface;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CameraIdentificationFragment extends Fragment {
    Context thiscontext;
    String token, name;
    String encoded_pic;
    ImageView cameraresult_mypic;
    RecyclerView cameraresult_recyclerview;
    AdapterSearchresultsfromupload adapterSearchresultsfromupload;
    RecyclerSearchresultsInterface recyclerSearchresultsInterface;
    RelativeLayout cameraidentification_layout;
    LottieAnimationView cameraanimationView;

    JSONArray resultImages;
    String species_scientificname;
    String family_scientifiname;
    String percentagetoprint;
    String createdBy;
    String timestamp;
    String wikkipediaLink;
    String userimage;
    String species_scientificnametrue;
    String genus_scientifiname;
    Set<String> commonnamesset = new HashSet<String>();
    String score;
    String postid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_camera_identification, container, false);
        initlayout();
        Init_views(root);
        CommonMethods.DisplayLongTOAST(thiscontext, "Please Wait for sometime on each result to make sure Images are Loaded Properly");
        Button_Clicks();


        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    private void Button_Clicks() {
        recyclerSearchresultsInterface = new RecyclerSearchresultsInterface() {
            @Override
            public void onItemClick(View v, int position, JSONObject object) {

                JSONObject data = object.optJSONObject("data");

                JSONObject createdByObj = data.optJSONObject("createdBy");
                postid = data.optString("_id");
                if (createdByObj != null) {
                    createdBy = createdByObj.optString("name");
                }

                timestamp = data.optString("timeStamp");
                wikkipediaLink = data.optString("wikkipediaLink");
                JSONArray userUploadedImage = data.optJSONArray("userUploadedImage");
                userimage = userUploadedImage.optString(0);


                JSONArray posts = data.optJSONArray("posts");
                JSONObject postobject = posts.optJSONObject(position);
                if (postobject != null) {
                    JSONObject species = postobject.optJSONObject("species");
                    species_scientificname = species.optString("scientificNameWithoutAuthor");
                    species_scientificnametrue = species.optString("scientificName");
                    JSONObject genus = species.optJSONObject("genus");
                    genus_scientifiname = genus.optString("scientificNameWithoutAuthor");
                    JSONObject family = species.optJSONObject("family");
                    family_scientifiname = family.optString("scientificNameWithoutAuthor");

                    JSONObject gbif = postobject.optJSONObject("gbif");
                    String gbifid = gbif.optString("id");

                    JSONArray common_namesarray = species.optJSONArray("commonNames");
                    ArrayList<String> common_names = new ArrayList<String>();
                    for (int i = 0; i < common_namesarray.length(); i++) {
                        common_names.add(common_namesarray.optString(i));
                    }

                    resultImages = postobject.optJSONArray("images");

                    ArrayList<String> resimg = new ArrayList<String>();
                    for (int i = 0; i < resultImages.length(); i++) {
                        resimg.add(resultImages.optString(i));
                    }
                    Set<String> resultimagesset = new HashSet<String>();
                    resultimagesset.addAll(resimg);

                    commonnamesset.addAll(common_names);


                    score = postobject.optString("score");
                    Double percentage_match = Double.parseDouble(score) * 100.0;
                    percentagetoprint = new DecimalFormat("##.##").format(percentage_match) + "%";

                    SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.prefdashboardcreatedby, name);
                    editor.putString(Constants.prefdashboardtimestamp, timestamp);
                    editor.putString(Constants.prefdashboardwikilink, wikkipediaLink);
                    editor.putString(Constants.prefdashboardusername, userimage);
                    editor.putString(Constants.prefdashboardgenus_gbif, gbifid);
                    editor.putString(Constants.prefdashboardspeciessceintific_nametrue, species_scientificnametrue);
                    editor.putString(Constants.prefdashboardspeciessceintific_name, species_scientificname);
                    editor.putString(Constants.prefdashboardgenus_scientificname, genus_scientifiname);
                    editor.putString(Constants.prefdashboardgenus_familyname, family_scientifiname);
                    editor.putString(Constants.prefdashboardgenus_score, percentagetoprint);
                    editor.putString(Constants.prefdashboardgenus_postid, postid);
                    editor.putString(Constants.prefdashboard_fromnotes, "0");
                    editor.putString(Constants.prefdashboard_fromcameraidentification, "1");
                    editor.putStringSet(Constants.prefdashboardgenus_commonnames, commonnamesset);
                    editor.putStringSet(Constants.prefdashboardgenus_resultimages, resultimagesset);
                    editor.apply();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new OpenDashboardFragment()).addToBackStack(null).commit();
                }
            }

        };
    }

    private void Init_views(View root) {
        cameraidentification_layout = root.findViewById(R.id.cameraidentification_layout);
        cameraanimationView = root.findViewById(R.id.cameraanimationView);
        showanim();
        cameraresult_mypic = root.findViewById(R.id.cameraresult_mypic);
        cameraresult_recyclerview = root.findViewById(R.id.cameraresult_recyclerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(thiscontext, 1, GridLayoutManager.VERTICAL, false);
        cameraresult_recyclerview.setLayoutManager(gridLayoutManager);
        cameraresult_recyclerview.addItemDecoration(new DecorationForRecyclerView(thiscontext, R.dimen.dp_2));

        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.token, "No data found!!!");
        name = sharedPreferences.getString(Constants.prefregistername, "You");
        SharedPreferences sharedPreferencess = thiscontext.getSharedPreferences(Constants.CAMERAPREFS, Context.MODE_PRIVATE);
        encoded_pic = sharedPreferencess.getString(Constants.prefcamerapicencoded, "No data found!!!");
        byte[] imageAsBytes = Base64.decode(encoded_pic.getBytes(), Base64.DEFAULT);
        cameraresult_mypic.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        SharedPreferences sharedPreferences3 = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS, Context.MODE_PRIVATE);
        String noapicall = sharedPreferences3.getString(Constants.prefdashboard_fromcameraidentification, "0");
        if (noapicall == "2") {
            secondndtimecall();
        } else {
            Postpicfromcamera(encoded_pic);
        }
    }

    private void secondndtimecall() {
        String url1 = Constants.secondtimesearchurl;
        APICall.okhttpmaster().newCall(APICall.post4secondtimeapicall(
                APICall.urlbuilderforhttp(url1),
                APICall.buildrequest4secondtimeapicall(postid)
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
                final String rResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject myResponsez = new JSONObject(rResponse);
                            if (myResponsez.optString("status").equals("fail")) {
                                stopanimation();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new CameraFragment()).commit();
                            } else {
                                adapterSearchresultsfromupload = new AdapterSearchresultsfromupload(myResponsez, recyclerSearchresultsInterface);
                                cameraresult_recyclerview.setAdapter(adapterSearchresultsfromupload);
                                stopanimation();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopanimation();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new CameraFragment()).commit();
                        }
                    }
                });
            }
        });
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
                        CommonMethods.DisplayLongTOAST(thiscontext, e.getMessage());
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
                            if (myResponsez.optString("status").equals("fail")) {
                                stopanimation();
                                CommonMethods.DisplayLongTOAST(thiscontext, "Image not clear enough to get results\nTry again");
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new CameraFragment()).commit();
                            } else {
                                adapterSearchresultsfromupload = new AdapterSearchresultsfromupload(myResponsez, recyclerSearchresultsInterface);
                                cameraresult_recyclerview.setAdapter(adapterSearchresultsfromupload);
                                stopanimation();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopanimation();
                            CommonMethods.DisplayLongTOAST(thiscontext, "Image not clear enough to get results\nTry again");
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new CameraFragment()).commit();
                        }
                    }
                });
            }
        });
    }

    private void showanim() {
        cameraidentification_layout.setVisibility(View.INVISIBLE);
        cameraanimationView.setVisibility(View.VISIBLE);
        CommonMethods.DisplayLongTOAST(thiscontext, "We are travelling all gardens on Earth\nTo get best Results for you\nPlease hold on!!");
    }

    private void stopanimation() {
        cameraidentification_layout.setVisibility(View.VISIBLE);
        cameraanimationView.setVisibility(View.INVISIBLE);
    }

    private void initlayout() {
        ((HomeActivityContainer) getActivity()).setDrawerLocked();
        ((HomeActivityContainer) getActivity()).setbotVisible();
        ((HomeActivityContainer) getActivity()).setfabinvisible();
    }
}