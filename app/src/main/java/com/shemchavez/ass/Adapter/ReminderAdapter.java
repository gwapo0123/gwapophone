package com.shemchavez.ass.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shemchavez.ass.Constructor.History;
import com.shemchavez.ass.Constructor.Reminder;
import com.shemchavez.ass.R;

import java.util.List;

/**
 * Created by shemchavez on 2/25/17.
 */

public class ReminderAdapter extends BaseAdapter{

    private TextView reminderRowTitle;;
    private Activity activity;
    private List<Reminder> reminderList;
    private LayoutInflater layoutInflater;

    public ReminderAdapter(Activity activity, List<Reminder> reminderList) {
        this.activity = activity;
        this.reminderList = reminderList;
    }

    @Override
    public int getCount() {
        return reminderList.size();
    }

    @Override
    public Object getItem(int position) {
        return reminderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        prepareLayoutInflater();
        if (row == null) {
            row = layoutInflater.inflate(R.layout.reminder_row, parent, false);
        }
        prepareRowUI(row);
        Reminder reminder = reminderList.get(position);
        reminderRowTitle.setText(reminder.getTitle());
        reminderRowTitle.setTextColor(row.getResources().getColor((reminder.getStatus() == 0) ?(R.color.accent) : (R.color.material_drawer_hint_text)));
        return row;
    }

    /**
     * Prepare inflater for row
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
        reminderRowTitle = (TextView) row.findViewById(R.id.reminderRowTitle);

    }
}
