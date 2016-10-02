package com.berggrentech.myeconomics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Simon on 2016-09-17.
 */
public class OverviewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        TextView welcome_text = (TextView) view.findViewById(R.id.welcome_text);
        String msg = "Welcome " + Utils.getUserPref().getFirstName() + "!\n" +
                "Here you find some overall information. Please click on statistics to see a nice diagram";

        welcome_text.setText(msg);

        ArrayList<Entry> entries = MainActivity.DBM.getEntries(Utils.getUserPref().getID());
        int totalIncome = 0;
        int totalExpense = 0;
        for(Entry entry : entries) {
            if(entry.getType().equalsIgnoreCase(getResources().getString(R.string.type_income_text))) {
                totalIncome += entry.getSum();
            } else {
                totalExpense += entry.getSum();
            }
        }
        TextView welcome_income = (TextView) view.findViewById(R.id.welcome_income);
        TextView welcome_expense = (TextView) view.findViewById(R.id.welcome_expense);

        welcome_income.setText("Total income: " + totalIncome);
        welcome_expense.setText("Total expense: " + totalExpense);


        return view;
    }
}
