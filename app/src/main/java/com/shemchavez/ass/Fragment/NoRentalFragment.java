package com.shemchavez.ass.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shemchavez.ass.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoRentalFragment extends Fragment {


    public NoRentalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_rental, container, false);
    }
}
