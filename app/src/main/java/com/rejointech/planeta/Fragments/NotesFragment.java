package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.Decoration.DecorationForRecyclerView;
import com.rejointech.planeta.R;
import com.rejointech.planeta.RecyclerClickInterface.RecyclernotesclickInterface;

import org.json.JSONObject;


public class NotesFragment extends Fragment {

    RecyclerView noterecycler;
    RecyclernotesclickInterface recyclernotesclickInterface;
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
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        noterecycler = root.findViewById(R.id.noterecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(thiscontext, 1, GridLayoutManager.VERTICAL, false);
        noterecycler.setLayoutManager(gridLayoutManager);
        noterecycler.addItemDecoration(new DecorationForRecyclerView(thiscontext, R.dimen.dp_2));


        showitems();
        setonclicklistiner();
        return root;
    }

    private void setonclicklistiner() {
        recyclernotesclickInterface = new RecyclernotesclickInterface() {
            @Override
            public void onItemClick(View v, int position, JSONObject object) {

            }
        };
    }

    private void showitems() {

    }
}