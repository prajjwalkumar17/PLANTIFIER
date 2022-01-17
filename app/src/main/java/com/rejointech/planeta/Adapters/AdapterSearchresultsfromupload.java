package com.rejointech.planeta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterSearchresultsfromupload extends RecyclerView.Adapter<AdapterSearchresultsfromupload.viewrecycler> {
    JSONObject object;
    int length;
    String species_scientificname;
    String family_scientifiname;
    String percentagetoprint;
    ArrayList<String> resultImages_array;

    public AdapterSearchresultsfromupload(JSONObject object) {
        this.object = object;
    }

    @NonNull
    @Override
    public AdapterSearchresultsfromupload.viewrecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_camera_results, parent, false);
        return new viewrecycler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchresultsfromupload.viewrecycler holder, int position) {
        try {
            extractdata(position);
            holder.recycler_camera_results_text_plantname.setText(species_scientificname);
            holder.recycler_camera_results_text_familyname.setText(family_scientifiname);
            holder.recycler_camera_results_text_percentage.setText(percentagetoprint);
            if (indexExists(resultImages_array, 0)) {
                Picasso.get()
                        .load(resultImages_array.get(0))
                        .into(holder.recycler_camera_results_image1);
            }
            if (indexExists(resultImages_array, 1)) {
                Picasso.get()
                        .load(resultImages_array.get(1))
                        .into(holder.recycler_camera_results_image2);
            }
            if (indexExists(resultImages_array, 2)) {
                Picasso.get()
                        .load(resultImages_array.get(2))
                        .into(holder.recycler_camera_results_image3);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public boolean indexExists(final ArrayList<String> list, final int index) {
        return index >= 0 && index < list.size();
    }

    private void extractdata(int position) throws JSONException {
        JSONObject data = object.optJSONObject("data");
        String status = object.optString("status");
        String timestamp = data.optString("timeStamp");
        String wikkipediaLink = data.optString("wikkipediaLink");
        JSONArray userUploadedImage = data.getJSONArray("userUploadedImage");
        String userimage = userUploadedImage.optString(0);

        JSONArray resultImages = data.getJSONArray("resultImages");
        resultImages_array = new ArrayList<String>();

        for (int i = 0; i < resultImages.length(); i++) {
            resultImages_array.add(resultImages.getString(i));
        }

        JSONArray posts = data.optJSONArray("posts");
        JSONObject postobject = posts.optJSONObject(position);
        String score = postobject.optString("score");
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
        ArrayList<String> common_names = new ArrayList<String>();
        for (int i = 0; i < common_namesarray.length(); i++) {
            common_names.add(common_namesarray.getString(i));
        }
    }

    @Override
    public int getItemCount() {
        try {
            length = object.optJSONObject("data").getJSONArray("posts").length();
            return length;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return length;
    }


    public class viewrecycler extends RecyclerView.ViewHolder {
        private AppCompatButton recycler_camera_results_bot_confirm;
        private TextView recycler_camera_results_text_plantname, recycler_camera_results_text_familyname,
                recycler_camera_results_text_percentage;
        private ImageView recycler_camera_results_bot_wikipedia, recycler_camera_results_image3, recycler_camera_results_image2, recycler_camera_results_image1;

        public viewrecycler(@NonNull View itemView) {
            super(itemView);
            initviews(itemView);
        }

        private void initviews(View itemView) {
            recycler_camera_results_bot_confirm = itemView.findViewById(R.id.recycler_camera_results_bot_confirm);
            recycler_camera_results_text_plantname = itemView.findViewById(R.id.recycler_camera_results_text_plantname);
            recycler_camera_results_text_familyname = itemView.findViewById(R.id.recycler_camera_results_text_familyname);
            recycler_camera_results_text_percentage = itemView.findViewById(R.id.recycler_camera_results_text_percentage);
            recycler_camera_results_bot_wikipedia = itemView.findViewById(R.id.recycler_camera_results_bot_wikipedia);
            recycler_camera_results_image3 = itemView.findViewById(R.id.recycler_camera_results_image3);
            recycler_camera_results_image2 = itemView.findViewById(R.id.recycler_camera_results_image2);
            recycler_camera_results_image1 = itemView.findViewById(R.id.recycler_camera_results_image1);
        }
    }
}
