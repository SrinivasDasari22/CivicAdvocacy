package com.srinivas.civiladvocacy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private MainActivity mainActivity;
    private ArrayList<Official> officialArrayList;


    public OfficialAdapter(MainActivity mainActivity, ArrayList<Official> officialArrayList) {
        this.mainActivity = mainActivity;
        this.officialArrayList = officialArrayList;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry,parent,false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {

        Official official = officialArrayList.get(position);
        String name_party = official.getName() + " (" + official.getParty() + ")";
        holder.office.setText(official.getOffice());
        holder.name_party.setText(name_party);

        //System.out.println("Image Link:" + officialArrayList.get(position).getImageUrl());

        downloadImage(position, holder);
//        holder.photo.setImageResource(officialArrayList.get(position).getImageUrl());

    }

    @Override
    public int getItemCount() {
        return officialArrayList.size();
    }



    private void downloadImage(int pos, OfficialViewHolder holder) {

        if (!officialArrayList.get(pos).getImageUrl().equals("")) {
                Picasso.get().load(officialArrayList.get(pos).getImageUrl())
                        .placeholder(R.drawable.missing)
                        .error(R.drawable.brokenimage)
                        .into(holder.photo);
        } else {
            holder.photo.setImageResource(R.drawable.missing);
        }
    }
}
