package com.example.libai.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.libai.model.User;

public class UserDao {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public UserDao(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    public void add(User user) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PHONE, user.getPhone());
        cv.put(DatabaseHelper.PASSWORD, user.getPassword());
        cv.put(DatabaseHelper.NAME, user.getName());
        cv.put(DatabaseHelper.PATH, "");
        db.insert(DatabaseHelper.TB_USER, null, cv);
    }

    public boolean checkUser(User user) {
        db = helper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_USER, null, DatabaseHelper.PHONE + "=? and " + DatabaseHelper.PASSWORD + "=?", new String[]{user.getPhone(),user.getPassword()}, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public User checkPhone(String phone1) {
        db = helper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_USER, null, DatabaseHelper.PHONE + "=?", new String[]{phone1}, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PASSWORD));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
            String path = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PATH));
            user = new User(name,path,phone,password);
            return user;
        }
        return user;
    }

    public boolean updatePwd(User user){
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PASSWORD,user.getPassword());
        int index = db.update(DatabaseHelper.TB_USER, cv, DatabaseHelper.PHONE + "=?", new String[]{user.getPhone()});
        if (index > 0) return true;
        return false;
    }

    public boolean updateName(User user){
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.NAME,user.getName());
        int index = db.update(DatabaseHelper.TB_USER, cv, DatabaseHelper.PHONE + "=?", new String[]{user.getPhone()});
        if (index > 0) return true;
        return false;
    }

    public boolean updatePath(User user){
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PATH,user.getPath());
        int index = db.update(DatabaseHelper.TB_USER, cv, DatabaseHelper.PHONE + "=?", new String[]{user.getPhone()});
        if (index > 0) return true;
        return false;
    }

    public void close(){
        if (db != null) db.close();
    }

}
