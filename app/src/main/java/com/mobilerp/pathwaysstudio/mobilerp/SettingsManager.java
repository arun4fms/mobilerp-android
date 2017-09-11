package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by Eligio Becerra on 10/09/2017.
 * Copyright (C) 2017 Eligio Becerra
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class SettingsManager {

    /**
     * Singleton instance
     */
    public static SettingsManager instance;
    static String fileName;
    static Context context;
    final int fileMode = Context.MODE_PRIVATE;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    protected SettingsManager() {

        sharedPrefs = context.getSharedPreferences(fileName, fileMode);
    }

    public static SettingsManager getInstance(Context _context) {
        context = _context;
        if (fileName == null) {
            fileName = context.getString(R.string.preferences_file);
        }
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public void saveString(String key, String _value) {
        editor = sharedPrefs.edit();
        editor.putString(key, _value);
        editor.apply();
    }

    public void saveBoolean(String key, boolean _value) {
        editor = sharedPrefs.edit();
        editor.putBoolean(key, _value);
        editor.apply();
    }

    @Nullable
    public String getString(String key) {
        return sharedPrefs.getString(key, null);
    }

    public Boolean getBoolean(String key) {
        return sharedPrefs.getBoolean(key, false);
    }
}
