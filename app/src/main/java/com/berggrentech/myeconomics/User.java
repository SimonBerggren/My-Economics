package com.berggrentech.myeconomics;

/**
 * Created by Simon Berggren for assignment 1 in the course Development of Mobile Devices.
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

    int getID() { return mID; }

    String getFirstName() {
        return mFirstName;
    }

    String getLastName() {
        return mLastName;
    }

    String getEmail() {
        return mEmail;
    }

    String getPassword() {
        return mPassword;
    }

    void setFirstName(String _FirstName) {
        mFirstName = _FirstName;
    }

    void setLastName(String _LastName) {
        mLastName = _LastName;
    }

    void setEmail(String _Email) {
        mEmail = _Email;
    }

    void setPassword(String _Password) {
        mPassword = _Password;
    }
}