/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import ke.co.toshngure.basecode.log.BeeLog;
import ke.co.toshngure.chatsdk.database.Database;
import ke.co.toshngure.chatsdk.model.Message;
import ke.co.toshngure.chatsdk.utils.FirebaseUtils;
import ke.co.toshngure.chatsdk.utils.MessageUtils;

/**
 * Created by Anthony Ngure on 04/02/2017.
 * Email : anthonyngure25@gmail.com.
 * Company : VibeCampo Social Network..
 */

public class MessageReceiptManager implements ValueEventListener {

    private static final String TAG = MessageReceiptManager.class.getSimpleName();

    /**
     * chats/chatId/messages/messageKey/{server_receipt, device_receipt, read_receipt}
     */
    private static HashMap<String, Long> mRefMessageMap;
    private static MessageReceiptManager mInstance;

    private MessageReceiptManager() {
    }

    public static MessageReceiptManager getInstance() {
        if (mInstance == null) {
            mRefMessageMap = new HashMap<>();
            mInstance = new MessageReceiptManager();
        }
        return mInstance;
    }

    private String databaseRefToString(DatabaseReference reference) {
        return reference.getRef().toString();
    }

    private void addReceiptRef(DatabaseReference receiptRef, Message message) {
        if (!mRefMessageMap.containsKey(databaseRefToString(receiptRef))) {
            mRefMessageMap.put(databaseRefToString(receiptRef), message.getId());
            receiptRef.addValueEventListener(this);
            BeeLog.d(TAG, databaseRefToString(receiptRef) + " Added to queue");
        } else {
            BeeLog.d(TAG, databaseRefToString(receiptRef) + " Already in queue");
        }
    }

    public void add(List<Message> messages){
        for (Message message : messages){
            add(message);
        }
    }

    public void add(Message message) {

        //Check if message needs server receipt
        if (message.getServerReceipt() == MessageUtils.NO_RECEIPT) {
            //Request for server receipt
            DatabaseReference serverReceiptRef = FirebaseUtils.getServerReceiptRef(message);
            addReceiptRef(serverReceiptRef, message);
        }

        //Check if message needs device receipt
        if (message.getDeviceReceipt() == MessageUtils.NO_RECEIPT) {
            //Request for device receipt
            DatabaseReference deviceReceiptRef = FirebaseUtils.getDeviceReceiptRef(message);
            addReceiptRef(deviceReceiptRef, message);
        }

        //Check if message needs read receipt
        if (message.getReadReceipt() == MessageUtils.NO_RECEIPT) {
            //Request for read receipt
            DatabaseReference readReceiptRef = FirebaseUtils.getReadReceiptRef(message);
            addReceiptRef(readReceiptRef, message);
        }
    }

    /**
     * Change at ----- chats/chatKey/messages/messageKey/{server_receipt, device_receipt, read_receipt}
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {


        Message message = Database.getInstance().getDaoSession().getMessageDao()
                .loadDeep(mRefMessageMap.get(databaseRefToString(dataSnapshot.getRef())));

        //Get value
        Long receiptValue = dataSnapshot.getValue(Long.class);

        BeeLog.d(TAG, "onDataChange, " + databaseRefToString(dataSnapshot.getRef()) + " VALUE = " + receiptValue);

        if ((receiptValue != null) && (message != null)) {

            //Determine the key for the receipt value
            if (dataSnapshot.getKey().equalsIgnoreCase(FirebaseUtils.READ_RECEIPT)) {

                message.setReadReceipt(receiptValue);

                // removeItem listener from the ref
                FirebaseUtils.getReadReceiptRef(message).removeEventListener(this);
            }

            if (dataSnapshot.getKey().equalsIgnoreCase(FirebaseUtils.DEVICE_RECEIPT)) {

                message.setDeviceReceipt(receiptValue);

                // removeItem listener from the ref
                FirebaseUtils.getDeviceReceiptRef(message).removeEventListener(this);

            }

            if (dataSnapshot.getKey().equalsIgnoreCase(FirebaseUtils.SERVER_RECEIPT)) {

                message.setTimestamp(receiptValue);

                message.setServerReceipt(receiptValue);

                // removeItem listener from the ref
                FirebaseUtils.getServerReceiptRef(message).removeEventListener(this);

            }

            //Updates the message
            Database.getInstance().getDaoSession().getMessageDao().update(message);

            //Broadcasts message has changed
            MessageUtils.onReceiptChange(message);

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
