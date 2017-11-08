package ke.co.toshngure.chatapp;

import android.app.Application;

import ke.co.toshngure.chatsdk.ChatSDK;

/**
 * Created by Anthony Ngure on 23/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class App extends Application {

    private static final String TAG = "App";
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        ChatSDK.init(this);

        mInstance = this;
        /*if (!FirebaseApp.getApps(this).isEmpty()) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(false);
            } catch (Exception e) {
                BeeLog.e(TAG, e);
            }
        }*/
    }

    public static App getInstance() {
        return mInstance;
    }
}
