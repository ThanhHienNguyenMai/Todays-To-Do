package com.example.goodhabits;

import android.renderscript.Sampler;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class IntegerValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public IntegerValueFormatter() {
        mFormat = new DecimalFormat("###,###,###"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format((int)value) + ""; // e.g. append a dollar-sign
    }


}