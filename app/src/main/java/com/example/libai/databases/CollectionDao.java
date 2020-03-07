package com.example.libai.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.libai.model.Collection;

import java.util.ArrayList;

public class CollectionDao {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public CollectionDao(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    public boolean add(Collection collection){
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PHONE,collection.getPhone());
        cv.put(DatabaseHelper.UNIQUEKEY,collection.getUniquekey());
        cv.put(DatabaseHelper.TITLE,collection.getTitle());
        cv.put(DatabaseHelper.URL,collection.getUrl());
        long index = db.insert(DatabaseHelper.TB_COLLECTION,null,cv);
        if (index > 0){
            return true;
        }
        return false;
    }

    public boolean del(Collection collection){
        db = helper.getWritableDatabase();
        int index = db.delete(DatabaseHelper.TB_COLLECTION, DatabaseHelper.PHONE + "=? and " + DatabaseHelper.UNIQUEKEY + "=?", new String[]{collection.getPhone(), collection.getUniquekey()});
        if (index > 0){
            return true;
        }
        return false;
    }

    public boolean isCheck(Collection collection){
        db = helper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_COLLECTION, null, DatabaseHelper.PHONE + "=? and " + DatabaseHelper.UNIQUEKEY + "=?", new String[]{collection.getPhone(), collection.getUniquekey()}, null, null, null);
        if (cursor.moveToFirst()) return true;
        return false;
    }

    public ArrayList<Collection> findPhone(String phone){
        db = helper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_COLLECTION, null, DatabaseHelper.PHONE + "=?", new String[]{phone}, null, null, null);
        ArrayList<Collection> collections = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                String phone1= cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE));
                String uniquekey= cursor.getString(cursor.getColumnIndex(DatabaseHelper.UNIQUEKEY));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
                String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.URL));
                Collection collection = new Collection(phone1,uniquekey,title,url);
                collections.add(collection);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return collections;
    }

    public ArrayList<Collection> findUniquekey(String uniquekey1){
        db = helper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_COLLECTION, null, DatabaseHelper.UNIQUEKEY + "=?", new String[]{uniquekey1}, null, null, null);
        ArrayList<Collection> collections = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                String phone1= cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE));
                String uniquekey= cursor.getString(cursor.getColumnIndex(DatabaseHelper.UNIQUEKEY));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
                String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.URL));
                Collection collection = new Collection(phone1,uniquekey,title,url);
                collections.add(collection);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return collections;
    }

    public void close(){
        if (db != null) db.close();
    }

}
