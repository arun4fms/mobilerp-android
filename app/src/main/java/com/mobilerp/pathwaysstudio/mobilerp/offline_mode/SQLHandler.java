package com.mobilerp.pathwaysstudio.mobilerp.offline_mode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.mobilerp.pathwaysstudio.mobilerp.R;

import java.io.File;

/**
 * Created by Eligio Becerra on 07/09/2017.
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

public class SQLHandler {

    private SQLiteDatabase mydatabase;
    private static boolean databaseOpen;
    private static Context context;
    private static SQLHandler instance;

    public static SQLHandler getInstance(Context _context){
        context = _context;
        if (instance == null || !instance.isDatabaseOpen()){
                instance = new SQLHandler();
        } else {
            File foo = instance.checkFile();
            if (!foo.exists()){
                databaseOpen = false;
                instance = new SQLHandler();
            }
        }
        return instance;
    }

    protected SQLHandler() {
        File file = checkFile();
        if (!file.exists()) {
            Toast.makeText(context, R.string.no_db_file, Toast.LENGTH_LONG).show();
            databaseOpen = false;
        } else {
            mydatabase = SQLiteDatabase.openDatabase(file.getPath(), null, 1);
            databaseOpen = mydatabase.isOpen();
        }
    }

    public static File checkFile(){
        File SDCardRoot = Environment.getExternalStorageDirectory();
        SDCardRoot = new File(SDCardRoot.getAbsolutePath() + "/MobilERP");
        File file = new File(SDCardRoot, context.getString(R.string.database_name));
        return file;
    }

    public boolean isDatabaseOpen() {
        return databaseOpen;
    }
}
