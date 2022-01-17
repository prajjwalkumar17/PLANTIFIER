package com.rejointech.planeta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rejointech.planeta.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class Adapterimgsliderdashboard extends SliderViewAdapter<Adapterimgsliderdashboard.viewholder> {
    JSONArray jsonArray;


    public Adapterimgsliderdashboard(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slideritem, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(viewholder viewHolder, int position) {
        try {
            String image = jsonArray.getString(position);

            Picasso.get()
                    .load(image)
                    .error(R.drawable.icon_pic_error)
                    .into(viewHolder.sliderimage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    public class viewholder extends SliderViewAdapter.ViewHolder {
        ImageView sliderimage;

        public viewholder(View itemView) {
            super(itemView);
            sliderimage = itemView.findViewById(R.id.sliderimage);
        }
    }
}
