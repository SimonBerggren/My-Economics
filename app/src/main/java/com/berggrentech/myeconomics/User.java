package com.berggrentech.myeconomics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simon on 2016-09-12.
 */
public class User {
    private int mID;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPassword;

    // constructor taking full info
    public User(int _ID, String _FirstName, String _LastName, String _Email, String _Password) {
        mID = _ID;
        mFirstName = _FirstName;
        mLastName = _LastName;
        mEmail = _Email;
        mPassword = _Password;
    }

    // constructor without id, for adding new user to database
    public User(String _FirstName, String _LastName, String _Email, String _Password) {
        this(0, _FirstName, _LastName, _Email, _Password);
    }

    // getters

    public int getID() { return mID; }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setFirstName(String _FirstName) {
        mFirstName = _FirstName;
    }

    public void setLastName(String _LastName) {
        mLastName = _LastName;
    }

    public void setEmail(String _Email) {
        mEmail = _Email;
    }

    public  void setPassword(String _Password) {
        mPassword = _Password;
    }
}