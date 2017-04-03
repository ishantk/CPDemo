package com.auribises.cpdemo;

import android.net.Uri;

import java.net.URI;

/**
 * Created by ishantkumar on 03/04/17.
 */

public class Util {

    // Information for the Database
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Users.db";

    // Information for Table
    public static final String TAB_NAME = "User";
    public static final String COL_ID = "_ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_PHONE = "PHONE";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_GENDER = "GENDER";
    public static final String COL_CITY = "CITY";

    public static final String CREATE_TAB_QUERY = "create table User(" +
            "_ID int primary key autoincrement," +
            "NAME varchar(256)," +
            "PHONE varchar(256)," +
            "EMAIL varchar(256)," +
            "GENDER varchar(256)," +
            "CITY varchar(256)" +
            ")";

    public static final Uri USER_URI = Uri.parse("content://com.auribises.cpdemo.usercp/"+TAB_NAME);

}
