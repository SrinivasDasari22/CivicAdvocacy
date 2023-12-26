package com.srinivas.civiladvocacy;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OfficialsDownloader {


    private static RequestQueue queue;
    private static MainActivity mainActivity;

    //"https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyAb-SWvEyLvviOICt3MLM_wzTlki8M20YQ&address=Chicago"
    private static final String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String api ="AIzaSyAb-SWvEyLvviOICt3MLM_wzTlki8M20YQ";

    public static void getSourceData(MainActivity mainActivity1, ArrayList<Official> officialArrayList,String locationString) {


        mainActivity = mainActivity1;
        MainActivity.officialArrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(DATA_URL).buildUpon();
        buildURL.appendQueryParameter("key", api);
        buildURL.appendQueryParameter("address",locationString);

        String urlToUse = buildURL.build().toString();
        System.out.println(urlToUse);

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response,officialArrayList);

        Response.ErrorListener error =
                error1 -> parseJSON(null,officialArrayList);


        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        //mainActivity.notifyAdapter();

    }

    private static void parseJSON (JSONObject jsonObject,ArrayList<Official> officialArrayList) {

        try {


            JSONObject jMain = jsonObject.getJSONObject("normalizedInput");
            StringBuilder location = new StringBuilder();

            String city = jMain.getString("city");
            String state = jMain.getString("state");
            String zip = jMain.getString("zip");
            location.append(city+", "+state+", "+zip);




            JSONArray jsonArray = jsonObject.getJSONArray("offices");

            JSONArray jsonArray2 = jsonObject.getJSONArray("officials");
            for(int i=0;i<jsonArray.length();i++) {
                jsonArray.length();


                JSONObject jsobj = jsonArray.getJSONObject(i);
                String office = jsobj.getString("name");


                JSONArray jsonArray1 = jsobj.getJSONArray("officialIndices");
                for(int d=0;d<jsonArray1.length();d++){

                    String name="";
                    String address="";
                    //StringBuilder address = new StringBuilder();

                    String party="";
                    String phone="";
                    String website="";
                    String email="";
                    String facebook="";
                    String twitter="";
                    String youtube="";
                    String image="";




                    String p = jsonArray1.getString(d);
                    int j = Integer.parseInt(p);
                    JSONObject jsonObject1 = jsonArray2.getJSONObject(j);
                    name = jsonObject1.getString("name");

                    JSONArray addressArray;
                    JSONObject addressObject;


                    //JSONArray addressArray = jsonObject1.getJSONArray("address");

                    if(jsonObject1.has("address")) {
                        addressArray = jsonObject1.getJSONArray("address");
                        if(addressArray.length()>0) {
                            addressObject = addressArray.getJSONObject(0);
                            JSONArray keys = addressObject.names ();
                            //System.out.println("keys: "+keys);
                            //keys = addressObject.names ();

                            for (int b = 0; b < keys.length (); b++) {

                                String key = keys.getString (b); // Here's your key
//                                System.out.println("key: "+ key);
                                address = address +addressObject.getString (key); // Here's your value
                                if(b!=keys.length()-1){
                                    address = address+" ";
                                }
                            }
                        }
                    }

                    //JSONObject addressObject = addressArray.getJSONObject(0);


                    //JSONArray keys = addressObject.names ();

                    //System.out.println("array list 1 :"+ MainActivity.officialArrayList.size());
//                    for (int b = 0; b < keys.length (); b++) {
//
//                        String key = keys.getString (b); // Here's your key
//                        address = address +addressObject.getString (key); // Here's your value
//                        if(b!=keys.length()-1){
//                            address = address+", ";
//                        }
//                    }

//                    while(addressObject.keys().hasNext()) {
//                        String key = addressObject.keys().next();
//                        //if (jsonObject.get(key) instanceof JSONObject) {
//                            address.append(addressObject.getString(key));
//                        //}
//                    }

//                    while(addressObject.keys().hasNext()) {
//
//
//
//                        address.append(addressObject.getString("line1") + ", ");
//                        address.append(addressObject.getString("line2") + ", ");
//                        address.append(addressObject.getString("city") + ", ");
//                        address.append(addressObject.getString("state") + ", ");
//                        address.append(addressObject.getString("zip"));
//                    }



                    if(jsonObject1.has("party")) {
                        party = jsonObject1.getString("party");
                    }

                    JSONArray phoneArray;
                    if(jsonObject1.has("phones")) {
                        phoneArray = jsonObject1.getJSONArray("phones");
                        if(phoneArray.length()>0) {
                            phone = phoneArray.getString(0);
                        }
                    }


                    JSONArray urlArray;
                    if(jsonObject1.has("urls")) {
                        urlArray = jsonObject1.getJSONArray("urls");
                        if (urlArray.length() > 0) {
                            website = urlArray.getString(0);
                        }
                    }

                    JSONArray mailArray;

                    if(jsonObject1.has("emails")) {
                        mailArray = jsonObject1.getJSONArray("emails");
                        if(mailArray.length()>0) {
                            email = mailArray.getString(0);
                        }
                    }


                    if(jsonObject1.has("photoUrl")) {
                        image = jsonObject1.getString("photoUrl");
                    }

                    if(jsonObject1.has("channels")){
                    JSONArray channelArray = jsonObject1.getJSONArray("channels");
                    for(int k=0;k<channelArray.length();k++) {
                        JSONObject channelObject = channelArray.getJSONObject(k);
                        String type = channelObject.getString("type");
                        if(type.equals("Facebook")){
                            facebook = channelObject.getString("id");
                        }
                        if(type.equals("Twitter")){
                            twitter = channelObject.getString("id");
                        }
                        if(type.equals("Youtube")){
                            youtube = channelObject.getString("id");
                        }
                    }
                }
                    System.out.println("facebook 111:"+ facebook);
                    System.out.println("twitter 111:"+ twitter);
                    System.out.println("youtube 111:"+ youtube);
                    System.out.println("......");
                    officialArrayList.add(new Official(name, office, party, address, phone, email,
                            website, facebook, twitter,
                            youtube, image));
                }







                mainActivity.notifyAdapter();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
