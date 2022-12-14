package com.example.inote.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.ImageViewCompat;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.CheckItem;
import com.example.inote.models.Note;
import com.example.inote.models.NoteStyle;
import com.example.inote.ui.MainActivity;
import com.example.inote.view.drawingview.ICopy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ConfigUtils {
    public final static List<String> listImageCache = new ArrayList<>();
    public final static List<String> listValueCache = new ArrayList<>();
    public final static List<CheckItem> listCheckList = new ArrayList<>();
    public final static NoteStyle noteStyleCustom = new NoteStyle(false, false, false, false, false, false, false, 0);
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
    private static String getImageUrlWithAuthority(Context context, Uri uri)
    {
        InputStream is = null;

        if (uri.getAuthority() != null)
        {
            try
            {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (is != null)
                    {
                        is.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
    public static File createImageFile(Context context,ICopy iUpdate) throws IOException {
        // Create an image file name
        String path = context.getExternalFilesDir(null).getAbsolutePath()+"/" + UUID.randomUUID() + ".png";
        File imageFile = new File(path);
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        imageFile.getAbsolutePath();
        iUpdate.onProgress(imageFile.getAbsolutePath());

        return imageFile;
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
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public static Bitmap getBitmapFromView(View view) {
        // Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        // Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            // does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        // return the bitmap
        return returnedBitmap;
    }
    public static void share_bitMap_to_Apps(View view,Context context) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File imageFile = null;
        try {
            String path = context.getExternalFilesDir(null).getAbsolutePath() + "/temp.png";
            imageFile = new File(path);
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ConfigUtils.storeBitmap(imageFile, ConfigUtils.getBitmapFromView(view));
        }
        catch (Exception e) {}
        Intent i = new Intent(Intent.ACTION_SEND);
        Uri imageUri = FileProvider.getUriForFile(
                context,
                "com.example.inote.provider", //(use your app signature + ".provider" )
                imageFile);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM,imageUri);
        try {
            context.startActivity(Intent.createChooser(i, "My Share  ..."));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    public static final  <T> void getConFigDark(Context context,T... arr) {

        for (T v : arr) {

                    if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) {

                        if (v instanceof EditText){
                            ((EditText) v).setBackground(context.getDrawable(R.drawable.background_edt_dialog_drak));
                            ((EditText) v).setTextColor(Color.WHITE);
                            ((EditText) v).setHintTextColor(Color.parseColor("#959595"));
                        }else if (v instanceof TextView){
                            ((TextView) v).setTextColor(Color.WHITE);
                        }else  if (v instanceof RelativeLayout ){
                            ((RelativeLayout) v).setBackgroundResource(R.drawable.background_white_radius_dark);

                        }else  if (v instanceof LinearLayout){
                            ((LinearLayout) v).setBackgroundResource(R.drawable.background_white_radius_dark);
                        }else {
                            ((View) v).setBackgroundResource(R.color.md_grey_600_dark);
                        }
                    }
//                    else {
//                        if (v instanceof EditText){
//                            ((EditText) v).setBackground(context.getDrawable(R.drawable.background_edt_dialog));
//                            ((EditText) v).setTextColor(Color.BLACK);
//                            ((EditText) v).setHintTextColor(Color.parseColor("#959595"));
//
//                        }else if (v instanceof TextView){
//                            ((TextView) v).setTextColor(Color.BLACK);
//                        }else  if (v instanceof RelativeLayout ){
//                            ((RelativeLayout) v).setBackgroundResource(R.drawable.background_white_radius);
//                        }else  if (v instanceof LinearLayout){
//                            ((LinearLayout) v).setBackgroundResource(R.drawable.background_white_radius);
//                        }else {
//                            ((View) v).setBackgroundResource(R.color.md_grey_300_dark);
//                        }
//                    }


    }


    }
    @SuppressLint("ResourceType")
    public static final  <T> void getConFigDark1(Context context, T... arr) {
        for (T v : arr) {

            if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) {
                if (v instanceof EditText) {
                    ((EditText) v).setTextColor(Color.WHITE);
                    ((EditText) v).setHintTextColor(Color.parseColor("#959595"));
                } else if (v instanceof TextView) {
                    ((TextView) v).setTextColor(Color.WHITE);
                } else if (v instanceof RelativeLayout) {
                        ((RelativeLayout) v).setBackground(context.getResources().getDrawable(R.drawable.background_white_radius_dark2));
                } else if (v instanceof LinearLayout) {
                    ((LinearLayout) v).setBackgroundResource(R.drawable.background_white_radius_top_dark);
                } else {
                    ((View) v).setBackgroundResource(R.color.md_grey_600);
                }
            }

        }

        }
        public static void darkRelativeRadius(View v){
            if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((RelativeLayout) v).setBackgroundResource(R.drawable.background_white_radius_dark2);
        }
    public static void darkRelativeGray(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((RelativeLayout) v).setBackgroundResource(R.drawable.background_header_light);
    }
    public static void darkRelativeTop(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((RelativeLayout) v).setBackgroundResource(R.drawable.background_white_radius_top_dark);

    }
    public static void darkRelativeBottom(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((RelativeLayout) v).setBackgroundResource(R.drawable.background_white_radius_bottom_click);

    }
    public static void darkBlack(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((RelativeLayout) v).setBackgroundResource(R.color.black);

    }

    public static void darkLinearLayoutRadius(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((LinearLayout) v).setBackgroundResource(R.drawable.background_white_radius_dark2);
    }
    public static void darkTextViewRadius(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((TextView) v).setBackgroundResource(R.drawable.background_white_radius_dark2);
    }
    public static void darkLinearLayoutGray(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((LinearLayout) v).setBackgroundResource(R.drawable.background_header_light);
    }
    public static void darkLinearLayoutTop(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true)((LinearLayout) v).setBackgroundResource(R.drawable.background_white_radius_top_dark);

    }
    public static void darkLinearLayoutBottom(View v){
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true) ((LinearLayout) v).setBackgroundResource(R.drawable.background_white_radius_bottom_click);

    }
    public static void darkImage(Context context,View v){

            if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) == true){
                if (v instanceof  ImageView){
                    ((ImageView) v).setColorFilter(ContextCompat.getColor(context, R.color.md_grey_400), PorterDuff.Mode.SRC_IN);

                }else v.setBackgroundColor(Color.BLACK);

        }

    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    public static String getMimeType(Context context, Uri uri) {
        String extension;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        String base64 = "data:image/"+extension+";base64,"+"";

        return base64;
    }
    public static List<Note> convertImageBase64(){
        List<Note> notes = new ArrayList<>();
        List<Note> list =  AppDatabase.noteDB.getNoteDAO().getAllNotes();
        for (Note note1:list){

            note1.setListImage(getListImage(note1));
            notes.add(note1);
        }
        return notes;


    }
    private static  List<String> getListImage(Note note){
        List<String> listImage =  new ArrayList<>();

        for (String image : note.getListImage()){
            if (image.contains("storage")){
                Bitmap bm = BitmapFactory.decodeFile(image);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                listImage.add(encodedImage);
            }else {
                listImage.add(image);
            }

        }
        return listImage;
    }
    public static void convertBase64toImage(ImageView image, String imageString){
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        image.setImageBitmap(decodedImage);

    }
    public static Bitmap convertBase64toImage1( String imageString){
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
       return decodedImage;
    }
    private static  boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            System.out.println("Storage is writeable");
            return true;
        }
        else{
            return false;
        }
    }
    public static void wirteFile(Context context,String result,IUpdate update){
        if (isExternalStorageWritable()) {
            File file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
            if (!file.exists()) {
                file.mkdirs();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("/BackUpINoteIos_");
            sb.append(System.currentTimeMillis());
            sb.append(".json");
            File child = new File(file,sb.toString());

            try {
                FileWriter writer = new FileWriter(child,true);
                writer.append(result);
                writer.flush();
                writer.close();
                update.onFinish();
                Toast.makeText(context, context.getResources().getString(R.string.exporting_successful), Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(context, context.getResources().getString(R.string.exporting_failed), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

        }
    }
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }
    public static void checkTextRight(boolean check, TextView textView) {

                if (check) {
                    textView.setBackgroundResource(R.drawable.bg_radius_end_yellow);
                    textView.setTextColor(Color.WHITE);
                } else if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundResource(R.drawable.bg_radius_end_while2);
                } else {
                    textView.setBackgroundResource(R.drawable.bg_radius_end_while);
                    textView.setTextColor(Color.BLACK);
                }

    }
    public static void checkTextLeft(boolean check, TextView textView) {

                if (check) {
                    textView.setBackgroundResource(R.drawable.bg_radius_start_yellow);
                    textView.setTextColor(Color.WHITE);
                } else if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundResource(R.drawable.bg_radius_start_while2);
                } else {
                    textView.setBackgroundResource(R.drawable.bg_radius_start_while);
                    textView.setTextColor(Color.BLACK);
                }

    }
    public static void checkText(boolean check, TextView textView) {

                if (check) {
                    textView.setBackgroundColor(Color.parseColor("#E4B645"));
                    textView.setTextColor(Color.WHITE);
                } else if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundColor(Color.parseColor("#2c2c2e"));
                } else {
                    textView.setBackgroundColor(Color.parseColor("#f2f1f6"));
                    textView.setTextColor(Color.BLACK);
                }


    }
    public static void getStyleTitle(NoteStyle noteStyle, TextView textView){
        SpannableString spanString = new SpannableString(textView.getText().toString());
        if (noteStyle.isCheckITitle()){
            spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
        } if (noteStyle.isCheckUTitle()){
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        } if (noteStyle.isCheckSTitle()){
            spanString.setSpan(new StrikethroughSpan(), 0, spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);

        textView.setText(spanString);
    }
    public static void getStyleContent(NoteStyle noteStyle, TextView textView){
        SpannableString spanString = new SpannableString(textView.getText().toString());
        if (noteStyle.isCheckIContent()){
            spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
        }
        if (noteStyle.isCheckUContent()){
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        }
        if (noteStyle.isCheckSContent()){
            spanString.setSpan(new StrikethroughSpan(), 0, spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (noteStyle.isCheckBContent()){
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        }
        textView.setText(spanString);
    }
    public static void getStyleGravity(NoteStyle noteStyle, TextView textView){
        if (noteStyle.getGravityNote()==0){
            textView.setGravity(Gravity.LEFT);
            return;
        }else  if (noteStyle.getGravityNote()==1){
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            return;

        }else  if (noteStyle.getGravityNote()==2){
            textView.setGravity(Gravity.RIGHT);
            return;
        }
            textView.setGravity(Gravity.LEFT);

    }


}
