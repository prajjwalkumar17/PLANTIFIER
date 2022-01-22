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

import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.Adapters.AdapterDashboard;
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

public class DashboardFragment extends Fragment {
    RecyclerView dashboard_recyclerview;
    AdapterDashboard adapterDashboard;
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
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboard_recyclerview = root.findViewById(R.id.dashboard_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(thiscontext, 1, GridLayoutManager.VERTICAL, false);
        dashboard_recyclerview.setLayoutManager(gridLayoutManager);
        dashboard_recyclerview.addItemDecoration(new DecorationForRecyclerView(thiscontext, R.dimen.dp_2));


        getalldashboarditems();
        return root;
    }

    private void getalldashboarditems() {
        String url = Constants.dashboardmainurl;
        APICall.okhttpmaster().newCall(APICall.get4alldashboarditems(APICall.urlbuilderforhttp(url))).enqueue(new Callback() {
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
                            adapterDashboard = new AdapterDashboard(myResponsez, thiscontext, getActivity());
                            dashboard_recyclerview.setAdapter(adapterDashboard);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}