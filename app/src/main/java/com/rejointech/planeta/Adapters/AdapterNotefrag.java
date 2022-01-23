package com.rejointech.planeta.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.rejointech.planeta.R;
import com.rejointech.planeta.RecyclerClickInterface.RecyclernotesclickInterface;
import com.rejointech.planeta.RecyclerClickInterface.RecyclernotesshareInterface;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdapterNotefrag extends RecyclerView.Adapter<AdapterNotefrag.viewholder> {
    JSONObject object;
    RecyclernotesclickInterface recyclernotesclickInterface;
    RecyclernotesshareInterface recyclernotesshareInterface;
    Context thiscontext;
    Activity myactivity;
    int length;

    JSONArray resultImages;
    String species_scientificname;
    String family_scientifiname;
    String percentagetoprint;
    String timestamp;
    String wikkipediaLink;
    String userimage;
    String species_scientificnametrue;
    String genus_scientifiname;
    Set<String> commonnamesset = new HashSet<String>();
    String score, note;
    String postid;
    String name;
    String id;

    public AdapterNotefrag(JSONObject object, RecyclernotesclickInterface recyclernotesclickInterface, RecyclernotesshareInterface recyclernotesshareInterface, Context thiscontext, Activity myactivity) {
        this.object = object;
        this.recyclernotesclickInterface = recyclernotesclickInterface;
        this.recyclernotesshareInterface = recyclernotesshareInterface;
        this.thiscontext = thiscontext;
        this.myactivity = myactivity;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritemshareandnote, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        extractdata(position, holder);
    }

    private void extractdata(int position, viewholder holder) {
        String status = object.optString("status");
        JSONArray dataarray = object.optJSONArray("data");
        JSONObject dataout = dataarray.optJSONObject(position);

        id = dataout.optString("_id");

        JSONObject useridobj = dataout.optJSONObject("userid");
        name = useridobj.optString("name");
        note = dataout.optString("note");

        JSONObject data = dataout.optJSONObject("postid");
        timestamp = data.optString("timeStamp");
        wikkipediaLink = data.optString("wikkipediaLink");
        JSONArray userUploadedImage = data.optJSONArray("userUploadedImage");
        userimage = userUploadedImage.optString(0);
        Picasso.get()
                .load(userimage)
                .error(R.drawable.icontree)
                .into(holder.recyclerallnotes_image);


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
                common_names.add(common_namesarray.optString(i));
            }
            commonnamesset.addAll(common_names);
            resultImages = postobject.optJSONArray("images");

            score = postobject.optString("score");
            postid = postobject.optString("_id");
            Double percentage_match = Double.parseDouble(score) * 100.0;
            percentagetoprint = new DecimalFormat("##.##").format(percentage_match) + "%";

            holder.recyclerallnotes_specinameout.setText(species_scientificname);
            holder.recyclerallnotes_familynameout.setText(family_scientifiname);
//            holder.recyclerallnotes_Timestamp.setText(timestamp);
            holder.recyclerallnotes_prevnote.setText(note);
        }
    }

    @Override
    public int getItemCount() {
        length = Integer.parseInt(object.optString("results"));
        return length;
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recyclerallnotes_specinameout, recyclerallnotes_familynameout, recyclerallnotes_Timestamp, recyclerallnotes_prevnote;
        ImageView recyclerallnotes_sharebot, recyclerallnotes_image;
        AppCompatButton recyclerallnotes_updatenotebot;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            init_Views(itemView);
            recyclerallnotes_updatenotebot.setOnClickListener(this);
            recyclerallnotes_sharebot.setOnClickListener(this);
        }

        private void init_Views(View itemView) {
            recyclerallnotes_specinameout = itemView.findViewById(R.id.recyclerallnotes_specinameout);
            recyclerallnotes_familynameout = itemView.findViewById(R.id.recyclerallnotes_familynameout);
            recyclerallnotes_Timestamp = itemView.findViewById(R.id.recyclerallnotes_Timestamp);
            recyclerallnotes_prevnote = itemView.findViewById(R.id.recyclerallnotes_prevnote);
            recyclerallnotes_sharebot = itemView.findViewById(R.id.recyclerallnotes_sharebot);
            recyclerallnotes_image = itemView.findViewById(R.id.recyclerallnotes_image);
            recyclerallnotes_updatenotebot = itemView.findViewById(R.id.recyclerallnotes_updatenotebot);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.recyclerallnotes_updatenotebot) {
                recyclernotesclickInterface.onItemClick(recyclerallnotes_updatenotebot, getAdapterPosition(), object);
            } else if (view.getId() == R.id.recyclerallnotes_sharebot) {
                recyclernotesshareInterface.onItemClick(recyclerallnotes_sharebot, getAdapterPosition(), object);
            }
        }
    }
}
