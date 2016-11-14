package cn.edu.ustc.igank.ui.settings;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.support.CONSTANT;
import cn.edu.ustc.igank.support.Settings;
import cn.edu.ustc.igank.support.Snack;
import cn.edu.ustc.igank.support.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private Settings mSettings;
    private Preference clearCache;
    private Preference mNightMode;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        mSettings = Settings.getInstance();
        clearCache=findPreference(Settings.CLEAR_CACHE);
        clearCache.setOnPreferenceClickListener(this);
        mNightMode=findPreference(Settings.NIGHT_MODE);
        mNightMode.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference == mNightMode){
            Settings.isNightMode = Boolean.valueOf(newValue.toString());
            Settings.needRecreate = true;
            mSettings.putBoolean(mSettings.NIGHT_MODE, Settings.isNightMode);

            if(mSettings.isNightMode && Utils.getSysScreenBrightness() > CONSTANT.NIGHT_BRIGHTNESS){
                Utils.setSysScreenBrightness(CONSTANT.NIGHT_BRIGHTNESS);
            }else if(mSettings.isNightMode == false && Utils.getSysScreenBrightness() == CONSTANT.NIGHT_BRIGHTNESS){
                Utils.setSysScreenBrightness(CONSTANT.DAY_BRIGHTNESS);
            }
            getActivity().recreate();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == clearCache){
            Utils.clearCache();
            Snack.showShort(getString(R.string.clear_cache_success));
        }
        return false;
    }
}
