package com.shemchavez.ass.Fragment;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class TaxiInformationDialogFragment extends DialogFragment {

    private CircularImageView dialogCITaxiImage;
    private TextView dialogTVTaxiID, dialogTVTaxiPlateNumber, dialogTVTaxiBrand, dialogTVTaxiLastChangeOilDate;

    private Bundle bundle;
    private String taxiID;

    private DatabaseHelper dbHelper;

    public TaxiInformationDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_fragment_taxi_information, container, false);
        dbHelper = new DatabaseHelper(getActivity());
        bundle = this.getArguments();

        if (bundle != null) {
            taxiID = bundle.getString("taxiID");
        }

        prepareUI(rootView);
        fetchDialogTaxiFromLocalDB();
        return rootView;
    }

    private void prepareUI(View rootView){
        dialogCITaxiImage = (CircularImageView)rootView.findViewById(R.id.dialogCITaxiImage);
        dialogTVTaxiID = (TextView)rootView.findViewById(R.id.dialogTVTaxiID);
        dialogTVTaxiPlateNumber = (TextView)rootView.findViewById(R.id.dialogTVTaxiPlateNumber);
        dialogTVTaxiBrand = (TextView)rootView.findViewById(R.id.dialogTVTaxiBrand);
        dialogTVTaxiLastChangeOilDate = (TextView)rootView.findViewById(R.id.dialogTVTaxiLastChangeOilDate);
    }

    private void fetchDialogTaxiFromLocalDB(){
        Taxi taxi = dbHelper.fetchTaxi(taxiID);
        String imageURL = Config.plainURL + taxi.getTaxiImage();
        Log.v("### imageURL", imageURL);
        if (imageURL.equals(Config.plainURL)){
            Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.new_taxi_thumbnail);
            dialogCITaxiImage.setImageBitmap(bitmap);
        }
        else{
            new fetchImageTask(dialogCITaxiImage).execute(imageURL);
        }
        dialogTVTaxiID.setText(taxi.getId());
        dialogTVTaxiPlateNumber.setText(taxi.getPlateNumber());
        dialogTVTaxiBrand.setText(taxi.getBrand());
        dialogTVTaxiLastChangeOilDate.setText(Global.formatDateFormat(taxi.getLastChangeOilDate()));
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
}