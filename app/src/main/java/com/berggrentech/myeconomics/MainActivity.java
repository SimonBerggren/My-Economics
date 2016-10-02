package com.berggrentech.myeconomics;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    public static DatabaseManager DBM;
    public static int ID;
    public static User USER;
    public static SharedPreferences PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomBar bar = (BottomBar) findViewById(R.id.bottomBar);
        bar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        setFragment(new OverviewFragment(), "overview_Fragment");
                        break;
                    case R.id.tab_economy:
                        setFragment(new EntriesFragment(), "entries_fragment");
                        break;
                    case R.id.tab_statistics:
                        setFragment(new StatisticsFragment(), "statistics_fragment");
                        break;
                    case R.id.tab_profile:
                        setFragment(new ProfileFragment(), "profile_fragment");
                        break;
                    case R.id.tab_settings:
                        setFragment(new SettingsFragment(), "settings_fragment");
                        break;
                }
            }
        });
    }


    public void setFragment(Fragment fragment, String _Tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment, _Tag);
        ft.commit();
    }
}
