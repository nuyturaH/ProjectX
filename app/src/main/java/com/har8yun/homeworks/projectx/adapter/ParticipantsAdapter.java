package com.har8yun.homeworks.projectx.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.util.DBUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder> {

    private static final String TAG = "ParticipantsAdapter";

    Context context;

    private List<User> mData = new ArrayList<>();



    private static ParticipantsAdapter.OnRvItemClickListener mOnRvItemClickListener;


    @NonNull
    @Override
    public ParticipantsAdapter.ParticipantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_chart_item, parent, false);
        context = parent.getContext();

        return new ParticipantsAdapter.ParticipantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantsAdapter.ParticipantsViewHolder holder, final int position) {
        User user = mData.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRvItemClickListener.onItemClicked(user.getId());
            }
        });

        holder.username.setText(user.getUsername());
        if (user.getUserInfo() != null) {
            if (user.getUserInfo().getAvatar() != null) {
                setAvatar(user.getUserInfo().getAvatar(), holder.avatar);
            } else {
                holder.avatar.setImageResource(R.drawable.ic_person_outline_grey);
                Log.e(TAG, "onBindViewHolder: YESSSSSS" );
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(List<User> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }
    public void addItem(User user)
    {
        mData.add(user);
        notifyDataSetChanged();
    }


    String res;

    public void setAvatar(String url, ImageView imageView) {

        DBUtil.getRefAvatars(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                res = uri.toString();
                //getContext != null lifecycle architecture component TODO
                if (context != null) {
                    Glide.with(context)
                            .load(res)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Toast.makeText(context, "FAILED " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("EDIT PROFILE", e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imageView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: Failed AGAIN");
            }
        });
    }


    public class ParticipantsViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView avatar;
        public TextView textView;
        public TextView dots;

        public ParticipantsViewHolder(final View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username_chart);
            avatar = itemView.findViewById(R.id.iv_avatar_chart);
            textView = itemView.findViewById(R.id.tv_skill_count_chart);
            dots = itemView.findViewById(R.id.tv_dots_chart);
            textView.setVisibility(View.GONE);
            dots.setVisibility(View.GONE);
        }
    }

    public void setOnRvItemClickListener(ParticipantsAdapter.OnRvItemClickListener mOnRvItemClickListener) {
        this.mOnRvItemClickListener = mOnRvItemClickListener;
    }

    public interface OnRvItemClickListener {
        void onItemClicked(String uid);
    }
}
