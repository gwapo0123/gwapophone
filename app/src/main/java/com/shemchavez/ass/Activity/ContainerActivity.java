package com.shemchavez.ass.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tagmanager.Container;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.shemchavez.ass.Adapter.ReminderAdapter;
import com.shemchavez.ass.Constructor.Driver;
import com.shemchavez.ass.Constructor.History;
import com.shemchavez.ass.Constructor.Reminder;
import com.shemchavez.ass.Constructor.Rental;
import com.shemchavez.ass.Constructor.Taxi;
import com.shemchavez.ass.Fragment.DriverInformationFragment;
import com.shemchavez.ass.Fragment.HistoryFragment;
import com.shemchavez.ass.Fragment.NoRentalFragment;
import com.shemchavez.ass.Fragment.ReminderFragment;
import com.shemchavez.ass.Fragment.ReminderInformationFragment;
import com.shemchavez.ass.Fragment.RentalInformationFragment;
import com.shemchavez.ass.Fragment.RentedTaxiInformationFragment;
import com.shemchavez.ass.R;
import com.shemchavez.ass.Request.AppController;
import com.shemchavez.ass.Request.CustomRequest;
import com.shemchavez.ass.Shared.Config;
import com.shemchavez.ass.Shared.Global;
import com.shemchavez.ass.Sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shemchavez.ass.R.string.reminder;

public class ContainerActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{

    private Toolbar toolbar;
    public Drawer drawer = null;

    private AccountHeader accountHeader;

    private PrimaryDrawerItem pdDriverInformationItem, pdTaxiInformationItem, pdRentalInformationItem, pdReminderItem, pdHistoryItem, pdLogout;
    private Drawable drawableDriverInformation, drawableTaxiInformation, drawableRentalInformation, drawableReminder, drawableHistory, drawableLogout;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private LocationManager locationManager;
    private Double lat, lng;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        dbHelper = new DatabaseHelper(this);
        prepareToolbar();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        prepareNavigationDrawerHeader();
        prepareNavigationDrawerItemsIcon();
        prepareNavigationDrawerItems();
        prepareNavigationDrawer();
        // Default view, driver information fragment
        drawer.setSelection(0, true);

