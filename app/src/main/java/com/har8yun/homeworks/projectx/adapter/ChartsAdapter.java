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
import java.util.List;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ChartsViewHolder> {

    private static final String TAG = "ChartsAdapter";

    private String kkk;

    Context context;

    private List<User> mData = new ArrayList<>();


    private static ChartsAdapter.OnRvItemClickListener mOnRvItemClickListener;


    @NonNull
    @Override
    public ChartsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_chart_item, parent, false);

        context = parent.getContext();

        return new ChartsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartsViewHolder holder, final int position) {
        User user = mData.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRvItemClickListener.onItemClicked(user.getId());
            }
        });

        holder.username.setText(user.getUsername());
        holder.skillCount.setText(String.valueOf(user.getSkills().get(kkk)));
        if (user.getUserInfo() != null) {
            if (user.getUserInfo().getAvatar() != null) {
                setAvatar(user.getUserInfo().getAvatar(), holder.avatar);
            }
            else {
                holder.avatar.setImageResource(R.drawable.ic_person_outline_grey);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(List<User> items,String key) {
        mData.clear();
        kkk = key;
        mData.addAll(items);
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




    public class ChartsViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView skillCount;
        public ImageView avatar;

        public ChartsViewHolder(final View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username_chart);
            skillCount = itemView.findViewById(R.id.tv_skill_count_chart);
            avatar = itemView.findViewById(R.id.iv_avatar_chart);
        }
    }

    public void setOnRvItemClickListener(ChartsAdapter.OnRvItemClickListener mOnRvItemClickListener) {
        this.mOnRvItemClickListener = mOnRvItemClickListener;
    }

    public interface OnRvItemClickListener {
        void onItemClicked(String uid);
    }
}
