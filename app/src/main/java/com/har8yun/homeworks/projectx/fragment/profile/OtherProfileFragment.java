package com.har8yun.homeworks.projectx.fragment.profile;


import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.har8yun.homeworks.projectx.adapter.SkillItemRecyclerAdapter;
import com.har8yun.homeworks.projectx.model.Skill;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.util.DBUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class OtherProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "OtherProfileFragment";

    //views
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mMessageButton;
    private TextView mPointsView;
    private TextView mStatusView;
    private TextView mHeightView;
    private TextView mWeightView;
    private TextView mFirstNameView;
    private TextView mLastNameView;
    private TextView mGenderView;
    private TextView mAgeView;
    private ImageView mAvatarView;
    private ProgressBar mProgressBar;
    private TextView mAllPhotosView;
    private ImageView mPhoto1View;
    private ImageView mPhoto2View;
    private ImageView mPhoto3View;
    private ImageView mPhoto4View;

    private Fragment mNavHostFragment;

    private List<ImageView> photos = new ArrayList<>();

    //navigation
    private NavController mNavController;



    //viewmodel
    private UserViewModel mUserViewModel;

    //user
    private User mOtherUser;

    public OtherProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getOtherUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                mOtherUser = user;
                setNavigationComponent(user);
            }
        });
        mOtherUser = mUserViewModel.getOtherUser().getValue();


        initViews(view);
        fillViews(view);


        return view;
    }

    private void initViews(View view) {
        mToolbar = view.findViewById(R.id.toolbar_other_profile);
        mCollapsingToolbarLayout = view.findViewById(R.id.ctl_other_profile);
        mMessageButton = view.findViewById(R.id.fab_message_other_profile);
        mPointsView = view.findViewById(R.id.tv_points_other_profile);
        mStatusView = view.findViewById(R.id.tv_status_other_profile);
        mHeightView = view.findViewById(R.id.tv_height_other_profile);
        mWeightView = view.findViewById(R.id.tv_weight_other_profile);
        mFirstNameView = view.findViewById(R.id.tv_first_name_other_profile);
        mLastNameView = view.findViewById(R.id.tv_last_name_other_profile);
        mGenderView = view.findViewById(R.id.tv_gender_other_profile);
        mAgeView = view.findViewById(R.id.tv_age_other_profile);
        mAvatarView = view.findViewById(R.id.other_app_bar_image);
        mProgressBar = view.findViewById(R.id.pb_avatar_other_profile);
        mAllPhotosView = view.findViewById(R.id.tv_all_photos_other_profile);
        mPhoto1View = view.findViewById(R.id.iv_photo1_other_profile);
        mPhoto2View = view.findViewById(R.id.iv_photo2_other_profile);
        mPhoto3View = view.findViewById(R.id.iv_photo3_other_profile);
        mPhoto4View = view.findViewById(R.id.iv_photo4_other_profile);
        mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);


        photos.add(mPhoto1View);
        photos.add(mPhoto2View);
        photos.add(mPhoto3View);
        photos.add(mPhoto4View);

        mPhoto1View.setOnClickListener(this);
        mPhoto2View.setOnClickListener(this);
        mPhoto3View.setOnClickListener(this);
        mPhoto4View.setOnClickListener(this);

        onClickNavigate(mAllPhotosView, R.id.action_other_profile_fragment_to_other_photos_fragment);

    }

    private void fillViews(View view) {

        Log.e("hhhh", "fillviews");
        if (mOtherUser.getUserInfo() != null) {
            if (mOtherUser.getUserInfo().getFirstName() != null) {
                mFirstNameView.setText(mOtherUser.getUserInfo().getFirstName());
            }
            if (mOtherUser.getUserInfo().getLastName() != null) {
                mLastNameView.setText(mOtherUser.getUserInfo().getLastName());
            }
            if (mOtherUser.getUserInfo().getGender() != null) {
                if (mOtherUser.getUserInfo().getGender() == 0) {
                    mGenderView.setText("Male");
                } else {
                    mGenderView.setText("Female");
                }
            }
            if (mOtherUser.getUserInfo().getBirthDate() != null) {
                Date date = mOtherUser.getUserInfo().getBirthDate();
                int b = date.getYear();
                int y = Calendar.getInstance().get(Calendar.YEAR);
                int m = Calendar.getInstance().get(Calendar.MONTH);
                int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int ss = (y - b) % 100;
                if (date.getMonth() < m && date.getDay() < d) {
                    mAgeView.setText(String.valueOf(ss));
                } else {
                    mAgeView.setText(String.valueOf(ss - 1));
                }

            }
            if (mOtherUser.getUserInfo().getWeight() != null) {
                mWeightView.setText(String.valueOf(mOtherUser.getUserInfo().getWeight()) + " kg");
            }
            if (mOtherUser.getUserInfo().getHeight() != null) {
                mHeightView.setText(String.valueOf((mOtherUser.getUserInfo().getHeight())) + " m");
            }
            if (mOtherUser.getUserInfo().getAvatar() != null) {
                mProgressBar.setVisibility(View.VISIBLE);
                setAvatar(mOtherUser.getUserInfo().getAvatar());
            } else {
                mAvatarView.setImageResource(R.drawable.ic_person_outline_grey);
                mProgressBar.setVisibility(View.GONE);
            }

            if (mOtherUser.getUsername() != null) {
                //username setting is in setNavigationComponent()
            }
            if (mOtherUser.getSkills() != null) {
                initRecyclerView(view);
            }
            if (mOtherUser.getImages() != null && mOtherUser.getImages().size() > 0) {
                Log.d(TAG, "fillViews: " + mOtherUser.getImages().size());
                for (int i = 0; i < mOtherUser.getImages().size() && i < photos.size(); i++) {
                    setImage(mOtherUser.getImages().get(mOtherUser.getImages().size() - 1 - i), photos.get(i));
                    Log.d(TAG, "for: "+i);
                }
            }
        }

    }

    String res;

    public void setImage(String url, ImageView imageView) {

        DBUtil.getRefImages(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                res = uri.toString();
                //getContext != null lifecycle architecture component TODO
                if (getContext() != null) {
                    Glide.with(getContext())
                            .load(res)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Toast.makeText(getContext(), "FAILED " + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void setAvatar(String url) {


        DBUtil.getRefAvatars(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                res = uri.toString();
                Glide.with(getContext())
                        .load(res)
                        .apply(RequestOptions.circleCropTransform())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //showMessage(getString(R.string.message_try_again));
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "FAILED " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("EDIT PROFILE", e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(mAvatarView);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: Failed AGAIN");
            }
        });

    }

    private void initRecyclerView(View view) {
        SkillItemRecyclerAdapter skillItemRecyclerAdapter = new SkillItemRecyclerAdapter();

        Map<String, Integer> map = mOtherUser.getSkills();
        List<Skill> list = new ArrayList<>();


        for (String currentKey : map.keySet()) {
            Skill skill = new Skill();
            skill.setSkillName(currentKey);
            skill.setSkillCount(map.get(currentKey));
            list.add(skill);
        }

        RecyclerView recyclerView = view.findViewById(R.id.rv_skills_other_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(skillItemRecyclerAdapter);
        skillItemRecyclerAdapter.addItems(list);
    }

    private void setNavigationComponent(final User user) {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.other_profile_fragment) {
                    if (mOtherUser.getUsername() != null) {
                        NavHostFragment.findNavController(mNavHostFragment).getCurrentDestination().setLabel(user.getUsername());
                    }
                }
            }
        });
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.my_profile_fragment)).build();
        NavigationUI.setupWithNavController(mCollapsingToolbarLayout, mToolbar, mNavController, appBarConfiguration);
    }

    @Override
    public void onClick(View v) {
        Drawable resource;

        ImageView imageView = (ImageView) v;
        resource = imageView.getDrawable();

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_image_gallery);

        ImageView image = dialog.findViewById(R.id.iv_image_gallery);
        ImageView closeImage = dialog.findViewById(R.id.iv_close_photo_gallery);
        image.setImageDrawable(resource);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
