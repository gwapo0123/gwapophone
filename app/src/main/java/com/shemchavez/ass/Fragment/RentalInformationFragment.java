package com.shemchavez.ass.Fragment;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.shemchavez.ass.Constructor.Driver;
import com.shemchavez.ass.Constructor.Rental;
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
public class RentalInformationFragment extends Fragment {

    private CircularImageView ciRentalDriverImage, ciRentalTaxiImage;
    private TextView tvRentalDate, tvRentalTotalPayment, tvRentalDriverName, tvRentalTaxiID;
    private DatabaseHelper dbHelper;

    public RentalInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rental_information, container, false);
        dbHelper = new DatabaseHelper(getActivity());
        prepareUI(rootView);
        return rootView;
    }

    private void prepareUI(View rootView){
        ciRentalDriverImage = (CircularImageView) rootView.findViewById(R.id.ciRentalDriverImage);
        ciRentalTaxiImage = (CircularImageView)rootView.findViewById(R.id.ciRentalTaxiImage);
        tvRentalDate = (TextView) rootView.findViewById(R.id.tvRentalDate);
        tvRentalTotalPayment = (TextView)rootView.findViewById(R.id.tvRentalTotalPayment);
        tvRentalDriverName = (TextView)rootView.findViewById(R.id.tvRentalDriverName);
        tvRentalTaxiID = (TextView)rootView.findViewById(R.id.tvRentalTaxiID);
    }

    private void fetchRentalFromLocalDB(){
        if(Global.getRentalStatus(getActivity())){
            Rental rental = dbHelper.fetchRental(Global.getTempRentalID(getActivity()));
            String driverImageURL = Config.plainURL + dbHelper.fetchDriver(Global.getTempID(getActivity())).getImage();
            String taxiImageURL = Config.plainURL + dbHelper.fetchTaxi(Global.getTempTaxiID(getActivity())).getTaxiImage();
            if (driverImageURL.equals(Config.plainURL)){
                Bitmap driverBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.driver_thumbnail);
                ciRentalDriverImage.setImageBitmap(driverBitmap);
            }
            else{
                new fetchImageTask(ciRentalDriverImage).execute(driverImageURL);
            }
            if (taxiImageURL.equals(Config.plainURL)){
                Bitmap taxiBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.new_taxi_thumbnail);
                ciRentalTaxiImage.setImageBitmap(taxiBitmap);
            }
            else{
                new fetchImageTask(ciRentalTaxiImage).execute(taxiImageURL);
            }

            String dateRange =  Global.formatDateFormat(rental.getRentalDateFrom()) + " - " + Global.formatDateFormat(rental.getRentalDateTo());
            tvRentalDate.setText(dateRange);
            tvRentalTotalPayment.setText(rental.getRentalTotalPayment());
            tvRentalDriverName.setText(Global.getTempFullName(getActivity()));
            tvRentalTaxiID.setText(rental.getRentalTaxiID());
        }
        else{
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
        fetchRentalFromLocalDB();
    }
}
