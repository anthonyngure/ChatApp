/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.toshngure.chatsdk.model.Message;
import ke.co.toshngure.chatsdk.model.BaseUser;

/**
 * Created by Anthony Ngure on 7/17/2016.
 * Email : anthonyngure25@gmail.com.
 * Company : VibeCampo Social Network..
 */
public class FirebaseUtils {

    public static final int VERSION = 12;
    public static final String CHATS_CHILD = "chats_v" + VERSION;
    public static final String UNREAD_MESSAGES_CHILD = "unread_messages_v" + VERSION;
    public static final String TYPING_CHILD = "typing";
    public static final String PRESENCE_CHILD = "present";
    public static final String SERVER_RECEIPT = "server_receipt";
    public static final String DEVICE_RECEIPT = "device_receipt";
    public static final String READ_RECEIPT = "read_receipt";
    private static final String TAG = FirebaseUtils.class.getSimpleName();
    public static String MESSAGES = "messages";
    private static DatabaseReference mLocalUserUnreadMessagesRef;
    public static DatabaseReference getUserConnectedStatusRef(long userId) {
        return FirebaseDatabase.getInstance().getReference("users/" + userId + "/lastConnected");
    }

    /**
     * chats/chatKey/messages/messageKey/read_receipt
     */
    public static DatabaseReference getReadReceiptRef(Message message) {
        return FirebaseDatabase.getInstance().getReference()
                .child(CHATS_CHILD)
                .child(String.valueOf(message.getConversationId()))
                .child(MESSAGES)
                .child(message.getServerKey())
                .child(READ_RECEIPT);

    }

    /**
     * chats/chatKey/messages/messageKey/device_receipt
     */
    public static DatabaseReference getDeviceReceiptRef(Message message) {
        return FirebaseDatabase.getInstance().getReference()
                .child(CHATS_CHILD)
                .child(String.valueOf(message.getConversationId()))
                .child(MESSAGES)
                .child(message.getServerKey())
                .child(DEVICE_RECEIPT);
    }

    /**
     * chats/chatKey/messages/messageKeyserver_receipt
     */
    public static DatabaseReference getServerReceiptRef(Message message) {
        return FirebaseDatabase.getInstance().getReference()
                .child(CHATS_CHILD)
                .child(String.valueOf(message.getConversationId()))
                .child(MESSAGES)
                .child(message.getServerKey())
                .child(SERVER_RECEIPT);
    }

    public static DatabaseReference getUserUnreadMessagesRef(BaseUser user) {

        if (mLocalUserUnreadMessagesRef == null) {
            mLocalUserUnreadMessagesRef = FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseUtils.UNREAD_MESSAGES_CHILD)
                    .child(String.valueOf(user.getId()));
            //mLocalUserUnreadMessagesRef.keepSynced(true);
        }
        return mLocalUserUnreadMessagesRef;
    }


    /**
     * chats/chatId/userId/typing {true/false}
     */
    public static DatabaseReference getUserTypingRef(long userId, long conversationId) {
        return FirebaseDatabase.getInstance().getReference()
                .child(CHATS_CHILD)
                .child(String.valueOf(conversationId))
                .child(String.valueOf(userId))
                .child(TYPING_CHILD);
    }

    /**
     * chats/chatKey/userId/present {true/false}
     *
     * @param userId
     * @param conversationKey
     * @return
     */
    public static DatabaseReference getUserPresenceRef(long userId, long conversationKey) {
        return FirebaseDatabase.getInstance().getReference()
                .child(CHATS_CHILD)
                .child(String.valueOf(conversationKey))
                .child(String.valueOf(userId))
                .child(PRESENCE_CHILD);
    }

    public class RemoteConfigParams {
        public static final String VERSION_NAME = "versionName";
        public static final String DOWNLOAD_URL = "downloadUrl";
        public static final String UPDATED = "updatedAt";
        public static final String FORCE_INSTALL = "forceInstall";
    }
}
