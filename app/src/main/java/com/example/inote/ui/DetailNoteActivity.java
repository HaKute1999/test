package com.example.inote.ui;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.inote.R;
import com.example.inote.adapter.CheckListAdapter;
import com.example.inote.adapter.CheckListIosAdapter;
import com.example.inote.adapter.ImageNoteAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.DetailNote;
import com.example.inote.models.NoteStyle;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.CustomEditText;
import com.example.inote.view.ImageSpanView;
import com.example.inote.view.NoteUtils;
import com.example.inote.view.ShareUtils;
import com.example.inote.view.drawingview.ICopy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetailNoteActivity extends BaseActivity implements  ICopy {
    RelativeLayout ivMore,rl_Pin ,rl_delete,rl_lock,rl_align,rl_size;
    RelativeLayout menuChooserContainer, layoutLock, imageChooserContainer, rl_sharenote,rl_search,search_root,main_note,rl_bottom,rl_top;
    View viewBackground;
    EditText edtTitle, search_query;
        CustomEditText text_note_view;
    TextView tvTime, tvViewNote, tvChoosePhoto, tvTakePhoto, tvDone,tvWordCount,tvSize,tvPin,tvLockNote;
    int idNote;
    int idFolder;
    ImageView ivDraw,iv_text, ivPhoto, ivCreate, ivChecklist,search_previous,search_next,search_clear,imgPin;
    ImageNoteAdapter imageNoteAdapter;
    CheckListAdapter checkListAdapter;
    CheckListIosAdapter checkListIosAdapter;
    List<String> listImage;
    NestedScrollView nestedScrollView;
    ImageView close_bottom;
    NoteStyle noteStyleCustom = ConfigUtils.noteStyleCustom;
    SpannableString spanString = new SpannableString("");
//    Map<CustomEditText, C7750c1> f23363K0;
    List<CustomEditText> list;
    Collection<ImageSpanView> spanViews;
    Collection<StyleSpan> styleBody;
    Collection<StyleSpan> styleItalic;
    Collection<UnderlineSpan> styleUnder;
    Collection<StrikethroughSpan> styleStrike;
    CustomEditText.selectChanged selectChanged;
    TextWatcher textWatcher;
    NestedScrollView scrollView;
    List<DetailNote> detailNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();

        setContentView(R.layout.activity_detail_note);
        onBack();
        initView();
        Intent intent = getIntent();
        idNote = intent.getIntExtra("idNote", 0);
        idFolder = intent.getIntExtra("idFolder", 0);
        ConfigUtils.listCheckList.clear();
        ConfigUtils.listValueCache.clear();
        spanViews = new ArrayList<>();
        detailNotes = new ArrayList<>();
        new ShareUtils(this);

        if (idNote != 0) {
            noteStyleCustom = AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getNoteStyle();
            ConfigUtils.listValueCache.addAll(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValue());

            if (AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getProtectionType() == 1) {
                tvLockNote.setText(getResources().getString(R.string.unlocks));
                layoutLock.setVisibility(View.VISIBLE);
            }else {
                tvLockNote.setText(getResources().getString(R.string.locks));

            }
            if (AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).isPinned()) {
                tvPin.setText(getResources().getString(R.string.unpin));
            }else {
                tvPin.setText(getResources().getString(R.string.pins));

            }
            edtTitle.setText(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getTitle());
            text_note_view.setText(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValue().get(0));

            tvTime.setText(ConfigUtils.formatDateTIme(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getTimeEdit()));
            ConfigUtils.getStyleTitle(noteStyleCustom,edtTitle);
            ConfigUtils.getStyleContent(noteStyleCustom,
                    text_note_view);

            ConfigUtils.getStyleGravity(noteStyleCustom,edtTitle);
            ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view);

        } else {
            ConfigUtils.listValueCache.add(0,"");
            ConfigUtils.listValueCache.add(1,"");
            ConfigUtils.listValueCache.add(2,"");

            tvTime.setVisibility(View.GONE);
        }
        findViewById(R.id.tvBack).setOnClickListener(view -> onBackPressed());

        selectChanged = new OnChangeState();
        text_note_view.setOnSelectionChangedListener(selectChanged);
