package com.srinivas.civiladvocacy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class OfficialActivity extends AppCompatActivity {


    private String locationString;
    private TextView locationOfc;


    private TextView name;
    private TextView office;
    private TextView party;
    private TextView address;
    private TextView phone;
    private TextView email;
    private TextView webSite;
    private ImageView facebook;
    private ImageView twitter;
    private ImageView youtube;
    private ImageView image;
    private ImageView party_symbol;

    private TextView address_t;
    private TextView phone_t;
    private TextView email_t;
    private TextView website_t;
    private ActivityResultLauncher<Intent> activityResultLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_official);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable color
                = new ColorDrawable(Color.parseColor("#400040"));
        actionBar.setBackgroundDrawable(color);

        ////////


        name = findViewById(R.id.name_ofc);
        office = findViewById(R.id.ofc_ofc);
        party = findViewById(R.id.party_ofc);
        address = findViewById(R.id.address_ofc2);
        phone = findViewById(R.id.phone_ofc2);
        email = findViewById(R.id.email_ofc2);
        webSite = findViewById(R.id.website_ofc2);
        facebook = findViewById(R.id.fb_ofc);
        twitter = findViewById(R.id.tw_ofc);
        youtube = findViewById(R.id.yt_ofc);
        image = findViewById(R.id.image_ofc);
        party_symbol = findViewById(R.id.party_symbol_ofc);
        address_t = findViewById(R.id.address_ofc);
        phone_t = findViewById(R.id.phone_ofc);
        email_t = findViewById(R.id.email_ofc);
        website_t = findViewById(R.id.website_ofc);
        locationOfc = findViewById(R.id.location_id2);






        if(getIntent().hasExtra("OPEN_PROFILE")){
            Official official = (Official) getIntent().getSerializableExtra("OPEN_PROFILE");
            locationString = (String) getIntent().getStringExtra("LOCATION");
            locationOfc.setText(locationString);


            name.setText(official.getName());
            office.setText(official.getOffice());
            party.setText("("+official.getParty()+")");

            if(official.getAddress().equals("")){
                address.setVisibility(View.GONE);
                address_t.setVisibility(View.GONE);
            }else {

                address.setText(official.getAddress());
            }


            if(official.getPhone().equals("")){
                phone_t.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
            }else {
                phone.setText(official.getPhone());
            }


            if(official.getEmail().equals("")){
                email_t.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
            }else {
                email.setText(official.getEmail());
            }


            if(official.getWebSite().equals("")){
                website_t.setVisibility(View.GONE);
                webSite.setVisibility(View.GONE);
            }else {
                webSite.setText(official.getWebSite());
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
                setActivityBackgroundColor(R.color.black);

                party_symbol.setVisibility(View.GONE);

            }



            if(!official.getFacebook().equals("")) {

                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String FACEBOOK_URL = official.getFacebook();

                        Intent intent;

                        try {
                            getPackageManager().getPackageInfo("com.facebook.katana", 0);
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + FACEBOOK_URL));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        } catch (Exception e) {
                            // no Twitter app, revert to browser
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + FACEBOOK_URL));
                        }
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }


                    }
                });
            } else {
                facebook.setVisibility(View.GONE);
            }



            if(!official.getTwitter().equals("")) {

                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user = official.getTwitter();


                        Intent intent = null;
                        try {
                            // get the Twitter app if possible
                            getPackageManager().getPackageInfo("com.twitter.android", 0);
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + user));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        } catch (Exception e) {
                            // no Twitter app, revert to browser
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + user));
                        }
                        startActivity(intent);
                    }
                });
            }else {
                twitter.setVisibility(View.GONE);
            }



            if(!official.getYoutube().equals("")) {

                youtube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = official.getYoutube();
                        Intent intent = null;
                        try {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setPackage("com.google.android.youtube");
                            intent.setData(Uri.parse("https://www.youtube.com/" + name));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
                        }

                    }
                });
            }else{
                youtube.setVisibility(View.GONE);
            }




            if (!official.getImageUrl().equals("")) {
                Picasso.get().load(official.getImageUrl())
                        .placeholder(R.drawable.missing)
                        .error(R.drawable.brokenimage)
                        .into(image);
            } else {
                image.setImageResource(R.drawable.missing);
            }


            if(!official.getImageUrl().equals("")){
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(),PhotoDetailActivity.class);


                        intent.putExtra("OPEN_PHOTO",official);
                        intent.putExtra("LOCATION",locationString);
                        activityResultLauncher.launch(intent);
                    }
                });
            }

        }

        Linkify.addLinks(phone, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(webSite, Linkify.WEB_URLS);
        Linkify.addLinks(address, Linkify.MAP_ADDRESSES);
        Linkify.addLinks(email, Linkify.EMAIL_ADDRESSES);


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),this::doAction);

    }
    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(color);
    }


    private  void doAction(ActivityResult result) {
    }




}