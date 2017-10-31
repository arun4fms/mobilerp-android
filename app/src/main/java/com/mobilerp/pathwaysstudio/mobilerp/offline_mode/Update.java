package com.mobilerp.pathwaysstudio.mobilerp.offline_mode;

import android.content.Context;

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

public class Update {

    public SQLHandler sqlHandler;
    public Context context;
    private String query;
    private boolean isQueryReady;

    public Update(Context context) {
        this.context = context;
        sqlHandler = SQLHandler.getInstance(this.context);
        isQueryReady = false;
    }

    public boolean getIsQueryReady() {
        return isQueryReady;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String... params) {
//        if ((params.length % 2) != 0)
//            isQueryReady = false;
//        this.query = q;
    }

    public boolean execute() {
        if (!isQueryReady) {
            return false;
        }
        sqlHandler.db.execSQL(this.query);
        return true;
    }
}
