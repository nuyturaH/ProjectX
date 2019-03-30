package com.har8yun.homeworks.projectx.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

    private static final String TAG = "SkillItemEditRecyclerAd";

    private List<Skill> mData = new ArrayList<>();
    private static OnRvItemClickListener mOnRvItemClickListener;

    String ss = "0";

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
        if (skill.getSkillCount() != 0) {
            holder.skillCountView.setText(String.valueOf(skill.getSkillCount()));
            Log.d(TAG, "onBindViewHolder: " + skill.getSkillCount());
        }
        if (skill.getSkillName().equals("Human Flag") || skill.getSkillName().equals("Planche")
                || skill.getSkillName().equals("ATM") || skill.getSkillName().equals("Akka")) {
            holder.skillCountView.setVisibility(View.INVISIBLE);
        }

//        holder.skillCountView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    mData.get(position).setSkillCount(Integer.parseInt(ss));
//                    updateItem(mData.get(position));
//                }
//            }
//        });


        holder.skillCountView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                if(!"".contentEquals(s)){
//                    try {
//                        int i = Integer.valueOf(s.toString());
//                        mData.get(position).setSkillCount(i);
//                    }catch (Exception e){
//                        mData.get(position).setSkillCount(0);
//                    }
//                }
                if (s.toString().isEmpty()) {
                    mData.get(position).setSkillCount(0);
                } else {
                    mData.get(position).setSkillCount(Integer.valueOf(s.toString()));
                }
            }
        });

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

    public void addItem(Skill item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public ArrayList<Skill> getSkills() {
        return (ArrayList<Skill>) mData;
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
