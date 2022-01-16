package com.rejointech.planeta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.viewholder> {
    JSONObject object;
    int length = 5;
    String species_scientificname;
    String family_scientifiname;
    String percentagetoprint;
    String createdBy;
    ArrayList<String> resultImages_array;
    JSONArray lengtharray;

    public AdapterDashboard(JSONObject object) {
        this.object = object;
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
            extractdata(position);
            holder.recycleritem_dashboard_text_speciesname.setText(species_scientificname);
            holder.recycleritem_dashboard_text_familyname.setText(family_scientifiname);
            holder.recycleritem_dashboard_text_createdbyname.setText(createdBy);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractdata(int position) throws JSONException {
        String status = object.optString("status");
        JSONArray dataarray = object.optJSONArray("data");
        JSONObject data = dataarray.optJSONObject(position);
        createdBy = data.optJSONObject("createdBy").optString("name");
        String timestamp = data.optString("timeStamp");
        String wikkipediaLink = data.optString("wikkipediaLink");
        JSONArray userUploadedImage = data.getJSONArray("userUploadedImage");
        String userimage = userUploadedImage.optString(0);

//        JSONArray resultImages = data.getJSONArray("resultImages");
//        resultImages_array = new ArrayList<String>();
//
//        for (int i = 0; i < resultImages.length(); i++) {
//            resultImages_array.add(resultImages.getString(i));
//        }

        JSONArray posts = data.optJSONArray("posts");
        JSONObject postobject = posts.optJSONObject(position);
        String score = postobject.optString("score");
        String postid = postobject.optString("_id");
        Double percentage_match = Double.parseDouble(score) * 100.0;
        percentagetoprint = new DecimalFormat("##.##").format(percentage_match) + "%";

        JSONObject species = postobject.optJSONObject("species");
        species_scientificname = species.optString("scientificNameWithoutAuthor");
        String species_scientificnametrue = species.optString("scientificName");
        JSONObject genus = species.optJSONObject("genus");
        String genus_scientifiname = genus.optString("scientificNameWithoutAuthor");
        JSONObject family = species.optJSONObject("family");
        family_scientifiname = family.optString("scientificNameWithoutAuthor");
        JSONArray common_namesarray = species.optJSONArray("commonNames");
//        ArrayList<String> common_names = new ArrayList<String>();
//        for (int i = 0; i < common_namesarray.length(); i++) {
//            common_names.add(common_namesarray.getString(i));
//        }
    }

    public boolean indexExists(final ArrayList<String> list, final int index) {
        return index >= 0 && index < list.size();
    }

    @Override
    public int getItemCount() {
        length = object.optJSONArray("data").length();
        return length;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private TextView recycleritem_dashboard_text_speciesname, recycleritem_dashboard_text_familyname,
                recycleritem_dashboard_text_createdbyname;
        private ImageView recycleritem_dashboard_bot_note;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            init_views(itemView);
        }

        private void init_views(View itemView) {
            recycleritem_dashboard_text_speciesname = itemView.findViewById(R.id.recycleritem_dashboard_text_speciesname);
            recycleritem_dashboard_text_familyname = itemView.findViewById(R.id.recycleritem_dashboard_text_familyname);
            recycleritem_dashboard_text_createdbyname = itemView.findViewById(R.id.recycleritem_dashboard_text_createdbyname);
            recycleritem_dashboard_bot_note = itemView.findViewById(R.id.recycleritem_dashboard_bot_note);
        }
    }
}
