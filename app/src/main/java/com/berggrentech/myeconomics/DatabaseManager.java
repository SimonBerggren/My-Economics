package com.berggrentech.myeconomics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Simon Berggren for assignment 1 in the course Development of Mobile Devices.
 */
class DatabaseManager extends SQLiteOpenHelper {

    private Context parent;

    // database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "economyDB.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ENTRIES = "entries";

    // user columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "firstname";
    private static final String COLUMN_LAST_NAME = "lastname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // incomes and expenses columns
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_SUM = "sum";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_TYPE = "type";    // income or expense
    private static final String COLUMN_OWNER = "owner";  // id of user

    DatabaseManager(Context _Parent) {
        super(_Parent, DATABASE_NAME, null, DATABASE_VERSION);
        parent = _Parent;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create users
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "("
                + COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + COLUMN_LAST_NAME + " TEXT NOT NULL,"
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

    // adds a user to the database
    void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // returns user from database if the user exists
    // returns null if the user does not exist
    User getUser(String _Email) {
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
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String email = cursor.getString(3);
            String password = cursor.getString(4);

            user = new User(id, firstName, lastName, email, password);
            cursor.close();
        }

        cursor.close();
        db.close();
        return user;
    }

    // updates a users info
    void updateUser(User _User) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRST_NAME, _User.getFirstName());
        values.put(COLUMN_LAST_NAME, _User.getLastName());
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
    String typeToDB(String _Type) {
        if(_Type.equalsIgnoreCase(parent.getResources().getString(R.string.type_income_text))){
            return parent.getResources().getString(R.string.db_type_income);
        } else if(_Type.equalsIgnoreCase(parent.getResources().getString(R.string.type_expense_text))){
            return parent.getResources().getString(R.string.db_type_expense);
        } else {
            return "";
        }
    }

    // same argument as above
    private String categoryToDB(String _Category) {
        if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_entertainment_text))){
            return parent.getResources().getString(R.string.db_category_entertainment);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_food_text))){
            return parent.getResources().getString(R.string.db_category_food);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_household_text))){
            return parent.getResources().getString(R.string.db_category_household);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_transport_text))){
            return parent.getResources().getString(R.string.db_category_transport);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_shopping_text))){
            return parent.getResources().getString(R.string.db_category_shopping);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_other_text))){
            return parent.getResources().getString(R.string.db_category_other);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_savings_text))){
            return parent.getResources().getString(R.string.db_category_savings);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_salary_text))){
            return parent.getResources().getString(R.string.db_category_salary);
        } else {
            return "";
        }
    }

    // same argument as above
    private String categoryFromDB(String _Category) {
        if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.db_category_entertainment))){
            return parent.getResources().getString(R.string.category_entertainment_text);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.db_category_food))){
            return parent.getResources().getString(R.string.category_food_text);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.db_category_household))){
            return parent.getResources().getString(R.string.category_household_text);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.db_category_transport))){
            return parent.getResources().getString(R.string.category_transport_text);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.category_shopping_text))){
            return parent.getResources().getString(R.string.category_shopping_text);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.db_category_other))){
            return parent.getResources().getString(R.string.category_other_text);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.db_category_savings))){
            return parent.getResources().getString(R.string.category_savings_text);
        } else if(_Category.equalsIgnoreCase(parent.getResources().getString(R.string.db_category_salary))){
            return parent.getResources().getString(R.string.category_salary_text);
        } else {
            return "";
        }
    }

    ArrayList<Entry> getEntries(int _ID) {
        return getEntries(_ID, "all", "all");
    }

    ArrayList<Entry> getEntries(int _ID, String _Type, String _Category) {
        return getEntries(_ID, _Type, _Category, "", "");
    }

    // returns a list of entries of the database base upon parameters
    // every parameter is optional, except for the users id
    ArrayList<Entry> getEntries(int _ID, String _Type, String _Category, String _DateFrom, String _DateTo) {

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

    // adds a new income or expense to the database
    void addEntry(int _ID, Entry _Entry) {
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

    // updates an existing entry in the database
    void updateEntry(int _ID, Entry _Entry) {
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

    // removes an entry from the database
    void removeEntry(int _ID) {
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