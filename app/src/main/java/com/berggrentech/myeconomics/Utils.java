package com.berggrentech.myeconomics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simon Berggren for assignment 1 in the course Development of Mobile Devices.
 */
public class Utils {

    // key to user in shared preferences
    private static String PREFS_USER = "user";

    // reads shared preferences and parse string to user object
    // returns current user or null if not logged in
    static User getUserPref() {
        Gson gson = new Gson();
        String json = MainActivity.PREFS.getString(PREFS_USER, "");
        return gson.fromJson(json, User.class);
    }

    // writes to shared preferences and parse user object to string
    static void saveUserPref(User _User) {
        SharedPreferences.Editor prefsEditor = MainActivity.PREFS.edit();
        Gson gson = new Gson();
        String json = gson.toJson(_User);
        prefsEditor.putString(PREFS_USER, json);
        prefsEditor.apply();
    }

    // may also be seen as log out - resets user info in shared preferences
    static void resetUserPref() {
        SharedPreferences.Editor prefsEditor = MainActivity.PREFS.edit();
        Gson gson = new Gson();
        String json = gson.toJson(null);
        prefsEditor.putString(PREFS_USER, json);
        prefsEditor.apply();
    }

    // used similarly to user in shared preferences above
    // parses entry to string for passing as extra argument to fragments
    static Bundle EntryAsArgs(Entry _Entry) {
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String output = gson.toJson(_Entry);
        bundle.putString(PREFS_USER, output);
        return bundle;
    }

    // reads from extra argument to fragments and returns entry
    static Entry EntryFromArgs(Bundle _Bundle) {
        String input = _Bundle.getString(PREFS_USER);
        Gson gson = new Gson();
        return gson.fromJson(input, Entry.class);
    }

    // helper method for easier handling of dates
    static Date toDate(String _Input) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(_Input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // helper method for easier handling of dates
    static Date createDate(int _Year, int _Month, int _Day) {
        String date = String.valueOf(_Year) + "-" +
                String.valueOf(_Month) + "-" +
                String.valueOf(_Day);
        return toDate(date);
    }

    // better-looking strings of dates
    static String dateToString(Date _Input) {
        return new SimpleDateFormat("yyyy-MM-dd").format(_Input);
    }

    // helper method, returns year of passed date
    static int yearOfDate(Date _Input) {
        return Integer.parseInt(dateToString(_Input).split("-")[0]);
    }

    // helper method, returns month of passed date
    static int monthOfDate(Date _Input) {
        return Integer.parseInt(dateToString(_Input).split("-")[1]);
    }

    // helper method, returns day of passed date
    static int dayOfDate(Date _Input) {
        return Integer.parseInt(dateToString(_Input).split("-")[2]);
    }

    // helper method for validating email input
    static boolean isValidEmail(CharSequence _Input) {
        return !TextUtils.isEmpty(_Input) && android.util.Patterns.EMAIL_ADDRESS.matcher(_Input).matches();
    }

    // helper method for easier logging
    static void Log(String _Msg) {
        Log.w("Simon says", _Msg);
    }

    // helper method for easier logging
    static void Log(int _Msg) {
        Log.w("Simon says", String.valueOf(_Msg));
    }

    // returns index of item in spinner - ignores case
    static int indexOf(Spinner _Spinner, String _String) {
        for(int i = 0; i < _Spinner.getCount(); ++i) {
            if(_Spinner.getItemAtPosition(i).toString().equalsIgnoreCase(_String))
                return i;
        }
        return -1;
    }
}
