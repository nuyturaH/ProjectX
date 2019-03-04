package com.har8yun.homeworks.projectx.fragment.bottomNavigation;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserInfo;
import com.har8yun.homeworks.projectx.util.DBUtil;
import com.har8yun.homeworks.projectx.util.PermissionChecker;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

import androidx.navigation.Navigation;


public class MyProfileEditFragment extends Fragment {

    public static final String TAG = MyProfileEditFragment.class.getSimpleName();
    public static final int PERMISSION_STORAGE = 10;
    public static final int PERMISSION_CAMERA = 11;

    //views
    private ImageView mAvatarView;

    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mFirstNameView;
    private EditText mLastNameView;

    private RadioGroup mGender;
    private RadioButton mMale;
    private RadioButton mFemale;

    private TextView mChangePasswordView;
    private TextView mBirthDateView;

    private EditText mWeightView;
    private EditText mHeightView;
    private Button mSaveButton;

    //Firebase
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;
    private StorageReference mStorageRef;

    //User
    private User mUser;  //current User,    TODO: get this user from MyProfile Fragment
    private FirebaseUser mFirebaseUser;

    //constructors
    public MyProfileEditFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile_edit, container, false);

        User user = new User();
        UserInfo userInfo = new UserInfo();
        userInfo.setWeight(20f);
        userInfo.setHeight(30f);
        userInfo.setFirstName("Anun");
        userInfo.setLastName("Azganun");
        userInfo.setGender(0);
        user.setUserInfo(userInfo);
        user.setEmail("test@gmail.com");
        //user.setUsername("TEST111");
        user.setUsername(getArguments().getString("username"));

        mUser = user;

        initViews(view);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setUsernameErrors();
        setPasswordError();
        setConfirmPasswordError();
        saveChanges();

        mChangePasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordView.setVisibility(View.VISIBLE);
                mConfirmPasswordView.setVisibility(View.VISIBLE);
                mChangePasswordView.setVisibility(View.GONE);
                updateUserPassword();
            }
        });

        mBirthDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });



        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    //************************************** METHODS ********************************************

    private void initViews(View view) {
        mAvatarView = view.findViewById(R.id.iv_avatar_my_profile_edit);
        mUsernameView = view.findViewById(R.id.etv_username_my_profile_edit);
        mFirstNameView = view.findViewById(R.id.etv_first_name_my_profile_edit);
        mLastNameView = view.findViewById(R.id.etv_last_name_my_profile_edit);
        mGender = view.findViewById(R.id.rg_gender_my_profile_edit);
        mMale = view.findViewById(R.id.rb_male_my_profile_edit);
        mFemale = view.findViewById(R.id.rb_female_my_profile_edit);
        mPasswordView = view.findViewById(R.id.etv_password_my_profile_edit);
        mConfirmPasswordView = view.findViewById(R.id.etv_confirm_password_my_profile_edit);
        mChangePasswordView = view.findViewById(R.id.tv_change_password_my_profile_edit);
        mBirthDateView = view.findViewById(R.id.tv_birth_date_my_profile_edit);
        mWeightView = view.findViewById(R.id.etv_weight_my_profile_edit);
        mHeightView = view.findViewById(R.id.etv_height_my_profile_edit);
        mSaveButton = view.findViewById(R.id.btn_save_my_profile_edit);

        mUsernameView.setText(mUser.getUsername());

        if (mUser.getUserInfo().getFirstName() != null) {
            mFirstNameView.setText(mUser.getUserInfo().getFirstName());
        }
        if (mUser.getUserInfo().getLastName() != null) {
            mLastNameView.setText(mUser.getUserInfo().getLastName());
        }
        if (mUser.getUserInfo().getWeight() != null) {
            mWeightView.setText(String.valueOf(mUser.getUserInfo().getHeight()));
        }
        if (mUser.getUserInfo().getHeight() != null) {
            mHeightView.setText(String.valueOf(mUser.getUserInfo().getWeight()));
        }

        if (mUser.getUserInfo().getBirthDate() != null) {
            mBirthDateView.setText(String.valueOf(mUser.getUserInfo().getBirthDate().getTime()));
        }
        if (mUser.getUserInfo().getGender() != null) {
            if (mUser.getUserInfo().getGender() == 0)
                mMale.setSelected(true);
            else mFemale.setSelected(true);
        }

//        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this.getContext(),
//                R.array.numbers, android.R.layout.simple_spinner_item);
//        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mWeightView.setAdapter(weightAdapter);
//        mWeightView.setOnItemSelectedListener(this);
//
//        ArrayAdapter<CharSequence> heightAdapter = ArrayAdapter.createFromResource(this.getContext(), //TODO change R.array.numbers
//                R.array.numbers, android.R.layout.simple_spinner_item);
//        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mWeightView.setAdapter(heightAdapter);
//        mWeightView.setOnItemSelectedListener(this);

    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        UserInfo mUserInfo = mUser.getUserInfo();
//
//        switch (parent.getId()) {
//            case R.id.sp_weight_my_profile_edit:
//                String text1 = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), text1, Toast.LENGTH_SHORT).show();
//                mUserInfo.setWeight(Float.valueOf(text1));
//                mFirebaseAnalytics.setUserProperty("weight", mUserInfo.getWeight().toString());
//                break;
//            case R.id.sp_height_my_profile_edit:
//                String text2 = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), text2, Toast.LENGTH_SHORT).show();
//                mUserInfo.setHeight(Float.valueOf(text2));
//                mFirebaseAnalytics.setUserProperty("height", mUserInfo.getHeight().toString());
//                break;
//        }
//    }
//
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    private void setConfirmPasswordError() {
        mConfirmPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!mConfirmPasswordView.getText().toString().equals(mPasswordView.getText().toString())
                            && mConfirmPasswordView.getText().length() != 0) {
                        mConfirmPasswordView.setError(getResources().getString(R.string.confirm_password_enter));
                    }
                }
            }
        });
    }

    private void setPasswordError() {
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mPasswordView.getText().length() < 6 && mPasswordView.getText().length() != 0) {
                        mPasswordView.setError(getResources().getString(R.string.password_length));
                    }
                }
            }
        });
    }


    private void setUsernameErrors() {
        mUsernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 20) {
                    mUsernameView.setError(getResources().getString(R.string.username_length_long));
                    mUsernameView.setTextColor(Color.RED);
                } else {
                    mUsernameView.setTextColor(Color.BLACK);
                }
                if (!s.toString().matches("[a-zA-Z0-9]*")) {
                    mUsernameView.setError(getResources().getString(R.string.username_content));
                }
                if (mUser.getUsername().equals(s.toString())) {
                    mUsernameView.setError(getResources().getString(R.string.username_existence));
                    mUsernameView.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mUsernameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mUsernameView.getText().length() < 6 && mUsernameView.getText().length() != 0) {
                        mUsernameView.setError(getResources().getString(R.string.username_length_short));
                    }
                }
            }
        });
    }


    private void saveChanges() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsernameView.getText().length() == 0) {
                    mUsernameView.setError(getResources().getString(R.string.username_enter));
                } else if (mUsernameView.getError() == null && mPasswordView.getError() == null &&
                        mConfirmPasswordView.getError() == null) {
                    if (!mConfirmPasswordView.getText().toString().equals(mPasswordView.getText().toString())) {
                        mConfirmPasswordView.setError(getResources().getString(R.string.confirm_password_matching));
                    } else {

                        //giving changed fields to MyProfileFragment
                        Bundle bundle = new Bundle();
                        bundle.putString("username2",mUsernameView.getText().toString());
                        Navigation.findNavController(v).navigate(R.id.action_my_profile_edit_fragment_to_my_profile_fragment,bundle);
                        //==========================================


                        mUser.setUsername(mUsernameView.getText().toString());

//                        updateUserPassword();
                        setUserInformation();
                        //updateUserInFirebase(mUser); //add user to firebase
                    }

                }
            }
        });

    }

    private void setUserInformation() {
        UserInfo mUserInfo = mUser.getUserInfo();

        if (mFirstNameView.getText() != null) {
            mUserInfo.setFirstName(mFirstNameView.getText().toString());
            mFirebaseAnalytics.setUserProperty("first_name", mUserInfo.getFirstName());
        }
        if (mLastNameView.getText() != null) {
            mUserInfo.setLastName(mLastNameView.getText().toString());
            mFirebaseAnalytics.setUserProperty("last_name", mUserInfo.getLastName());
        }

        if (mWeightView.getText() != null) {
            mUserInfo.setWeight(mUser.getUserInfo().getWeight());
        }
        if (mHeightView.getText() != null) {
            mUserInfo.setHeight(mUser.getUserInfo().getHeight());
        }
        if (mGender.getCheckedRadioButtonId() == mMale.getId()) {
            mUserInfo.setGender(0);
            mFirebaseAnalytics.setUserProperty("gender", "male");
        } else if (mGender.getCheckedRadioButtonId() == mFemale.getId())
            mUserInfo.setGender(1);
        mFirebaseAnalytics.setUserProperty("gender", "female");
        if (dateOpened) {
            mUserInfo.setBirthDate(mDate);
            mFirebaseAnalytics.setUserProperty("birth_date", mUserInfo.getBirthDate().toString());
        }

    }

    private void updateUserPassword() {
        if (mFirebaseUser != null) {
            mFirebaseUser.updatePassword(mPasswordView.getText().toString())
                    .addOnCompleteListener(this.getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Success");
                            } else {
                                Log.d(TAG, "Unsuccess");

                            }
                        }
                    });
        }
    }

    private void updateUserInFirebase(User user) {

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
                                if(PermissionChecker.hasCameraPermission(getContext())) {
                                    takePhotoFromCamera();
                                }
                                else{
                                    requestCameraPermission();
                                }
                                break;
                        }
                    }

                });
        pictureDialog.show();
    }


    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Uri chosenPhotoURI = galleryIntent.getData();
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), contentURI);
//            String path = saveImage(bitmap);
//        Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
//        //mAvatarView.setImageBitmap(bitmap);
//        Glide.with(getContext())
//                .load(contentURI) //path
//                .into(mAvatarView);
        loadSelectedImage(chosenPhotoURI);
        mUser.getUserInfo().setAvatar(chosenPhotoURI);
    }


    private void takePhotoFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


        Uri takenPhotoURI = (Uri)cameraIntent.getExtras().get("data");
        //Bitmap thumbnail = (Bitmap) cameraIntent.getExtras().get("data");
        loadSelectedImage(takenPhotoURI);
        mUser.getUserInfo().setAvatar(takenPhotoURI);
        //TODO save this image in firebase
        Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();

    }



    private void loadSelectedImage(Uri uri) { //call this method when


        Glide.with(this)
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //showMessage(getString(R.string.message_try_again));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        uploadImageToFirebase(resource);
                        return false;
                    }
                })
                .into(mAvatarView);
    }

    private void uploadImageToFirebase(Drawable resource) {
        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = DBUtil.getRefAvatars().putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                showMessage(getString(R.string.message_try_again));
//                switchToPhotoMessageView(false);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //switchToPhotoMessageView(false);
                Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                if (null != downloadUrl) {
                    DBUtil.addAvatarToFirebase(downloadUrl.toString());
                } else {
                    //showMessage(getString(R.string.message_try_again));
                }
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


    private Date mDate;
    private int year, month, dayOfMonth;
    private boolean dateOpened;

    public void DateDialog() {

        final Calendar calendar = Calendar.getInstance();


        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        calendar.set(Calendar.DAY_OF_MONTH, d);
                        calendar.set(Calendar.MONTH, m);
                        calendar.set(Calendar.YEAR, y);
                        mDate.setTime(calendar.getTimeInMillis());
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        dateOpened = true;
    }


}
