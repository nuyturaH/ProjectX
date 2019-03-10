package com.har8yun.homeworks.projectx.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;

public class EventInformationDialog extends Dialog {

    public TextView mTitleView;
    public TextView mDescriptionView;
    public TextView mDateLocationView;
    public Button mGoingButton;
    public Button mCancelButton;
    public ImageView mEditEventView;

    EventInformationDialog.OnEventInformationActionListener mOnEventInformationActionListener;

    public EventInformationDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_event_information);

        mTitleView = findViewById(R.id.tv_title_dialog);
        mDescriptionView = findViewById(R.id.tv_description_dialog);
        mDateLocationView = findViewById(R.id.tv_date_location_dialog);
        mGoingButton = findViewById(R.id.btn_going_dialog);
        mCancelButton = findViewById(R.id.btn_cancel_dialog);
        mEditEventView = findViewById(R.id.iv_to_change_event_dialog);

    }

    public void setOnEventInformationActionListener(EventInformationDialog.OnEventInformationActionListener listener) {
        mOnEventInformationActionListener = listener;
    }


    public interface OnEventInformationActionListener {
        void onGoingClickedListener();
    }
}
