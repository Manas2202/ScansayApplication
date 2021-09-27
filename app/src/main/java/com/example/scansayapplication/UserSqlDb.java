package com.example.scansayapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
public class UserSqlDb extends SQLiteOpenHelper{
    public UserSqlDb(@Nullable Context context) {
        super(context, "user.db", null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table if not exists UserData(username TEXT , mobile TEXT , shopname Text , address Text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists UserData");
    }
    public Boolean insertUserData(String username,String mobile,String shopname,String address){
        SQLiteDatabase dbs = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("username",username);
        cValues.put("mobile",mobile);
        cValues.put("shopname",shopname);
        cValues.put("address",address);
        long result = dbs.insert("UserData",null,cValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserData",null);
        return cursor;
    }

    public Boolean clearUserTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from UserData");
        return true;
    }
}
