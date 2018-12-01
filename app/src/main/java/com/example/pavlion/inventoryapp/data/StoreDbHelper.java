package com.example.pavlion.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pavlion.inventoryapp.data.StoreContract.StoreEntry;

public class StoreDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = StoreDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "store.db";

    private static final int DATABASE_VERSION = 1;

    public StoreDbHelper(Context context){

        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String SQL_CREATE_BOOKS_TABLE=" CREATE TABLE " + StoreEntry.TABLE_NAME + " ("
                + StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StoreEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + StoreEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + StoreEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + StoreEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + StoreEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
