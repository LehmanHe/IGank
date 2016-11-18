package cn.edu.ustc.igank.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.support.CONSTANT;
import cn.edu.ustc.igank.support.Settings;
import cn.edu.ustc.igank.support.StatusBarUtil;
import cn.edu.ustc.igank.support.Utils;
import cn.edu.ustc.igank.ui.android.AndroidFragment;
import cn.edu.ustc.igank.ui.extend.ExtendFragment;
import cn.edu.ustc.igank.ui.girl.GirlFragment;
import cn.edu.ustc.igank.ui.ios.IOSFragment;
import cn.edu.ustc.igank.ui.settings.SettingsActivity;
import cn.edu.ustc.igank.ui.webDesign.WebDesignFragment;

public class MainActivity extends AppCompatActivity {

    private int mLang = -1;
    private Settings mSettings = Settings.getInstance();

    private BottomBar mBottomBar;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private int CHANGE_LANGUAGE = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Language
        mLang = Utils.getCurrentLanguage();
        if (mLang > -1) {
            Utils.changeLanguage(this, mLang);
        }

        // change Brightness
        if(mSettings.isNightMode && Utils.getSysScreenBrightness() > CONSTANT.NIGHT_BRIGHTNESS){
            Utils.setSysScreenBrightness(CONSTANT.NIGHT_BRIGHTNESS);
        }else if(mSettings.isNightMode == false && Utils.getSysScreenBrightness() == CONSTANT.NIGHT_BRIGHTNESS){
            Utils.setSysScreenBrightness(CONSTANT.DAY_BRIGHTNESS);
        }

        if(Settings.isNightMode){
            this.setTheme(R.style.NightTheme);
        }else{
            this.setTheme(R.style.DayTheme);
        }

        StatusBarUtil.setColor(MainActivity.this, ContextCompat.getColor(this, R.color.colorAccent));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("fuck");
        toolbar.setTitle("you");
        Log.e("Meizhi",getString(R.string.meizhi));
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        setBottomBar(savedInstanceState);
    }

    private void setBottomBar(Bundle savedInstanceState) {
        //固定显示
        //mBottomBar = BottomBar.attach(this, savedInstanceState);
        //滑动时自动隐藏
        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.main_content),
                findViewById(R.id.container), savedInstanceState);

        //mBottomBar.useFixedMode();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bb_menu_meizhi) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                    StatusBarUtil.setColor(MainActivity.this, ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                    mViewPager.setCurrentItem(0);
                    toolbar.setTitle(R.string.meizhi);
                } else if (menuItemId == R.id.bb_menu_android) {
                    toolbar.setBackgroundColor(0xFF5FAA3D);
                    StatusBarUtil.setColor(MainActivity.this, 0xFF5FAA3D);
                    mViewPager.setCurrentItem(1);
                    toolbar.setTitle(R.string.android);
                }else if (menuItemId == R.id.bb_menu_ios) {
                    toolbar.setBackgroundColor(0xFF7B1FA2);
                    StatusBarUtil.setColor(MainActivity.this, 0x7B1FA2);
                    mViewPager.setCurrentItem(2);
                    toolbar.setTitle(R.string.ios);
                } else if (menuItemId == R.id.bb_menu_webdesign){
                    toolbar.setBackgroundColor(0xFFFF5252);
                    StatusBarUtil.setColor(MainActivity.this,0xFF5252);
                    mViewPager.setCurrentItem(3);
                    toolbar.setTitle(R.string.web_desigin);
                }else if (menuItemId == R.id.bb_menu_extend) {
                    toolbar.setBackgroundColor(0xFFFF9800);
                    StatusBarUtil.setColor(MainActivity.this, 0xFF9800);
                    mViewPager.setCurrentItem(4);
                    toolbar.setTitle(R.string.extend);
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5FAA3D);
        mBottomBar.mapColorForTab(2, 0xFF7B1FA2);
        mBottomBar.mapColorForTab(3, "#FF5252");
        mBottomBar.mapColorForTab(4, "#FF9800");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_settings){
            Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent,CHANGE_LANGUAGE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_LANGUAGE){
            this.recreate();
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new GirlFragment();
            }else if (position==1){
                return new AndroidFragment();
            }
            else if (position == 2){
                return new IOSFragment();
          //return new AndroidFragment();
            }else if(position == 3){
                return new WebDesignFragment();
            }
            else{
                return new ExtendFragment();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Settings.needRecreate) {
            Settings.needRecreate = false;
            this.recreate();
        }
    }
}
