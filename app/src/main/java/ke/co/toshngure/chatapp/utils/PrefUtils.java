package ke.co.toshngure.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ke.co.toshngure.basecode.utils.PrefUtilsImpl;
import ke.co.toshngure.chatapp.App;

/**
 * Created by Anthony Ngure on 24/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class PrefUtils extends PrefUtilsImpl {


    private static PrefUtils mInstance;
    private static SharedPreferences mSharedPreferences;

    public PrefUtils() {
    }

    public static PrefUtils getInstance() {
        if (mInstance == null){
            mInstance = new PrefUtils();
        }
        return mInstance;
    }

    @Override
    protected SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null){
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        }
        return mSharedPreferences;
    }

    @Override
    protected Context getContext() {
        return App.getInstance();
    }

    @Override
    protected void invalidate() {
        mSharedPreferences = null;
    }
}
