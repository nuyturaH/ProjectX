package com.har8yun.homeworks.projectx.fragment.profile;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.activity.MainActivity;
import com.har8yun.homeworks.projectx.adapter.SkillItemRecyclerAdapter;
import com.har8yun.homeworks.projectx.model.Skill;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;
import com.har8yun.homeworks.projectx.util.DBUtil;
import com.har8yun.homeworks.projectx.util.PermissionChecker;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class MyProfileFragment extends Fragment {

    private static final String TAG = "MyProfileFragment";
    public static final int PERMISSION_STORAGE = 10;
    public static final int PERMISSION_CAMERA = 11;

    private static final int GALLERY_REQUEST_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 221;


    //shared preferences
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //navigation
    private NavController mNavController;

    //views
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mEditButton;
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
    private ImageView mAddImageView;
    private ImageView mPhoto1View;
    private TextView mAllPhotosView;
    private ImageView appBarImageView;

    //navigation
    private BottomNavigationView mBottomNavigationView;
    private Fragment mNavHostFragment;

    //user
    private User mCurrentUser;
    private UserViewModel mUserViewModel;

    //firebase
    private DatabaseReference mDatebaseReference = FirebaseDatabase.getInstance().getReference("users");



    //constructor
    public MyProfileFragment() {
    }


    //************************************** LIFECYCLE METHODS ********************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initViews(view);
        showBotNavBar();
        setMyProfileToolbar();

        onClickNavigate(mEditButton, R.id.action_my_profile_fragment_to_my_profile_edit_fragment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                Log.e("hhhh", "ViewModel in My profile " + user.toString());
                setNavigationComponent(user);
                mCurrentUser.setUserInfo(user.getUserInfo());
                mCurrentUser = user;
                fillViews();
            }
        });
    }


    //************************************ OVERRIDE METHODS ****************************************
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_log_out:
                logOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //************************************** METHODS ********************************************
    private void setNavigationComponent(final User user) {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.my_profile_fragment) {
                    if (mCurrentUser.getUsername() != null) {
                        NavHostFragment.findNavController(mNavHostFragment).getCurrentDestination().setLabel(user.getUsername());
                    }
                }
            }
        });
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.my_profile_fragment)).build();
        NavigationUI.setupWithNavController(mCollapsingToolbarLayout, mToolbar, mNavController, appBarConfiguration);
    }

    private void setMyProfileToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
    }

    Drawable imageDrawable;

    private void initViews(View view) {
        mToolbar = view.findViewById(R.id.toolbar_my_profile);
        mCollapsingToolbarLayout = view.findViewById(R.id.ctl_my_profile);
        appBarImageView = view.findViewById(R.id.app_bar_image);
        mEditButton = view.findViewById(R.id.fab_edit_my_profile);
        mPointsView = view.findViewById(R.id.tv_points_my_profile);
        mStatusView = view.findViewById(R.id.tv_status_my_profile);
        mHeightView = view.findViewById(R.id.tv_height_my_profile);
        mWeightView = view.findViewById(R.id.tv_weight_my_profile);
        mFirstNameView = view.findViewById(R.id.tv_first_name_my_profile);
        mLastNameView = view.findViewById(R.id.tv_last_name_my_profile);
        mGenderView = view.findViewById(R.id.tv_gender_my_profile);
        mAgeView = view.findViewById(R.id.tv_age_my_profile);
        mAvatarView = view.findViewById(R.id.app_bar_image);
        mProgressBar = view.findViewById(R.id.pb_avatar_my_profile);
        mAddImageView = view.findViewById(R.id.iv_add_photo_my_profile);
        mPhoto1View = view.findViewById(R.id.iv_photo1_my_profile);
        mAllPhotosView = view.findViewById(R.id.tv_all_photos_my_profile);
        mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);


        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
