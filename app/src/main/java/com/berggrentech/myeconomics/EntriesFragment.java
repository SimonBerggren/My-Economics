package com.berggrentech.myeconomics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by Simon on 2016-09-08.
 */
public class EntriesFragment extends Fragment {
    Spinner dropdownCategories;
    Spinner dropdownTypes;
    EntryViewAnimated entries;
    String dateFrom = "";
    String dateTo = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);
        entries = (EntryViewAnimated) view.findViewById(R.id.entry_listview);
        entries.setAdapter(new EntryViewAdapter(this, MainActivity.DBM.getEntries(MainActivity.ID)));
        entries.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int previousGroup = -1;

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                if(groupPosition != previousGroup)
                    entries.collapseGroupWithAnimation(previousGroup);
                previousGroup = groupPosition;

                // wait until animation has ended before removing element
                entries.postDelayed(new TimerTask() {
                    @Override
                    public void run() {
                        if (entries.isGroupExpanded(groupPosition)) {
                            entries.collapseGroupWithAnimation(groupPosition);
                        } else {
                            entries.expandGroupWithAnimation(groupPosition);
                        }
                    }
                }, 300);
                return true;
            }
        });

        String all = getResources().getString(R.string.type_all_text);
        String income = getResources().getString(R.string.type_income_text);
        String expense = getResources().getString(R.string.type_expense_text);
        String[] types = {all, income, expense};

        String[] categories = {""};

        dropdownCategories = (Spinner) view.findViewById(R.id.category_spinner);
        dropdownCategories.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, categories));
        dropdownCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long _id) {
                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dropdownTypes = (Spinner) view.findViewById(R.id.type_spinner);
        dropdownTypes.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, types));
        dropdownTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long _id) {
                String type = EntriesFragment.this.dropdownTypes.getSelectedItem().toString();
                List<String> categories;

                if(type.equals(getResources().getString(R.string.type_income_text))) {
                    categories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.income_category_spinner_entries)));
                } else if (type.equals(getResources().getString(R.string.type_expense_text))) {
                    categories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.expense_category_spinner_entries)));
                } else {
                    categories = new ArrayList<>();
                }

                categories.add(0, getResources().getString(R.string.type_all_text));

                dropdownCategories.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, categories));

                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // add new entry button
        view.findViewById(R.id.btnAddEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryFragment entFrag = new EntryFragment();
                entFrag.setArguments(Utils.EntryAsArgs(null));
                entFrag.show(getActivity().getFragmentManager(), "entryFragment");
                entFrag.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateList();
                    }
                });
            }
        });

        class DateToDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

            public DateToDialogFragment()
            {
            }
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {

                Calendar calendar = Calendar.getInstance();
                Date date;
                if(dateTo.equals(""))
                    date = calendar.getTime();
                else
                    date = Utils.toDate(dateTo);

                int year = Utils.yearOfDate(date);
                int month = Utils.monthOfDate(date);
                int day = Utils.dayOfDate(date);

                return new DatePickerDialog(getActivity(), this, year, month - 1, day);
            }
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateTo = Utils.dateToString(Utils.createDate(year, monthOfYear+1, dayOfMonth));
                updateList();
            }
            @Override
            public void onCancel(DialogInterface dialog) {
                dateTo = "";
                updateList();
            }
        }

        class DateFromDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

            public DateFromDialogFragment()
            {
            }
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {

                Calendar calendar = Calendar.getInstance();
                Date date;
                if(dateFrom.equals(""))
                    date = calendar.getTime();
                else
                    date = Utils.toDate(dateFrom);

                int year = Utils.yearOfDate(date);
                int month = Utils.monthOfDate(date);
                int day = Utils.dayOfDate(date);

                return new DatePickerDialog(getActivity(), this, year, month - 1, day);
            }
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateFrom = Utils.dateToString(Utils.createDate(year, monthOfYear+1, dayOfMonth));
                updateList();
            }
            @Override
            public void onCancel(DialogInterface dialog) {
                dateFrom = "";
                updateList();
            }
        }

        // set date from
        view.findViewById(R.id.button_set_from_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFromDialogFragment d = new DateFromDialogFragment();
                d.show(getActivity().getFragmentManager(), "dateFromDialog");
            }
        });

        // set date to
        view.findViewById(R.id.button_set_to_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateToDialogFragment d = new DateToDialogFragment();
                d.show(getActivity().getFragmentManager(), "dateToDialog");
            }
        });

        return view;
    }

    public void updateList() {
        String type = EntriesFragment.this.dropdownTypes.getSelectedItem().toString();
        String category = EntriesFragment.this.dropdownCategories.getSelectedItem().toString();
        entries.setAdapter(new EntryViewAdapter(EntriesFragment.this, MainActivity.DBM.getEntries(MainActivity.ID, type, category, dateFrom, dateTo)));
    }
}
