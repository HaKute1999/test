package com.example.inote.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.style.ImageSpan;

public class ImageSpanView extends ImageSpan {
    private long a;
    private boolean check;
    int width;
    int height;

    /* renamed from: e  reason: collision with root package name */
    Bitmap bitmap;

    public ImageSpanView(long j2, int width, int height, Context context) {
        super(context, create(width, height));
        this.a = j2;
        this.bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        this.width = width;
        this.height = height;
        this.check = true;
    }

    private static Bitmap create(int width, int height) {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public long b() {
        return this.a;
    }

    public boolean isCheck() {
        return this.check;
    }

    public void clear() {
        if (!this.bitmap.isRecycled()) {
            this.bitmap.recycle();
        }
        this.check = true;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public ImageSpanView( Bitmap bitmap, Context context) {
        super(context, bitmap);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.bitmap = bitmap;
        this.check = false;
    }
}