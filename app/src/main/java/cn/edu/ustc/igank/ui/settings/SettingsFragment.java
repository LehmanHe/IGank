package cn.edu.ustc.igank.ui.settings;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.support.Settings;
import cn.edu.ustc.igank.support.Snack;
import cn.edu.ustc.igank.support.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private Preference clearCache;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        clearCache=findPreference(Settings.CLEAR_CACHE);
        clearCache.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
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
