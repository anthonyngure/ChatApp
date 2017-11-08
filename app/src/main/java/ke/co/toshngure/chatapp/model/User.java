/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatapp.model;

import android.os.Parcel;

import ke.co.toshngure.chatsdk.model.BaseUser;

/**
 * Created by Anthony Ngure on 23/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class User implements BaseUser {

    private long id;
    private long lastSeen;
    private String name;
    private String avatar;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getLastSeen() {
        return lastSeen;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
