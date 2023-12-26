package com.srinivas.civiladvocacy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView aboutLink;

    String webUrl =
            "https://developers.google.com/civic- information/";
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable color
                = new ColorDrawable(Color.parseColor("#400040"));
        actionBar.setBackgroundDrawable(color);



        aboutLink = (TextView) findViewById(R.id.about_link);

        //aboutLink.setText("Google Civic Information API");

        SpannableString text = new SpannableString("Google Civic Information API");
        text.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        aboutLink.setText(text);

        aboutLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://developers.google.com/civic-information/"));

                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
        }});




    }
}