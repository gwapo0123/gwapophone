package com.shemchavez.ass.Fragment;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shemchavez.ass.Adapter.HistoryAdapter;
import com.shemchavez.ass.Constructor.History;
import com.shemchavez.ass.Constructor.Reminder;
import com.shemchavez.ass.Constructor.Taxi;
import com.shemchavez.ass.R;
import com.shemchavez.ass.Request.AppController;
import com.shemchavez.ass.Request.CustomRequest;
import com.shemchavez.ass.Shared.Config;
import com.shemchavez.ass.Shared.Global;
import com.shemchavez.ass.Sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private HistoryAdapter historyAdapter;
    private List<History> historyList;

    private ListView lvHistory;

    private DatabaseHelper dbHelper;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        dbHelper = new DatabaseHelper(getActivity());
        return rootView;
    }

    private void fetchHistoryList(){

        JSONObject jsonObjectFetchRentalHistory = new JSONObject();

        try {
            lvHistory = (ListView)getActivity().findViewById(R.id.lvHistory);
            historyList = new ArrayList<>();

            historyAdapter = new HistoryAdapter(getActivity(), historyList);
            lvHistory.setAdapter(historyAdapter);

            jsonObjectFetchRentalHistory.put("id", Global.getTempID(getActivity()));

            Map<String, String> params = new HashMap<String, String>();
            params.put("json", String.valueOf(jsonObjectFetchRentalHistory));
            params.put("request", "fetch_rental_history");

            // Start CR_AllTickets
            CustomRequest CR_FETCH_HISTORY = new CustomRequest(Request.Method.POST, Config.url, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    if(response != null){

                        try {

                            JSONArray historyArray = response.getJSONArray("rental_history");

                            for (int i = 0; i < historyArray.length(); i++) {
                                JSONObject historyObject = (JSONObject) historyArray.get(i);

                                History fetchHistory = new History(
                                        historyObject.getString("id"),
                                        historyObject.getString("taxi_id"),
                                        historyObject.getString("rental_date_from"),
                                        historyObject.getString("rental_date_to"),
                                        historyObject.getString("total_payment")
                                );
                                dbHelper.addHistory(fetchHistory);
                            }
                            fetchHistoryFromLocalDB();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError response) {
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                    fetchHistoryFromLocalDB();
                }
            });
        AppController.getInstance().addToRequestQueue(CR_FETCH_HISTORY, "crFetchHistory");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchHistoryFromLocalDB(){
        Cursor result = dbHelper.fetchHistory();
        if(result.getCount() != 0){
            while (result.moveToNext()){
                History history = new History(
                        result.getString(0),
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4)

                );
                historyList.add(history);
            }
            historyAdapter.notifyDataSetChanged();
        }
    }

    public void onResume() {
        super.onResume();
        Log.v("###", "onResume");
        fetchHistoryList();
    }
}
