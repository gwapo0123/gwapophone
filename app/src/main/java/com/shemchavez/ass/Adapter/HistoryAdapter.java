package com.shemchavez.ass.Adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shemchavez.ass.Constructor.History;
import com.shemchavez.ass.Fragment.TaxiInformationDialogFragment;
import com.shemchavez.ass.R;
import com.shemchavez.ass.Shared.Global;

import java.util.List;

/**
 * Created by shemchavez on 1/29/2017.
 */

public class HistoryAdapter extends BaseAdapter {

    private TextView rowTaxiNumber, rowRentalDateFrom, rowRentalDateTo, rowRentalDuration, rowRentalPayment;

    private Activity activity;
    private List<History> historyList;
    private LayoutInflater layoutInflater;
    private Bundle bundle;

    private FragmentManager fragmentManager;

    public HistoryAdapter(Activity activity, List<History> historyList) {
        this.activity = activity;
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        prepareLayoutInflater();

        if (row == null) {
            row = layoutInflater.inflate(R.layout.history_row, parent, false);
        }

        // Prepare row UI
        prepareRowUI(row);

        if(position == 0){
            setHeader();
        }
        else{
            final History history = historyList.get(position - 1);
            rowTaxiNumber.setText(history.getTaxiNumber());
            rowRentalDateFrom.setText(Global.formatDateFormat(history.getRentalDateFrom()));
            rowRentalDateTo.setText(Global.formatDateFormat(history.getRentalDateTo()));
            rowRentalDuration.setText(Global.getDateRange(history.getRentalDateTo(), history.getRentalDateFrom()));
            rowRentalPayment.setText(history.getTotalPayment());

            rowTaxiNumber.setPaintFlags(rowTaxiNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            rowTaxiNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaxiInformationDialogFragment taxiInformationDialogFragment = new TaxiInformationDialogFragment();
                    fragmentManager = activity.getFragmentManager();

                    bundle = new Bundle();
                    bundle.putString("taxiID", history.getTaxiNumber());

                    taxiInformationDialogFragment.setArguments(bundle);
                    taxiInformationDialogFragment.show(fragmentManager, "");
                }
            });
        }
        setAlternativeRowColor(row,position);
        return row;
    }

    /**
     * Prepare layout inflater for row view
     */
    private void prepareLayoutInflater() {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    /**
     * Prepare row UI
     * @param row - view from row
     */
    private void prepareRowUI(View row) {
        rowTaxiNumber = (TextView) row.findViewById(R.id.rowTaxiNumber);
        rowRentalDateFrom = (TextView) row.findViewById(R.id.rowRentalDateFrom);
        rowRentalDateTo = (TextView) row.findViewById(R.id.rowRentalDateTo);
        rowRentalDuration = (TextView) row.findViewById(R.id.rowRentalDuration);
        rowRentalPayment = (TextView) row.findViewById(R.id.rowRentalPayment);
    }

    /**
     * Set header
     */
    private void setHeader(){
        rowTaxiNumber.setText(R.string.label_taxi_number);
        rowRentalDateFrom.setText("Rental");
        rowRentalDateTo.setText("date");
        rowRentalDuration.setText(R.string.label_rental_duration);
        rowRentalPayment.setText(R.string.label_rental_payment);
    }

    /**
     * Set alternate color
     * @param row - row
     * @param position - index of row
     */
    private void setAlternativeRowColor(View row, int position){
        int rowBackgroundColor = ((position % 2) == 0) ?  Color.WHITE: Color.argb(255, 238, 238, 238);
        row.setBackgroundColor(rowBackgroundColor);
    }
}
