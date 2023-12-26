package com.srinivas.civiladvocacy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class PhotoDetailActivity extends AppCompatActivity {

    private TextView name;
    private TextView office;
    private ImageView image;
    private ImageView party_symbol;

    private  String locationString;

    private TextView locationPho;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable color
                = new ColorDrawable(Color.parseColor("#400040"));
        actionBar.setBackgroundDrawable(color);


        name = findViewById(R.id.name_pho);
        office = findViewById(R.id.ofc_pho);
        image = findViewById(R.id.pho_pho);
        party_symbol = findViewById(R.id.party_symbol_pho);
        locationPho = findViewById(R.id.location_id3);


        if(getIntent().hasExtra("OPEN_PHOTO")){
            Official official = (Official) getIntent().getSerializableExtra("OPEN_PHOTO");
             locationString = (String) getIntent().getStringExtra("LOCATION");
            locationPho.setText(locationString);

            name.setText(official.getName());
            office.setText(official.getOffice());


            if (!official.getImageUrl().equals("")) {
                Picasso.get().load(official.getImageUrl())
                        .placeholder(R.drawable.missing)
                        .error(R.drawable.brokenimage)
                        .into(image);
            } else {
                image.setImageResource(R.drawable.missing);
            }


            if(official.getParty().toLowerCase(Locale.ROOT).contains("demo")){
                setActivityBackgroundColor(R.color.democrate);
                party_symbol.setImageResource(R.drawable.dem_logo);



                party_symbol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://democrats.org"));

                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        startActivity(intent);
                    }
                });
            }else if(official.getParty().toLowerCase(Locale.ROOT).contains("rep")){
                setActivityBackgroundColor(R.color.republican);
                party_symbol.setImageResource(R.drawable.rep_logo);


                party_symbol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.gop.com"));

                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        startActivity(intent);
                    }
                });

            }else{
                party_symbol.setVisibility(View.GONE);
                setActivityBackgroundColor(R.color.black);


            }


        }




    }


    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(color);
    }
}