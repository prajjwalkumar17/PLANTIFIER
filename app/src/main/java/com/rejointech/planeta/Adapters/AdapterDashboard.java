package com.rejointech.planeta.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.R;
import com.rejointech.planeta.RecyclerClickInterface.Recyclerdashboardclick;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.viewholder> {
    JSONObject object;
    Context thiscontext;
    Activity myactivity;
    int length;
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
    String genus_scientifiname;
    Set<String> commonnamesset = new HashSet<String>();
    String score;
    String postid;


    public AdapterDashboard(JSONObject object, Context thiscontext, Activity myactivity, Recyclerdashboardclick recyclerdashboardclick) {
        this.object = object;
        this.thiscontext = thiscontext;
        this.myactivity = myactivity;
        this.recyclerdashboardclick = recyclerdashboardclick;
    }

    @NonNull
    @Override
    public AdapterDashboard.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerdisportalitem, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDashboard.viewholder holder, int position) {
        try {
            extractdata(position, holder);
            holder.recycleritem_dashboard_text_speciesname.setText(species_scientificname);
            holder.recycleritem_dashboard_text_familyname.setText(family_scientifiname);
            holder.recycleritem_dashboard_text_createdbyname.setText(createdBy);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractdata(int position, viewholder holder) throws JSONException {
        String status = object.optString("status");
        JSONArray dataarray = object.optJSONArray("data");
        JSONObject data = dataarray.optJSONObject(position);

        JSONObject createdByObj = data.optJSONObject("createdBy");
        if (createdByObj != null) {
            createdBy = createdByObj.optString("name");
        }

        timestamp = data.optString("timeStamp");
        wikkipediaLink = data.optString("wikkipediaLink");
        JSONArray userUploadedImage = data.optJSONArray("userUploadedImage");
        userimage = userUploadedImage.optString(0);
        Picasso.get()
                .load(userimage)
                .error(R.drawable.icontree)
                .into(holder.imageviewfordashboard);


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
                common_names.add(common_namesarray.getString(i));
            }
            commonnamesset.addAll(common_names);
            resultImages = postobject.optJSONArray("images");

            score = postobject.optString("score");
            postid = postobject.optString("_id");
            Double percentage_match = Double.parseDouble(score) * 100.0;
            percentagetoprint = new DecimalFormat("##.##").format(percentage_match) + "%";


        }
    }

    public boolean indexExists(final ArrayList<String> list, final int index) {
        return index >= 0 && index < list.size();
    }

    @Override
    public int getItemCount() {
        length = object.optJSONArray("data").length();
        return length;
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView recycleritem_dashboard_text_speciesname, recycleritem_dashboard_text_familyname,
                recycleritem_dashboard_text_createdbyname;
        private ImageView recycleritem_dashboard_bot_note;
        private ImageView imageviewfordashboard;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            init_views(itemView);
            itemView.setOnClickListener(this);


        }

        private void init_views(View itemView) {
            recycleritem_dashboard_text_speciesname = itemView.findViewById(R.id.recycleritem_dashboard_text_speciesname);
            recycleritem_dashboard_text_familyname = itemView.findViewById(R.id.recycleritem_dashboard_text_familyname);
            recycleritem_dashboard_text_createdbyname = itemView.findViewById(R.id.recycleropendashboard_picby);
            recycleritem_dashboard_bot_note = itemView.findViewById(R.id.recycleropendashboard_wiki_bot);
            imageviewfordashboard = itemView.findViewById(R.id.recyclerallnotes_image);
        }

        @Override
        public void onClick(View view) {
            recyclerdashboardclick.onItemClick(itemView, getAdapterPosition(), object);
        }
    }
}
