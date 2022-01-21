package com.rejointech.planeta.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.R;

public class OpenDashboardFragment extends Fragment {

    TextView recycleropendashboard_speciename, recycleropendashboard_familyname, recycleropendashboard_datetime, recycleropendashboard_Percentage, recycleropendashboard_familynametextin, recycleropendashboard_genusnameinner, recycleritem_dashboard_createdby, recycleropendashboard_picby, recycleropendashboard_commonname1, recycleropendashboard_commonname2, recycleropendashboard_commonname3;
    ImageView recycleropendashboard_backbot, recycleropendashboard_image, recycleropendashboard_wiki_bot;
    AppCompatButton recycleropendashboard_savenotebot;
    AppCompatEditText recycleropendashboard_addnoteedittext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_open_dashboard, container, false);
        Init_views(root);


        return root;
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
        recycleropendashboard_addnoteedittext = root.findViewById(R.id.recycleropendashboard_addnoteedittext);
    }
}