//        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
//            @Override
//            public void onChanged(@Nullable final User user) {
//                Log.e("hhhh", "ViewModel in My profile " + user.toString());
//                mCurrentUser = user;
//            }
//        });

        mCurrentUser = mUserViewModel.getUser().getValue();

        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });


        mPhoto1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.fragment_image_gallery);
                imageDrawable = mPhoto1View.getDrawable();
                ImageView image = dialog.findViewById(R.id.iv_image_gallery);
                ImageView closeImage = dialog.findViewById(R.id.iv_close_photo_gallery);
                image.setImageDrawable(imageDrawable);
                closeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });

        onClickNavigate(mAllPhotosView, R.id.action_my_profile_fragment_to_photos_fragment);

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this.getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (PermissionChecker.hasStoragePermission(getContext())) {
                                    choosePhotoFromGallery();
                                } else {
                                    requestStoragePermission();
                                }
                                break;
                            case 1:
                                if (PermissionChecker.hasCameraPermission(getContext())) {
                                    takePhotoFromCamera();
                                } else {
                                    requestCameraPermission();
                                }
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public void choosePhotoFromGallery() {
        Intent galleryAction = new Intent();
        galleryAction.setType("image/*");
        galleryAction.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryAction, "Select Picture"), GALLERY_REQUEST_CODE);
    }


    private void takePhotoFromCamera() {
        ((MainActivity) getActivity()).takePicture(CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            loadSelectedImage(data.getData().toString());
            Log.d(TAG, "onActivityResult: " + data.getData().toString());
        }
    }


    private void loadSelectedImage(String uri) {

        Log.d(TAG, "loadSelectedImage: " + uri);
        Glide.with(this)
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(getContext(), "FAILED " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("EDIT PROFILE", e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        uploadImageToFirebase(resource);
                        return false;
                    }
                })
                .into(mPhoto1View);
    }

    private void uploadImageToFirebase(Drawable resource) {
        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String imageName = mCurrentUser.getId() + System.currentTimeMillis();

        UploadTask uploadTask = DBUtil.getRefImages(imageName).putBytes(data);
        uploadTask.addOnCompleteListener(taskSnapshot -> {
            if (taskSnapshot.isSuccessful()) {
                DBUtil.addImageToFirebase(imageName);
                mCurrentUser.getImages().add(imageName);
                Log.d(TAG, "uploadImageToFirebase: " + mCurrentUser.getImages().get(0));
                Log.d(TAG, "uploadImageToFirebase: " + uploadTask.getResult());
                mDatebaseReference.child(mCurrentUser.getId()).setValue(mCurrentUser);

            } else {
                Toast.makeText(getActivity(), "SOMETHING WENT WRONG, TRY AGAIN", Toast.LENGTH_SHORT).show();
            }
        });


    }

    protected void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }
    }

    protected void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhotoFromGallery();
            }
        } else if (requestCode == PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoFromCamera();
            }
        }
    }

    public void onUploadFileCreated(String path) {
        loadSelectedImage(path);
    }

    private void fillViews() {
        Log.e("hhhh", "fillviews");
        if (mCurrentUser.getUserInfo() != null) {
            if (mCurrentUser.getUserInfo().getFirstName() != null) {
                mFirstNameView.setText(mCurrentUser.getUserInfo().getFirstName());
            }
            if (mCurrentUser.getUserInfo().getLastName() != null) {
                mLastNameView.setText(mCurrentUser.getUserInfo().getLastName());
            }
            if (mCurrentUser.getUserInfo().getGender() != null) {
                if (mCurrentUser.getUserInfo().getGender() == 0) {
                    mGenderView.setText("Male");
                } else {
                    mGenderView.setText("Female");
                }
            }
            if (mCurrentUser.getUserInfo().getBirthDate() != null) {
                Date date = mCurrentUser.getUserInfo().getBirthDate();
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
            if (mCurrentUser.getUserInfo().getWeight() != null) {
                mWeightView.setText(String.valueOf(mCurrentUser.getUserInfo().getWeight()) + " kg");
            }
            if (mCurrentUser.getUserInfo().getHeight() != null) {
                mHeightView.setText(String.valueOf((mCurrentUser.getUserInfo().getHeight())) + " m");
            }
            if (mCurrentUser.getUserInfo().getAvatar() != null) {
                mProgressBar.setVisibility(View.VISIBLE);
                setAvatar(mCurrentUser.getUserInfo().getAvatar());
            } else {
                appBarImageView.setImageResource(R.drawable.ic_add_a_photo);
                mProgressBar.setVisibility(View.GONE);
            }

            if (mCurrentUser.getUsername() != null) {
                //username setting is in setNavigationComponent()
            }
            if (mCurrentUser.getSkills() != null) {
                initRecyclerView();
            }
            if (mCurrentUser.getImages() != null && mCurrentUser.getImages().size() > 0) {
                Log.d(TAG, "fillViews: " + mCurrentUser.getImages().size());
                setImage(mCurrentUser.getImages().get(mCurrentUser.getImages().size() - 1));
            }
        }

    }


    String res;

    public void setAvatar(String url) {

        DBUtil.getRefAvatars(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                res = uri.toString();
                //getContext != null lifecycle architecture component TODO
                if (getContext() != null) {
                    Glide.with(getContext())
                            .load(res)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: Failed AGAIN");
            }
        });


    }

    public void setImage(String url) {

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
                            .into(mPhoto1View);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: Failed AGAIN");
            }
        });

    }


    private void logOut() {
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.map_fragment, true).build();
        Fragment mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.menu_item_log_out, null, navOptions);
        sharedPreferences.setLoggedIn(getActivity(), false);
        mUserViewModel.setUser(mCurrentUser);

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getContext(), "Signed Out", Toast.LENGTH_SHORT).show();
    }


    private void initRecyclerView() {
        SkillItemRecyclerAdapter skillItemRecyclerAdapter = new SkillItemRecyclerAdapter();
//        mSkillItemRecyclerAdapter.setOnRvItemClickListener(new SkillItemRecyclerAdapter.OnRvItemClickListener() {
//            @Override
//            public void onItemClicked(int pos) {
//                Skill item = mSkillList.get(pos);
//                Toast.makeText(getActivity(), "Clicked : " + item.getSkillName() + ": " + item.getSkillCount(), Toast.LENGTH_SHORT).show();
//            }
//        });
        Map<String, Integer> map = mCurrentUser.getSkills();
        List<Skill> list = new ArrayList<>();


        for (String currentKey : map.keySet()) {
            Skill skill = new Skill();
            skill.setSkillName(currentKey);
            skill.setSkillCount(map.get(currentKey));
            list.add(skill);
        }

        RecyclerView recyclerView = getView().findViewById(R.id.rv_skills_my_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(skillItemRecyclerAdapter);
        skillItemRecyclerAdapter.addItems(list);
    }

    private void showBotNavBar() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPause() {
        super.onPause();
        mUserViewModel.setUser(mCurrentUser);

    }
}