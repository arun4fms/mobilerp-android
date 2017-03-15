package com.mobilerp.pathwaysstudio.mobilerp;

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

public class CustomListModel {
    private int icon;
    private String title;

    private boolean isGroupHeader = false;

    public CustomListModel(String title) {
        this(-1, title);
        isGroupHeader = true;
    }

    public CustomListModel(int icon, String title) {
        super();
        this.setIcon(icon);
        this.setTitle(title);
    }

    public boolean getIsGroupHeader() {
        return isGroupHeader;
    }

    public void setIsGroupHeader(boolean groupHeader) {
        isGroupHeader = groupHeader;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
