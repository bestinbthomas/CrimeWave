package com.example.crimewave.Force;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.crimewave.Crimes.Crime;
import com.google.gson.Gson;

public class ForcesDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "forces.db";
    public static final String TABLE_NAME = "force";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "SPFORCE";
    public static final int ERROR_INSERTING = 3;
    public static final int ALREADY_EXIST = 2;
    public static final int SUCUSS = 1;

    public ForcesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"("+COL_1+" TEXT PRIMARY KEY,"+COL_2+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }
    public int AddForcetoDB(SpecificForce force){
        SQLiteDatabase db = this.getWritableDatabase();
        if(ckeckIfpresent(force,db)) return ALREADY_EXIST;
        Gson gson = new Gson();
        String forcestring = gson.toJson(force,SpecificForce.class);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,force.getId());
        contentValues.put(COL_2,forcestring);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return ERROR_INSERTING;
        else  return SUCUSS;
    }

    public Cursor getAllFORCESCursor(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }

    boolean ckeckIfpresent(SpecificForce force, SQLiteDatabase db){
        String id = force.getId();
        Cursor cursor = db.query(TABLE_NAME,new String[]{COL_1},"ID = ?",new String[]{id},null,null,null);
        int count = cursor.getCount();
        cursor.close();
        if(count == 0) return false;
        else return true;
    }
}
