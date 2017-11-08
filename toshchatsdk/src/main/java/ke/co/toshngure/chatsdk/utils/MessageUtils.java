package ke.co.toshngure.chatsdk.utils;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import ke.co.toshngure.basecode.utils.DatesHelper;
import ke.co.toshngure.basecode.utils.PrefUtilsImpl;
import ke.co.toshngure.chatsdk.ChatSDK;
import ke.co.toshngure.chatsdk.R;
import ke.co.toshngure.chatsdk.database.Database;
import ke.co.toshngure.chatsdk.model.BaseUser;
import ke.co.toshngure.chatsdk.model.Conversation;
import ke.co.toshngure.chatsdk.model.Message;

/**
 * Created by Anthony Ngure on 01/02/2017.
 * Email : anthonyngure25@gmail.com.
 * Company : VibeCampo Social Network..
 */

public class MessageUtils {

    private static final String TAG = MessageUtils.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String ACTION_RECEIPT_CHANGE = "com.vibecampo.android.ACTION_RECEIPT_CHANGE";
    public static final String ACTION_NEW_MESSAGE = "com.vibecampo.android.ACTION_NEW_MESSAGE";
    public static final int NO_RECEIPT = -1;
    public static final String ACTION_CHAT_OPENED = "com.vibecampo.android.ACTION_CHAT_OPENED";
    public static final String EXTRA_CHAT = "extra_chat";
    public static final String EXTRA_CHAT_ID = "extra_chat_id";
    public static final String ACTION_CHAT_MESSAGES_DELETED = "com.vibecampo.android.ACTION_CHAT_MESSAGES_DELETED";
    public static final String ACTION_CHAT_DELETED = "com.vibecampo.android.ACTION_CHAT_DELETED";
    private static long openedChat = -1;


    public static void registerForNewMessage(BroadcastReceiver receiver) {
        IntentFilter newMessageFilter = new IntentFilter(ACTION_NEW_MESSAGE);
        LocalBroadcastManager.getInstance(ChatSDK.getInstance().getContext()).registerReceiver(receiver, newMessageFilter);
    }

    public static void onReceiptChange(Message message) {
        Intent intent = new Intent(ACTION_RECEIPT_CHANGE);
        intent.putExtra(EXTRA_MESSAGE, message);
        LocalBroadcastManager.getInstance(ChatSDK.getInstance().getContext()).sendBroadcast(intent);
    }

    public static String getDisplayLastSeen(long lastSeen) {
        String val;
        if (DatesHelper.isToday(lastSeen)) {
            val = String.format(ChatSDK.getInstance().getContext().getString(R.string.last_seen_today),
                    DatesHelper.formatJustTime(ChatSDK.getInstance().getContext(), lastSeen)
            );
        } else if (DatesHelper.isYesterday(lastSeen)) {
            val = String.format(ChatSDK.getInstance().getContext().getString(R.string.last_seen_yesterday),
                    DatesHelper.formatJustTime(ChatSDK.getInstance().getContext(), lastSeen)
            );
        } else {
            val = DatesHelper.formatJustDate(lastSeen);
        }
        return val;
    }







