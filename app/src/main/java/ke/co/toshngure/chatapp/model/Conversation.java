/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Anthony Ngure on 03/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

@Entity(nameInDb = "conversations")
public class Conversation {

    @Id(autoincrement = true)
    private Long id;

    private int unread;

    private String background;

    private long friendId;

    private long lastMessageId;
/*

    @ToOne(joinProperty = "friendId")
    private User friend;

    @ToOne(joinProperty = "lastMessageId")
    private MessageOld lastMessage;
*/


    private int myMessageBackground;

    private int myMessageTextColor;

    private int friendMessageBackground;

    private int friendMessageTextColor;

    public Conversation() {
    }

    @Generated(hash = 849051878)
    public Conversation(Long id, int unread, String background, long friendId,
            long lastMessageId, int myMessageBackground, int myMessageTextColor,
            int friendMessageBackground, int friendMessageTextColor) {
        this.id = id;
        this.unread = unread;
        this.background = background;
        this.friendId = friendId;
        this.lastMessageId = lastMessageId;
        this.myMessageBackground = myMessageBackground;
        this.myMessageTextColor = myMessageTextColor;
        this.friendMessageBackground = friendMessageBackground;
        this.friendMessageTextColor = friendMessageTextColor;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUnread() {
        return this.unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getBackground() {
        return this.background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public long getFriendId() {
        return this.friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getLastMessageId() {
        return this.lastMessageId;
    }

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public int getMyMessageBackground() {
        return this.myMessageBackground;
    }

    public void setMyMessageBackground(int myMessageBackground) {
        this.myMessageBackground = myMessageBackground;
    }

    public int getMyMessageTextColor() {
        return this.myMessageTextColor;
    }

    public void setMyMessageTextColor(int myMessageTextColor) {
        this.myMessageTextColor = myMessageTextColor;
    }

    public int getFriendMessageBackground() {
        return this.friendMessageBackground;
    }

    public void setFriendMessageBackground(int friendMessageBackground) {
        this.friendMessageBackground = friendMessageBackground;
    }

    public int getFriendMessageTextColor() {
        return this.friendMessageTextColor;
    }

    public void setFriendMessageTextColor(int friendMessageTextColor) {
        this.friendMessageTextColor = friendMessageTextColor;
    }
}
