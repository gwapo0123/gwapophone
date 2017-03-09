package com.shemchavez.ass.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shemchavez.ass.Activity.ContainerActivity;
import com.shemchavez.ass.Adapter.HistoryAdapter;
import com.shemchavez.ass.Adapter.ReminderAdapter;
import com.shemchavez.ass.Constructor.Driver;
import com.shemchavez.ass.Constructor.History;
import com.shemchavez.ass.Constructor.Reminder;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {

    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminderList;

    private ListView lvReminder;

    private DatabaseHelper dbHelper;

    public ReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);
        dbHelper = new DatabaseHelper(getActivity());
        lvReminder = (ListView)rootView.findViewById(R.id.lvReminder);
        lvReminder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                updateReminderStatus(reminderList.get(position));
            }
        });
        return rootView;
    }

    private void fetchReminderFromLocalDB(){
        reminderList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(getActivity(), reminderList);
        lvReminder.setAdapter(reminderAdapter);

        Cursor result = dbHelper.fetchReminder();
        if(result.getCount() != 0){
            while (result.moveToNext()){
                Reminder reminder = new Reminder(
                        result.getString(0),
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4)
                );
                reminderList.add(reminder);
            }
            reminderAdapter.notifyDataSetChanged();
        }
    }

    private void updateReminderStatus(final Reminder reminder){
        final JSONObject jsonObjectReminderStatus = new JSONObject();
        try {
            jsonObjectReminderStatus.put("driverID", Global.getTempID(getActivity()));
            jsonObjectReminderStatus.put("reminderID", reminder.getReminderID());

            Log.v("### JSON", String.valueOf(jsonObjectReminderStatus));

            StringRequest SR_UPDATE_REMINDER_STATUS = new StringRequest(Request.Method.POST, Config.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("Error")) {
                                ReminderInformationFragment reminderInformationFragment = new ReminderInformationFragment();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                                Bundle bundle = new Bundle();
                                bundle.putString("title", reminder.getTitle());
                                bundle.putString("message", reminder.getMessage());
                                bundle.putString("date", reminder.getDate());
                                reminderInformationFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragmentsContainer, reminderInformationFragment);
                                fragmentTransaction.addToBackStack(Config.FragmentReminderInformation);
                                fragmentTransaction.commit();
                                dbHelper.updateReminderStatus(reminder.getReminderID());
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
                        params.put("request", "reminder_status");
                        params.put("json", String.valueOf(jsonObjectReminderStatus));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(SR_UPDATE_REMINDER_STATUS, "SR_UPDATE_REMINDER_STATUS");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        Log.v("###", "onResume");
        fetchReminderFromLocalDB();
    }
}
