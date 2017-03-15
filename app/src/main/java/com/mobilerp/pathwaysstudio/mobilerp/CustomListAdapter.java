package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eligio Becerra on 15/03/2017.
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

public class CustomListAdapter extends ArrayAdapter<CustomListModel> {

    private final Context contx;
    private final ArrayList<CustomListModel> modelsArrayList;

    public CustomListAdapter(Context context, ArrayList<CustomListModel> modelsArrayList) {
        super(context, R.layout.option_row, modelsArrayList);
        this.contx = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) contx.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

        View rowView = null;
        if (!modelsArrayList.get(position).getIsGroupHeader()) {
            rowView = inflater.inflate(R.layout.option_row, parent, false);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.rowIcon);
            TextView titleView = (TextView) rowView.findViewById(R.id.rowTextView);

            imageView.setImageResource(modelsArrayList.get(position).getIcon());
            titleView.setText(modelsArrayList.get(position).getTitle());
        } else {
            rowView = inflater.inflate(R.layout.list_header, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.listTitle);
            titleView.setText(modelsArrayList.get(position).getTitle());
        }

        return rowView;
    }

}
