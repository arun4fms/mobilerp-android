package com.mobilerp.pathwaysstudio.mobilerp.offline_mode;

import android.content.Context;
import android.database.Cursor;

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

public class Select {
    public SQLHandler sqlHandler;
    public Context context;
    public Cursor results;
    private String query;

    public Select(Context context) {
        this.context = context;
        sqlHandler = SQLHandler.getInstance(this.context);
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String q) {
        this.query = q;
    }

    public boolean execute() {
        if (query == null)
            return false;
        results = sqlHandler.mydatabase.rawQuery(query, null);
        return true;
    }
}
