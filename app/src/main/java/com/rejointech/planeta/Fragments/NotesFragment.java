package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.Adapters.AdapterNotefrag;
import com.rejointech.planeta.Decoration.DecorationForRecyclerView;
import com.rejointech.planeta.R;
import com.rejointech.planeta.RecyclerClickInterface.RecyclernotesclickInterface;
import com.rejointech.planeta.RecyclerClickInterface.RecyclernotesshareInterface;
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


public class NotesFragment extends Fragment {

    RecyclerView noterecycler;
    RecyclernotesclickInterface recyclernotesclickInterface;
    RecyclernotesshareInterface recyclernotesshareInterface;
    Context thiscontext;
    String token, name;

    JSONArray resultImages;
    String species_scientificname;
    String family_scientifiname;
    String percentagetoprint;
    String timestamp;
    String wikkipediaLink;
    String userimage, note;
    String species_scientificnametrue;
    String genus_scientifiname;
    Set<String> commonnamesset = new HashSet<String>();
    String score;
    String postid, id;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        noterecycler = root.findViewById(R.id.noterecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(thiscontext, 1, GridLayoutManager.VERTICAL, false);
        noterecycler.setLayoutManager(gridLayoutManager);
        noterecycler.addItemDecoration(new DecorationForRecyclerView(thiscontext, R.dimen.dp_2));
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.token, "No data found!!!");
        name = sharedPreferences.getString(Constants.prefregistername, "You");

        showitems();
        setonclicklistiner();
        return root;
    }

    private void setonclicklistiner() {
        recyclernotesshareInterface = new RecyclernotesshareInterface() {
            @Override
            public void onItemClick(View v, int position, JSONObject object) {
                String status = object.optString("status");
                JSONArray dataarray = object.optJSONArray("data");
                JSONObject dataout = dataarray.optJSONObject(position);
                id = dataout.optString("_id");

                JSONObject useridobj = dataout.optJSONObject("userid");
                name = useridobj.optString("name");
                note = dataout.optString("note");

                JSONObject data = dataout.optJSONObject("postid");
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

                    JSONArray common_namesarray = species.optJSONArray("commonNames");
                    ArrayList<String> common_names = new ArrayList<String>();
                    for (int i = 0; i < common_namesarray.length(); i++) {
                        common_names.add(common_namesarray.optString(i));
                    }
                    commonnamesset.addAll(common_names);
                    resultImages = postobject.optJSONArray("images");

                    score = postobject.optString("score");
                    postid = postobject.optString("_id");
                    Double percentage_match = Double.parseDouble(score) * 100.0;
                    percentagetoprint = new DecimalFormat("##.##").format(percentage_match) + "%";

                    String msg = userimage + "\n\n"
                            + "See this Image clicked by " + name + "\n" + "and Identified by Plantifier Application\n"
                            + "Family :- " + family_scientifiname + "\n"
                            + "Genus :- " + genus_scientifiname + "\n"
                            + "Species :- " + species_scientificname + "\n"
                            + "Common names :- \n" + "\t" + "1. " + common_names.get(0) + "\n" + "\t" + "2. " + common_names.get(1) + "\n\n"
                            + "For more Information visit \n"
                            + wikkipediaLink;

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, msg);
                    intent.setType("text/plain");
                    intent = Intent.createChooser(intent, "Share by");
                    startActivity(intent);
                }
            }
        };
        recyclernotesclickInterface = new RecyclernotesclickInterface() {
            @Override
            public void onItemClick(View v, int position, JSONObject object) {
                String status = object.optString("status");
                JSONArray dataarray = object.optJSONArray("data");
                JSONObject dataout = dataarray.optJSONObject(position);

                JSONObject useridobj = dataout.optJSONObject("userid");
                name = useridobj.optString("name");
                note = dataout.optString("note");
                id = dataout.optString("_id");
                JSONObject data = dataout.optJSONObject("postid");
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

                    JSONArray common_namesarray = species.optJSONArray("commonNames");
                    ArrayList<String> common_names = new ArrayList<String>();
                    for (int i = 0; i < common_namesarray.length(); i++) {
                        common_names.add(common_namesarray.optString(i));
                    }
                    commonnamesset.addAll(common_names);
                    resultImages = postobject.optJSONArray("images");

                    score = postobject.optString("score");
                    postid = postobject.optString("_id");
                    Double percentage_match = Double.parseDouble(score) * 100.0;
                    percentagetoprint = new DecimalFormat("##.##").format(percentage_match) + "%";

                    SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.prefdashboardcreatedby, name);
                    editor.putString(Constants.prefdashboardtimestamp, timestamp);
                    editor.putString(Constants.prefdashboardwikilink, wikkipediaLink);
                    editor.putString(Constants.prefdashboardnoteid, id);
                    editor.putString(Constants.prefdashboardnote, note);
                    editor.putString(Constants.prefdashboardusername, userimage);
                    editor.putString(Constants.prefdashboardspeciessceintific_nametrue, species_scientificnametrue);
                    editor.putString(Constants.prefdashboardspeciessceintific_name, species_scientificname);
                    editor.putString(Constants.prefdashboardgenus_scientificname, genus_scientifiname);
                    editor.putString(Constants.prefdashboardgenus_familyname, family_scientifiname);
                    editor.putString(Constants.prefdashboardgenus_score, percentagetoprint);
                    editor.putString(Constants.prefdashboardgenus_postid, postid);
                    editor.putStringSet(Constants.prefdashboardgenus_commonnames, commonnamesset);
                    editor.putString(Constants.prefdashboard_fromnotes, "1");
                    editor.apply();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new OpenDashboardFragment()).addToBackStack(null).commit();
                }
            }
        };
    }

    private void showitems() {
        String url = Constants.getallnoteurl;
        APICall.okhttpmaster()
                .newCall(APICall.get4allnotes(APICall.urlbuilderforhttp(url)
                        , token)).enqueue(new Callback() {
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
                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(myResponse);
                            AdapterNotefrag adapterNotefrag = new AdapterNotefrag(object,
                                    recyclernotesclickInterface, recyclernotesshareInterface, thiscontext, getActivity());
                            noterecycler.setAdapter(adapterNotefrag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}