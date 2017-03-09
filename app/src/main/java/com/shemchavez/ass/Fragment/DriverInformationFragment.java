package com.shemchavez.ass.Fragment;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.shemchavez.ass.Constructor.Driver;
import com.shemchavez.ass.R;
import com.shemchavez.ass.Request.AppController;
import com.shemchavez.ass.Shared.Config;
import com.shemchavez.ass.Shared.Global;
import com.shemchavez.ass.Sqlite.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.support.v4.content.ContextCompat.getDrawable;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverInformationFragment extends Fragment {

    private CircularImageView ciDriverImage;
    private TextView tvDriverFullName, tvEmailAddress, tvCompleteAddress, tvContactNumber, tvEmergencyNumber, tvDriverLicenseNumber;

    private FragmentTransaction fragmentTransaction;
    private DatabaseHelper dbHelper;
    private Bitmap driverBitmap;

    public DriverInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set edit button in toolbar appear on this fragment only
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_driver_information, container, false);
        dbHelper = new DatabaseHelper(getActivity());
        prepareUI(rootView);
        return rootView;
    }

    private void prepareUI(View rootView) {
        ciDriverImage = (CircularImageView) rootView.findViewById(R.id.ciDriverImage);
        tvDriverFullName = (TextView) rootView.findViewById(R.id.tvDriverFullName);
        tvEmailAddress = (TextView) rootView.findViewById(R.id.tvEmailAddress);
        tvCompleteAddress = (TextView) rootView.findViewById(R.id.tvCompleteAddress);
        tvContactNumber = (TextView) rootView.findViewById(R.id.tvContactNumber);
        tvEmergencyNumber = (TextView) rootView.findViewById(R.id.tvEmergencyNumber);
        tvDriverLicenseNumber = (TextView) rootView.findViewById(R.id.tvDriverLicenseNumber);
    }

    private void fetchDriverInformation(){
        final JSONObject jsonObjectFetchDriverInformation = new JSONObject();
        try {
            String id = Global.getTempID(getActivity());
            jsonObjectFetchDriverInformation.put("id", id);
            jsonObjectFetchDriverInformation.put("fetchJSON", "true");

            StringRequest SR_FETCH_DRIVER_INFORMATION = new StringRequest(Request.Method.POST, Config.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("Error")) {
                                try {
                                    JSONObject jsonObjectLoginResult = new JSONObject(response);
                                    Driver fetchDriver = new Driver(jsonObjectLoginResult.getString("id"),
                                            jsonObjectLoginResult.getString("email_address"),
                                            jsonObjectLoginResult.getString("password"),
                                            jsonObjectLoginResult.getString("image"),
                                            jsonObjectLoginResult.getString("fname"),
                                            jsonObjectLoginResult.getString("mname"),
                                            jsonObjectLoginResult.getString("lname"),
                                            jsonObjectLoginResult.getString("complete_address"),
                                            jsonObjectLoginResult.getString("contact_no"),
                                            jsonObjectLoginResult.getString("emergency_no"),
                                            jsonObjectLoginResult.getString("license_no"),
                                            jsonObjectLoginResult.getString("lat"),
                                            jsonObjectLoginResult.getString("lng"),
                                            jsonObjectLoginResult.getString("change_password_status"));
                                    Global.setTempImage(getActivity(), jsonObjectLoginResult.getString("image"));
                                    Log.v("Response",response);
                                    if(!dbHelper.addDriver(fetchDriver)){
                                        dbHelper.updateDriver(fetchDriver);
                                    }
                                    fetchDriverFromLocalDB();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.v("### Error", e.toString());
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            Log.v("### Error", error.toString());
                            fetchDriverFromLocalDB();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                        params.put("request", "fetch_driver_information");
                        params.put("json", String.valueOf(jsonObjectFetchDriverInformation));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(SR_FETCH_DRIVER_INFORMATION, "srFetchDriverInformation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchDriverFromLocalDB(){
        Driver driver = dbHelper.fetchDriver(Global.getTempID(getActivity()));
        String imageURL = Config.plainURL + driver.getImage();
        Log.v("### image URL", imageURL);
            if (imageURL.equals(Config.plainURL)){
                Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.driver_thumbnail);
                ciDriverImage.setImageBitmap(bitmap);
            }
            else{
                new fetchImageTask(ciDriverImage).execute(imageURL);
            }
        String fullName = driver.getFirstName() + " " + driver.getMiddleName().charAt(0) + ". " + driver.getLastName();
        tvDriverFullName.setText(fullName);
        tvEmailAddress.setText(driver.getEmailAddress());
        tvCompleteAddress.setText(driver.getCompleteAddress());
        tvContactNumber.setText(driver.getContactNumber());
        tvEmergencyNumber.setText(driver.getEmergencyNumber());
        tvDriverLicenseNumber.setText(driver.getLicenseNumber());
    }

    class fetchImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView ivImage;
        public fetchImageTask(ImageView ivImage) {
            this.ivImage = ivImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            ivImage.setImageBitmap(result);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.actionEdit) {
            EditDriverInformationFragment editDriverInformationFragment = new EditDriverInformationFragment();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentsContainer, editDriverInformationFragment);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.edit_driver_information);
            fragmentTransaction.addToBackStack(Config.FragmentEditDriverInformation);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("###", "onResume");
        fetchDriverInformation();
    }
}


