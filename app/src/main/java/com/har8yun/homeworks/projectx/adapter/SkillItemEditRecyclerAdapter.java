package com.har8yun.homeworks.projectx.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.Skill;


import java.util.ArrayList;
import java.util.List;


public class SkillItemEditRecyclerAdapter extends RecyclerView.Adapter<SkillItemEditRecyclerAdapter.SkillItemViewHolder> {

    private List<Skill> mData = new ArrayList<>();
    private static OnRvItemClickListener mOnRvItemClickListener;

    @NonNull
    @Override
    public SkillItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill_my_profile_edit, parent, false);
        return new SkillItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillItemViewHolder holder, final int position) {
        Skill skill = mData.get(position);

        holder.skillNameView.setText(skill.getSkillName());
        if (skill.getSkillCount() == 0) {
            holder.dotsView.setVisibility(View.INVISIBLE);
            holder.skillCountView.setVisibility(View.INVISIBLE);
        } else {
            holder.skillCountView.setText(skill.getSkillCount().toString());

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class SkillItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView skillNameView;
        EditText skillCountView;
        TextView dotsView;
        ImageView removeView;

        SkillItemViewHolder(View itemView) {
            super(itemView);

            skillNameView = itemView.findViewById(R.id.tv_skill_name_edit_item);
            skillCountView = itemView.findViewById(R.id.tv_skill_count_edit_item);
            dotsView = itemView.findViewById(R.id.tv_dots_edit_item);
            removeView = itemView.findViewById(R.id.iv_remove_item);

            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRvItemClickListener.onItemClicked(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }


    public void addItems(List<Skill> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }


    public void updateItem(Skill item) {
        for (int i = 0; i < mData.size(); i++) {
            if (item.getSkillName().equals(mData.get(i).getSkillName())) {
                mData.set(i, item);
                notifyItemChanged(i);
            }
        }
    }
    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }


    public void setOnRvItemClickListener(OnRvItemClickListener mOnRvItemClickListener) {
        this.mOnRvItemClickListener = mOnRvItemClickListener;
    }

    public interface OnRvItemClickListener {
        void onItemClicked(int pos);
    }
}
