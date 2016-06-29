/*
 *  Copyright (C) 2015 MummyDing
 *
 *  This file is part of Leisure( <https://github.com/MummyDing/Leisure> )
 *
 *  Leisure is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *                             ｀
 *  Leisure is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Leisure.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.edu.ustc.igank.support;

import android.content.Context;
import android.content.SharedPreferences;


import cn.edu.ustc.igank.IGankApplication;

public class Settings {

    public static boolean needRecreate = false;
    public static boolean isShakeMode = true;
    public static boolean noPicMode = false;
    public static boolean isNightMode = false;
    public static boolean isAutoRefresh = false;
    public static boolean isExitConfirm = true;
    public static int searchID = 0;
    public static int swipeID = 0;

    public static final String XML_NAME = "settings";

    public static final String SHAKE_TO_RETURN = "shake_to_return_new";

    public static final String NO_PIC_MODE = "no_pic_mode";

    public static final String NIGHT_MODE = "night_mode";

    public static final String AUTO_REFRESH = "auto_refresh";

    public static final String LANGUAGE = "language";

    public static final String EXIT_CONFIRM = "exit_confirm";

    public static final String CLEAR_CACHE = "clear_cache";

    public static final String SEARCH = "search";

    public static final String SWIPE_BACK = "swipe_back";
    private static Settings sInstance;

    private SharedPreferences mPrefs;

    public static Settings getInstance() {
        if (sInstance == null) {
            sInstance = new Settings(IGankApplication.AppContext);
        }
        return sInstance;
    }

    private Settings(Context context) {
        mPrefs = context.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
    }

    public Settings putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).commit();
        return this;
    }

    public boolean getBoolean(String key, boolean def) {
        return mPrefs.getBoolean(key, def);
    }

    public Settings putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).commit();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public Settings putString(String key, String value) {
        mPrefs.edit().putString(key, value).commit();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

}
