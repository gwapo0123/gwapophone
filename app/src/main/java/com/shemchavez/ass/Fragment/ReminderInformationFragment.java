package com.shemchavez.ass.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shemchavez.ass.R;
import com.shemchavez.ass.Shared.Global;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderInformationFragment extends Fragment {

    private TextView RITitle, RIMessage, RIDate;
    public ReminderInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reminder_information_fragment, container, false);
        prepareUI(rootView);

        return rootView;
    }

    private void prepareUI(View rootView){
        RITitle = (TextView)rootView.findViewById(R.id.RITitle);
        RIMessage = (TextView)rootView.findViewById(R.id.RIMessage);
        RIDate = (TextView)rootView.findViewById(R.id.RIDate);
    }
    private void getReminderInformation(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            RITitle.setText(bundle.getString("title"));
            RIMessage.setText(bundle.getString("message"));
            RIDate.setText(Global.formatDateTimeFormat(bundle.getString("date")));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getReminderInformation();
    }
}
