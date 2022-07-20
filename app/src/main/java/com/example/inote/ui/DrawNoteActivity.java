package com.example.inote.ui;

import static java.util.UUID.randomUUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.drawingview.BrushView;
import com.example.inote.view.drawingview.DrawingView;
import com.example.inote.view.drawingview.brushes.BrushSettings;
import com.example.inote.view.drawingview.brushes.Brushes;
import com.panshen.gridcolorpicker.OnColorSelectListener;
import com.panshen.gridcolorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DrawNoteActivity extends BaseActivity {

    ConstraintLayout boxPencil,mainDraw,boxMarker,boxErase;
    AppCompatSeekBar size_seek_bar,seekBarMarker,seekBarErase;
    ImageView ivChooseColor,ivMarker,ivPencil,ivErase,arrowPencil,arrowMarker,arrowErase;
    private DrawingView mDrawingView;
    private SeekBar mSizeSeekBar;
    private ImageView mUndoButton;
    private ImageView mRedoButton;
    int color  = Color.YELLOW;
    BrushView brushView,markerPreview,erasePreview;
    TextView tvDone,tvBackPain;
    int idNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_note);
        initVIew();
        onBack();
        Intent intent = getIntent();
        idNote = intent.getIntExtra("idNote",0);
        mDrawingView.setUndoAndRedoEnable(true);