//        text_note_view.addTextChangedListener(textWatcher);

        ConfigUtils.getConFigDark1(getApplicationContext(),edtTitle,text_note_view,tvTime,ids(R.id.tvAlign1),ids(R.id.tvAlign));
        ConfigUtils.getConFigDark1(getApplicationContext(),ids(R.id.viewOption),ids(R.id.viewOption1),ids(R.id.viewOption2),ids(R.id.tvChoosePhoto),ids(R.id.tvTakePhoto),ids(R.id.tvCancelPhoto));
        ConfigUtils.getConFigDark1(getApplicationContext(),ids(R.id.tv_share),ids(R.id.tvSearch),ids(R.id.tvCount),ids(R.id.tvSize1),ids(R.id.tvWordCount),ids(R.id.tvSize),ids(R.id.tvNoti),ids(R.id.tvForgotPass));
        ConfigUtils.darkLinearLayoutTop(ids(R.id.ln_option));
        ConfigUtils.darkLinearLayoutRadius(ids(R.id.ln_option2));
        ConfigUtils.darkLinearLayoutRadius(ids(R.id.ll_camera));
        ConfigUtils.darkTextViewRadius(ids(R.id.tvCancelPhoto));
        ConfigUtils.darkRelativeRadius(ids(R.id.rl_pin));
        ConfigUtils.darkRelativeRadius(ids(R.id.rl_delete));
        ConfigUtils.darkRelativeRadius(ids(R.id.rl_lock));
        ConfigUtils.darkBlack(rl_bottom);
        ConfigUtils.darkBlack(rl_top);
        ConfigUtils.darkImage(getApplicationContext(),ids(R.id.viewLock));
        ConfigUtils.darkImage(getApplicationContext(),ids(R.id.viewLock2));
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.hideKeyboard(DetailNoteActivity.this);
                viewBackground.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInUp).duration(100L).playOn(viewBackground);
                YoYo.with(Techniques.SlideInDown).duration(200L).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public final void call(Animator animator) {
                        imageChooserContainer.setVisibility(View.VISIBLE);
                    }
                }).playOn(findViewById(R.id.imageChooserContainer));
            }
        });
        tvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permission(DetailNoteActivity.this)) {
                    viewBackground.setVisibility(View.GONE);
                    imageChooserContainer.setVisibility(View.GONE);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Image"),
                            PICK_IMAGE);
                }

            }
        });
        ivChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable myIcon = getApplicationContext().getResources().getDrawable(R.drawable.circle_bg_note);
                myIcon.setBounds(0,0,60,60);

                int selectionStart = text_note_view.getSelectionStart();
                text_note_view.getText().insert(selectionStart, "\n");
                int start = selectionStart + 1;
                text_note_view.getText().insert(start, " ");
                int end = selectionStart + 2;
                text_note_view.getText().setSpan(new ImageSpan(myIcon, ImageSpan.ALIGN_CENTER), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                text_note_view.getText().insert(end, "   ");


            }
        });
