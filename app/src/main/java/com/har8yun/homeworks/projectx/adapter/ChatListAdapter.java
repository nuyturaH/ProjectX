package com.har8yun.homeworks.projectx.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.util.DBUtil;

import java.util.Collection;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    //private Context mContext;
    private List<User> mUsersList;
    private OnUserItemClickListener mListener;


    public ChatListAdapter() {
    }

//    ChatListAdapter withContext(Context context) {
//        mContext = context;
//        return this;
//    }

    ChatListAdapter withItems(List<User> usersList) {
        mUsersList = usersList;
        return this;
    }

//    ChatListAdapter withFollowers(List<String> followers) {
//        mFollowersList = followers;
//        return this;
//    }

    ChatListAdapter withListener(OnUserItemClickListener listener) {
        mListener = listener;
        return this;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_chat_user, parent, false);
        return new ChatListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.ViewHolder viewHolder, int position) {
        final User user = mUsersList.get(position);


        viewHolder.mUsername.setText(user.getUsername());
        viewHolder.mUserPhotoImg.setImageURI(user.getUserInfo().getAvatar());


        viewHolder.mUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserSelected(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public void addItems(Collection<User> items) {
        mUsersList.addAll(items);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mUserPhotoImg;
        private TextView mUsername;


        ViewHolder(View itemView) {
            super(itemView);
            mUserPhotoImg = itemView.findViewById(R.id.iv_avatar_chat_user);
            mUsername = itemView.findViewById(R.id.tv_username_chat_user);
        }
    }

    public interface OnUserItemClickListener {
        void onUserSelected(User user);
    }

    public void setmListener(OnUserItemClickListener mListener) {
        this.mListener = mListener;
    }
}
