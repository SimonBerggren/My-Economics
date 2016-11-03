package com.berggrentech.myeconomics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Simon Berggren for assignment 1 in the course Development of Mobile Devices.
 */
public class EntryFragment extends DialogFragment {
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    boolean addingNew = false;
    Entry mEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mEntry = Utils.EntryFromArgs(getArguments());
        if(mEntry == null) {
            addingNew = true;
            mEntry = new Entry("Title", new Date(), 0, "all", "all");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater _Inflater, ViewGroup _Container, Bundle _SavedInstanceState) {
        final View mView =_Inflater.inflate(R.layout.fragment_entry, _Container, false);

        TextView id = (TextView) mView.findViewById(R.id.entry_id);
        final TextView title = (TextView) mView.findViewById(R.id.entry_title);
        final Spinner type = (Spinner) mView.findViewById(R.id.edit_type);
        final TextView date = (TextView) mView.findViewById(R.id.entry_date);
        final Spinner category = (Spinner) mView.findViewById(R.id.edit_category);
        final EditText sum = (EditText) mView.findViewById(R.id.entry_sum);

        List<String> array_types;
        String[] a = getResources().getStringArray(R.array.type_spinner_entries);
        // remove "all" type from array
        array_types = Arrays.asList(Arrays.copyOfRange(a, 1, a.length));

        ArrayAdapter<String> type_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, array_types);
        type.setAdapter(type_adapter);

        id.setText(String.valueOf(mEntry.getID()));
        title.setText(mEntry.getTitle());
        date.setText(Utils.dateToString(mEntry.getDate()));
        sum.setText(String.valueOf(mEntry.getSum()));

        if(!addingNew) {
            String[] entry_categories = {""};
            if(mEntry.getType().equalsIgnoreCase(getResources().getString(R.string.type_income_text))) {
                entry_categories = getResources().getStringArray(R.array.income_category_spinner_entries);
            } else if (mEntry.getType().equalsIgnoreCase(getResources().getString(R.string.type_expense_text))) {
                entry_categories = getResources().getStringArray(R.array.expense_category_spinner_entries);
            }

            ArrayAdapter<String> category_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, entry_categories);
            category.setAdapter(category_adapter);

            category.setSelection(Utils.indexOf(category, mEntry.getCategory()));
            type.setSelection(Utils.indexOf(type, mEntry.getType()));
        }

        class DateDialogFragment extends DialogFragment  implements DatePickerDialog.OnDateSetListener{

            public DateDialogFragment()
            {
            }
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                int year = Utils.yearOfDate(mEntry.getDate());
                int month = Utils.monthOfDate(mEntry.getDate());
                int day = Utils.dayOfDate(mEntry.getDate());
                return new DatePickerDialog(getActivity(), this, year, month - 1, day);
            }
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date d = Utils.createDate(year, monthOfYear, dayOfMonth);
                mEntry.setDate(d);
                date.setText(Utils.dateToString(mEntry.getDate()));
            }
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment d = new DateDialogFragment();
                d.show(getFragmentManager(), "datePicker");
            }
        });

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array = {""};

                if(type.getItemAtPosition(position).toString().equals(getResources().getString(R.string.type_income_text))) {
                    array = getResources().getStringArray(R.array.income_category_spinner_entries);
                } else if (type.getItemAtPosition(position).toString().equals(getResources().getString(R.string.type_expense_text))) {
                    array = getResources().getStringArray(R.array.expense_category_spinner_entries);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, array);
                category.setAdapter(adapter);

                mEntry.setType(MainActivity.DBM.typeToDB(type.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (category.getSelectedItem() == null) {
                    return;
                }
                mEntry.setCategory(category.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mView.findViewById(R.id.save_entry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = ((EditText) mView.findViewById(R.id.entry_sum)).getText().toString();
                int sum = (value.equals("") ? 0 : Integer.parseInt(value));
                mEntry.setSum(sum);
                mEntry.setTitle(title.getText().toString());

                Utils.Log("new sum: " + sum);

                if(addingNew) {
                    MainActivity.DBM.addEntry(Utils.getUserPref().getID(), mEntry);
                } else {
                    MainActivity.DBM.updateEntry(mEntry.getID(), mEntry);
                }
                EntryFragment.this.dismiss();
            }
        });

        mView.findViewById(R.id.cancel_entry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryFragment.this.dismiss();
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout(width, height);
    }
}


