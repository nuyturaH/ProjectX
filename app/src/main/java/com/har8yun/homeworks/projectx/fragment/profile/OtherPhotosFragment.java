package com.har8yun.homeworks.projectx.fragment.profile;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.PhotosAdapter;
import com.har8yun.homeworks.projectx.fragment.profile.ImageGalleryFragment;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import java.util.List;

public class OtherPhotosFragment extends Fragment {

    private static final String TAG = "OtherPhotosFragment";
    public static final String FRAGMENT_CODE = "Gallery";


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


    //constructor
    public OtherPhotosFragment() {
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

            }
        });

        mPhotosAdapter.addItems(mPhotosList);
    }

    private void addFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_gallery, fragment,FRAGMENT_CODE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
