package com.har8yun.homeworks.projectx.util;

import android.os.Environment;

import com.har8yun.homeworks.projectx.application.Application;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;



public class ImageLoadingUtil {

    private static final String IMAGE_NAME_PRF = "Sport_";
    private static final String IMAGE_FORMAT = "png";

    private static String generateFileName(String extension) {
        Calendar c = Calendar.getInstance();
        return IMAGE_NAME_PRF + Integer.toString(c.get(Calendar.MONTH)) +
                Integer.toString(c.get(Calendar.DAY_OF_MONTH)) +
                Integer.toString(c.get(Calendar.YEAR)) +
                Integer.toString(c.get(Calendar.HOUR_OF_DAY)) +
                Integer.toString(c.get(Calendar.MINUTE)) +
                Integer.toString(c.get(Calendar.SECOND)) +
                Integer.toString(c.get(Calendar.MILLISECOND)) + "." + extension;
    }

    public static File createImageFile() throws IOException {
        File storageDir = Application.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(generateFileName(IMAGE_FORMAT) + "_", ".", storageDir);
    }
}