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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {

    private static final String TAG = "PhotosAdapter";
    Context context;


    private List<String> mData = new ArrayList<>();

    private boolean isDeleteMode;
    private boolean toClearCheckBoxState;


    private static PhotosAdapter.OnRvItemClickListener mOnRvItemClickListener;
    private static PhotosAdapter.OnOtherRvItemClickListener mOnOtherRvItemClickListener;


    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_photo_item, parent, false);

        context = parent.getContext();
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosViewHolder holder, final int position) {
        String url = mData.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRvItemClickListener !=null)
                {
                    mOnRvItemClickListener.onItemClicked(holder.photo.getDrawable());
                }
                else if(mOnOtherRvItemClickListener !=null)
                {
                    mOnOtherRvItemClickListener.onItemClicked(holder.photo.getDrawable());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnRvItemClickListener != null) {
                    showAllCheckBoxes(true);
                    mOnRvItemClickListener.onItemLongClicked(mData.get(position));
                }
                return true;
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("ADAPTER", "ItemCheck");
                mOnRvItemClickListener.onItemChecked(url, isChecked);
            }
        });

        if (toClearCheckBoxState) {
            holder.checkBox.setChecked(false);
        }
        toClearCheckBoxState = false;

        holder.checkBox.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);


        setPhoto(url, holder.photo);
    }

    public void showAllCheckBoxes(boolean show) {
        isDeleteMode = show;
        notifyDataSetChanged();
    }

    public void clearCheckBoxState() {
        toClearCheckBoxState = true;
        notifyDataSetChanged();
    }

    String res;

    public void setPhoto(String url, ImageView imageView) {

        Log.e(TAG, "setPhoto: " + url);

        DBUtil.getRefImages(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                res = uri.toString();
                //getContext != null lifecycle architecture component TODO
                Glide.with(context)
                        .load(res)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(context, "FAILED " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("EDIT PROFILE", e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                mProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: Failed AGAIN");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(List<String> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }


    public class PhotosViewHolder extends RecyclerView.ViewHolder {
        public ImageView photo;
        public CheckBox checkBox;

        public PhotosViewHolder(final View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.iv_photo_adapter);
            checkBox = itemView.findViewById(R.id.cb_photos);

        }
    }

    public void setOnRvItemClickListener(PhotosAdapter.OnRvItemClickListener mOnRvItemClickListener) {
        this.mOnRvItemClickListener = mOnRvItemClickListener;
    }

    public interface OnRvItemClickListener {
        void onItemClicked(Drawable resource);

        void onItemLongClicked(String item);

        void onItemChecked(String item, boolean isChecked);
    }

    public void setOnOtherRvItemClickListener(PhotosAdapter.OnOtherRvItemClickListener mOnRvItemClickListener) {
        this.mOnOtherRvItemClickListener = mOnRvItemClickListener;
    }

    public interface OnOtherRvItemClickListener {
        void onItemClicked(Drawable resource);
    }
}