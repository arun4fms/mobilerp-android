package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eligio Becerra on 28/03/2017.
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

public class ItemListAdapter extends ArrayAdapter<ItemListModel> {

    private final Context context;
    private final ArrayList<ItemListModel> modelsArrayList;

    public ItemListAdapter(Context context, ArrayList<ItemListModel> modelsArrayList, int layout) {
        super(context, layout, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

        View rowView;

        if (!modelsArrayList.get(position).getIsGroupHeader()) {
            rowView = inflater.inflate(R.layout.item_row, parent, false);

            TextView itemNameView = (TextView) rowView.findViewById(R.id.itemName);
            TextView itemPriceView = (TextView) rowView.findViewById(R.id.itemPrice);
            TextView itemTotalView = (TextView) rowView.findViewById(R.id.itemTotal);

            itemNameView.setText(modelsArrayList.get(position).getName());
            itemPriceView.setText(modelsArrayList.get(position).getPriceString());
            itemTotalView.setText(modelsArrayList.get(position).getTotalString());
        } else {
            rowView = inflater.inflate(R.layout.list_header_multi_items, parent, false);
            TextView itemTitleView = (TextView) rowView.findViewById(R.id.itemName);
            TextView itemPriceView = (TextView) rowView.findViewById(R.id.itemPrice);
            TextView itemTotalView = (TextView) rowView.findViewById(R.id.itemTotal);
            itemTitleView.setText(R.string.item_name);
            itemPriceView.setText(R.string.item_price);
            itemTotalView.setText(R.string.item_total);
        }

        return rowView;
    }
}