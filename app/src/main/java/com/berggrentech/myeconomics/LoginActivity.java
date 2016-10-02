package com.berggrentech.myeconomics;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Simon on 2016-09-26.
 */

public class LoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        MainActivity.PREFS = getSharedPreferences("userInfo", MODE_PRIVATE);
        MainActivity.DBM = new DatabaseManager(this);


         Utils.resetUserPref();

        deleteDatabase("economyDB.db");
        SQLiteDatabase db = MainActivity.DBM.getWritableDatabase();
        String query = "INSERT INTO users(firstname, lastname, email, password) VALUES" +
                "('Simon', 'Berggren', 'simon.berggren581@gmail.com', 'abc123'), " +
                "('Harlan', 'Strong', 'aliquet.odio@Aliquamrutrumlorem.co.uk', 'orci'), " +
                "('Wallace', 'Hays', 'magna.tellus.faucibus@turpis.org', 'adipiscing'), " +
                "('Rinah', 'Cross', 'auctor.Mauris@portaelit.com', 'amet'), " +
                "('Heidi', 'Owens', 'ante.ipsum@velquam.com', 'libero.'), " +
                "('Gavin', 'Frazier', 'et@Donecsollicitudinadipiscing.com', 'sodales');";
        db.execSQL(query);

        query = "INSERT INTO entries(title, date, sum, category, type, owner) VALUES " +
                "('Skånetrafiken', '2016-08-07', 650, 'Transport', 'Expense', 1), " +
                "('Malmö Högskola', '2016-09-02', 6500, 'Salary', 'Income', 1), " +
                "('Jesper Swisch', '2016-09-09', 200, 'Other', 'Income', 1), " +
                "('Pressbyrån', '2016-09-16', 15, 'Food', 'Expense', 1); ";
        db.execSQL(query);
        db.close();



        User user = Utils.getUserPref();
        if(user == null) {

        } else {
            MainActivity.ID = user.getID();
            MainActivity.USER = user;
            Toast.makeText(this, "going to main!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
}
