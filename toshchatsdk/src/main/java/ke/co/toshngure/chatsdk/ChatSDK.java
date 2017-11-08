/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk;

import android.content.Context;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

/**
 * Created by Anthony Ngure on 04/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class ChatSDK {

    private Context context;
    private static ChatSDK mInstance;

    private ChatSDK(Context context) {
        this.context = context.getApplicationContext();
    }

    public static void init(Context context) {
        if (mInstance  == null){
            mInstance = new ChatSDK(context.getApplicationContext());
            EmojiManager.install(new IosEmojiProvider());
        }
    }

    public static ChatSDK getInstance() {
        return mInstance;
    }

    public Context getContext() {
        return context;
    }
}
