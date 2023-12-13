package com.uit.myairquality.Model;

import android.content.Context;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.uit.myairquality.DashboardFragment;
import com.uit.myairquality.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomChart extends MarkerView {
    private final TextView Value, Time;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());


    public CustomChart(Fragment fragment, int layoutResource) {
        super(fragment.requireContext(), layoutResource);
        Time = findViewById(R.id.Time);
        Value = findViewById(R.id.Value);
    }



    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Value.setText("Value: " + e.getY());

        String Datetime = dateFormat.format(new Date((long) e.getX()));
        Time.setText("Time: " + Datetime);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
