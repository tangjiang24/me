package com.tj.myandroid.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.tj.myandroid.greendao.dao.AnimalsDao;
import com.tj.myandroid.greendao.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;


/**
 * @Description:数据库升级帮助类
 * @Createdtime:2019/10/18
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public class DbUpgradeOpenHelper extends DaoMaster.OpenHelper {

    public DbUpgradeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                    onCreateAllTables(db,ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                    onDropAllTables(db,ifExists);
            }
        }, AnimalsDao.class);
    }
}
