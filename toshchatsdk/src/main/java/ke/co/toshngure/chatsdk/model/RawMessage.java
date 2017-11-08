/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RawMessage implements Parcelable {

    public static final String TEXT = "text";
    public static final String SENDER_ID = "senderId";
    public static final String TIMESTAMP = "timestamp";
    public static final String QUOTED_TEXT = "quotedText";
    public static final String QUOTED_SERVER_KEY = "quotedServerKey";
    public static final String QUOTED_SENDER_ID = "quotedSenderId";

    private String text;
    private long timestamp;
    private String serverKey;
    private long senderId;
    private long quotedSenderId;
    private String quotedServerKey;
    private String quotedText;

    public RawMessage() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getQuotedServerKey() {
        return quotedServerKey;
    }

    public void setQuotedServerKey(String quotedServerKey) {
        this.quotedServerKey = quotedServerKey;
    }

    public String getQuotedText() {
        return quotedText;
    }

    public void setQuotedText(String quotedText) {
        this.quotedText = quotedText;
    }

    @Override
    public String toString() {
        return "RawMessage{" +
                "text='" + text + '\'' +
                ", timestamp=" + timestamp +
                ", serverKey='" + serverKey + '\'' +
                ", senderId=" + senderId +
                ", quotedSenderId=" + quotedSenderId +
                ", quotedServerKey='" + quotedServerKey + '\'' +
                ", quotedText='" + quotedText + '\'' +
                '}';
    }

    public long getQuotedSenderId() {
        return quotedSenderId;
    }

    public void setQuotedSenderId(long quotedSenderId) {
        this.quotedSenderId = quotedSenderId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeLong(this.timestamp);
        dest.writeString(this.serverKey);
        dest.writeLong(this.senderId);
        dest.writeLong(this.quotedSenderId);
        dest.writeString(this.quotedServerKey);
        dest.writeString(this.quotedText);
    }

    protected RawMessage(Parcel in) {
        this.text = in.readString();
        this.timestamp = in.readLong();
        this.serverKey = in.readString();
        this.senderId = in.readLong();
        this.quotedSenderId = in.readLong();
        this.quotedServerKey = in.readString();
        this.quotedText = in.readString();
    }

    public static final Creator<RawMessage> CREATOR = new Creator<RawMessage>() {
        @Override
        public RawMessage createFromParcel(Parcel source) {
            return new RawMessage(source);
        }

        @Override
        public RawMessage[] newArray(int size) {
            return new RawMessage[size];
        }
    };
}
