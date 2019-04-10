package com.har8yun.homeworks.projectx.fragment.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.ViewPagerAdapter;

import java.util.List;

import static com.har8yun.homeworks.projectx.fragment.profile.OtherPhotosFragment.FRAGMENT_CODE;

public class ImageGalleryFragment extends Fragment
{

    private static final String TAG = "ImageGalleryFragment";

    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private Button mCloseButton;

    public  List<String> mPhotosList;

    public ImageGalleryFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_gallery, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCloseButton = view.findViewById(R.id.iv_close_photo_gallery);
        mViewPagerAdapter = new ViewPagerAdapter(getContext(),mPhotosList);
        mViewPager = view.findViewById(R.id.vp_gallery);
        mViewPager.setAdapter(mViewPagerAdapter);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ImageGalleryFragment myFragment = (ImageGalleryFragment)getFragmentManager().findFragmentByTag(FRAGMENT_CODE);
                fragmentTransaction.remove(myFragment);
                fragmentTransaction.commit();
            }
        });

    }
}
