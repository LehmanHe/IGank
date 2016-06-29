package cn.edu.ustc.igank.ui.settings;


import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.support.Settings;
import cn.edu.ustc.igank.support.Utils;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int mLang = -1;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        // Language
        mLang = Utils.getCurrentLanguage();
        if (mLang > -1) {
            Utils.changeLanguage(this, mLang);
        }


        //set Theme
//        if(Settings.isNightMode){
//            this.setTheme(R.style.NightTheme);
//        }else{
//            this.setTheme(R.style.DayTheme);
//        }

        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.setting);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.framelayout,new SettingsFragment()).commit();
    }
}
