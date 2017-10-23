package ke.co.toshngure.chatsdk.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "conversations")
public class Conversation {

    @NotNull
    @Id
    private long id;

    @NotNull
    private int unread;

    private String background;

    @NotNull
    private long partnerId;

    @NotNull
    private long lastMessageId;

    @NotNull
    private int myMessageBackground;

    @NotNull
    private int myMessageTextColor;

    @NotNull
    private int partnerMessageBackground;

    @NotNull
    private int partnerMessageTextColor;

    public Conversation() {
    }

    @Generated(hash = 1679367171)
    public Conversation(long id, int unread, String background, long partnerId,
            long lastMessageId, int myMessageBackground, int myMessageTextColor,
            int partnerMessageBackground, int partnerMessageTextColor) {
        this.id = id;
        this.unread = unread;
        this.background = background;
        this.partnerId = partnerId;
        this.lastMessageId = lastMessageId;
        this.myMessageBackground = myMessageBackground;
        this.myMessageTextColor = myMessageTextColor;
        this.partnerMessageBackground = partnerMessageBackground;
        this.partnerMessageTextColor = partnerMessageTextColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public int getMyMessageBackground() {
        return myMessageBackground;
    }

    public void setMyMessageBackground(int myMessageBackground) {
        this.myMessageBackground = myMessageBackground;
    }

    public int getMyMessageTextColor() {
        return myMessageTextColor;
    }

    public void setMyMessageTextColor(int myMessageTextColor) {
        this.myMessageTextColor = myMessageTextColor;
    }

    public int getPartnerMessageBackground() {
        return partnerMessageBackground;
    }

    public void setPartnerMessageBackground(int partnerMessageBackground) {
        this.partnerMessageBackground = partnerMessageBackground;
    }

    public int getPartnerMessageTextColor() {
        return partnerMessageTextColor;
    }

    public void setPartnerMessageTextColor(int partnerMessageTextColor) {
        this.partnerMessageTextColor = partnerMessageTextColor;
    }
}
