package com.berggrentech.myeconomics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Simon on 2016-09-12.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    Context mParent;

    // database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "economyDB.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ENTRIES = "entries";

    // user columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // incomes and expenses columns
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SUM = "sum";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TYPE = "type";    // income or expense
    public static final String COLUMN_OWNER = "owner";  // id of user

    public DatabaseManager(Context _Parent) {
        super(_Parent, DATABASE_NAME, null, DATABASE_VERSION);
        mParent = _Parent;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create users
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "("
                + COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRSTNAME  + " TEXT NOT NULL, "
                + COLUMN_LASTNAME   + " TEXT NOT NULL,"
                + COLUMN_EMAIL      + " TEXT NOT NULL, "
                + COLUMN_PASSWORD   + " TEXT NOT NULL"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // create entries
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        String CREATE_ENTRIES_TABLE = "CREATE TABLE " +
                TABLE_ENTRIES + "("
                + COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_DATE + " DATE NOT NULL, "
                + COLUMN_SUM + " INTEGER NOT NULL, "
                + COLUMN_CATEGORY + " TEXT NOT NULL, "
                + COLUMN_TYPE + " TEXT NOT NULL, "
                + COLUMN_OWNER + " INTEGER NOT NULL, "
                + "FOREIGN KEY(owner) REFERENCES users(id)"
                + ")";
        db.execSQL(CREATE_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRSTNAME, user.getFirstName());
        values.put(COLUMN_LASTNAME, user.getLastName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // returns user from database if exist
    // returns null if not exist
    public User getUser(String _Email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,            // table to query
                null,                  // return all columns
                COLUMN_EMAIL + " = ?",  // which columns to ask WHERE
                new String[]{_Email},   // what to put instead of '?' in WHERE
                null,                   // don't group rows
                null,                   // don't filter row groups
                null                    // don't sort order
        );

        User user = null;

        // if user is found, get info
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String firstname = cursor.getString(1);
            String lastname = cursor.getString(2);
            String email = cursor.getString(3);
            String password = cursor.getString(4);

            user = new User(id, firstname, lastname, email, password);
            cursor.close();
        }

        cursor.close();
        db.close();
        return user;
    }

    public void updateUser(User _User) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRSTNAME, _User.getFirstName());
        values.put(COLUMN_LASTNAME, _User.getLastName());
        values.put(COLUMN_EMAIL, _User.getEmail());
        values.put(COLUMN_PASSWORD, _User.getPassword());

        String selection = COLUMN_ID + " = ?";
        String[] selectionArguments = {String.valueOf(_User.getID())};

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(
                TABLE_USERS,
                values,
                selection,
                selectionArguments
        );
        db.close();
    }

    // in case the app's language is in another language than english
    // we don't want to add entries with categories in another language
    // so we translate them into english
    public String typeToDB(String _Type) {
        if(_Type.equalsIgnoreCase(mParent.getResources().getString(R.string.type_income_text))){
            return mParent.getResources().getString(R.string.db_type_income);
        } else if(_Type.equalsIgnoreCase(mParent.getResources().getString(R.string.type_expense_text))){
            return mParent.getResources().getString(R.string.db_type_expense);
        } else {
            return "";
        }
    }

    public String typeFromDB(String _Type) {
        if(_Type.equalsIgnoreCase(mParent.getResources().getString(R.string.db_type_income))){
            return  mParent.getResources().getString(R.string.type_income_text);
        } else if(_Type.equalsIgnoreCase(mParent.getResources().getString(R.string.db_type_expense))){
            return mParent.getResources().getString(R.string.type_expense_text);
        } else {
            return "";
        }
    }

    public String categoryToDB(String _Category) {
        if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_entertainment_text))){
            return mParent.getResources().getString(R.string.db_category_entertainment);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_food_text))){
            return mParent.getResources().getString(R.string.db_category_food);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_household_text))){
            return mParent.getResources().getString(R.string.db_category_household);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_transport_text))){
            return mParent.getResources().getString(R.string.db_category_transport);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_shopping_text))){
            return mParent.getResources().getString(R.string.db_category_shopping);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_other_text))){
            return mParent.getResources().getString(R.string.db_category_other);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_savings_text))){
            return mParent.getResources().getString(R.string.db_category_savings);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_salary_text))){
            return mParent.getResources().getString(R.string.db_category_salary);
        } else {
            return "";
        }
    }

    public String categoryFromDB(String _Category) {
        if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.db_category_entertainment))){
            return mParent.getResources().getString(R.string.category_entertainment_text);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.db_category_food))){
            return mParent.getResources().getString(R.string.category_food_text);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.db_category_household))){
            return mParent.getResources().getString(R.string.category_household_text);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.db_category_transport))){
            return mParent.getResources().getString(R.string.category_transport_text);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.category_shopping_text))){
            return mParent.getResources().getString(R.string.category_shopping_text);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.db_category_other))){
            return mParent.getResources().getString(R.string.category_other_text);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.db_category_savings))){
            return mParent.getResources().getString(R.string.category_savings_text);
        } else if(_Category.equalsIgnoreCase(mParent.getResources().getString(R.string.db_category_salary))){
            return mParent.getResources().getString(R.string.category_salary_text);
        } else {
            return "";
        }
    }

    public ArrayList<Entry> getEntries(int _ID) {
        return getEntries(_ID, "all", "all");
    }

    public ArrayList<Entry> getEntries(int _ID, String _Type, String _Category) {
        return getEntries(_ID, _Type, _Category, "", "");
    }

    public ArrayList<Entry> getEntries(int _ID, String _Type, String _Category, String _DateFrom, String _DateTo) {

        Utils.Log("Getting entries....");

        SQLiteDatabase db = this.getWritableDatabase();

        boolean selectType = !typeToDB(_Type).equals("");
        boolean selectCategory = !categoryToDB(_Category).equals("");
        boolean selectDateFrom = !_DateFrom.equals("");
        boolean selectDateTo = !_DateTo.equals("");

        String selection = COLUMN_OWNER + " = ?";
        String selectionType = " AND " + COLUMN_TYPE + " = ?";
        String selectionCategory = " AND " + COLUMN_CATEGORY + " = ?";
        String selectionDateFrom = " AND " + COLUMN_DATE + " >= date(?)";
        String selectionDateTo = " AND " + COLUMN_DATE + " <= date(?)";

        ArrayList<String> args = new ArrayList<>();
        args.add(String.valueOf(_ID));

        if(selectType) {
            selection += selectionType;
            args.add(_Type);
        }
        if(selectCategory) {
            selection += selectionCategory;
            args.add(_Category);
        }

        if(selectDateFrom) {
            selection += selectionDateFrom;
            args.add(_DateFrom);
        }

        if(selectDateTo) {
            selection += selectionDateTo;
            args.add(_DateTo);
        }

        String order = COLUMN_DATE + " ASC ";
        String[] selectionArgs = new String[args.size()];
        selectionArgs = args.toArray(selectionArgs);

        Utils.Log("Arguments:");
        for(String s : selectionArgs) {
            Utils.Log(s);
        }

        Cursor cursor = db.query(
                TABLE_ENTRIES,          // table to query
                null,                   // return all columns
                selection,              // which columns to ask WHERE
                selectionArgs,          // what to put instead of '?' in WHERE
                null,                   // don't group rows
                null,                   // don't filter row groups
                order                   // sort order by date
        );

        ArrayList<Entry> result = new ArrayList<>();

        // if found result, save values
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Date date = Utils.toDate(cursor.getString(2));
                int sum = cursor.getInt(3);
                String category = categoryFromDB(cursor.getString(4));
                String type = cursor.getString(5);

                // add new entry to list
                result.add(new Entry(id, title, date, sum, category, type));

            } while(cursor.moveToNext());
        }

        // close connections and return result
        cursor.close();
        db.close();

        Utils.Log("Got " + result.size() + " entries.");
        return result;
    }

    public void addEntry(int _ID, Entry _Entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, _Entry.getTitle());
        values.put(COLUMN_DATE, Utils.dateToString(_Entry.getDate()));
        values.put(COLUMN_SUM, _Entry.getSum());
        values.put(COLUMN_CATEGORY, categoryToDB(_Entry.getCategory()));
        values.put(COLUMN_TYPE, typeToDB(_Entry.getType()));
        values.put(COLUMN_OWNER, _ID);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_ENTRIES, null, values);
        db.close();
    }

    public void updateEntry(int _ID, Entry _Entry) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, _Entry.getTitle());
        values.put(COLUMN_DATE, Utils.dateToString(_Entry.getDate()));
        values.put(COLUMN_SUM, _Entry.getSum());
        values.put(COLUMN_CATEGORY, categoryToDB(_Entry.getCategory()));
        values.put(COLUMN_TYPE, typeToDB(_Entry.getType()));

        String selection = COLUMN_ID + " = ?";
        String[] selectionArguments = {String.valueOf(_ID)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(
                TABLE_ENTRIES,
                values,
                selection,
                selectionArguments
        );
        db.close();
    }

    public void removeEntry(int _ID) {
        String selection = COLUMN_ID + " = ?";
        String[] selectionArguments = {String.valueOf(_ID)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TABLE_ENTRIES,
                selection,
                selectionArguments
        );
        db.close();
    }
}