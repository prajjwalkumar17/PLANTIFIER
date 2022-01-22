package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Set;

public class OpenDashboardFragment extends Fragment {

    TextView recycleropendashboard_speciename, recycleropendashboard_familyname, recycleropendashboard_datetime,
            recycleropendashboard_Percentage, recycleropendashboard_familynametextin, recycleropendashboard_genusnameinner,
            recycleritem_dashboard_createdby, recycleropendashboard_picby, recycleropendashboard_commonname1, recycleropendashboard_speciesnameinner,
            recycleropendashboard_commonname2, recycleropendashboard_commonname3;
    ImageView recycleropendashboard_backbot, recycleropendashboard_image, recycleropendashboard_wiki_bot;
    AppCompatButton recycleropendashboard_savenotebot;
    AppCompatEditText recycleropendashboard_addnoteedittext;
    Context thiscontext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_open_dashboard, container, false);
        Init_views(root);
        retrievedatafromprefandset();


        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    private void retrievedatafromprefandset() {
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS, Context.MODE_PRIVATE);
        String createdBy = sharedPreferences.getString(Constants.prefdashboardcreatedby, "No data found!!!");
        String timestamp = sharedPreferences.getString(Constants.prefdashboardtimestamp, "No data found!!!");
        String wikkipediaLink = sharedPreferences.getString(Constants.prefdashboardwikilink, "No data found!!!");
        String userimage = sharedPreferences.getString(Constants.prefdashboardusername, "No data found!!!");
        String species_scientificnametrue = sharedPreferences.getString(Constants.prefdashboardspeciessceintific_nametrue, "No data found!!!");
        String species_scientificname = sharedPreferences.getString(Constants.prefdashboardspeciessceintific_name, "No data found!!!");
        String genus_scientifiname = sharedPreferences.getString(Constants.prefdashboardgenus_scientificname, "No data found!!!");
        String family_scientifiname = sharedPreferences.getString(Constants.prefdashboardgenus_familyname, "No data found!!!");
        String percentagetoprint = sharedPreferences.getString(Constants.prefdashboardgenus_score, "No data found!!!");
        String postid = sharedPreferences.getString(Constants.prefdashboardgenus_postid, "No data found!!!");
        Set<String> commonnameset = sharedPreferences.getStringSet(Constants.prefdashboardgenus_commonnames, null);
        ArrayList<String> commonnames = new ArrayList<String>();
        commonnames.addAll(commonnameset);
        if (commonnames.size() > 2) {
            recycleropendashboard_commonname1.setText(commonnames.get(0));
            recycleropendashboard_commonname2.setText(commonnames.get(1));
            recycleropendashboard_commonname3.setText(commonnames.get(2));
        } else if (commonnames.size() == 2) {
            recycleropendashboard_commonname1.setText(commonnames.get(0));
            recycleropendashboard_commonname2.setText(commonnames.get(1));
        } else if (commonnames.size() == 1) {
            recycleropendashboard_commonname1.setText(commonnames.get(0));
        } else {
            recycleropendashboard_commonname1.setText("No name found");
            recycleropendashboard_commonname2.setText("No name found");
            recycleropendashboard_commonname3.setText("No name found");
        }

        recycleropendashboard_speciename.setText(species_scientificname);
        recycleropendashboard_familyname.setText(family_scientifiname);
        recycleropendashboard_datetime.setText(timestamp);
        recycleropendashboard_Percentage.setText(percentagetoprint);
        recycleropendashboard_familynametextin.setText(family_scientifiname);
        recycleropendashboard_genusnameinner.setText(genus_scientifiname);
        recycleropendashboard_speciesnameinner.setText(species_scientificnametrue);
        recycleropendashboard_picby.setText(createdBy);
        Picasso.get()
                .load(userimage)
                .error(R.drawable.icon_pic_error)
                .into(recycleropendashboard_image);

    }

    private void Init_views(View root) {
        recycleropendashboard_speciename = root.findViewById(R.id.recycleropendashboard_speciename);
        recycleropendashboard_familyname = root.findViewById(R.id.recycleropendashboard_familyname);
        recycleropendashboard_datetime = root.findViewById(R.id.recycleropendashboard_datetime);
        recycleropendashboard_Percentage = root.findViewById(R.id.recycleropendashboard_Percentage);
        recycleropendashboard_familynametextin = root.findViewById(R.id.recycleropendashboard_familynametextin);
        recycleropendashboard_genusnameinner = root.findViewById(R.id.recycleropendashboard_genusnameinner);
        recycleritem_dashboard_createdby = root.findViewById(R.id.recycleritem_dashboard_createdby);
        recycleropendashboard_picby = root.findViewById(R.id.recycleropendashboard_picby);
        recycleropendashboard_commonname1 = root.findViewById(R.id.recycleropendashboard_commonname1);
        recycleropendashboard_commonname2 = root.findViewById(R.id.recycleropendashboard_commonname2);
        recycleropendashboard_commonname3 = root.findViewById(R.id.recycleropendashboard_commonname3);
        recycleropendashboard_backbot = root.findViewById(R.id.recycleropendashboard_backbot);
        recycleropendashboard_image = root.findViewById(R.id.recycleropendashboard_image);
        recycleropendashboard_wiki_bot = root.findViewById(R.id.recycleropendashboard_wiki_bot);
        recycleropendashboard_savenotebot = root.findViewById(R.id.recycleropendashboard_savenotebot);
        recycleropendashboard_speciesnameinner = root.findViewById(R.id.recycleropendashboard_speciesnameinner);
        recycleropendashboard_addnoteedittext = root.findViewById(R.id.recycleropendashboard_addnoteedittext);
    }
}