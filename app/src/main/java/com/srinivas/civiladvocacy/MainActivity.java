package com.srinivas.civiladvocacy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {


    private RecyclerView recyclerView;
    private TableLayout tableLayout;
    private TextView noInternet;
    private TextView netData;

    private  EditText enterAdd;
    private OfficialAdapter officialAdapter;
    public   final static ArrayList<Official> officialArrayList = new ArrayList<>();
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private Official currentOfficial;
    private TextView locationS;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;

    private static String locationString = "";

    private String api_key = "AIzaSyAb-SWvEyLvviOICt3MLM_wzTlki8M20YQ";
    //AIzaSyDMGscoQq3qr7zrtv2aQ4GCVOk3_v4Ll-8

    private String webUrl ="https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyAb-SWvEyLvviOICt3MLM_wzTlki8M20YQ&address=Chicago";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable color
                = new ColorDrawable(Color.parseColor("#400040"));
        actionBar.setBackgroundDrawable(color);


        setContentView(R.layout.activity_main);


        if(!hasNetworkConnection()){
            locationS = findViewById(R.id.location_id);
            tableLayout = findViewById(R.id.tableLayout);
            noInternet = findViewById(R.id.noInternetid);
            netData = findViewById(R.id.netData);

            locationS.setText("No Data For Location");
            noInternet.setText("No Network Connection");
            netData.setText("Data cannot be accessed/loaded without an internet connection");
            setTitle("Know Your Government");


        } else {

            tableLayout = findViewById(R.id.tableLayout);
            noInternet = findViewById(R.id.noInternetid);
            netData = findViewById(R.id.netData);

            tableLayout.setVisibility(View.GONE);
            noInternet.setVisibility(View.GONE);
            netData.setVisibility(View.GONE);

            locationS = findViewById(R.id.location_id);
            recyclerView = findViewById(R.id.recyclerView);
            officialAdapter = new OfficialAdapter(this, officialArrayList);
            recyclerView.setAdapter(officialAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            mFusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this);
            determineLocation();


//        location.setText("Chicago");

            System.out.println("array List:" + officialArrayList.size());

//        for(int i =0; i<5; i++){
//            officialArrayList.add(new Official("Mahesh", "GMB", "Tollywood", "831 S Bishop St, Chicago, IL 60607 ", "3128669405", "gmb@gmail.com",
//                    "https://www.greatandhra.com/movies", "whitehouse", "https://twitter.com/urstrulyMahesh?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor",
//                    "youtube.com/channel/UC1EyYYYoXf_YaLkVzvJhjRQ", "https://pbs.twimg.com/profile_images/1585132762067275776/eSJ0Othk_400x400.jpg"));
//        }

            activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::doAction);
        }

    }

    private void determineLocation() {
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        locationString = getPlace(location);
                        locationS.setText(locationString);
                        OfficialsDownloader.getSourceData(this,officialArrayList,locationString);
                        notifyAdapter();
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                }
            }
        }
    }

    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s, %s%n%nProvider: %s%n%n%.5f, %.5f",
                    city, state, loc.getProvider(), loc.getLatitude(), loc.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    private  void doAction(ActivityResult result) {
    }

    public void notifyAdapter(){

        officialAdapter.notifyItemRangeChanged(0,officialArrayList.size());


    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this,OfficialActivity.class);

        int position = recyclerView.getChildLayoutPosition(v);

        currentOfficial = officialArrayList.get(position);
        intent.putExtra("OPEN_PROFILE",currentOfficial);
        intent.putExtra("LOCATION",locationString);
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onLongClick(View v) {



        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() ==R.id.search_item) {

            enterAdd = new EditText(this);
            enterAdd.setGravity(Gravity.CENTER);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);



            builder.setPositiveButton("OK",(dialog, which) -> {


                locationString = enterAdd.getText().toString();

                locationS.setText(locationString);
                OfficialsDownloader.getSourceData(this,officialArrayList,locationString);
                notifyAdapter();

            });

            builder.setNegativeButton("CANCEL",(dialog, which) -> {
                dialog.dismiss();
            });

            builder.setTitle(" Enter Address"  );

            builder.setView(enterAdd);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else if(item.getItemId() ==R.id.about){
//            Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
            return true;
        }

            return super.onOptionsItemSelected(item);
    }
}