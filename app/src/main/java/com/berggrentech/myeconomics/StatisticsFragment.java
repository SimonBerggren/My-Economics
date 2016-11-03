package com.berggrentech.myeconomics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simon Berggren for assignment 1 in the course Development of Mobile Devices.
 */
public class StatisticsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ArrayList<com.berggrentech.myeconomics.Entry> entries = MainActivity.DBM.getEntries(MainActivity.ID);
        final LineChart linechart = (LineChart) view.findViewById(R.id.linechart_chart);
        List<com.github.mikephil.charting.data.Entry> incomes = new ArrayList<>();
        List<com.github.mikephil.charting.data.Entry> expenses = new ArrayList<>();

        // add data to a line graph
        for(com.berggrentech.myeconomics.Entry e : entries) {
            if(e.getType().equalsIgnoreCase(getResources().getString(R.string.db_type_income)))
                incomes.add(new com.github.mikephil.charting.data.Entry(Utils.dayOfDate(e.getDate()), e.getSum()));
            else if(e.getType().equalsIgnoreCase(getResources().getString(R.string.db_type_expense)))
                expenses.add(new com.github.mikephil.charting.data.Entry(Utils.dayOfDate(e.getDate()), e.getSum()));
        }
        Collections.sort(incomes, new EntryXComparator());
        Collections.sort(expenses, new EntryXComparator());
        LineDataSet incomesDataSet = new LineDataSet(incomes, "incomes");
        LineDataSet expensesDataSet = new LineDataSet(expenses, "expenses");
        LineData data = new LineData(incomesDataSet);
        data.addDataSet(expensesDataSet);
        linechart.setData(data);
        return view;
    }
}