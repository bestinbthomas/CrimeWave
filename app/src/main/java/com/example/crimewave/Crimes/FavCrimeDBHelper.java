package com.example.crimewave.Crimes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

public class FavCrimeDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favorite.db";
    public static final String TABLE_NAME = "crime";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CRIME";
    public static final int ERROR_INSERTING = 3;
    public static final int ALREADY_EXIST = 2;
    public static final int SUCUSS = 1;

    public FavCrimeDBHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(ID TEXT PRIMARY KEY,CRIME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    public int AddCrime(Crime crime){
        SQLiteDatabase db = this.getWritableDatabase();
        if(ckeckIfpresent(crime,db)) return ALREADY_EXIST;
        Gson gson = new Gson();
        String crimeString = gson.toJson(crime,Crime.class);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,crime.getId());
        contentValues.put(COL_2,crimeString);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return ERROR_INSERTING;
        else  return SUCUSS;
    }

    public Cursor getAllFavCrimeCursor(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }

    boolean ckeckIfpresent(Crime crime, SQLiteDatabase db){
        String id = crime.getId();
        Cursor cursor = db.query(TABLE_NAME,new String[]{COL_1},"ID = ?",new String[]{id},null,null,null);
        int count = cursor.getCount();
        cursor.close();
        if(count == 0) return false;
        else return true;
    }

    public int deleteCrime(Crime crime){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_NAME,"ID = ?",new String[]{crime.getId()});
    }
}
