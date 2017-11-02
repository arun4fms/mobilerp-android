package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;

/**
 * Created by Eligio Becerra on 01/11/2017.
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

public class AppState {


    private static AppState instance = null;
    private static Context context;
    private boolean offlineMode;
    private boolean hasPendingOperations;

    protected AppState() {
        offlineMode = SettingsManager.getInstance(context).getBoolean
                (context.getString(R.string.use_offline_mode));
        hasPendingOperations = SettingsManager.getInstance(context).getBoolean
                (context.getString(R.string.has_pending_ops));
    }

    public static AppState getInstance(Context c) {
        context = c;
        if (instance == null)
            instance = new AppState();
        return instance;
    }

    public void setHasPendingOperations(boolean v) {
        SettingsManager.getInstance(context).saveBoolean(context.getString(R.string
                .has_pending_ops), v);
        hasPendingOperations = v;
    }

    public boolean HasPendingOperations() {
        return hasPendingOperations;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(boolean v) {
        SettingsManager.getInstance(context).saveBoolean(context.getString(R.string
                .use_offline_mode), v);
        offlineMode = v;
    }

    public void flushContext() {
        context = null;
    }
}
