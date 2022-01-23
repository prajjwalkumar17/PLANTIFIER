package com.rejointech.planeta.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;


public class HistoryFragment extends Fragment {
    RecyclerView history_recyclerview;

//TODO profile picin leaderboard too,history,2times req

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        initlayout();
        history_recyclerview = root.findViewById(R.id.history_recyclerview);

        return root;
    }

    private void initlayout() {
        ((HomeActivityContainer) getActivity()).setToolbarVisible();
        ((HomeActivityContainer) getActivity()).setbotVisible();
        ((HomeActivityContainer) getActivity()).setfabvisible();
    }

}