//        String t = ShareUtils.getStr("1234567", "");
        String t = "/storage/emulated/0/Android/data/com.example.inote/files/b1c88aca-246f-47a7-ae7f-7ef4d0d6f305.png";
        File file = new File(t);
        String imagePath  = t;
        String html = ShareUtils.getStr("aaaa", "");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
            text_note_view.setText(Html.fromHtml(ShareUtils.getStr("aaaa", ""),new ImageGetter(),null));
        }else{

        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 2;



    private void initView() {
        ivMore = findViewById(R.id.ivMore);
        ivPhoto = findViewById(R.id.ivPhoto);
        ivCreate = findViewById(R.id.ivCreate);
        ivChecklist = findViewById(R.id.ivChecklist);
        menuChooserContainer = findViewById(R.id.menuChooserContainer);
        imageChooserContainer = findViewById(R.id.imageChooserContainer);
        layoutLock = findViewById(R.id.layoutLock);
        viewBackground = findViewById(R.id.viewBackground);
        edtTitle = findViewById(R.id.edtTitle);
        text_note_view = findViewById(R.id.text_note_view);

        tvViewNote = findViewById(R.id.tvViewNote);
        tvTime = findViewById(R.id.tvTime);
        ivDraw = findViewById(R.id.ivDraw);
        iv_text = findViewById(R.id.iv_text);
        tvChoosePhoto = findViewById(R.id.tvChoosePhoto);
        tvTakePhoto = findViewById(R.id.tvTakePhoto);

        tvDone = findViewById(R.id.tvDone);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        rl_sharenote = findViewById(R.id.rl_sharenote);
        rl_search = findViewById(R.id.rl_search);
        tvWordCount = findViewById(R.id.tvWordCount);
        tvSize = findViewById(R.id.tvSize);
        search_root = findViewById(R.id.search_root);
        search_clear = findViewById(R.id.search_clear);
        search_next = findViewById(R.id.search_next);
        search_previous = findViewById(R.id.search_previous);
        search_query = findViewById(R.id.search_query);
        main_note = findViewById(R.id.main_note);
        rl_bottom = findViewById(R.id.rl_bottom);
        rl_top = findViewById(R.id.rl_top);
        rl_Pin = findViewById(R.id.rl_pin);
        rl_delete = findViewById(R.id.rl_delete);
        rl_lock = findViewById(R.id.rl_lock);
        tvPin = findViewById(R.id.tvPin);
        tvLockNote = findViewById(R.id.tvLockNote);
        imgPin = findViewById(R.id.imgPin);
        rl_align = findViewById(R.id.rl_align);
        rl_size = findViewById(R.id.rl_size);
        scrollView = findViewById(R.id.scrollView);

        listImage = new ArrayList<>();

    }
    String target = "";
    int index=0;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                selectedImageUri = data.getData();
                String selectedImagePath;
                if (data.getData().toString().contains("photos")){
//
//
//                    selectedImagePath = selectedImageUri.getPath();
                    File photoFile = null;
                    try {
                        photoFile = ConfigUtils.createImageFile(getApplicationContext(),DetailNoteActivity.this);
                        InputStream is = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bmp = BitmapFactory.decodeStream(is);
                        ConfigUtils.storeBitmap(photoFile,bmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
                    File imageFile = new File(selectedImagePath);
                    String des = ConfigUtils.copyFile(getApplicationContext(), imageFile, this);

                    byte[] bytes = NoteUtils.changeUriToByte(getApplicationContext(),selectedImageUri);
                    Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap imageSpan = NoteUtils.getImage(text_note_view,decodeByteArray);
                    ImageSpanView imageSpanView = new ImageSpanView(des,imageSpan,getApplicationContext());
                    int selectionStart = this.text_note_view.getSelectionStart();
                    this.text_note_view.getText().insert(selectionStart, "\n");
                    int start = selectionStart + 1;
                    this.text_note_view.getText().insert(start, " ");
                    int end = selectionStart + 2;
                    this.text_note_view.getText().setSpan(imageSpanView, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    this.text_note_view.getText().insert(end, "\n");


                }
            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {

        }

    }

    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStypePan();
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) ==true){
            main_note.setBackgroundColor(Color.BLACK);
        }else  main_note.setBackgroundColor(getResources().getColor(R.color.color_main));
    }
   // update add image

    //update check lisst;

    private void showDialogCreatePassCode() {
        Context mContext = getApplicationContext();
        final Dialog dialog = new Dialog(DetailNoteActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_create_pass);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText edtPasscode = dialog.findViewById(R.id.edtPasscode);
        EditText edtRePasscode = dialog.findViewById(R.id.edtRePasscode);
        EditText edtQuestion = dialog.findViewById(R.id.edtQuestion);
        TextView tvOk = dialog.findViewById(R.id.tvOk);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        RelativeLayout root_dl = dialog.findViewById(R.id.root_dl);
        View view1_dl = dialog.findViewById(R.id.view1_dl);
        View view2_dl = dialog.findViewById(R.id.view2_dl);
        ConfigUtils.getConFigDark(mContext, root_dl,edtPasscode,edtRePasscode,edtQuestion,tvOk,tvCancel,view1_dl,view2_dl,tvTitle);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPasscode.getText().toString().length() < 6 && edtRePasscode.getText().toString().length() < 6) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.must_6), Toast.LENGTH_LONG).show();

                } else if (!edtPasscode.getText().toString().contains(edtRePasscode.getText().toString())) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.no_match), Toast.LENGTH_LONG).show();
                } else if (edtQuestion.getText().toString().length() == 0) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.must_empty), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.locks), Toast.LENGTH_LONG).show();
                    ShareUtils.setStr(ShareUtils.PASSCODE, edtPasscode.getText().toString());
                    tvLockNote.setText(getString(R.string.unlocks));

