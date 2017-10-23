/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatapp.cell;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

import ke.co.toshngure.chatapp.model.Conversation;


/**
 * Created by Anthony Ngure on 03/10/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class ConversationCell extends SimpleCell<Conversation, ConversationCell.ConversationViewHolder> {
    public ConversationCell(@NonNull Conversation item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }

    @NonNull
    @Override
    protected ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull View view) {
        return null;
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationViewHolder conversationViewHolder, int i, @NonNull Context context, Object o) {

    }

    public class ConversationViewHolder extends SimpleViewHolder {
        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
