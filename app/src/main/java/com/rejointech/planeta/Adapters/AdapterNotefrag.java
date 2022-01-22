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
import com.rejointech.planeta.RecyclerClickInterface.RecyclernotesclickInterface;

import org.json.JSONObject;

public class AdapterNotefrag extends RecyclerView.Adapter<AdapterNotefrag.viewholder> {
    JSONObject object;
    RecyclernotesclickInterface recyclernotesclickInterface;

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritemshareandnote, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recyclerallnotes_specinameout, recyclerallnotes_familynameout, recyclerallnotes_Timestamp, recyclerallnotes_prevnote, recyclerallnotes_showmorebot;
        ImageView recyclerallnotes_sharebot, recyclerallnotes_image;
        AppCompatButton recyclerallnotes_updatenotebot;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            init_Views(itemView);
            itemView.setOnClickListener(this);
        }

        private void init_Views(View itemView) {
            recyclerallnotes_specinameout = itemView.findViewById(R.id.recyclerallnotes_specinameout);
            recyclerallnotes_familynameout = itemView.findViewById(R.id.recyclerallnotes_familynameout);
            recyclerallnotes_Timestamp = itemView.findViewById(R.id.recyclerallnotes_Timestamp);
            recyclerallnotes_prevnote = itemView.findViewById(R.id.recyclerallnotes_prevnote);
            recyclerallnotes_showmorebot = itemView.findViewById(R.id.recyclerallnotes_showmorebot);
            recyclerallnotes_sharebot = itemView.findViewById(R.id.recyclerallnotes_sharebot);
            recyclerallnotes_image = itemView.findViewById(R.id.recyclerallnotes_image);
            recyclerallnotes_updatenotebot = itemView.findViewById(R.id.recyclerallnotes_updatenotebot);
        }

        @Override
        public void onClick(View view) {
            recyclernotesclickInterface.onItemClick(itemView, getAdapterPosition(), object);
        }
    }
}
