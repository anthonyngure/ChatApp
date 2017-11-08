/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.decoration.SimpleSectionHeaderProvider;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.app.DialogAsyncTask;
import ke.co.toshngure.basecode.database.BaseLoader;
import ke.co.toshngure.basecode.images.BaseNetworkImage;
import ke.co.toshngure.basecode.images.simplecrop.util.CropUtils;
import ke.co.toshngure.basecode.log.BeeLog;
import ke.co.toshngure.basecode.utils.BaseUtils;
import ke.co.toshngure.basecode.utils.DatesHelper;
import ke.co.toshngure.chatsdk.cell.MessageCell;
import ke.co.toshngure.chatsdk.database.Database;
import ke.co.toshngure.chatsdk.model.BaseUser;
import ke.co.toshngure.chatsdk.model.Conversation;
import ke.co.toshngure.chatsdk.model.Message;
import ke.co.toshngure.chatsdk.model.MessageDao;
import ke.co.toshngure.chatsdk.model.RawMessage;
import ke.co.toshngure.chatsdk.utils.ConversationUtils;
import ke.co.toshngure.chatsdk.utils.FirebaseUtils;
import ke.co.toshngure.chatsdk.utils.MessageUtils;

public class ConversationActivity extends BaseAppActivity
        implements LoaderManager.LoaderCallbacks<List<MessageCell>>,
        SimpleCell.OnCellClickListener<Message>,
        SimpleCell.OnCellLongClickListener<Message>{

    public static final String EXTRA_CONVERSATION = "extra_conversation";
    private static final String TAG = "ConversationActivity";
    private static final String EXTRA_USER = "extra_user";
    private static final String EXTRA_PARTNER = "extra_partner";
    private Conversation mConversation;
    private EmojiPopup mEmojiPopup;
    private ImageButton mEmojiButton;
    private ImageButton mSendButton;
    private EmojiEditText mEmojiEditText;
    private DatabaseReference myTypingReference;
    private DatabaseReference myPresenceReference;
    private boolean mFriendIsOnline;
    private TextView mStatusTv;
    private MenuItem mVisibleMenuItem;
    private View mRootView;
    private BaseUser mPartner;
    private BaseUser mUser;
    private SimpleRecyclerView mSimpleRecyclerView;
    View quotedMessageView;
    View quotedColorView;
    TextView quotedSenderNameTV;
    EmojiTextView quotedTextTV;
    private Message mQuotedMessage;


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BeeLog.d(TAG, "onReceive, " + intent.getAction());
            switch (intent.getAction()) {
                case ConversationUtils.ACTION_USER_PRESENT:
                    if (UserPresenceStatusManager.isUserPresent()) {
                        onFriendPresent();
                    } else {
                        onFriendNotPresent();
                    }
                    break;
                case ConversationUtils.ACTION_USER_TYPING:
                    if (UserTypingStatusManager.isUserTyping(mPartner)) {
                        onFriendTyping();
                    } else {
                        onFriendStoppedTyping();
                    }
                    break;
                case ConversationUtils.ACTION_USER_CONNECTED_STATUS_CHANGE:
                    if (UserConnectedStatusManager.isUserOnline(mPartner)) {
                        onFriendOnline();
                    } else {
                        onFriendNotOnline();
                    }
                    break;
                case MessageUtils.ACTION_NEW_MESSAGE:
                    Message message = intent.getParcelableExtra(MessageUtils.EXTRA_MESSAGE);
                    if (message.getConversationId() == mConversation.getId()) {
                        /*mAdapter.addItem(new MessageSection(TODAY, DateFormat.getDateInstance().format(System.currentTimeMillis()), message));
                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                            }
                        }, SCROLL_TO_BOTTOM_DELAY);*/
                    }
                    break;
            }
        }
    };
    private void setStatusToLastSeenText() {
        String lastSeenText = MessageUtils.getDisplayLastSeen(mPartner.getLastSeen());
        mStatusTv.setText(lastSeenText);
        if (TextUtils.isEmpty(lastSeenText)) {
            mStatusTv.setVisibility(View.GONE);
        } else {
            mStatusTv.setVisibility(View.VISIBLE);
        }

    }

    private void onFriendOnline() {
        //Is Online
        mFriendIsOnline = true;
        mStatusTv.setText(R.string.online);
    }

    private void onFriendNotOnline() {
        //Not Visible
        if (mVisibleMenuItem != null) {
            mVisibleMenuItem.setVisible(false);
        }
        //Not Online
        mStatusTv.setText(MessageUtils.getDisplayLastSeen(mPartner.getLastSeen()));
        setStatusToLastSeenText();
    }

    private void onFriendTyping() {
        //Is Visible
        if (mVisibleMenuItem != null) {
            mVisibleMenuItem.setVisible(true);
        }
        //Is Online
        mFriendIsOnline = true;
        //Is Typing
        mStatusTv.setText(R.string.typing);

    }

    private void onFriendStoppedTyping() {
        BeeLog.d(TAG, "onFriendStoppedTyping, mFriendIsOnline = " + String.valueOf(mFriendIsOnline));
        //Not Typing
        if (!mFriendIsOnline) {
            mStatusTv.setText(MessageUtils.getDisplayLastSeen(mPartner.getLastSeen()));
            setStatusToLastSeenText();
        } else {
            mStatusTv.setText(R.string.online);
        }
    }

    private void onFriendPresent() {
        //Is Visible
        if (mVisibleMenuItem != null) {
            mVisibleMenuItem.setVisible(true);
        }

        //Is Online
        mFriendIsOnline = true;
        if (UserTypingStatusManager.isUserTyping(mPartner)) {
            mStatusTv.setText(R.string.typing);
        } else {
            mStatusTv.setText(getString(R.string.online));
        }
    }

    private void onFriendNotPresent() {
        BeeLog.d(TAG, "onFriendNotPresent, mFriendIsOnline = " + String.valueOf(mFriendIsOnline));
        //Not Visible
        if (mVisibleMenuItem != null) {
            mVisibleMenuItem.setVisible(false);
        }
        if (!mFriendIsOnline) {
            mStatusTv.setText(MessageUtils.getDisplayLastSeen(mPartner.getLastSeen()));
            setStatusToLastSeenText();
        } else {
            mStatusTv.setText(R.string.online);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConversation = getIntent().getParcelableExtra(EXTRA_CONVERSATION);
        mUser = getIntent().getParcelableExtra(EXTRA_USER);
        mPartner = getIntent().getParcelableExtra(EXTRA_PARTNER);
        setContentView(R.layout.activity_conversation);
        initViews();
    }

    private void initViews() {
        mRootView = findViewById(R.id.rootView);
        mEmojiEditText = findViewById(R.id.editText);
        mEmojiEditText.setEmojiSize(BaseUtils.dpToPx(18));
        mEmojiButton = findViewById(R.id.emojiButton);
        mEmojiPopup = EmojiPopup.Builder.fromRootView(mRootView)
                .setOnSoftKeyboardOpenListener(keyBoardHeight -> {
                    int screenHeight = mRootView.getHeight();
                    if (keyBoardHeight > screenHeight * 0.15) {
                        myTypingReference.setValue(Boolean.TRUE);
                    } else {
                        myTypingReference.setValue(Boolean.FALSE);
                    }
                })
                .setOnSoftKeyboardCloseListener(() -> myTypingReference.setValue(Boolean.FALSE))
                .setOnEmojiPopupShownListener(() -> mEmojiButton.setImageResource(R.drawable.ic_keyboard_black_24dp))
                .setOnEmojiPopupDismissListener(() -> mEmojiButton.setImageResource(R.drawable.ic_insert_emoticon_black_24dp))
                .build(mEmojiEditText);
        mEmojiButton.setOnClickListener(view -> mEmojiPopup.toggle());
        mSendButton = findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(view -> sendMessage());
        mEmojiEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSendButton.setEnabled(!TextUtils.isEmpty(editable.toString()));
                mSendButton.setClickable(!TextUtils.isEmpty(editable.toString()));
                mSendButton.setActivated(!TextUtils.isEmpty(editable.toString()));
            }
        });

        quotedColorView = findViewById(R.id.quotedCancelView);
        quotedMessageView = findViewById(R.id.quotedMessageView);
        quotedSenderNameTV = findViewById(R.id.quotedSenderNameTV);
        quotedTextTV = findViewById(R.id.quotedTextTV);

        mSimpleRecyclerView = findViewById(R.id.baseapp_simpleRecyclerView);
        SimpleSectionHeaderProvider<Message> headerProvider = new SimpleSectionHeaderProvider<Message>() {
            @NonNull
            @Override
            public View getSectionHeaderView(@NonNull Message message, int position) {
                View view = LayoutInflater.from(getThis()).inflate(R.layout.cell_message_header, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                headerTV.setText(getMessageDateSection(message));
                return view;
            }

            @Override
            public boolean isSameSection(@NonNull Message message, @NonNull Message nextMessage) {
                return getMessageDateSection(message).equalsIgnoreCase(getMessageDateSection(nextMessage));
            }

            @Override
            public boolean isSticky() {
                return true;
            }

            @Override
            public int getSectionHeaderMarginTop(@NonNull Message item, int position) {
                return super.getSectionHeaderMarginTop(item, position);
            }
        };
        mSimpleRecyclerView.setSectionHeader(headerProvider);

    }

    private String getMessageDateSection(Message message){
        String section;
        if (DatesHelper.isToday(message.getTimestamp())) {
            section = getString(R.string.today);
        } else if (DatesHelper.isYesterday(message.getTimestamp())) {
            section = getString(R.string.yesterday);
        } else {
            section = DateFormat.getDateInstance().format(message.getTimestamp());
        }
        return section;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSimpleRecyclerView.isEmpty()) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }


    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        BaseNetworkImage avatarNI = findViewById(R.id.avatarNI);
        avatarNI.loadImageFromNetwork(mPartner.getAvatar());
        TextView titleTV = findViewById(R.id.titleTV);
        titleTV.setText(mPartner.getName());
        mStatusTv = findViewById(R.id.subTitleTV);
        findViewById(R.id.backAction).setOnClickListener(view -> exitActivity());
        BeeLog.i(TAG, String.valueOf(mConversation));
    }

    public static void start(Context context, Conversation conversation, BaseUser user, BaseUser partner) {
        Intent starter = new Intent(context, ConversationActivity.class);
        starter.putExtra(EXTRA_CONVERSATION, conversation);
        starter.putExtra(EXTRA_USER, user);
        starter.putExtra(EXTRA_PARTNER, partner);
        context.startActivity(starter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserTypingStatusManager.isUserTyping(mPartner)) {
            BeeLog.d(TAG, "onResume, " + mPartner.getName() + " is typing");
            onFriendTyping();
        }/* else {
            BeeLog.d(TAG, "onResume, " + mPartner().getName() + " is not typing");
            onFriendStoppedTyping();
        }*/

        if (UserConnectedStatusManager.isUserOnline(mPartner)) {
            BeeLog.d(TAG, "onResume, " + mPartner.getName() + " is online");
            onFriendOnline();
        } else {
            BeeLog.d(TAG, "onResume, " + mPartner.getName() + " is offline");
            onFriendNotOnline();
        }

        //Notify chatsFragment to refresh
        //MessageUtils.onChatOpened(mConversation);

        initMyDatabaseReferences();

        MessageUtils.registerForNewMessage(mBroadcastReceiver);

        ConversationUtils.registerForUserConnectedStatus(mBroadcastReceiver);

        UserConnectedStatusManager.getInstance().add(mPartner);

        ConversationUtils.registerForUserTyping(mBroadcastReceiver);

        UserTypingStatusManager.getInstance().add(mPartner, mConversation.getId());

        UserPresenceStatusManager.getInstance().add(mPartner, mConversation.getId());

        ConversationUtils.registerForUserPresence(mBroadcastReceiver);

    }

    @Override
    protected void onPause() {
        super.onPause();

        //MessageUtils.onChatClosed();

        myPresenceReference.setValue(Boolean.FALSE);

        myTypingReference.setValue(Boolean.FALSE);

        UserPresenceStatusManager.getInstance().remove(mPartner, mConversation.getId());

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private void initMyDatabaseReferences() {

        if (myTypingReference == null) {
            myTypingReference = FirebaseUtils.getUserTypingRef(mUser.getId(), mConversation.getId());
        }

        myTypingReference.onDisconnect().setValue(Boolean.FALSE);

        if (myPresenceReference == null) {
            myPresenceReference = FirebaseUtils.getUserPresenceRef(mUser.getId(), mConversation.getId());
        }

        myPresenceReference.setValue(Boolean.TRUE);
        myPresenceReference.onDisconnect().setValue(Boolean.FALSE);
    }

    @Override
    public void onBackPressed() {
        if (mEmojiPopup != null && mEmojiPopup.isShowing()) {
            mEmojiPopup.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        if (mEmojiPopup != null) {
            mEmojiPopup.dismiss();
        }

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_conversation, menu);
        mVisibleMenuItem = menu.findItem(R.id.action_visible);
        MenuItem dummyMenuItem = menu.findItem(R.id.action_dummy);
        if (BuildConfig.DEBUG) {
            dummyMenuItem.setVisible(true);
        }
        //BaseUtils.tintMenu(mVisibleMenuItem, ContextCompat.getColor(this, R.color.colorAccent), null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_visible) {
            return false;
        } else if (i == R.id.action_dummy) {
            addDummy();
            return true;
        } else if (i == R.id.action_clear) {
            clearMessages();
            return true;
        } else if (i == R.id.action_chat_colors) {
            changeChatColors();
            return true;
        } else if (i == R.id.action_no_background) {
            clearChatBackground();
            return true;
        } else if (i == R.id.action_change_background) {
            CropUtils.pickImage(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void addDummy() {
        new DialogAsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < 20; i++) {

                    Message dummyMessage = new Message();
                    dummyMessage.setConversationId(mConversation.getId());
                    dummyMessage.setText(getString(R.string.lorem_text));
                    String key;

                    if ((i % 2) == 0) {
                        key = FirebaseUtils.getUserUnreadMessagesRef(mUser).push().getKey();
                        dummyMessage.setSenderId(mUser.getId());
                    } else {
                        key = FirebaseUtils.getUserUnreadMessagesRef(mPartner).push().getKey();
                        dummyMessage.setSenderId(mPartner.getId());
                    }

                    dummyMessage.setServerKey(key);

                    dummyMessage.setTimestamp(getRandomTimestamp());
                    dummyMessage.setServerReceipt(getRandomTimestamp());
                    dummyMessage.setDeviceReceipt(getRandomTimestamp());
                    dummyMessage.setReadReceipt(getRandomTimestamp());

                    Database.getInstance().getDaoSession().getMessageDao().insertOrReplace(dummyMessage);
                }
                return null;
            }

            @Override
            protected Activity getActivity() {
                return ConversationActivity.this;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getSupportLoaderManager().restartLoader(0, null, ConversationActivity.this);
            }

        }.execute();
    }

    private long getRandomTimestamp() {
        Random random = new Random();


        Calendar calendar = Calendar.getInstance();
        //Set the calender to a random day this year
        calendar.set(Calendar.DAY_OF_YEAR, random.nextInt(calendar.get(Calendar.DAY_OF_YEAR)));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //Make time random
        Date date = new Date(calendar.getTimeInMillis() + random.nextInt(60 * 60 * 24 * 1000));
        BeeLog.d(TAG, "getRandomTimestamp, " + DateFormat.getDateTimeInstance().format(date));

        return date.getTime();
    }


    private void clearChatBackground() {
        /*mConversation.setBackground(null);

        if ((mConversation.getBackground() != null) && (!TextUtils.isEmpty(mChat.getBackground()))) {
            File file = new File(mChat.getBackground());
            if (file.exists()) {
                BeeLog.d(TAG, "clearChatBackground, File deleted = " + file.delete());
            }
            mConversation.setBackground("");
            mChat.update();
            mChat.refresh();
        }
        mChatBackgroundImage.setImageResource(R.drawable.background_chat);*/
    }

    private void changeChatColors() {
        /*ChatColorsFragment.newInstance(mChat).show(getSupportFragmentManager(),
                ChatColorsFragment.class.getSimpleName());*/
    }

    @SuppressLint("StaticFieldLeak")
    private void clearMessages() {
        new DialogAsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Database.getInstance().getDaoSession().getMessageDao().queryBuilder()
                        .where(MessageDao.Properties.ConversationId.eq(mConversation.getId()))
                        .buildDelete()
                        .executeDeleteWithoutDetachingEntities();
                Database.getInstance().getDaoSession().getMessageDao().detachAll();
                //MessageUtils.onChatMessagesDeleted(mChat);
                return null;
            }

            @Override
            protected Activity getActivity() {
                return ConversationActivity.this;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //mAdapter.clear();
                //MessageUtils.onChatMessagesDeleted(mChat);
            }
        }.execute();
    }

    public void sendMessage() {

        //Compose message to send
        Message messageToSend = new Message();

        //Add fields required for display before the message is sent
        messageToSend.setConversationId(mConversation.getId());
        messageToSend.setText(mEmojiEditText.getText().toString());

        //Clear the edit text
        mEmojiEditText.getText().clear();

        messageToSend.setSenderId(mUser.getId());
        messageToSend.setTimestamp(System.currentTimeMillis());
        messageToSend.setServerReceipt(MessageUtils.NO_RECEIPT);
        messageToSend.setDeviceReceipt(MessageUtils.NO_RECEIPT);
        messageToSend.setReadReceipt(MessageUtils.NO_RECEIPT);


        //Prepare message for push

        DatabaseReference newMessageRef = FirebaseUtils.getUserUnreadMessagesRef(mPartner).push();
        String newMessageKey = newMessageRef.getKey(); //for this message status in the chats node

        //Assign the key to the message for it to observe receipts
        messageToSend.setServerKey(newMessageKey);

        Map<String, Object> updateValues = new HashMap<String, Object>();

        /* to add serverReceipt to chat messages node
          chats/chatKey/messages/messageKey/server_receipt
         */
        updateValues.put(FirebaseUtils.CHATS_CHILD + "/" +
                        mConversation.getId() + "/" +
                        FirebaseUtils.MESSAGES + "/" +
                        newMessageKey + "/" +
                        FirebaseUtils.SERVER_RECEIPT,
                ServerValue.TIMESTAMP);


        //Prepare message values
        //Create a message for server
        Map<String, Object> newRawMessage = new HashMap<String, Object>();
        newRawMessage.put(RawMessage.TEXT, messageToSend.getText());
        newRawMessage.put(RawMessage.SENDER_ID, messageToSend.getSenderId());
        newRawMessage.put(RawMessage.TIMESTAMP, ServerValue.TIMESTAMP);

        if (mQuotedMessage != null){

            newRawMessage.put(RawMessage.QUOTED_TEXT, mQuotedMessage.getText());
            newRawMessage.put(RawMessage.QUOTED_SERVER_KEY, mQuotedMessage.getServerKey());
            newRawMessage.put(RawMessage.QUOTED_SENDER_ID, mQuotedMessage.getSenderId());
            newRawMessage.put(RawMessage.QUOTED_TEXT, mQuotedMessage.getText());
            newRawMessage.put(RawMessage.QUOTED_SERVER_KEY, mQuotedMessage.getServerKey());
            newRawMessage.put(RawMessage.QUOTED_SENDER_ID, mQuotedMessage.getSenderId());

            messageToSend.setQuotedText(mQuotedMessage.getText());
            messageToSend.setQuotedServerKey(mQuotedMessage.getServerKey());
            messageToSend.setQuotedSenderId(mQuotedMessage.getSenderId());

            mQuotedMessage = null;
        } else {
            newRawMessage.put(RawMessage.QUOTED_TEXT, "");
            newRawMessage.put(RawMessage.QUOTED_SERVER_KEY, "");
            newRawMessage.put(RawMessage.QUOTED_SENDER_ID, 0);
        }



        /*
          add message content to unread messages message node
          unread_messages/userId/messageKey
         */
        updateValues.put(FirebaseUtils.UNREAD_MESSAGES_CHILD + "/" +
                mPartner.getId() + "/" +
                newMessageKey, newRawMessage);

        //BeeLog.d(TAG, "Message sent  "+updateValues.toString());

        //Send the messages
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.updateChildren(updateValues);

        long messageId = Database.getInstance().getDaoSession()
                .getMessageDao().insertOrReplace(messageToSend);

        //Add receipt listeners to the message
        MessageReceiptManager.getInstance().add(messageToSend);

        //Update this chat lastMessage Id
        mConversation.setLastMessageId(messageId);
        Database.getInstance().getDaoSession().getConversationDao().update(mConversation);

        mSimpleRecyclerView.addOrUpdateCell(buildMessageCell(messageToSend));
        mSimpleRecyclerView.scrollToPosition((mSimpleRecyclerView.getItemCount() - 1));

        //quotedMessageView.setVisibility(View.GONE);

        //MessageUtils.onNewMessage(messageToSend);

    }

    private MessageCell buildMessageCell(Message message){
        MessageCell cell = new MessageCell(message, mConversation, mUser, mPartner);
        cell.setOnCellClickListener(ConversationActivity.this);
        cell.setOnCellLongClickListener(ConversationActivity.this);
        return cell;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<MessageCell>> onCreateLoader(int id, Bundle args) {
        return new BaseLoader<MessageCell>(this) {
            @Override
            public List<MessageCell> onLoad() {
                List<MessageCell> messageCells = new ArrayList<>();
                List<Message> messageList = Database.getInstance().getDaoSession().getMessageDao()
                        .queryBuilder().orderAsc(MessageDao.Properties.Timestamp)
                        .listLazyUncached();
                for (Message message : messageList){
                    messageCells.add(buildMessageCell(message));
                }
                return messageCells;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<MessageCell>> loader, List<MessageCell> data) {
        mSimpleRecyclerView.getAllCells().clear();
        mSimpleRecyclerView.addOrUpdateCells(data);
    }

    @Override
    public void onLoaderReset(Loader<List<MessageCell>> loader) {

    }

    @Override
    public void onCellClicked(@NonNull Message message) {

    }

    @Override
    public void onCellLongClicked(@NonNull Message message) {

    }
}
