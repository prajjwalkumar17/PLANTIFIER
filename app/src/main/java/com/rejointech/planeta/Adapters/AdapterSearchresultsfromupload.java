package com.rejointech.planeta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.R;

import org.json.JSONObject;

public class AdapterSearchresultsfromupload extends RecyclerView.Adapter<AdapterSearchresultsfromupload.viewrecycler> {
    JSONObject object;

    public AdapterSearchresultsfromupload(JSONObject object) {
        this.object = object;
    }

    public AdapterSearchresultsfromupload() {
    }

    @NonNull
    @Override
    public AdapterSearchresultsfromupload.viewrecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclersearchresultsfromupload, parent, false);
        return new viewrecycler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchresultsfromupload.viewrecycler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class viewrecycler extends RecyclerView.ViewHolder {
        public viewrecycler(@NonNull View itemView) {
            super(itemView);
            initviews(itemView);
        }

        private void initviews(View itemView) {
        }
    }
}
