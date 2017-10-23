/*
 * Copyright (c) 2017. Laysan Incorporation
 * Website http://laysan.co.ke
 * Tel +254723203475/+254706356815
 */

package ke.co.toshngure.chatsdk.database;

/**
 * Created by Anthony Ngure on 8/25/2016.
 * Email : anthonyngure25@gmail.com.
 * Company : Laysan Incorporation
 */
public class Database extends DatabaseImpl {

    private static final String DB_NAME = "data.db";
    private static volatile Database mInstance;
    private Database() {
        super(DB_NAME);
    }

    public static Database getInstance() {
        if (mInstance == null) {
            mInstance = new Database();
        }
        return mInstance;
    }

    @Override
    protected void onClean() {
        mInstance = null;
    }

}
