package cn.edu.ustc.igank.ui.settings;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.support.CONSTANT;
import cn.edu.ustc.igank.support.Settings;
import cn.edu.ustc.igank.support.Snack;
import cn.edu.ustc.igank.support.Utils;
import cn.edu.ustc.igank.ui.MainActivity;
import cn.edu.ustc.igank.ui.about.AboutDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private Settings mSettings;
    private Preference clearCache;
    private Preference mNightMode;
    private Preference mLanguage;
    private Preference mAboutIGank;

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
        mLanguage=findPreference(Settings.LANGUAGE);
        mLanguage.setOnPreferenceClickListener(this);
        mAboutIGank = findPreference(Settings.ABOUT_IGANK);
        mAboutIGank.setOnPreferenceClickListener(this);

        mLanguage.setSummary(getResources().getStringArray(R.array.langs)[Utils.getCurrentLanguage()]);
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
            Toast.makeText(getActivity(),R.string.clear_cache_success,Toast.LENGTH_SHORT).show();
        }else if(preference == mLanguage){
            showLangDialog();
        }else if(preference == mAboutIGank){
            FragmentManager fm = getFragmentManager();
            AboutDialogFragment dialogFragment = new AboutDialogFragment ();
            dialogFragment.show(fm,"about");
        }
        return false;
    }

    private void showLangDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.text_language))
                .setSingleChoiceItems(
                        getResources().getStringArray(R.array.langs), Utils.getCurrentLanguage(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which != Utils.getCurrentLanguage()) {
                                    mSettings.putInt(Settings.LANGUAGE, which);
                                    Settings.needRecreate = true;
                                }
                                dialog.dismiss();
                                if (Settings.needRecreate) {
                                    getActivity().recreate();
                                    ((SettingsActivity)(getActivity())).setLanguage(which);
                                }
                            }
                        }
                ).show();

    }
}
