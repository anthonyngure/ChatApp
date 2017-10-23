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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ke.co.toshngure.basecode.log.BeeLog;
import ke.co.toshngure.basecode.utils.BaseUtils;
import ke.co.toshngure.chatsdk.model.BaseUser;
import ke.co.toshngure.chatsdk.utils.ConversationUtils;
import ke.co.toshngure.chatsdk.utils.FirebaseUtils;

/**
 * Created by Anthony Ngure on 04/02/2017.
 * Email : anthonyngure25@gmail.com.
 * Company : VibeCampo Social Network..
 */

public class UserTypingStatusManager implements ValueEventListener {

    private static final String TAG = UserTypingStatusManager.class.getSimpleName();
    private static HashMap<String, Long> mTypingUserRefs = new HashMap<>();
    private static List<Long> mTypingUserList = new ArrayList<>();
    private static UserTypingStatusManager mInstance;

    private UserTypingStatusManager() {
    }

    public static UserTypingStatusManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserTypingStatusManager();
        }
        return mInstance;
    }

    public static boolean isUserTyping(BaseUser user) {
        return (mTypingUserList.contains(user.getId()) && BaseUtils.canConnect(ChatSDK.getInstance().getContext()));
    }

    private String databaseRefToString(DatabaseReference reference) {
        return reference.getRef().toString();
    }

    public void add(BaseUser user, long conversationId) {
        DatabaseReference userTypingStatusRef = FirebaseUtils.getUserTypingRef(user.getId(), conversationId);
        if (!mTypingUserRefs.containsKey(databaseRefToString(userTypingStatusRef))) {
            userTypingStatusRef.addValueEventListener(this);
            mTypingUserRefs.put(userTypingStatusRef.getRef().toString(), user.getId());
            BeeLog.d(TAG, databaseRefToString(userTypingStatusRef) + " Added to queue");
        } else {
            BeeLog.d(TAG, databaseRefToString(userTypingStatusRef) + " Already exists");
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        BeeLog.d(TAG, "onDataChange, "+ databaseRefToString(dataSnapshot.getRef()));
        if (dataSnapshot.getValue(Boolean.class) != null) {
            long userId = mTypingUserRefs.get(databaseRefToString(dataSnapshot.getRef()));
            boolean val = dataSnapshot.getValue(Boolean.class);
            if (val) {
                if (!mTypingUserList.contains(userId)) {
                    mTypingUserList.add(userId);
                }
                UserConnectedStatusManager.onUserTyping(userId);
            } else {
                if (mTypingUserList.contains(userId)) {
                    mTypingUserList.remove(userId);
                }
            }
            ConversationUtils.onUserTyping();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
