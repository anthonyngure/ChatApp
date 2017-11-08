package ke.co.toshngure.chatsdk.cell;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;
import com.vanniktech.emoji.EmojiTextView;

import ke.co.toshngure.basecode.utils.BaseUtils;
import ke.co.toshngure.chatsdk.R;
import ke.co.toshngure.chatsdk.model.BaseUser;
import ke.co.toshngure.chatsdk.model.Conversation;
import ke.co.toshngure.chatsdk.model.Message;
import ke.co.toshngure.chatsdk.view.ChatMessageView;

/**
 * Created by Anthony Ngure on 23/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class MessageCell extends SimpleCell<Message, MessageCell.MessageViewHolder> implements Updatable<Message> {
    private Conversation mConversation;
    private BaseUser mUser;
    private BaseUser mPartner;

    public MessageCell(@NonNull Message item, Conversation conversation, BaseUser user, BaseUser partner) {
        super(item);
        this.mConversation = conversation;
        this.mUser = user;
        this.mPartner = partner;

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.cell_message;
    }

    @NonNull
    @Override
    protected MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull View view) {
        return new MessageViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i, @NonNull Context context, Object o) {
        messageViewHolder.bind(getItem(), context);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Message message) {
        return false;
    }

    @Override
    public Object getChangePayload(@NonNull Message message) {
        return message;
    }

    public class MessageViewHolder extends SimpleViewHolder {

        EmojiTextView textTV;
        TextView timeTV;
        ImageView statusIV;
        View selectedOverlayView;
        ChatMessageView messageView;
        View quotedMessageView;
        View quotedColorView;
        TextView quotedSenderNameTV;
        EmojiTextView quotedTextTV;

        private Message message;


        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textTV = itemView.findViewById(R.id.messageTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            statusIV = itemView.findViewById(R.id.statusIV);
            messageView = itemView.findViewById(R.id.messageView);
            selectedOverlayView = itemView.findViewById(R.id.selectedOverlayView);
            quotedColorView = itemView.findViewById(R.id.quotedCancelView);
            quotedMessageView = itemView.findViewById(R.id.quotedMessageView);
            quotedSenderNameTV = itemView.findViewById(R.id.quotedSenderNameTV);
            quotedTextTV = itemView.findViewById(R.id.quotedTextTV);
        }

        void bind(Message message, Context context) {

            this.message = message;
            textTV.setText(message.getText());
            timeTV.setText(DateUtils.formatDateTime(context, message.getTimestamp(), DateUtils.FORMAT_SHOW_TIME));

            if (!TextUtils.isEmpty(message.getQuotedText())) {
                String quotedName = message.getQuotedSenderId() == mConversation.getPartnerId() ?
                        mPartner.getName() : context.getString(R.string.you);
                quotedSenderNameTV.setText(quotedName);
                quotedTextTV.setText(message.getQuotedText());
                quotedMessageView.setVisibility(View.VISIBLE);
            } else {
                quotedMessageView.setVisibility(View.GONE);
            }


            if (!isPartnerMessage(message)) {
                textTV.setTextColor(mConversation.getMyMessageTextColor());
                timeTV.setTextColor(mConversation.getMyMessageTextColor());
                quotedColorView.setBackgroundColor(mConversation.getPartnerMessageBackground());
                quotedSenderNameTV.setTextColor(mConversation.getPartnerMessageTextColor());
                messageView.pullRight(true, mConversation);
                statusIV.setVisibility(View.VISIBLE);

                //Check if its status
                if ((message.getServerReceipt() != -1)
                        && (message.getDeviceReceipt() != -1)
                        && (message.getReadReceipt() != -1)) {

                    //This message has been read
                    statusIV.setImageResource(R.drawable.traffic_message_got_read_receipt_from_target);

                } else if ((message.getDeviceReceipt() != -1)
                        && (message.getServerReceipt() != -1)) {

                    //This message has gone to the device
                    statusIV.setImageResource(R.drawable.traffic_message_got_receipt_from_target);

                }

                //Add client can connect to make sure the message can leave device
                else if ((message.getServerReceipt() != -1) && BaseUtils.canConnect(context)) {

                    //This message has gone to the server
                    statusIV.setImageResource(R.drawable.traffic_message_got_receipt_from_server);

                } else {
                    //This message is still in device
                    statusIV.setImageResource(R.drawable.traffic_message_unsent);

                }
            } else {
                textTV.setTextColor(mConversation.getPartnerMessageTextColor());
                timeTV.setTextColor(mConversation.getPartnerMessageTextColor());
                quotedColorView.setBackgroundColor(mConversation.getMyMessageBackground());
                quotedSenderNameTV.setTextColor(mConversation.getMyMessageBackground());
                messageView.pullLeft(true, mConversation);
                statusIV.setVisibility(View.GONE);
            }
        }

    }

    private boolean isPartnerMessage(Message message) {
        return message.getSenderId() == mConversation.getPartnerId();
    }
}
