package com.rejointech.planeta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rejointech.planeta.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapterimgsliderdashboard extends SliderViewAdapter<Adapterimgsliderdashboard.viewholder> {
    ArrayList<String> array;

    public Adapterimgsliderdashboard(ArrayList<String> array) {
        this.array = array;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slideritem, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(viewholder viewHolder, int position) {

        String image = array.get(position);

        Picasso.get()
                .load(image)
                .error(R.drawable.icontree)
                .into(viewHolder.sliderimage);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    public class viewholder extends SliderViewAdapter.ViewHolder {
        ImageView sliderimage;

        public viewholder(View itemView) {
            super(itemView);
            sliderimage = itemView.findViewById(R.id.sliderimage);
        }
    }
}
