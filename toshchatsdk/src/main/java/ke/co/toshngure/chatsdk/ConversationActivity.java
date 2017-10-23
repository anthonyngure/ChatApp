/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.images.BaseNetworkImage;
import ke.co.toshngure.basecode.log.BeeLog;

public class ConversationActivity extends BaseAppActivity {

    public static final String EXTRA_CONVERSATION = "extra_conversation";
    private static final String TAG = "ConversationActivity";
    private ConversationBuilder mConversation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConversation = getIntent().getParcelableExtra(EXTRA_CONVERSATION);
        setContentView(R.layout.activity_conversation);
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        BaseNetworkImage avatarNI = findViewById(R.id.avatarNI);
        avatarNI.loadImageFromNetwork(mConversation.getPartner().getAvatar());
        TextView titleTV = findViewById(R.id.titleTV);
        titleTV.setText(mConversation.getPartner().getName());
        TextView subTitleTV = findViewById(R.id.subTitleTV);
        findViewById(R.id.backAction).setOnClickListener(view -> exitActivity());
        BeeLog.i(TAG, String.valueOf(mConversation));
    }

    public static void start(Context context, ConversationBuilder conversation) {
        Intent starter = new Intent(context, ConversationActivity.class);
        starter.putExtra(EXTRA_CONVERSATION, conversation);
        context.startActivity(starter);
    }

}
