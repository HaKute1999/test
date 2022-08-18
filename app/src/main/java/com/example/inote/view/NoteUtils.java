package com.example.inote.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.widget.EditText;
import android.widget.SeekBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class NoteUtils {
    public static byte[] changeUriToByte(Context context,Uri uri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        byte[] bArr = new byte[4096];


        try {
            try {
                inputStream =context.getContentResolver().openInputStream(uri);
                while (true) {
                    int read = inputStream.read(bArr);
                    if (-1 == read) {
                        return byteArrayOutputStream.toByteArray();
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
            } catch (FileNotFoundException unused) {

            } catch (IOException e2) {
                if (e2.getMessage() == null || (!e2.getMessage().contains("EACCES") && !e2.getMessage().contains("Permission denied") && !e2.getMessage().contains("ENOENT"))) {
                    throw new RuntimeException(e2);
                }
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception unused) {
                }
            }

            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception unused) {
                }
            }
        }
        return bArr;
    };
    public static ImageSpanView getImage(EditText editText,Bitmap bitmap) {
        int width = bitmap.getWidth();
        double width2 = editText.getWidth();
        Double.isNaN(width2);
        if (width > ((int) (width2 * 0.9d))) {
            double width3 = editText.getWidth();
            Double.isNaN(width3);
            int i2 = (int) (width3 * 0.9d);
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i2, (bitmap.getHeight() * i2) / bitmap.getWidth(), false);
            bitmap.recycle();
            bitmap = createScaledBitmap;
        }
        return new ImageSpanView( bitmap, editText.getContext());
    }




}
