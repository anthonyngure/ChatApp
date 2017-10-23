/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.chatapp.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.jaychang.srv.SimpleCell;

import java.io.Serializable;
import java.util.List;

import ke.co.toshngure.basecode.dataloading.DataLoadingConfig;
import ke.co.toshngure.basecode.dataloading.ModelListFragment;
import ke.co.toshngure.chatapp.cell.ConversationCell;
import ke.co.toshngure.chatapp.model.Conversation;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationsFragment extends ModelListFragment<Conversation, ConversationCell>
        implements SimpleCell.OnCellClickListener<Conversation>,
        SimpleCell.OnCellLongClickListener<Conversation> {


    public static class Builder implements Serializable {

        private Context context;

        public Builder(Activity activity) {
            this.context = activity;
        }
    }


    public ConversationsFragment() {
        // Required empty public constructor
    }

    public static ConversationsFragment newInstance() {

        Bundle args = new Bundle();

        ConversationsFragment fragment = new ConversationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ConversationCell onCreateCell(Conversation item) {
        ConversationCell conversationCell = new ConversationCell(item);
        conversationCell.setOnCellClickListener(this);
        conversationCell.setOnCellLongClickListener(this);
        return conversationCell;
    }

    @Override
    protected DataLoadingConfig getDataLoadingConfig() {
        return new DataLoadingConfig().disableConnection().setCacheEnabled(true);
    }

    @Override
    protected Class<Conversation> getModelClass() {
        return Conversation.class;
    }

    @Override
    protected List<Conversation> onLoadCaches() {
        return super.onLoadCaches();
    }

    @Override
    public void onCellClicked(@NonNull Conversation conversation) {

    }

    @Override
    public void onCellLongClicked(@NonNull Conversation conversation) {

    }
}
