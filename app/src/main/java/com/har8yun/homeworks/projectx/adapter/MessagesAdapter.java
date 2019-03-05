package com.har8yun.homeworks.projectx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.Message;
import com.har8yun.homeworks.projectx.model.User;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private Context mContext;
    private List<Message> mMessageList;

    //Users
    private User sender;
    private User reciever;

    MessagesAdapter(User s,User r) {
        this.sender = s;
        this.reciever = r;
    }

    MessagesAdapter withContext(Context context) {
        mContext = context;
        return this;
    }

    MessagesAdapter withItems(List<Message> messages) {
        mMessageList = messages;
        return this;
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_message, parent, false);
        return new MessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessagesAdapter.ViewHolder viewHolder, int position) {
        Message message = mMessageList.get(position);

        if (!message.isImage()) {
            viewHolder.mMessageView.setVisibility(View.VISIBLE);
            viewHolder.mPhotoImg.setVisibility(View.GONE);

            viewHolder.mMessageView.setText(message.getText());

            viewHolder.mMessageView.setGravity(message.getmSender().equals(sender) ?
                    Gravity.END : Gravity.START);
        } else {
            viewHolder.mMessageView.setVisibility(View.GONE);
            viewHolder.mPhotoImg.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(message.getText())
                    .into(viewHolder.mPhotoImg);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessageView;
        private ImageView mPhotoImg;

        ViewHolder(View itemView) {
            super(itemView);
            mMessageView = itemView.findViewById(R.id.tv_message);
            mPhotoImg = itemView.findViewById(R.id.iv_message);
        }
    }
}
