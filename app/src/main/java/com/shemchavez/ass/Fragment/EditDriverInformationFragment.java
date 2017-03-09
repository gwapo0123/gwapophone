package com.shemchavez.ass.Fragment;


import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
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

/**
 * A simple {@link Fragment} subclass.
 */

public class EditDriverInformationFragment extends Fragment implements ChooseImageDialogFragment.Callback{

    private CircularImageView ciDialogEditDriverImage;
    private EditText edtFirstName, edtMiddleName, edtLastName, edtCompleteAddress, edtContactNumber, edtEmergencyNumber, edtLicenseNumber;

    private FragmentTransaction fragmentTransaction;
    private android.support.v4.app.FragmentManager fragmentManager;

    private DatabaseHelper dbHelper;

    private boolean haveNewImage = false;

    public EditDriverInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_edit_driver_information, container, false);
        dbHelper = new DatabaseHelper(getActivity());
        prepareUI(rootView);
        ciDialogEditDriverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseImageDialog();
            }
        });
        return rootView;
    }

    private void prepareUI(View rootView) {
        ciDialogEditDriverImage = (CircularImageView) rootView.findViewById(R.id.ciDialogEditDriverImage);
        edtFirstName = (EditText) rootView.findViewById(R.id.edtFirstName);
        edtMiddleName = (EditText) rootView.findViewById(R.id.edtMiddleName);
        edtLastName = (EditText) rootView.findViewById(R.id.edtLastName);
        edtCompleteAddress = (EditText) rootView.findViewById(R.id.edtCompleteAddress);
        edtContactNumber = (EditText) rootView.findViewById(R.id.edtContactNumber);
        edtEmergencyNumber = (EditText) rootView.findViewById(R.id.edtEmergencyNumber);
        edtLicenseNumber = (EditText) rootView.findViewById(R.id.edtLicenseNumber);
    }

    private void showChooseImageDialog(){
        ChooseImageDialogFragment chooseImageDialogFragment = new ChooseImageDialogFragment();
        chooseImageDialogFragment.setTargetFragment(this, 0);
        fragmentManager = getFragmentManager();
        chooseImageDialogFragment.show(fragmentManager, "");
    }

    private String getDriverImageBitmap(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ciDialogEditDriverImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        String encodedDriverImage = Global.bitmapToBase64(bitmap);
        Log.v("### encoded image", encodedDriverImage);
        return encodedDriverImage;
    }

    private void fetchDriverFromLocalDB(){
        Driver driver = dbHelper.fetchDriver(Global.getTempID(getActivity()));
        String imageURL = Config.plainURL + driver.getImage();
        Log.v("### image URL", imageURL);

        if(!haveNewImage){
            if (imageURL.equals(Config.plainURL)){
                Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.driver_thumbnail);
                ciDialogEditDriverImage.setImageBitmap(bitmap);
            }
            else{
                new fetchImageTask(ciDialogEditDriverImage).execute(imageURL);
            }
        }

        edtFirstName.setText(driver.getFirstName());
        edtMiddleName.setText(driver.getMiddleName());
        edtLastName.setText(driver.getLastName());
        edtCompleteAddress.setText(driver.getCompleteAddress());
        edtContactNumber.setText(driver.getEmergencyNumber());
        edtEmergencyNumber.setText(driver.getContactNumber());
        edtLicenseNumber.setText(driver.getLicenseNumber());
    }

    private void updatedDriverInformation() {
        try {
            final JSONObject jsonObjectUpdateDriverInformation = new JSONObject();
            jsonObjectUpdateDriverInformation.put("image", getDriverImageBitmap());
            jsonObjectUpdateDriverInformation.put("id", Global.getTempID(getActivity()));
            jsonObjectUpdateDriverInformation.put("fname", edtFirstName.getText().toString().trim());
            jsonObjectUpdateDriverInformation.put("mname", edtMiddleName.getText().toString().trim());
            jsonObjectUpdateDriverInformation.put("lname", edtLastName.getText().toString().trim());
            jsonObjectUpdateDriverInformation.put("complete_address", edtCompleteAddress.getText().toString().trim());
            jsonObjectUpdateDriverInformation.put("contact_no", edtContactNumber.getText().toString().trim());
            jsonObjectUpdateDriverInformation.put("emergency_no", edtEmergencyNumber.getText().toString().trim());
            jsonObjectUpdateDriverInformation.put("license_no", edtLicenseNumber.getText().toString().trim());
            StringRequest SR_UPDATE_DRIVER_INFORMATION = new StringRequest(Request.Method.POST, Config.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("Error")) {
                                try {
                                    JSONObject jsonObjectUpdateInformationResponse = new JSONObject(response);
                                    Driver updatedDriver = new Driver(jsonObjectUpdateInformationResponse.getString("id"),
                                            jsonObjectUpdateInformationResponse.getString("email_address"),
                                            jsonObjectUpdateInformationResponse.getString("password"),
                                            jsonObjectUpdateInformationResponse.getString("image"),
                                            jsonObjectUpdateInformationResponse.getString("fname"),
                                            jsonObjectUpdateInformationResponse.getString("mname"),
                                            jsonObjectUpdateInformationResponse.getString("lname"),
                                            jsonObjectUpdateInformationResponse.getString("complete_address"),
                                            jsonObjectUpdateInformationResponse.getString("contact_no"),
                                            jsonObjectUpdateInformationResponse.getString("emergency_no"),
                                            jsonObjectUpdateInformationResponse.getString("license_no"),
                                            jsonObjectUpdateInformationResponse.getString("lat"),
                                            jsonObjectUpdateInformationResponse.getString("lng"),
                                            jsonObjectUpdateInformationResponse.getString("change_password_status"));
                                    dbHelper.updateDriver(updatedDriver);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                intentToDriverInformation();
                                Toast.makeText(getActivity(), R.string.label_driver_information_updated, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            Log.v("### Error", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                        params.put("request", "update_driver_information");
                        params.put("json", String.valueOf(jsonObjectUpdateDriverInformation));
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(SR_UPDATE_DRIVER_INFORMATION, "srUpdateDriverInformation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void intentToDriverInformation(){
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentsContainer, new DriverInformationFragment());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.driver_information);
        fragmentTransaction.commit();
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
        inflater.inflate(R.menu.edit_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.actionDone) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Save changes?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updatedDriverInformation();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(new IconicsDrawable(getActivity()).icon(GoogleMaterial.Icon.gmd_warning))
                    .show();
        }
        else{
            intentToDriverInformation();
        }
        Global.hideKeyboard(getActivity());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        Log.v("###", "Set bitmap");
        haveNewImage = true;
        ciDialogEditDriverImage.setImageBitmap(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("###", "onResume");
        fetchDriverFromLocalDB();
    }
}
