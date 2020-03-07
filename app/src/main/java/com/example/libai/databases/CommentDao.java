package com.example.libai.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.libai.model.Comment;

import java.util.ArrayList;

public class CommentDao {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public CommentDao(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    public boolean add(Comment comment) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PHONE, comment.getPhone());
        cv.put(DatabaseHelper.UNIQUEKEY, comment.getUniquekey());
        cv.put(DatabaseHelper.CONTENT, comment.getContent());
        cv.put(DatabaseHelper.TIME, comment.getTime());
        long index = db.insert(DatabaseHelper.TB_COMMENT, null, cv);
        if (index > 0) {
            return true;
        }
        return false;
    }

    public ArrayList<Comment> findUniquekey(String uniquekey1) {
        db = helper.getWritableDatabase();
        ArrayList<Comment> list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TB_COMMENT, null, DatabaseHelper.UNIQUEKEY + "=?", new String[]{uniquekey1}, null, null, DatabaseHelper.TIME + " DESC");
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID));
                String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE));
                String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONTENT));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME));
                String uniquekey = cursor.getString(cursor.getColumnIndex(DatabaseHelper.UNIQUEKEY));
                Comment comment = new Comment(id,phone, content, time, uniquekey);
                list.add(comment);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<Comment> findPhone(String phone1) {
        db = helper.getWritableDatabase();
        ArrayList<Comment> list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TB_COMMENT, null, DatabaseHelper.PHONE + "=?", new String[]{phone1}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID));
                String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE));
                String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONTENT));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME));
                String uniquekey = cursor.getString(cursor.getColumnIndex(DatabaseHelper.UNIQUEKEY));
                Comment comment = new Comment(id ,phone, content, time, uniquekey);
                list.add(comment);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public boolean del(int id){
        db = helper.getWritableDatabase();
        int index = db.delete(DatabaseHelper.TB_COMMENT, DatabaseHelper.ID + "=?", new String[]{String.valueOf(id)});
        if (index > 0) return true;
        return false;
    }

    public void close(){
        if (db != null) db.close();
    }

}
