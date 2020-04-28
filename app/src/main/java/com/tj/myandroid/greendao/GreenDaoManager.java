package com.tj.myandroid.greendao;


import android.database.sqlite.SQLiteDatabase;

import com.tj.myandroid.MyApplication;
import com.tj.myandroid.greendao.dao.DaoMaster;
import com.tj.myandroid.greendao.dao.DaoSession;


/**
 * greendao 管理类
 */
public class GreenDaoManager  {
    private static final String TAG = "GreenDaoManager";

    private static final String DB_NAME = "food";
    private static DbUpgradeOpenHelper devOpenHelper;
    private static SQLiteDatabase database;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static void init() {
        devOpenHelper = new DbUpgradeOpenHelper(MyApplication.getContext(),DB_NAME,null);   //数据库名
        database = devOpenHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
    public static SQLiteDatabase getDb() {
        return database;
    }
}
