package br.com.pcpleao.apmonitor;

import android.provider.BaseColumns;

/**
 * Created by Placido on 31/10/2015.
 */

public final class APContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public APContract() {}

    /* Inner class that defines the table contents */
    public static abstract class APTable implements BaseColumns {
        public static final String TABLE_NAME = "AP";
        public static final String COLUMN_NAME_SITE = "site";
        public static final String COLUMN_NAME_IP_ADDRESS = "ipaddress";
        public static final String COLUMN_NAME_HOST_NAME = "hostname";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_STATUS = "status";

    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + APTable.TABLE_NAME + " (" +
                    APTable._ID + " INTEGER PRIMARY KEY," +
                    APTable.COLUMN_NAME_SITE + TEXT_TYPE + COMMA_SEP +
                    APTable.COLUMN_NAME_IP_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    APTable.COLUMN_NAME_HOST_NAME + TEXT_TYPE + COMMA_SEP +
                    APTable.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    APTable.COLUMN_NAME_STATUS + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + APTable.TABLE_NAME;
}