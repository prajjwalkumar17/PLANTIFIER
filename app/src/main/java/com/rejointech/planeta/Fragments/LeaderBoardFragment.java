package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.Adapters.Adapter_leaderboard;
import com.rejointech.planeta.Container.HomeActivityContainer;
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

public class LeaderBoardFragment extends Fragment {
    Context thiscontext;
    RecyclerView leaderboard_recyclerview;
    private ShimmerFrameLayout leaderboardshimmer;
    TextView toolwithbackbothead;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_leader_board, container, false);
        initlayout();
        toolwithbackbothead = root.findViewById(R.id.toolwithbackbothead);
        toolwithbackbothead.setText("Leaderboard");
        leaderboard_recyclerview = root.findViewById(R.id.leaderboard_recyclerview);
        leaderboardshimmer = root.findViewById(R.id.leaderboardshimmer);
        shimmersetup();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(thiscontext, 1, GridLayoutManager.VERTICAL, false);
        leaderboard_recyclerview.setLayoutManager(gridLayoutManager);
        leaderboard_recyclerview.addItemDecoration(new DecorationForRecyclerView(thiscontext, R.dimen.dp_2));


        String url = Constants.leaderboardurl;


        APICall.okhttpmaster().newCall(APICall.get4leaderboard(APICall.urlbuilderforhttp(url))).enqueue(new Callback() {
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
                            JSONObject object = new JSONObject(rResponse);
                            Adapter_leaderboard adapter_leaderboard = new Adapter_leaderboard(object);
                            leaderboard_recyclerview.setAdapter(adapter_leaderboard);
                            stopshimmer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });


        return root;

    }

    private void shimmersetup() {
        leaderboard_recyclerview.setVisibility(View.GONE);
        leaderboardshimmer.setVisibility(View.VISIBLE);
        leaderboardshimmer.startShimmer();

    }

    private void stopshimmer() {
        leaderboard_recyclerview.setVisibility(View.VISIBLE);
        leaderboardshimmer.setVisibility(View.GONE);
        leaderboardshimmer.stopShimmer();

    }

    private void initlayout() {
        ((HomeActivityContainer) getActivity()).setToolbarInvisible();
        ((HomeActivityContainer) getActivity()).setbotVisible();
        ((HomeActivityContainer) getActivity()).setfabinvisible();


    }


}