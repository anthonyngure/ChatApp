package ke.co.toshngure.chatsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Message implements Parcelable {


    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private long timestamp;
    @NotNull
    private long serverReceipt;
    @NotNull
    private long deviceReceipt;
    @NotNull
    private long readReceipt;
    @NotNull
    private boolean seen;
    @NotNull
    @Unique
    private String serverKey;
    @NotNull
    private String text;
    @NotNull
    private long senderId;
    @NotNull
    private long conversationId;
    @NotNull
    private long quotedMessageId;
    @NotNull
    private long quotedSenderId;
    private String quotedServerKey;
    private String quotedText;

    public String getQuotedText() {
        return quotedText;
    }

    public void setQuotedText(String quotedText) {
        this.quotedText = quotedText;
    }

    public String getQuotedServerKey() {
        return quotedServerKey;
    }

    public void setQuotedServerKey(String quotedServerKey) {
        this.quotedServerKey = quotedServerKey;
    }

    @ToOne(joinProperty = "quotedMessageId")
    private Message quotedMessage;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 859287859)
    private transient MessageDao myDao;
    @Generated(hash = 1232071475)
    private transient Long quotedMessage__resolvedKey;

    public Long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getServerReceipt() {
        return serverReceipt;
    }

    public void setServerReceipt(long serverReceipt) {
        this.serverReceipt = serverReceipt;
    }

    public long getDeviceReceipt() {
        return deviceReceipt;
    }

    public void setDeviceReceipt(long deviceReceipt) {
        this.deviceReceipt = deviceReceipt;
    }

    public long getReadReceipt() {
        return readReceipt;
    }

    public void setReadReceipt(long readReceipt) {
        this.readReceipt = readReceipt;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public boolean getSeen() {
        return this.seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getQuotedMessageId() {
        return this.quotedMessageId;
    }

    public void setQuotedMessageId(long quotedMessageId) {
        this.quotedMessageId = quotedMessageId;
    }

    public long getQuotedSenderId() {
        return quotedSenderId;
    }

    public void setQuotedSenderId(long quotedSenderId) {
        this.quotedSenderId = quotedSenderId;
    }

    public Message() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp);
        dest.writeLong(this.serverReceipt);
        dest.writeLong(this.deviceReceipt);
        dest.writeLong(this.readReceipt);
        dest.writeByte(this.seen ? (byte) 1 : (byte) 0);
        dest.writeString(this.serverKey);
        dest.writeString(this.text);
        dest.writeLong(this.senderId);
        dest.writeLong(this.conversationId);
        dest.writeLong(this.quotedMessageId);
        dest.writeLong(this.quotedSenderId);
        dest.writeString(this.quotedServerKey);
        dest.writeString(this.quotedText);
        dest.writeParcelable(this.quotedMessage, flags);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1257674848)
    public Message getQuotedMessage() {
        long __key = this.quotedMessageId;
        if (quotedMessage__resolvedKey == null
                || !quotedMessage__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            Message quotedMessageNew = targetDao.load(__key);
            synchronized (this) {
                quotedMessage = quotedMessageNew;
                quotedMessage__resolvedKey = __key;
            }
        }
        return quotedMessage;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1348270540)
    public void setQuotedMessage(@NotNull Message quotedMessage) {
        if (quotedMessage == null) {
            throw new DaoException(
                    "To-one property 'quotedMessageId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.quotedMessage = quotedMessage;
            quotedMessageId = quotedMessage.getId();
            quotedMessage__resolvedKey = quotedMessageId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 747015224)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMessageDao() : null;
    }

    protected Message(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.timestamp = in.readLong();
        this.serverReceipt = in.readLong();
        this.deviceReceipt = in.readLong();
        this.readReceipt = in.readLong();
        this.seen = in.readByte() != 0;
        this.serverKey = in.readString();
        this.text = in.readString();
        this.senderId = in.readLong();
        this.conversationId = in.readLong();
        this.quotedMessageId = in.readLong();
        this.quotedSenderId = in.readLong();
        this.quotedServerKey = in.readString();
        this.quotedText = in.readString();
        this.quotedMessage = in.readParcelable(Message.class.getClassLoader());
    }

    @Generated(hash = 1682585521)
    public Message(Long id, long timestamp, long serverReceipt, long deviceReceipt,
            long readReceipt, boolean seen, @NotNull String serverKey,
            @NotNull String text, long senderId, long conversationId,
            long quotedMessageId, long quotedSenderId, String quotedServerKey,
            String quotedText) {
        this.id = id;
        this.timestamp = timestamp;
        this.serverReceipt = serverReceipt;
        this.deviceReceipt = deviceReceipt;
        this.readReceipt = readReceipt;
        this.seen = seen;
        this.serverKey = serverKey;
        this.text = text;
        this.senderId = senderId;
        this.conversationId = conversationId;
        this.quotedMessageId = quotedMessageId;
        this.quotedSenderId = quotedSenderId;
        this.quotedServerKey = quotedServerKey;
        this.quotedText = quotedText;
    }
    
}
