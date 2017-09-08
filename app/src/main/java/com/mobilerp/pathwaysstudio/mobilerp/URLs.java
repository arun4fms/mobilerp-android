package com.mobilerp.pathwaysstudio.mobilerp;

/**
 * Created by Eligio Becerra on 20/07/2017.
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

public class URLs {

    public static final String LOGIN = "api/v1.0/user/checkLogin/";
    public static final String LIST_PRODUCTS = "api/v1.0/listProducts/";
    public static final String LIST_DEPLETED = "api/v1.0/listDepletedProducts/";
    public static final String FIND_PRODUCT = "api/v1.0/findProduct/";
    public static final String NEW_PRODUCT = "api/v1.0/newProduct/";
    public static final String UPDATE_PRODUCT = "api/v1.0/updateProduct/";
    public static final String MAKE_SALE = "api/v1.0/makeSale/";
    public static final String SALES_REPORT = "api/v1.0/getReport/salesreport.pdf";
    public static final String DEPLETED_REPORT = "api/v1.0/getReport/depletedreport.pdf";
    public static final String DB_BACKUP = "api/v1.0/dbBackup/";
    public static String BASE_URL = null;

    private static URLs instance = null;

    protected URLs() {

    }

    public static URLs getInstance() {
        if (instance == null)
            instance = new URLs();
        return instance;
    }

    public void setBASE_URL(String u) {
        BASE_URL = u + "/";
    }
}
