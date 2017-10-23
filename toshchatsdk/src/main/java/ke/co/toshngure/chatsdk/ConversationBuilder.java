/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import ke.co.toshngure.chatsdk.model.BaseUser;

/**
 * Created by Anthony Ngure on 04/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class ConversationBuilder implements Parcelable {

    private BaseUser user;
    private BaseUser partner;

    public ConversationBuilder() {

    }
    public void startConversation(Activity activity) {
        ConversationActivity.start(activity, this);
    }

    public BaseUser getUser() {
        return user;
    }

    public ConversationBuilder withUser(BaseUser user) {
        this.user = user;
        return this;
    }

    public BaseUser getPartner() {
        return partner;
    }

    public ConversationBuilder withPartner(BaseUser partner) {
        this.partner = partner;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.partner, flags);
    }

    protected ConversationBuilder(Parcel in) {
        this.user = in.readParcelable(BaseUser.class.getClassLoader());
        this.partner = in.readParcelable(BaseUser.class.getClassLoader());
    }

    public static final Creator<ConversationBuilder> CREATOR = new Creator<ConversationBuilder>() {
        @Override
        public ConversationBuilder createFromParcel(Parcel source) {
            return new ConversationBuilder(source);
        }

        @Override
        public ConversationBuilder[] newArray(int size) {
            return new ConversationBuilder[size];
        }
    };
}

