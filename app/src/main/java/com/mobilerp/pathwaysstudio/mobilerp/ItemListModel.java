package com.mobilerp.pathwaysstudio.mobilerp;

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

public class ItemListModel {

    private String name;
    private Double price;
    private Integer total;

    private boolean isGroupHeader = false;

    public ItemListModel(String title) {
        this(title, 0.0, 0);
        this.setIsGroupHeader(true);
    }

    public ItemListModel(String name, Double price, Integer total) {
        super();
        this.setName(name);
        this.setPrice(price);
        this.setTotal(total);
    }

    public boolean getIsGroupHeader() {
        return isGroupHeader();
    }

    public void setIsGroupHeader(boolean groupHeader) {
        this.isGroupHeader = groupHeader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPriceString() {
        return String.valueOf(price);
    }

    public Integer getTotal() {
        return total;
    }

    public String getTotalString() {
        return total.toString();
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }
}
