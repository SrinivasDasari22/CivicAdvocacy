package com.srinivas.civiladvocacy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder{

    TextView office;
    TextView name_party;
    ImageView photo;

    public OfficialViewHolder(@NonNull View itemView) {
        super(itemView);


        office = itemView.findViewById(R.id.office);
        name_party = itemView.findViewById(R.id.name_party);
        photo = itemView.findViewById(R.id.image_id);
    }
}
