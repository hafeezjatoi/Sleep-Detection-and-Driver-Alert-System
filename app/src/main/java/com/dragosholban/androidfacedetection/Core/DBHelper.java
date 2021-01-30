package com.dragosholban.androidfacedetection.Core;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.dragosholban.androidfacedetection.Models.DataModel;

public class DBHelper extends SQLiteOpenHelper
{

    public static final String DATABASE_NAME = "MyAppDb.db";
    public static final String CONTACTS_TABLE_NAME = "tbl_contacts";

    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_PHONE = "phone";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "(" + CONTACTS_COLUMN_ID + " integer primary key, " + CONTACTS_COLUMN_PHONE + " text)"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean InsertPhone (String phone)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_PHONE, phone);
            db.insert(CONTACTS_TABLE_NAME, null, contentValues);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public int TotalNumbers()
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
            return numRows;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public Integer DeleteContact (Integer id)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(CONTACTS_TABLE_NAME,
                    CONTACTS_COLUMN_ID + " = ? ",
                    new String[] { Integer.toString(id) });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<DataModel> GetAllCotacts()
    {
        ArrayList<DataModel> array_list = new ArrayList<DataModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CONTACTS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            DataModel _model = new DataModel(Integer.parseInt(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID))), res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE)));
            array_list.add(_model);
            res.moveToNext();
        }

        return array_list;
    }
}
