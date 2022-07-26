package com.example.inote.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;

import com.example.inote.models.CheckItem;
import com.example.inote.view.drawingview.ICopy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ConfigUtils {
    public final static List<String> listImageCache = new ArrayList<>();
    public final static List<CheckItem> listCheckList = new ArrayList<>();
    public static void hideKeyboard(Activity activity) {

        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
        }

    }
    public static void displashKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }
    public static String formatDateTIme(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        Date resultdate = new Date(time);
        return sdf.format(resultdate);
    }
    public static String  copyFile(Context context, File sourceFilePath, ICopy iUpdate) {
        String path = context.getExternalFilesDir(null).getAbsolutePath()+"/" + UUID.randomUUID() + ".png";
        File imageFile = new File(path);
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{

            if (!sourceFilePath.exists()) {
                return null;
            }

            FileChannel source = null;
            FileChannel destination = null;
            source = new FileInputStream(sourceFilePath).getChannel();
            destination = new FileOutputStream(imageFile).getChannel();
            if (destination != null && source != null) {
                destination.transferFrom(source, 0, source.size());
            }
            iUpdate.onProgress(path);
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return path;

    }
    public static void storeBitmap(File file, Bitmap bitmap) throws Exception {
        if (!file.exists() && !file.createNewFile())
            throw new Exception("Not able to create " + file.getPath());
        FileOutputStream stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.flush();
        stream.close();

    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