//                        data.get(getAdapterPosition()).setProtectionType(1);
//                    AppDatabase.noteDB.getNoteDAO().updateprotectionType(1, idNote);

                    dialog.dismiss();

                }

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    @Override
    public void onFinish(List<String> data) {

    }

    @Override
    public void onProgress(String path) {

    }

    private void getListItemSpanView(){
        this.spanViews = new ArrayList<>();
        for ( Object imageSpanView : text_note_view.getText().getSpans(0, text_note_view.getText().length(),DetailNoteActivity.class)){
        }
    }
    public void getStypePan() {

        int scrollY = this.scrollView.getScrollY();
        int height = this.scrollView.getHeight() + scrollY;
        int lineCount = text_note_view.getLineCount();
        StringBuilder sb=  new StringBuilder();
        sb.append(text_note_view.getText().toString());
//        for (int i5 = 0; i5 < lineCount; i5++) {
//           int start  =  text_note_view.getLayout().getLineStart(i5);
//           int end =  text_note_view.getLayout().getLineEnd(i5);
        int i3 = 0;
        CharacterStyle[] spanned = this.text_note_view.getText().getSpans(0, this.text_note_view.getText().length(), CharacterStyle.class);
        int i4 = 0;
        while (true){

            int nextSpanTransition = text_note_view.getText().nextSpanTransition(i3, text_note_view.getText().toString().length(), CharacterStyle.class);
            if (i3 ==nextSpanTransition){
                break;
            }
            sb.append(text_note_view.getText().toString().substring(i3,nextSpanTransition-1));
            if (i4 < spanned.length) {
                if (spanned[i4] instanceof ImageSpan){

                    String t;
                    if (spanned[i4] instanceof ImageSpanView){
                        t =  ((ImageSpanView) spanned[i4]).getId();
                    }else {
                        t = Uri.parse(((ImageSpan) spanned[i4]).getSource()).getPath();
                    }
                    sb.append("<img src=\"");
                    sb.append(t);
                    sb.append("\">");
                }
                i4++;
            }
            i3 = nextSpanTransition;
        }

//            for (Object obj : this.text_note_view.getText().getSpans(0, this.text_note_view.getText().length(), Object.class)) {
//
//            }

        String t= text_note_view.getText().toString();
        t.replaceAll("<br>","\n");
        ShareUtils.setStr("aaaa",sb.toString());

//            }
        Object[] spans;
        this.styleBody = new ArrayList();
        this.styleItalic = new ArrayList();
        this.styleUnder = new ArrayList();
        this.styleStrike = new ArrayList();
        for (Object obj : this.text_note_view.getText().getSpans(0, this.text_note_view.getText().toString().length(), Object.class)) {

            if (obj instanceof StyleSpan) {
                StyleSpan styleSpan = (StyleSpan) obj;
                if (styleSpan.getStyle() == Typeface.BOLD) {
                    this.styleBody.add(styleSpan);
                }
                if (styleSpan.getStyle() == Typeface.ITALIC) {
                    this.styleItalic.add(styleSpan);
                }
            } else if (obj instanceof UnderlineSpan) {
                this.styleUnder.add((UnderlineSpan) obj);
            } else if (obj instanceof StrikethroughSpan) {
                this.styleStrike.add((StrikethroughSpan) obj);
            }
        }
    }
    public  class OnChangeState implements CustomEditText.selectChanged{

        @Override
        public void onChange(int i2, int i3) {
        }
    }

    public void N() {
        getListItemSpanView();
        int scrollY = this.scrollView.getScrollY();
        int height = this.scrollView.getHeight() + scrollY;
        int lineCount = text_note_view.getLineCount();
        int i2 = -1;
        int i3 = -1;
        int i4 = -1;
        for (int i5 = 0; i5 < lineCount; i5++) {
            if (i3 == -1 && text_note_view.getLayout().getLineBottom(i5) >= scrollY) {
                i3 = text_note_view.getLayout().getLineStart(i5);
            }
            if (text_note_view.getLayout().getLineTop(i5) <= height) {
                i4 = text_note_view.getLayout().getLineEnd(i5);
            }
        }
       ImageSpanView[] oVarArr = (ImageSpanView[]) text_note_view.getText().getSpans(i3, i4, ImageSpanView.class);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
//        for (ImageSpanView oVar : spanViews) {
//                int length = oVarArr.length;
//                int i6 = 0;
//                while (true) {
//                    if (i6 >= length) {
//                        int spanStart = text_note_view.getText().getSpanStart(oVar);
//                        int spanEnd = text_note_view.getText().getSpanEnd(oVar);
//                        if (!(spanStart == i2 || spanEnd == i2)) {
//                            oVar.clear();
//                            ImageSpanView oVar2 = new ImageSpanView(oVar.b(), oVar.getWidth(), oVar.getHeight(), this);
//                            text_note_view.getText().setSpan(oVar2, spanStart, spanEnd, 33);
//                            arrayList2.add(oVar2);
//                            text_note_view.getText().removeSpan(oVar);
//                            arrayList.add(oVar);
//                            i2 = -1;
//                        }
//                    } else if (oVarArr[i6].b() == oVar.b()) {
//                        break;
//                    } else {
//                        i6++;
//                    }
//                }
//
//        }
        spanViews.addAll(arrayList2);
//        for (ImageSpanView oVar3 : oVarArr) {
//            if (oVar3.isCheck()) {
//                int spanStart2 = text_note_view.getText().getSpanStart(oVar3);
//                int spanEnd2 = text_note_view.getText().getSpanEnd(oVar3);
//                long b2 = oVar3.b();
//                k.j.h hVar = new k.j.h();
//                hVar.a = Long.valueOf(b2);
//                Collection<k.i.q> a2 = k.d.h.e().a(hVar);
//                if (!a2.isEmpty()) {
//                    ImageSpanView a3 = a(a2.iterator().next());
//                    text_note_view.getText().setSpan(a3, spanStart2, spanEnd2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spanViews.add(a3);
//                    text_note_view.getText().removeSpan(oVar3);
//                    arrayList.add(oVar3);
//                }
//            }
//        }
        spanViews.removeAll(arrayList);
    }
    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
                Drawable d = Drawable.createFromPath(source);
                d.setBounds(0,0,ConfigUtils.getScreenWidth(),(d.getIntrinsicHeight()/d.getIntrinsicWidth())*ConfigUtils.getScreenWidth() );
                return d;

        }

    }


}