        fetchRentalInformation();
        fetchAllTaxiInformation();
        fetchAllReminder();
    }

    /**
     * Prepare toolbar
     */
    private void prepareToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_complete_name);
    }

    /**
     * Prepare navigation drawer
     */
    private void prepareNavigationDrawer() {
        drawer = new DrawerBuilder(this)
                .withRootView(R.id.activity_container)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentNavigationBar(false)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        pdDriverInformationItem,
                        pdTaxiInformationItem,
                        pdRentalInformationItem,
                        pdReminderItem,
                        pdHistoryItem,
                        new DividerDrawerItem(),
                        pdLogout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        navigationDrawerOnItemClicked(drawerItem);
                        return false;
                    }
                })
                .build();
    }

    /**
     * Prepare navigation drawer header
     */
    private void prepareNavigationDrawerHeader() {
         accountHeader = new AccountHeaderBuilder()
                .withActivity(ContainerActivity.this)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(Global.getTempFullName(ContainerActivity.this))
                                .withIcon(Global.getBitmapFromURL(Global.getTempImage(ContainerActivity.this)))
                )
                .build();
    }

    /**
     * Prepare navigation drawer items
     */
    private void prepareNavigationDrawerItems() {
        pdDriverInformationItem = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.driver_information)
                .withIcon(drawableDriverInformation);

        pdTaxiInformationItem = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.taxi_information)
                .withIcon(drawableTaxiInformation);

        pdRentalInformationItem = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.rental_information)
                .withIcon(drawableRentalInformation);

        pdReminderItem = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(reminder)
                .withIcon(drawableReminder).withSelectable(true).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));

        pdHistoryItem = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName(R.string.history)
                .withIcon(drawableHistory);
        pdLogout = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withName(R.string.logout)
                .withIcon(drawableLogout);
    }

    /**
     * Preapre navigation drawer item icons
     */
    private void prepareNavigationDrawerItemsIcon() {
        drawableDriverInformation = new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_account_circle);
        drawableTaxiInformation = new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_local_taxi);
        drawableRentalInformation = new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_assignment);
        drawableReminder = new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_notifications);
        drawableHistory = new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_history);
        drawableLogout = new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_power_settings_new);
    }

    /**
     * When navigation drawer item is clicked
     * @param drawerItem - drawer item
     */
    private void navigationDrawerOnItemClicked(IDrawerItem drawerItem) {
        if((int) drawerItem.getIdentifier() != 5){
            fetchRentalInformation();
            fetchAllTaxiInformation();
            fetchAllReminder();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        switch ((int) drawerItem.getIdentifier()) {
            case 0:
                fragmentTransaction.replace(R.id.fragmentsContainer, new DriverInformationFragment());
                fragmentTransaction.addToBackStack(Config.FragmentDriverInformation);
                break;
            case 1:
                fragmentTransaction.replace(R.id.fragmentsContainer, new RentedTaxiInformationFragment());
                fragmentTransaction.addToBackStack(Config.FragmentRentedTaxiInformation);
                break;
            case 2:
                fragmentTransaction.replace(R.id.fragmentsContainer, new RentalInformationFragment());
                fragmentTransaction.addToBackStack(Config.FragmentRentalInformation);
                break;
            case 3:
                fragmentTransaction.replace(R.id.fragmentsContainer, new ReminderFragment());
                fragmentTransaction.addToBackStack(Config.FragmentReminder);
                break;
            case 4:
                fragmentTransaction.replace(R.id.fragmentsContainer, new HistoryFragment());
                fragmentTransaction.addToBackStack(Config.FragmentHistory);
                break;
            case 5:
                Global.removeSession(ContainerActivity.this);
                dbHelper.deletaAll();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }
        fragmentTransaction.commit();
    }

    /**
     * Set toolbar title
     * @param entryName - entry name from fragment add to backstack
     */
    private void setToolbarName(String entryName){
        switch (entryName){
            case Config.FragmentDriverInformation:
                getSupportActionBar().setTitle(R.string.driver_information);
                drawer.setSelectionAtPosition(1, false);
                break;
            case Config.FragmentRentedTaxiInformation:
                getSupportActionBar().setTitle(R.string.taxi_information);
                drawer.setSelectionAtPosition(2, false);
                break;
            case Config.FragmentRentalInformation:
                getSupportActionBar().setTitle(R.string.rental_information);
                drawer.setSelectionAtPosition(3, false);
                break;
            case Config.FragmentReminder:
                getSupportActionBar().setTitle(reminder);
                drawer.setSelectionAtPosition(4, false);
                break;
            case Config.FragmentHistory:
                getSupportActionBar().setTitle(R.string.history);
                drawer.setSelectionAtPosition(5, false);
                break;
        }
    }

    /**
     * Get fragment name from add to backstack
     * @return - name of fragment
     */
    private String getFragmentName(){
        int index = fragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
        String entryName = backEntry.getName();
        return entryName;
    }

    LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {

            lat = location.getLatitude();
            lng = location.getLongitude();

            updateDriverLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(ContainerActivity.this, R.string.label_gps_not_available, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Update driver location
     */
    private void updateDriverLocation(){
        final JSONObject jsonObjectUpdateDriverLocation = new JSONObject();
        try {
            jsonObjectUpdateDriverLocation.put("id", Global.getTempID(this));
            jsonObjectUpdateDriverLocation.put("lat", lat);
            jsonObjectUpdateDriverLocation.put("lng", lng);

            Log.v("### JSON", String.valueOf(jsonObjectUpdateDriverLocation));

            StringRequest SR_UPDATE_DRIVER_LOCATION = new StringRequest(Request.Method.POST, Config.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("Error")) {
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ContainerActivity.this, R.string.label_please_check_internet_connection, Toast.LENGTH_SHORT).show();
                            Log.v("### Error", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("request", "update_driver_location");
                    params.put("json", String.valueOf(jsonObjectUpdateDriverLocation));

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(SR_UPDATE_DRIVER_LOCATION, "SR_UPDATE_DRIVER_LOCATION");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch all taxi information
     */
    private void fetchAllTaxiInformation(){
        Log.v("###", "fetchAllTaxiInformation");
        Map<String, String> params = new HashMap<String, String>();
        params.put("request", "fetch_all_taxi_information");
        params.put("json", "");
        CustomRequest CR_FETCH_ALL_TAXI_INFORMATION = new CustomRequest(Request.Method.POST, Config.url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    Log.v("###","have response");
                    Log.v("### JSON", String.valueOf(response));
                    try {
                        JSONArray allTaxiInformationArray = response.getJSONArray("all_taxi_information");
                        for (int i = 0; i < allTaxiInformationArray.length(); i++) {
                            JSONObject allTaxiInformationObject = (JSONObject) allTaxiInformationArray.get(i);

                            Taxi fetchTaxi = new Taxi(allTaxiInformationObject.getString("id"),
                                    allTaxiInformationObject.getString("image"),
                                    allTaxiInformationObject.getString("plate_no"),
                                    allTaxiInformationObject.getString("brand"),
                                    allTaxiInformationObject.getString("body_no"),
                                    allTaxiInformationObject.getString("last_change_oil_date"),
                                    allTaxiInformationObject.getString("availability_status")
                            );
                            if(!dbHelper.addTaxi(fetchTaxi)){
                                dbHelper.updateTaxi(fetchTaxi);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Log.v("###","taxi response is null");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.v("Error in fetch taxi", response.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(CR_FETCH_ALL_TAXI_INFORMATION, "CR_FETCH_ALL_TAXI_INFORMATION");
        Log.v("###","end");
    }

    private void fetchRentalInformation(){
        final JSONObject jsonObjectFetchRentalInformation = new JSONObject();
        try {
            String id = Global.getTempID(this);
            jsonObjectFetchRentalInformation.put("id", id);
            StringRequest SR_FETCH_RENTAL_INFORMATION = new StringRequest(Request.Method.POST, Config.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("Error")) {
                                try {
                                    JSONObject jsonObjectResponse = new JSONObject(response);
                                    Rental fetchRental = new Rental(jsonObjectResponse.getString("id"),
                                            jsonObjectResponse.getString("driver_id"),
                                            jsonObjectResponse.getString("taxi_id"),
                                            jsonObjectResponse.getString("rental_date_from"),
                                            jsonObjectResponse.getString("rental_date_to"),
                                            jsonObjectResponse.getString("total_payment"));
                                    Global.setTempTaxiID(ContainerActivity.this, jsonObjectResponse.getString("taxi_id"));
                                    Global.setTempRentalID(ContainerActivity.this, jsonObjectResponse.getString("id"));
                                    Global.setRentalStatus(ContainerActivity.this, true);
                                    if(!dbHelper.addRental(fetchRental)){
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Global.setRentalStatus(ContainerActivity.this, false);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ContainerActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            Log.v("### Error", error.toString());
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("request", "fetch_rental");
                    params.put("json", String.valueOf(jsonObjectFetchRentalInformation));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(SR_FETCH_RENTAL_INFORMATION, "SR_FETCH_RENTAL_INFORMATION");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch all reminder
     */
    private void fetchAllReminder(){
        final JSONObject jsonObjectReminder = new JSONObject();
        try {
            jsonObjectReminder.put("id", Global.getTempID(ContainerActivity.this));

            Map<String, String> params = new HashMap<String, String>();
            params.put("request", "reminder");
            params.put("json", String.valueOf(jsonObjectReminder));
            CustomRequest CR_FETCH_REMINDER = new CustomRequest(Request.Method.POST, Config.url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response != null){
                        try {
                            JSONArray reminderArray = response.getJSONArray("reminder");
                            Log.v("### JSON", String.valueOf(response));
                            for (int i = 0; i < reminderArray.length(); i++) {
                                JSONObject reminderObject = (JSONObject) reminderArray.get(i);
                                Reminder reminder = new Reminder(
                                        reminderObject.getString("id"),
                                        reminderObject.getString("title"),
                                        reminderObject.getString("message"),
                                        reminderObject.getString("date"),
                                        reminderObject.getInt("status")
                                );
                                dbHelper.addReminder(reminder);
                            }
                            drawer.updateBadge(3, new StringHolder(dbHelper.fetchCountUnreadReminder() + ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError response) {
                    Toast.makeText(ContainerActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }
            });
            AppController.getInstance().addToRequestQueue(CR_FETCH_REMINDER, "CR_FETCH_REMINDER");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check location permission
     * @return - permission
     */
    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Permission result
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Back stack change callback
     */
    @Override
    public void onBackStackChanged() {
        setToolbarName(getFragmentName());
    }

    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    public void onResume() {
        super.onResume();

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("GPS is off");
            alertDialogBuilder.setMessage("This app requires GPS to be enabled.");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Turn on   ", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isNetwork) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
        }

        if (isGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        }
        else{
            int count = fragmentManager.getBackStackEntryCount();

            if (count <= 1) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory( Intent.CATEGORY_HOME );
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                fragmentManager.popBackStack(getFragmentName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }
}