    /*
    public static void onChatClosed() {
        BeeLog.d(TAG, "onChatClosed");
        openedChat = -1;
    }

    public static void onChatOpened(Conversation chat) {
        BeeLog.d(TAG, "onChatOpened " + chat.getId());
        openedChat = chat.getId();
        chat.setUnread(0);
        Database.getInstance().getChatDao().update(chat);
        List<MessageOld> unseenMessagesInThisChat = Database.getInstance().getMessageDao()
                .queryBuilder().where(
                        MessageDao.Properties.Seen.eq(false),
                        MessageDao.Properties.ChatId.eq(chat.getId())
                ).list();

        for (MessageOld message : unseenMessagesInThisChat) {
            message.setSeen(true);
            FirebaseUtils.getReadReceiptRef(message).setValue(ServerValue.TIMESTAMP);
            Database.getInstance().getMessageDao().update(message);
        }

        long unseen = Database.getInstance().getMessageDao().queryBuilder().where(MessageDao.Properties.Seen.eq(false)).count();

        MainActivity.onUnreadMessages(Integer.parseInt(String.valueOf(unseen)));

        updateMessageNotifications(true);

        Intent intent = new Intent(ACTION_CHAT_OPENED);
        intent.putExtra(EXTRA_CHAT, chat);
        LocalBroadcastManager.getInstance(VCApplication.getInstance()).sendBroadcast(intent);
    }

    public static void registerForChatOpened(BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(ACTION_CHAT_OPENED);
        LocalBroadcastManager.getInstance(VCApplication.getInstance()).registerReceiver(receiver, filter);
    }


    public static String getDisplayLastMessageTimestamp(long lastSeen) {
        String val;
        if (DatesHelper.isToday(lastSeen)) {
            val = (String) DatesHelper.formatJustTime(lastSeen);
        } else if (DatesHelper.isYesterday(lastSeen)) {
            val = VCApplication.getInstance().getString(R.string.yesterday);
        } else {
            val = DatesHelper.formatJustDate(lastSeen);
        }
        return val;
    }


    public static void onNewMessage(Message message) {

        Intent intent = new Intent(ACTION_NEW_MESSAGE);
        intent.putExtra(EXTRA_MESSAGE, message);


        if (isForLocalUser(message)) {
            LocalBroadcastManager.getInstance(VCApplication.getInstance()).sendBroadcast(intent);
            return;
        }

        if (message == null) {
            return;
        }

        MessageOld quotedMessage = Database.getInstance().getMessageDao().queryBuilder()
                .where(MessageDao.Properties.ServerKey.eq(message.getServerKey()),
                        MessageDao.Properties.ChatId.eq(message.getId()))
                .unique();

        if (quotedMessage != null) {
            message.setQuotedMessageId(quotedMessage.getId());
            message.setQuotedMessage(quotedMessage);
        }

        //Update message got to device
        FirebaseUtils.getDeviceReceiptRef(message).setValue(ServerValue.TIMESTAMP);

        if (message.getConversationId() == openedChat) {

            message.setSeen(true);
            Database.getInstance().getMessageDao().update(message);

            //Because the message is received in a conversation request for incoming message sound
            SoundEventsUtil.getInstance().onEvent(SoundEventsUtil.Event.INCOMING_MESSAGE);

            //Send read receipt for this message coz its chat is opened
            FirebaseUtils.getReadReceiptRef(message).setValue(ServerValue.TIMESTAMP);

            LocalBroadcastManager.getInstance(VCApplication.getInstance()).sendBroadcast(intent);

            //updateMessageNotifications(true);

        } else {
            //This might be a new chat
            //Check if user is in db
            User sender = Database.getInstance().getUserDao().load(message.getSenderId());

            //Check if the message sender is null
            if (sender == null) {
                BeeLog.i(TAG, "Sender details not available.");
                VCApplication.getInstance()
                        .getJobManager()
                        .addJobInBackground(new GetMessageSenderJob(message.getSenderId()));
            } else {
                LocalBroadcastManager.getInstance(VCApplication.getInstance()).sendBroadcast(intent);
                updateMessageNotifications(false);
            }
        }
    }

    public static boolean isForLocalUser(Message message) {
        return (message.getSenderId() == PrefUtils.getInstance().getUser().getId());
    }

    public static void updateMessageNotifications(boolean silently) {


        int chatsWithUnread = 0;

        //Get all unread chats
        List<Conversation> chats = Database.getInstance().getChatDao()
                .queryBuilder().where(ChatDao.Properties.Unread.gt(0)).list();

        BeeLog.d(TAG, "Chats with unread = " + chats.size());

        //Collect valid unread messages for notifications i.e sender ny=ust not be null
        List<MessageOld> validUnreadMessages = new ArrayList<>();

        for (Conversation chat : chats) {
            //Get unread messages in this chat
            List<MessageOld> unseenInThisChat = Database.getInstance().getMessageDao()
                    .queryBuilder().where(
                            MessageDao.Properties.ChatId.eq(chat.getId()),
                            MessageDao.Properties.Seen.eq(false),
                            MessageDao.Properties.SenderId.notEq(PrefUtils.getInstance().getUser().getId())
                    ).list();

            for (MessageOld message : unseenInThisChat) {
                if ((message.getSender() != null)) {
                    validUnreadMessages.add(message);
                }
            }

            chatsWithUnread++;
        }

        if (MainActivity.isRunning() && !silently) {
            silently = true;
        }

        //If there are unread messages update message notification
        if (validUnreadMessages.size() > 0) {

            MessageNotification.notify(validUnreadMessages, chatsWithUnread, silently);

            //There are no unread messages so cancel messages notification
        } else {
            MessageNotification.cancel();
        }

        MainActivity.onUnreadMessages(validUnreadMessages.size());

    }

    public static long chatIdWith(User user) {
        User localUser = PrefUtils.getInstance().getUser();
        return (user.getId() + localUser.getId());
    }

    public static void registerForMessageReceipt(BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(ACTION_RECEIPT_CHANGE);
        LocalBroadcastManager.getInstance(VCApplication.getInstance()).registerReceiver(receiver, filter);
    }

    public static void registerForMessagesDeleted(BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(ACTION_CHAT_MESSAGES_DELETED);
        LocalBroadcastManager.getInstance(VCApplication.getInstance()).registerReceiver(receiver, filter);
    }

    public static void onChatMessagesDeleted(Conversation chat) {
        List<MessageOld> messages = Database.getInstance().getMessageDao()
                .queryBuilder()
                .orderAsc(MessageDao.Properties.Timestamp)
                .where(MessageDao.Properties.ChatId.eq(chat.getId())).limit(1).list();
        MessageOld maxMessage = null;

        if ((messages != null) && (messages.size() > 0)) {
            maxMessage = messages.get(0);
        }

        Intent intent;
        if (maxMessage == null) {
            chat.delete();
            Database.getInstance().getChatDao().detach(chat);
            intent = new Intent(ACTION_CHAT_DELETED);
            intent.putExtra(EXTRA_CHAT_ID, chat.getId());
        } else {
            chat.setLastMessageId(maxMessage.getId());
            chat.setLastMessage(maxMessage);
            intent = new Intent(ACTION_CHAT_MESSAGES_DELETED);
            intent.putExtra(EXTRA_CHAT, chat);
        }

        LocalBroadcastManager.getInstance(VCApplication.getInstance()).sendBroadcast(intent);
    }

    public static void registerForDeletedChat(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(VCApplication.getInstance())
                .registerReceiver(receiver, new IntentFilter(ACTION_CHAT_DELETED));
    }

    public static boolean isSenderIdForThisChat(long senderId, Conversation chat) {
        return ((senderId == chat.getPartnerId()) || senderId == PrefUtils.getInstance().getUser().getId());
    }*/
}
