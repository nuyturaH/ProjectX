package com.har8yun.homeworks.projectx.util;

import android.view.View;

import androidx.navigation.Navigation;

public final class NavigationHelper {

    public static void onClickNavigate(View view, final int actionId) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Navigation.findNavController(v).navigate(actionId);



            }
        });
    }

}
