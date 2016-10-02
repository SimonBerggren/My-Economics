package com.berggrentech.myeconomics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * Created by Simon on 2016-09-08.
 */
public class ProfileFragment extends Fragment {

    User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mUser = Utils.getUserPref();

        final TextView name_text = (TextView) view.findViewById(R.id.name_text);
        final String name = mUser.getFirstName() + " " + mUser.getLastName();
        name_text.setText(name);

        final EditText first_name_edit = (EditText) view.findViewById(R.id.first_name_edit);
        final String first_name = mUser.getFirstName();
        first_name_edit.setText(first_name);

        final EditText last_name_edit = (EditText) view.findViewById(R.id.last_name_edit);
        String last_name = mUser.getLastName();
        last_name_edit.setText(last_name);

        final TextView email_text = (TextView) view.findViewById(R.id.email_text);
        String email = mUser.getEmail();
        email_text.setText(email);

        final TextView email_edit = (TextView) view.findViewById(R.id.email_edit);
        email_edit.setText(email);

        final EditText password_edit = (EditText) view.findViewById(R.id.password_edit);
        final EditText conf_password_edit = (EditText) view.findViewById(R.id.password_confirm_edit);
        String password = mUser.getPassword();
        conf_password_edit.setText(password);
        password_edit.setText(password);

        List<Entry> entries = MainActivity.DBM.getEntries(mUser.getID());

        final TextView last_entry_text = (TextView) view.findViewById(R.id.last_entry_text);
        String lastEntry = entries.size() > 0 ?  Utils.dateToString(entries.get(0).getDate()) : "no entries available";
        last_entry_text.setText(lastEntry);

        TextView first_entry_text = (TextView) view.findViewById(R.id.first_entry_text);
        String firstEntry = entries.size() > 0 ? Utils.dateToString(entries.get(entries.size() - 1).getDate()) : "no entries available";
        first_entry_text.setText(firstEntry);

        int totalIncome = 0;
        int totalExpense = 0;

        for(Entry entry : entries) {
            if(entry.getType().equalsIgnoreCase(getResources().getString(R.string.type_income_text))) {
                totalIncome += entry.getSum();
            } else {
                totalExpense += entry.getSum();
            }
        }

        // setup some fun info

        TextView total_income_text = (TextView) view.findViewById(R.id.total_income_text);
        total_income_text.setText(String.valueOf(totalIncome));

        TextView total_expense_text = (TextView) view.findViewById(R.id.total_expense_text);
        total_expense_text.setText(String.valueOf(totalExpense));

        // wait until toast is finished
        final Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                    Utils.resetUserPref();
                    MainActivity.ID = 0;
                    MainActivity.USER = null;
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // logs the user out
        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.resetUserPref();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_LONG).show();
                thread.start();
            }
        });

        view.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditMode(view);
            }
        });

        // save the new credentials
        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = first_name_edit.getText().toString();
                String lastname = last_name_edit.getText().toString();
                String email = email_edit.getText().toString();

                String password = password_edit.getText().toString();
                String confPassword = conf_password_edit.getText().toString();

                if(password.equals(confPassword)) {

                    mUser.setFirstName(firstname);
                    mUser.setLastName(lastname);
                    mUser.setEmail(email);
                    mUser.setPassword(password);

                    MainActivity.DBM.updateUser(mUser);
                    Utils.resetUserPref();
                    Utils.saveUserPref(mUser);
                    MainActivity.USER = mUser;

                    name_text.setText(mUser.getFirstName() + " " + mUser.getLastName());
                    email_text.setText(mUser.getEmail());

                } else {
                    Toast.makeText(getActivity(), "Passwords doesn't match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                toggleEditMode(view);
            }
        });

        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditMode(view);
            }
        });

        return view;
    }

    // turns on all view switchers, remaking the whole ui quite fancy
    private void toggleEditMode(View view) {
        ViewSwitcher email = (ViewSwitcher)view.findViewById(R.id.name_switcher);
        ViewSwitcher name = (ViewSwitcher)view.findViewById(R.id.email_switcher);
        ViewSwitcher password = (ViewSwitcher)view.findViewById(R.id.password_switcher);
        ViewSwitcher buttons = (ViewSwitcher)view.findViewById(R.id.button_switcher);

        email.showNext();
        name.showNext();
        password.showNext();
        buttons.showNext();
    }
}
