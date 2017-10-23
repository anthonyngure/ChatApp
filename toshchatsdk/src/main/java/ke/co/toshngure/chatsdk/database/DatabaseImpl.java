/*
 * Copyright (c) 2017. Laysan Incorporation
 * Website http://laysan.co.ke
 * Tel +254723203475/+254706356815
 */

package ke.co.toshngure.chatsdk.database;

import android.database.sqlite.SQLiteDatabase;

import ke.co.toshngure.chatsdk.ChatSDK;
import ke.co.toshngure.chatsdk.model.DaoMaster;
import ke.co.toshngure.chatsdk.model.DaoSession;

/**
 * Created by Anthony Ngure on 28/04/2017.
 * Email : anthonyngure25@gmail.com.
 * Company : Laysan Incorporation
 */

public abstract class DatabaseImpl {

    String mDbName;
    DaoSession mDaoSession;
    DaoMaster mDaoMaster;

    protected DatabaseImpl(String dbName) {
        this.mDbName = dbName;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ChatSDK.getInstance().getContext(), dbName, null);
        //MySQLiteOpenHelper helper = new MySQLiteOpenHelper(VCApplication.getInstance(), dbName, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        this.mDaoMaster = new DaoMaster(db);
        this.mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void clean() {
        ChatSDK.getInstance().getContext().deleteDatabase(mDbName);
        onClean();
    }

    protected abstract void onClean();
}
