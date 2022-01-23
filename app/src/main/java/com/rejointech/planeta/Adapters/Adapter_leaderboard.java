package com.rejointech.planeta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class Adapter_leaderboard extends RecyclerView.Adapter<Adapter_leaderboard.viewholder> {
    JSONObject object;
    int length;

    public Adapter_leaderboard(JSONObject object) {
        this.object = object;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerleaderboarditem, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        JSONArray datarry = object.optJSONArray("data");
        JSONObject data = datarry.optJSONObject(position);
        String name = data.optString("name");
        String email = data.optString("email");
        String searches = data.optString("searches");
        holder.recycler_leaderboard_name.setText(name);
        holder.recycler_leaderboard_email.setText(email);
        holder.recycler_leaderboard_clicked.setText(searches);

    }

    @Override
    public int getItemCount() {
        length = object.optJSONArray("data").length();
        return length;
    }


    public class viewholder extends RecyclerView.ViewHolder {
        TextView recycler_leaderboard_clicked, recycler_leaderboard_email, recycler_leaderboard_name;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            recycler_leaderboard_clicked = itemView.findViewById(R.id.recycler_leaderboard_clicked);
            recycler_leaderboard_email = itemView.findViewById(R.id.recycler_leaderboard_email);
            recycler_leaderboard_name = itemView.findViewById(R.id.recycler_leaderboard_name);
        }
    }
}
