package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.Adapters.AdapterDashboard;
import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.Decoration.DecorationForRecyclerView;
import com.rejointech.planeta.R;
import com.rejointech.planeta.RecyclerClickInterface.Recyclerdashboardclick;
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

public class DashboardFragment extends Fragment {
    RecyclerView dashboard_recyclerview;
    AdapterDashboard adapterDashboard;
    Context thiscontext;
    Recyclerdashboardclick recyclerdashboardclick;

    JSONArray resultImages;
    String species_scientificname;
    String family_scientifiname;
    String percentagetoprint;
    String createdBy;
    String timestamp;
    String wikkipediaLink;
    String userimage;
    String species_scientificnametrue;
    String genus_scientifiname, gbifid;
    Set<String> commonnamesset = new HashSet<String>();
    String score;
    String postid;
    private ShimmerFrameLayout dashboardshimmmer;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initlayout();
        SharedPreferences sharedPreferences8 = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences8.edit();
        editor.putString(Constants.prefdashboard_fromcameraidentification, "0");
        editor.apply();

        dashboard_recyclerview = root.findViewById(R.id.dashboard_recyclerview);
        dashboardshimmmer = root.findViewById(R.id.dashboardshimmmer);
        shimmersetup();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(thiscontext, 1, GridLayoutManager.VERTICAL, false);
        dashboard_recyclerview.setLayoutManager(gridLayoutManager);
        dashboard_recyclerview.addItemDecoration(new DecorationForRecyclerView(thiscontext, R.dimen.dp_2));

        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor7 = sharedPreferences.edit();
        editor7.putStringSet(Constants.prefdashboardgenus_resultimages, null);


        getalldashboarditems();
        setonclicklistner();
        return root;
    }

    private void initlayout() {
        ((HomeActivityContainer) getActivity()).setToolbarVisible();
        ((HomeActivityContainer) getActivity()).setbotVisible();
        ((HomeActivityContainer) getActivity()).setfabvisible();
    }

    private void shimmersetup() {
        dashboard_recyclerview.setVisibility(View.GONE);
        dashboardshimmmer.setVisibility(View.VISIBLE);
        dashboardshimmmer.startShimmer();

    }

    private void stopshimmer() {
        dashboard_recyclerview.setVisibility(View.VISIBLE);
        dashboardshimmmer.setVisibility(View.GONE);
        dashboardshimmmer.stopShimmer();

    }

    private void setonclicklistner() {
        recyclerdashboardclick = new Recyclerdashboardclick() {
            @Override
            public void onItemClick(View v, int position, JSONObject object) {
                JSONArray dataarray = object.optJSONArray("data");
                JSONObject data = dataarray.optJSONObject(position);
                postid = data.optString("_id");
                JSONObject createdByObj = data.optJSONObject("createdBy");
                if (createdByObj != null) {
                    createdBy = createdByObj.optString("name");
                }

                timestamp = data.optString("timeStamp");
                wikkipediaLink = data.optString("wikkipediaLink");
                JSONArray userUploadedImage = data.optJSONArray("userUploadedImage");
                userimage = userUploadedImage.optString(0);


                JSONArray posts = data.optJSONArray("posts");
                JSONObject postobject = posts.optJSONObject(0);
                if (postobject != null) {
                    JSONObject species = postobject.optJSONObject("species");
                    species_scientificname = species.optString("scientificNameWithoutAuthor");
                    species_scientificnametrue = species.optString("scientificName");
                    JSONObject genus = species.optJSONObject("genus");
                    genus_scientifiname = genus.optString("scientificNameWithoutAuthor");
                    JSONObject family = species.optJSONObject("family");
                    family_scientifiname = family.optString("scientificNameWithoutAuthor");

                    JSONObject gbif = postobject.optJSONObject("gbif");
                    gbifid = gbif.optString("id");

                    JSONArray common_namesarray = species.optJSONArray("commonNames");
                    ArrayList<String> common_names = new ArrayList<String>();
                    for (int i = 0; i < common_namesarray.length(); i++) {
                        common_names.add(common_namesarray.optString(i));
                    }
                    commonnamesset.addAll(common_names);
                    resultImages = postobject.optJSONArray("images");

                    CommonMethods.LOGthesite(Constants.LOG, resultImages.toString());
                    ArrayList<String> resimg = new ArrayList<String>();
                    for (int i = 0; i < resultImages.length(); i++) {
                        resimg.add(resultImages.optString(i));
                    }
                    Set<String> resultimagesset = new HashSet<String>();
                    resultimagesset.addAll(resimg);


                    score = postobject.optString("score");
                    Double percentage_match = Double.parseDouble(score) * 100.0;
                    percentagetoprint = new DecimalFormat("##.##").format(percentage_match) + "%";

                    SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.prefdashboardcreatedby, createdBy);
                    editor.putString(Constants.prefdashboardtimestamp, timestamp);
                    editor.putString(Constants.prefdashboardwikilink, wikkipediaLink);
                    editor.putString(Constants.prefdashboardusername, userimage);
                    editor.putString(Constants.prefdashboardgenus_gbif, gbifid);
                    editor.putStringSet(Constants.prefdashboardgenus_resultimages, resultimagesset);
                    editor.putString(Constants.prefdashboardspeciessceintific_nametrue, species_scientificnametrue);
                    editor.putString(Constants.prefdashboardspeciessceintific_name, species_scientificname);
                    editor.putString(Constants.prefdashboardgenus_scientificname, genus_scientifiname);
                    editor.putString(Constants.prefdashboardgenus_familyname, family_scientifiname);
                    editor.putString(Constants.prefdashboardgenus_score, percentagetoprint);
                    editor.putString(Constants.prefdashboardgenus_postid, postid);
                    editor.putString(Constants.prefdashboard_fromnotes, "0");
                    editor.putStringSet(Constants.prefdashboardgenus_commonnames, commonnamesset);
                    editor.apply();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new OpenDashboardFragment()).addToBackStack(null).commit();
                }
            }
        };
    }

    private void getalldashboarditems() {
        String url = Constants.dashboardmainurl;
        APICall.okhttpmaster().newCall(APICall.get4alldashboarditems(APICall.urlbuilderforhttp(url))).enqueue(new Callback() {
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
                            adapterDashboard = new AdapterDashboard(myResponsez, thiscontext, getActivity(), recyclerdashboardclick);
                            dashboard_recyclerview.setAdapter(adapterDashboard);
                            stopshimmer();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}