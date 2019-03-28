package com.har8yun.homeworks.projectx.fragment.tasks;


import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.PhotosAdapter;
import com.har8yun.homeworks.projectx.adapter.ViewPagerAdapter;
import com.har8yun.homeworks.projectx.fragment.ImageGalleryFragment;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

public class OtherPhotosFragment extends Fragment {

    private static final String TAG = "OtherPhotosFragment";


    //view
    private RecyclerView mRecyclerView;
    private TextView textView;
    private LinearLayout layout;

    //adapter
    private PhotosAdapter mPhotosAdapter;


    //
    private List<String> mPhotosList;
    private User mOtherUser;

    //viewmodel
    private UserViewModel mUserViewModel;



    public OtherPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_photos, container, false);

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getOtherUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mOtherUser = user;
                mPhotosList = mOtherUser.getImages();
            }
        });
        mOtherUser = mUserViewModel.getOtherUser().getValue();
        mPhotosList = mOtherUser.getImages();


        initViews(view);

        return view;
    }



    private void initViews(View view)
    {
        mRecyclerView = view.findViewById(R.id.rv_other_photos);
        textView = view.findViewById(R.id.tv_other_text_adapter);
        layout = view.findViewById(R.id.layout_gallery);

        initRecyclerView();
    }

    private void initRecyclerView() {
        if(!mPhotosList.isEmpty())
        {
            textView.setVisibility(View.GONE);
        }
        mPhotosAdapter = new PhotosAdapter();

        mPhotosAdapter.setOnRvItemClickListener(null);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        mRecyclerView.setAdapter(mPhotosAdapter);

        mPhotosAdapter.setOnOtherRvItemClickListener(new PhotosAdapter.OnOtherRvItemClickListener() {
            @Override
            public void onItemClicked(Drawable resource) {

                ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
                imageGalleryFragment.mPhotosList = mPhotosList;
                layout.setVisibility(View.VISIBLE);
                addFragment(imageGalleryFragment);



//                Dialog dialog = new Dialog(getContext());
//                dialog.setContentView(R.layout.fragment_image_gallery);
//                ImageView image = dialog.findViewById(R.id.iv_image_gallery);
//                ImageView closeImage = dialog.findViewById(R.id.iv_close_photo_gallery);
//                image.setImageDrawable(resource);
//                closeImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();

            }
        });

        mPhotosAdapter.addItems(mPhotosList);
    }

    private void addFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_gallery, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
