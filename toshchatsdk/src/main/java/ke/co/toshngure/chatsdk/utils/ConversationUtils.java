/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk.utils;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import ke.co.toshngure.basecode.utils.PrefUtilsImpl;
import ke.co.toshngure.chatsdk.ChatSDK;
import ke.co.toshngure.chatsdk.R;
import ke.co.toshngure.chatsdk.database.Database;
import ke.co.toshngure.chatsdk.model.BaseUser;
import ke.co.toshngure.chatsdk.model.Conversation;


/**
 * Created by Anthony Ngure on 01/02/2017.
 * Email : anthonyngure25@gmail.com.
 * Company : VibeCampo Social Network..
 */

public class ConversationUtils {

    private static final String TAG = "ConversationUtils";
    public static final String EXTRA_USER = "extra_user";
    public static final String ACTION_USER_CONNECTED_STATUS_CHANGE = "ke.co.toshngure.chatsdk.ACTION_USER_CONNECTED_STATUS_CHANGE";
    public static final String ACTION_USER_TYPING = "ke.co.toshngure.chatsdk.ACTION_USER_TYPING";
    public static final String ACTION_USER_PRESENT = "ke.co.toshngure.chatsdk.ACTION_USER_PRESENT";



    public static void onUserConnected(){
        Intent intent = new Intent(ACTION_USER_CONNECTED_STATUS_CHANGE);
        LocalBroadcastManager.getInstance(ChatSDK.getInstance().getContext())
                .sendBroadcast(intent);
    }

    public static void onUserTyping(){
        Intent intent = new Intent(ACTION_USER_TYPING);
        LocalBroadcastManager.getInstance(ChatSDK.getInstance().getContext())
                .sendBroadcast(intent);
    }

    public static void registerForUserTyping(BroadcastReceiver receiver){
        IntentFilter filter = new IntentFilter(ACTION_USER_TYPING);
        LocalBroadcastManager.getInstance(ChatSDK.getInstance().getContext())
                .registerReceiver(receiver, filter);
    }

    public static void registerForUserConnectedStatus(BroadcastReceiver receiver){
        IntentFilter filter = new IntentFilter(ACTION_USER_CONNECTED_STATUS_CHANGE);
        LocalBroadcastManager.getInstance(ChatSDK.getInstance().getContext())
                .registerReceiver(receiver, filter);
    }

    public static void onUserPresent() {
        Intent intent = new Intent(ACTION_USER_PRESENT);
        LocalBroadcastManager.getInstance(ChatSDK.getInstance().getContext())
                .sendBroadcast(intent);
    }

    public static void registerForUserPresence(BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(ACTION_USER_PRESENT);
        LocalBroadcastManager.getInstance(ChatSDK.getInstance().getContext())
                .registerReceiver(receiver, filter);
    }


    public static Conversation createNewConversation(BaseUser user, BaseUser partner, PrefUtilsImpl prefUtils) {
        Conversation conversation = new Conversation();
        conversation.setId(user.getId()+partner.getId());
        conversation.setPartnerId(partner.getId());
        conversation.setUnread(0);
        setChatDefaults(conversation, prefUtils);
        Database.getInstance().getDaoSession().getConversationDao().insertOrReplace(conversation);
        return conversation;
    }

    public static void setChatDefaults(Conversation conversation, PrefUtilsImpl prefUtils) {
        int globalMyMessageBackground = prefUtils.getInt(R.string.pref_global_my_message_background);
        if (globalMyMessageBackground == 0) {
            globalMyMessageBackground = ContextCompat.getColor(ChatSDK.getInstance().getContext(),
                    R.color.outgoing_message_background);
        }
        conversation.setMyMessageBackground(globalMyMessageBackground);

        int globalMyMessageTextColor = prefUtils.getInt(R.string.pref_global_my_message_text_color);
        if (globalMyMessageTextColor == 0) {
            globalMyMessageTextColor = ContextCompat.getColor(ChatSDK.getInstance().getContext(),
                    R.color.outgoing_message_text_color);
        }
        conversation.setMyMessageTextColor(globalMyMessageTextColor);

        int globalPartnerMessageBackground = prefUtils.getInt(R.string.pref_global_partner_message_background);
        if (globalPartnerMessageBackground == 0) {
            globalPartnerMessageBackground = ContextCompat.getColor(ChatSDK.getInstance().getContext(),
                    R.color.incoming_message_background);
        }
        conversation.setPartnerMessageBackground(globalPartnerMessageBackground);

        int globalPartnerMessageTextColor = prefUtils.getInt(R.string.pref_global_partner_message_text_color);
        if (globalPartnerMessageTextColor == 0) {
            globalPartnerMessageTextColor = ContextCompat.getColor(ChatSDK.getInstance().getContext(),
                    R.color.incoming_message_text_color);
        }
        conversation.setPartnerMessageTextColor(globalPartnerMessageTextColor);

    }
}
