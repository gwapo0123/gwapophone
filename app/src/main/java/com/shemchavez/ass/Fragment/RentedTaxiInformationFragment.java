package com.shemchavez.ass.Fragment;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.shemchavez.ass.Constructor.Taxi;
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
public class RentedTaxiInformationFragment extends Fragment {

    private CircularImageView ciRentedTaxiImage;
    private TextView tvRentedTaxiID, tvRentedTaxiPlateNumber, tvRentedTaxiBrand, tvRentedTaxiLastChangeOilDate;

    private DatabaseHelper dbHelper;

    public RentedTaxiInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rented_taxi_information, container, false);
        dbHelper = new DatabaseHelper(getActivity());
        prepareUI(rootView);
        return rootView;
    }

    private void prepareUI(View rootView) {
        // Set taxi image
        ciRentedTaxiImage = (CircularImageView) rootView.findViewById(R.id.ciRentedTaxiImage);
        tvRentedTaxiID = (TextView)rootView.findViewById(R.id.tvRentedTaxiID);
        tvRentedTaxiPlateNumber = (TextView) rootView.findViewById(R.id.tvRentedTaxiPlateNumber);
        tvRentedTaxiBrand = (TextView) rootView.findViewById(R.id.tvRentedTaxiBrand);
        tvRentedTaxiLastChangeOilDate = (TextView) rootView.findViewById(R.id.tvRentedTaxiLastChangeOilDate);
    }

    private void fetchTaxiFromLocalDB(){
        if(Global.getRentalStatus(getActivity())){
            Taxi taxi = dbHelper.fetchTaxi(Global.getTempTaxiID(getActivity()));
            Log.v("### taxi id", Global.getTempTaxiID(getActivity()));
            String imageURL = Config.plainURL + taxi.getTaxiImage();
            Log.v("### imageURL", imageURL);
            if (imageURL.equals(Config.plainURL)){
                Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.new_taxi_thumbnail);
                ciRentedTaxiImage.setImageBitmap(bitmap);
            }
            else{
                new fetchImageTask(ciRentedTaxiImage).execute(imageURL);
            }
            tvRentedTaxiID.setText(taxi.getId());
            tvRentedTaxiPlateNumber.setText(taxi.getPlateNumber());
            tvRentedTaxiBrand.setText(taxi.getBrand());
            tvRentedTaxiLastChangeOilDate.setText(Global.formatDateFormat(taxi.getLastChangeOilDate()));
        }
        else {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentsContainer, new NoRentalFragment());
            fragmentTransaction.commit();
        }
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

    public void onResume() {
        super.onResume();
        Log.v("###", "onResume");
        fetchTaxiFromLocalDB();
    }
}