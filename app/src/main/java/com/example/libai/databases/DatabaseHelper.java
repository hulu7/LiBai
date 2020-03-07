package com.example.libai.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    private static final String DB_NAME = "my.db";
    public static final int DB_VERSION = 1;

    public static String TB_USER = "user";
    public static String PHONE = "phone";
    public static String PASSWORD = "password";
    public static String NAME = "name";
    public static String PATH = "path";
    //用户数据表
    private static String SQL_USER = "create table " + TB_USER + "("
            + PHONE + " varchar(20) primary key,"
            + NAME + " varchar(20),"
            + PATH + " varchar(50),"
            + PASSWORD + " varchar(20))";

    public static String TB_COLLECTION = "collection";
    public static String TITLE = "title";
    public static String URL = "url";
    public static String UNIQUEKEY = "uniquekey";
    //收藏表
    private static String SQL_COLLECTION = "create table " + TB_COLLECTION + "("
            + PHONE + " varchar(20),"
            + UNIQUEKEY + " varchar(50),"
            + TITLE + " varchar(200),"
            + URL + " varchar(200))";

    public static String TB_COMMENT = "comment";
    public static String ID = "id";
    public static String CONTENT = "content";
    public static String TIME = "time";
    //评论表
    private static String SQL_COMMENT = "create table " + TB_COMMENT + "("
            + ID + " integer primary key autoincrement,"
            + PHONE + " varchar(20),"
            + CONTENT + " text,"
            + TIME + " varchar(20),"
            + UNIQUEKEY + " varchar(50))";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_USER);
        db.execSQL(SQL_COLLECTION);
        db.execSQL(SQL_COMMENT);
    }

    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

}
