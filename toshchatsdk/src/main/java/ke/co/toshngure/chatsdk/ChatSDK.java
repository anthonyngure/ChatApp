/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk;

import android.content.Context;

/**
 * Created by Anthony Ngure on 04/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class ChatSDK {

    private Context context;
    private static ChatSDK mInstance;

    private ChatSDK(Context context) {
        this.context = context;
    }

    public static ChatSDK init(Context context) {
        if (mInstance  == null){
            mInstance = new ChatSDK(context);

        }
        return mInstance;
    }

    public static ChatSDK getInstance() {
        return mInstance;
    }

    public Context getContext() {
        return context;
    }
}
