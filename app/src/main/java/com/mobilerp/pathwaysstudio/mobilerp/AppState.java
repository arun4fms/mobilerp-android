package com.mobilerp.pathwaysstudio.mobilerp;

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

    public static void setHasPendingOperations
    private static boolean isAppOffline;
    private static AppState instance;
    private static boolean hasPendingOperations;

    {

    }

    protected AppState() {
        isAppOffline = SettingsManager.getInstance(getContext()).getBoolean
                (getString(R.string.use_offline_mode));
        isAppOffline = SettingsManager.getInstance(getContext()).getBoolean
                (getString(R.string.has_pending_ops));
    }

    public static AppState getInstance() {
        if (instance == null)
            instance = new AppState();
        return instance;
    }

    public static boolean HasPendingOperations() {
        return hasPendingOperations;
    }

    public static boolean isIsAppOffline() {
        return isAppOffline;
    }

    public static void setIsAppOffline(boolean v) {
        isAppOffline = v;
    }

}
