package com.har8yun.homeworks.projectx.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ChartsViewHolder> {

    private static final String TAG = "ChartsAdapter";

    private String kkk;

    private List<User> mData = new ArrayList<>();


    private static ChartsAdapter.OnRvItemClickListener mOnRvItemClickListener;


    @NonNull
    @Override
    public ChartsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_chart_item, parent, false);

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




    public class ChartsViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView skillCount;

        public ChartsViewHolder(final View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username_chart);
            skillCount = itemView.findViewById(R.id.tv_skill_count_chart);
        }
    }

    public void setOnRvItemClickListener(ChartsAdapter.OnRvItemClickListener mOnRvItemClickListener) {
        this.mOnRvItemClickListener = mOnRvItemClickListener;
    }

    public interface OnRvItemClickListener {
        void onItemClicked(String uid);
    }
}
