package cn.edu.ustc.igank.ui.settings;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewOutlineProvider;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.support.StatusBarUtil;
import cn.edu.ustc.igank.support.Utils;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int mLang = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        toolbar.setBackgroundColor(getResources().getColor(R.color.appbar));
        StatusBarUtil.setColor(SettingsActivity.this, ContextCompat.getColor(this, R.color.appbar));
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

    public void setLanguage(int lang){
        Intent intent = getIntent();
        intent.putExtra("lang",lang);
        setResult(1,intent);
    }

}
