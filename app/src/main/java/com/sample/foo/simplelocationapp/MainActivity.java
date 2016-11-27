package com.sample.foo.simplelocationapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    double longitudeNetwork, latitudeNetwork;

    TextView longitudeValueNetwork, latitudeValueNetwork;

    TextView mTextView;

    String jsonResponse, jsonResponse2;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        longitudeValueNetwork = (TextView) findViewById(R.id.longitudeValueNetwork);
        latitudeValueNetwork = (TextView) findViewById(R.id.latitudeValueNetwork);

        mTextView = (TextView) findViewById(R.id.text);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void toggleNetworkUpdates(View view) {
        if (!checkLocation())
            return;
        Button button = (Button) view;
        if (button.getText().equals(getResources().getString(R.string.pause))) {
            locationManager.removeUpdates(locationListenerNetwork);
            button.setText(R.string.resume);
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 500, 1, locationListenerNetwork);
            Toast.makeText(this, "Network provider started running", Toast.LENGTH_LONG).show();
            button.setText(R.string.pause);
        }
    }

    String data = "";
    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    longitudeValueNetwork.setText(longitudeNetwork + "");
                    latitudeValueNetwork.setText(latitudeNetwork + "");

//                    // Instantiate the RequestQueue.
//                    RequestQueue queue = Volley.newRequestQueue(getBaseContext());
//                    String url = "https://api.breezometer.com/baqi/?lat=" + latitudeNetwork + "&lon=" + longitudeNetwork + "&key=(your_key)";
//
//                    // Request a string response from the provided URL.
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    // Display the first 500 characters of the response string.
//                                    mTextView.setText("Response is: " + response.substring(0, 500));
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            mTextView.setText("That didn't work!");
//                        }
//                    });
//                    // Add the request to the RequestQueue.
//                    queue.add(stringRequest);

                    ///////////
                    RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                    RequestQueue queue2 = Volley.newRequestQueue(getBaseContext());
                    String url = "https://api.breezometer.com/baqi/?lat=" + latitudeNetwork + "&lon=" + longitudeNetwork + "&key=(your_key)";
                    String url2 = "http://api.wunderground.com/api/(your_key)/alerts/q/Finland/Helsinki.json";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(  Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        // Parsing json object response
                                        // response will be a json object
                                        String datetime = response.getString("datetime");
                                        String country = response.getString("country_name");
                                        String status = response.getString("breezometer_description");
                                        JSONObject random_recommendations = response.getJSONObject("random_recommendations");
                                        String children = random_recommendations.getString("children");
                                        String sport = random_recommendations.getString("sport");
                                        String health = random_recommendations.getString("health");
                                        String inside = random_recommendations.getString("inside");
                                        String outside = random_recommendations.getString("outside");
                                        String pollutants = response.getString("dominant_pollutant_description");

                                        JSONObject pollutants_detail = response.getJSONObject("dominant_pollutant_text");
                                        String effects = pollutants_detail.getString("effects");


                                        jsonResponse = "";
                                        jsonResponse += "Date-Time: " + datetime + "\n\n";
                                        jsonResponse += "Country: " + country + "\n\n";
                                        jsonResponse += "Status: " + status + "\n\n";
                                        jsonResponse += "Pollutants: " + pollutants + "\n\n";
                                        jsonResponse += "Effects of Pollutants: " + effects + "\n\n";
                                        jsonResponse += "Recommendation for Children: " + children + "\n\n";
                                        jsonResponse += "Recommendation for Sports: " + sport + "\n\n";
                                        jsonResponse += "Recommendation for Health: " + health + "\n\n";
                                        jsonResponse += "Recommendation for Inside: " + inside + "\n\n";
                                        jsonResponse += "Recommendation for Outside: " + outside + "\n\n";

                                        mTextView.setText(jsonResponse);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(),
                                                "Error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    //add request to queue
                    queue.add(jsonObjectRequest);
                    ////////////
                    RequestQueue requestQueue;

                    ///////////
                    requestQueue = Volley.newRequestQueue(getBaseContext());
                    JsonArrayRequest arrayreq = new JsonArrayRequest(url2,
                            // The second parameter Listener overrides the method onResponse() and passes
                            //JSONArray as a parameter
                            new Response.Listener<JSONArray>() {

                                // Takes the response from the JSON request
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        // Retrieves first JSON object in outer array
                                        JSONObject alerts = response.getJSONObject(0);
                                        // Retrieves "colorArray" from the JSON object
                                        JSONArray arr0 = alerts.getJSONArray("0");
                                        // Iterates through the JSON Array getting objects and adding them
                                        //to the list view until there are no more objects in colorArray
                                        for (int i = 0; i < arr0.length(); i++) {
                                            //gets each JSON object within the JSON array
                                            JSONObject jsonObject = arr0.getJSONObject(i);

                                            // Retrieves the string labeled "colorName" and "hexValue",
                                            // and converts them into javascript objects
                                            String type = jsonObject.getString("wtype_meteoalarm_name");
                                            String hex = jsonObject.getString("hexValue");

                                            // Adds strings from the current object to the data string
                                            //spacing is included at the end to separate the results from
                                            //one another
                                            data += type;
                                        }
                                        // Adds the data string to the TextView "results"
                                        mTextView.setText(mTextView.getText() + "\n\n" + data);
                                    }
                                    // Try and catch are included to handle any errors due to JSON
                                    catch (JSONException e) {
                                        // If an error occurs, this prints the error to the log
                                        e.printStackTrace();
                                    }
                                }
                            },
                            // The final parameter overrides the method onErrorResponse() and passes VolleyError
                            //as a parameter
                            new Response.ErrorListener() {
                                @Override
                                // Handles errors that occur due to Volley
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Volley", "Error");
                                }
                            }
                    );
                    //add request to queue
                    requestQueue.add(arrayreq);
                    //////////


                    Toast.makeText(MainActivity.this, "Network Provider update", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.sample.foo.simplelocationapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.sample.foo.simplelocationapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