//        mDrawingView.enterZoomMode();
        brushView.setDrawingView(mDrawingView);
        markerPreview.setDrawingView(mDrawingView);
        erasePreview.setDrawingView(mDrawingView);
        BrushSettings brushSettings = mDrawingView.getBrushSettings();
        brushSettings.setSelectedBrushSize(10/100f);
        size_seek_bar.setProgress(10);
        size_seek_bar.setMax(100);
        brushSettings.setColor(getResources().getColor(R.color.main_color));
        size_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                brushSettings.setSelectedBrushSize(i/100f);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seekBarMarker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                brushSettings.setSelectedBrushSize(i/100f);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seekBarErase.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                brushSettings.setSelectedBrushSize(i/100f);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
         setupUndoAndRedo();
        ivChooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog1 = new ColorPickerDialogBuilder(new ContextThemeWrapper(DrawNoteActivity.this, com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> {
                            ivChooseColor.setImageTintList(ColorStateList.valueOf(color));
                            brushSettings.setColor(color);

                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {})
                        .alphaViewEnable(true)
                        .setOnColorSelectListener(new OnColorSelectListener() {
                            @Override
                            public void onColorChanged(@NonNull String s) {
                            }
                            @Override
                            public void afterColorChanged(@NonNull String s) {
                                color = Color.parseColor(s);

                            }
                        })
                        .show();


            }
        });
        mDrawingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boxErase.setVisibility(View.GONE);
                boxMarker.setVisibility(View.GONE);
                boxPencil.setVisibility(View.GONE);
                arrowErase.setVisibility(View.GONE);
                arrowMarker.setVisibility(View.GONE);
                arrowPencil.setVisibility(View.GONE);
                return false;
            }
        });

        ivPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPercent(ivPencil,0.09F);
                setPercent(ivErase,0.07F);
                setPercent(ivMarker,0.07F);
                boxPencil.setVisibility(View.VISIBLE);
                boxErase.setVisibility(View.GONE);
                boxMarker.setVisibility(View.GONE);
                arrowPencil.setVisibility(View.VISIBLE);
                arrowMarker.setVisibility(View.GONE);
                arrowErase.setVisibility(View.GONE);
                setBrushSelected(Brushes.PEN);

            }
        });
        ivMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPercent(ivMarker,0.09F);
                setPercent(ivErase,0.07F);
                setPercent(ivPencil,0.07F);
                boxPencil.setVisibility(View.GONE);
                boxErase.setVisibility(View.GONE);
                boxMarker.setVisibility(View.VISIBLE);
                arrowMarker.setVisibility(View.VISIBLE);
                arrowErase.setVisibility(View.GONE);
                arrowPencil.setVisibility(View.GONE);
                setBrushSelected(Brushes.CALLIGRAPHY);
            }
        });
        ivErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPercent(ivErase,0.09F);
                setPercent(ivPencil,0.07F);
                setPercent(ivMarker,0.07F);
                boxPencil.setVisibility(View.GONE);
                boxErase.setVisibility(View.VISIBLE);
                boxMarker.setVisibility(View.GONE);
                arrowPencil.setVisibility(View.GONE);
                arrowMarker.setVisibility(View.GONE);
                arrowErase.setVisibility(View.VISIBLE);
                setBrushSelected(Brushes.ERASER);
            }
        });
    }
    private void setPercent(View view,float value){
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        lp.matchConstraintPercentHeight = value;
        view.setLayoutParams(lp);
    }
    private void setBrushSelected(int brushID){
        BrushSettings settings = mDrawingView.getBrushSettings();
        settings.setSelectedBrush(brushID);
//        int sizeInPercentage = (int) (settings.getSelectedBrushSize() * 100);
//        mSizeSeekBar.setProgress(sizeInPercentage);
    }
    private void setupUndoAndRedo() {
        mUndoButton = findViewById(R.id.ivUndo);
        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawingView.undo();
                mUndoButton.setEnabled(!mDrawingView.isUndoStackEmpty());
                mRedoButton.setEnabled(!mDrawingView.isRedoStackEmpty());
            }
        });

        mRedoButton = findViewById(R.id.ivRedo);
        mRedoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawingView.redo();
                mUndoButton.setEnabled(!mDrawingView.isUndoStackEmpty());
                mRedoButton.setEnabled(!mDrawingView.isRedoStackEmpty());
            }
        });

        mDrawingView.setOnDrawListener(new DrawingView.OnDrawListener() {
            @Override
            public void onDraw() {
                mUndoButton.setEnabled(true);
                mRedoButton.setEnabled(false);
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(DrawNoteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(DrawNoteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);//ignoring the request code
                    return;
                }
                Bitmap bitmap = mDrawingView.exportDrawing();
                exportImage(bitmap);
                onBackPressed();
            }
        });
    }
    private void initVIew(){
        boxPencil = findViewById(R.id.boxPencil);
        boxMarker = findViewById(R.id.boxMarker);
        boxErase = findViewById(R.id.boxErase);
        mainDraw = findViewById(R.id.mainDraw);
        size_seek_bar = findViewById(R.id.size_seek_bar);
        seekBarMarker = findViewById(R.id.seekBarMarker);
        seekBarErase = findViewById(R.id.seekBarErase);
        ivChooseColor = findViewById(R.id.ivChooseColor);
        ivMarker = findViewById(R.id.ivMarker);
        ivPencil = findViewById(R.id.ivPencil);
        ivErase = findViewById(R.id.ivErase);
        brushView = findViewById(R.id.brush_view);
        markerPreview = findViewById(R.id.markerPreview);
        erasePreview = findViewById(R.id.erasePreview);
        arrowErase = findViewById(R.id.arrowErase);
        arrowMarker = findViewById(R.id.arrowMarker);
        arrowPencil = findViewById(R.id.arrowPencil);
        tvDone = findViewById(R.id.tvDone);

        mDrawingView = findViewById(R.id.drawing_view);
    }
    private void exportImage(Bitmap bitmap) {
        String path = getApplicationContext().getExternalFilesDir(null).getAbsolutePath()+"/" +UUID.randomUUID() + ".png";
        File imageFile = new File(path);
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ConfigUtils.storeBitmap(imageFile, bitmap);
            if (idNote != 0){
                List<String> listImage = AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getListImage();
                listImage.add(path);
                AppDatabase.noteDB.getNoteDAO().updateListImage(listImage,idNote);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        MediaScannerConnection.scanFile(
//                this,
//                new String[]{},
//                new String[]{"image/png"},
//                null);
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
    }

}