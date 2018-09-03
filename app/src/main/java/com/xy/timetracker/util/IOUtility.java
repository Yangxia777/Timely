package com.xy.timetracker.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xy.timetracker.model.TrackerElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * utility functions deal with I/O
 */
public class IOUtility {
    public static final String TAG = "IOUtility";

    public static void saveTrackersToInternal(Context context, String fileName, String content) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Logger.getLogger(TAG).log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<TrackerElement> restoreTrackersFromInternal(Context context, String fileName) {
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(new File(context.getFilesDir(), fileName)));
            Type listType = new TypeToken<ArrayList<TrackerElement>>() {
            }.getType();
            return gson.fromJson(br, listType);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
