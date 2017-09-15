package com.dashubio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/21 0021
 * 时间： 上午 10:23
 * 描述： DBHelper继承了SQLiteOpenHelper，作为维护和管理数据库的基类
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "dashubio.db";//数据库名称
    public static final String LOGIN_TABLE_NAME = "login_table";//设备登录表
    public static final String USER_TABLE_NAME = "user_table";//小用户表
    public static final String MULTI_TABLE_NAME = "multi_table";//多参数检测仪表
    public static final String BC_TABLE_NAME = "bc_table";//尿液检测仪表
    public static final String BIOCHEMICAL_TABLE_NAME = "biochemical_table";//干式生化仪检测表
    public static final String BREATHING_TABLE_NAME = "breathing_table";//呼吸监控器检测表
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING,pwds STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, mid,name STRING,sex STRING,card_no STRING,phone STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MULTI_TABLE_NAME + "(_id STRING, date STRING, rhythm STRING,pulse STRING,sysdif STRING,sys STRING,dias STRING,mean STRING,oxygen STRING,resp STRING,st STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BC_TABLE_NAME + "(_id,time STRING,URO STRING,BLD STRING,BIL STRING,KET STRING,GLU STRING,PRO STRING,PH STRING,NIT STRING,LEU STRING,SG STRING,VC STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BIOCHEMICAL_TABLE_NAME + "(_id,message STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BREATHING_TABLE_NAME + "(_id,date STRING,tvPef STRING,tvFvc STRING,tvFev1 STRING)");
    }

    //数据库第一次创建时onCreate方法会被调用，我们可以执行创建表的语句，当系统发现版本变化之后，会调用onUpgrade方法，我们可以执行修改表结构等语句
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //在表info中增加一列other
        //db.execSQL("ALTER TABLE info ADD COLUMN other STRING");
    }
}
