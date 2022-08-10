package com.example.inote.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.inote.R;
import com.example.inote.adapter.CheckListAdapter;
import com.example.inote.adapter.ImageNoteAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.CheckItem;
import com.example.inote.models.Note;
import com.example.inote.models.NoteStyle;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.ICheckList;
import com.example.inote.view.ShareUtils;
import com.example.inote.view.drawingview.ICopy;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends BaseActivity implements TextWatcher, View.OnClickListener, ICopy, ICheckList {
    RelativeLayout ivMore,rl_Pin ,rl_delete,rl_lock,rl_align;
    RelativeLayout menuChooserContainer, layoutLock, imageChooserContainer, rl_sharenote,rl_search,search_root,main_note,rl_bottom,rl_top;
    View viewBackground;
    EditText edtTitle, text_note_view, text_note_view2, text_note_view3,search_query;
    TextView tvTime, tvViewNote, tvChoosePhoto, tvTakePhoto, tvDone,tvWordCount,tvSize,tvPin,tvLockNote;
    int idNote;
    int idFolder;
    ImageView ivDraw, ivPhoto, ivCreate, ivChecklist,search_previous,search_next,search_clear,imgPin;
    RecyclerView rv_image, checklist_list;
    ImageNoteAdapter imageNoteAdapter;
    CheckListAdapter checkListAdapter;
    List<String> listImage;
    NestedScrollView nestedScrollView;
    ImageView close_bottom;
    NoteStyle noteStyleCustom = ConfigUtils.noteStyleCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();

        setContentView(R.layout.activity_add_note);
        onBack();
        initView();
        Intent intent = getIntent();
        idNote = intent.getIntExtra("idNote", 0);
        idFolder = intent.getIntExtra("idFolder", 0);
        ConfigUtils.listCheckList.clear();
        ConfigUtils.listValueCache.clear();

        if (idNote != 0) {

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
            text_note_view2.setText(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValue().get(1));
            text_note_view3.setText(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValue().get(2));
            tvTime.setText(ConfigUtils.formatDateTIme(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getTimeEdit()));
        } else {
            ConfigUtils.listValueCache.add(0,"");
            ConfigUtils.listValueCache.add(1,"");
            ConfigUtils.listValueCache.add(2,"");

            tvTime.setVisibility(View.GONE);
        }
        findViewById(R.id.tvBack).setOnClickListener(view -> onBackPressed());
        ivMore.setOnClickListener(view -> {
            ConfigUtils.hideKeyboard(AddNoteActivity.this);
            viewBackground.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInUp).duration(100L).playOn(viewBackground);
            YoYo.with(Techniques.SlideInDown).duration(200L).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public final void call(Animator animator) {
                    menuChooserContainer.setVisibility(View.VISIBLE);
                    if (idNote != 0 ){
                        if (AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).isPinned()){
                            tvPin.setText(getString(R.string.unpin));
                            imgPin.setImageDrawable(getDrawable(R.drawable.ic_unpin));
                        }else{
                            tvPin.setText(getString(R.string.pins));
                            imgPin.setImageDrawable(getDrawable(R.drawable.ic_pin));

                        }
                    }


                }
            }).playOn(findViewById(R.id.menuChooserContainer));
        });

        viewBackground.setOnClickListener(view -> {
            ConfigUtils.hideKeyboard(AddNoteActivity.this);
            viewBackground.setVisibility(View.GONE);
            imageChooserContainer.setVisibility(View.GONE);

            YoYo.with(Techniques.SlideOutDown).duration(300L).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public final void call(Animator animator) {
                    menuChooserContainer.setVisibility(View.GONE);
                }
            }).playOn(findViewById(R.id.menuChooserContainer));


        });
        tvViewNote.setOnClickListener(view -> {
            showDialogConfirmDialog();
        });
        ivDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AddNoteActivity.this, DrawNoteActivity.class);
                intent1.putExtra("idNote", idNote);
                startActivity(intent1);
            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.hideKeyboard(AddNoteActivity.this);
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
        ivChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChecklist();
            }
        });
        tvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permission(AddNoteActivity.this)) {
                    viewBackground.setVisibility(View.GONE);
                    imageChooserContainer.setVisibility(View.GONE);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "select Image"),
                            PICK_IMAGE);
                }
                ;

            }
        });
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permission(AddNoteActivity.this)) {
                    viewBackground.setVisibility(View.GONE);
                    imageChooserContainer.setVisibility(View.GONE);
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }

            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rl_sharenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.share_bitMap_to_Apps(nestedScrollView,AddNoteActivity.this);
            }
        });
        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.hideKeyboard(AddNoteActivity.this);
                viewBackground.setVisibility(View.GONE);
                imageChooserContainer.setVisibility(View.GONE);

                YoYo.with(Techniques.SlideOutDown).duration(300L).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public final void call(Animator animator) {
                        menuChooserContainer.setVisibility(View.GONE);
                    }
                }).playOn(findViewById(R.id.menuChooserContainer));
                search_root.setVisibility(View.VISIBLE);
            }
        });
        initSearch();

        text_note_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ConfigUtils.listValueCache.remove(0);
                if (charSequence.length() != 0){
                    ConfigUtils.listValueCache.add(0,charSequence.toString());
                }else ConfigUtils.listValueCache.add(0,"");

            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        text_note_view2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ConfigUtils.listValueCache.remove(1);
                if (charSequence.length() != 0){
                    ConfigUtils.listValueCache.add(1,charSequence.toString());
                }else {
                    ConfigUtils.listValueCache.add(1,"");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        text_note_view3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ConfigUtils.listValueCache.remove(2);
                if (charSequence.length() != 0){
                    ConfigUtils.listValueCache.add(2,charSequence.toString());
                }else ConfigUtils.listValueCache.add(2,"");

            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        rl_Pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvPin.getText().toString().contains(getString(R.string.unpin))) {
                    tvPin.setText(getString(R.string.pins));
                    imgPin.setImageDrawable(getDrawable(R.drawable.ic_pin));
                } else {
                    tvPin.setText(getString(R.string.unpin));
                    imgPin.setImageDrawable(getDrawable(R.drawable.ic_unpin));

                }
            }
        });
        rl_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rl_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShareUtils.getStr(ShareUtils.PASSCODE, "").length() == 0 ) {
                    showDialogCreatePassCode();
                } else if (!tvLockNote.getText().toString().contains(getString(R.string.unlocks))){
                        showDialogConfirmDialog();
                    }else {
                    tvLockNote.setText(getString(R.string.locks));
                }


            }
        });
        rl_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuChooserContainer.setVisibility(View.GONE);
                ConfigUtils.hideKeyboard(AddNoteActivity.this);
                viewBackground.setVisibility(View.GONE);
                showDialogAlign();
            }
        });

        ConfigUtils.getConFigDark1(getApplicationContext(),edtTitle,text_note_view,text_note_view2,text_note_view3,tvTime,ids(R.id.tvAlign1),ids(R.id.tvAlign));
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

    }

    static final int REQUEST_IMAGE_CAPTURE = 2;

    private void dialogChecklist() {
        List<EditText> lisChild = new ArrayList<>();
        final Dialog dialog = new Dialog(AddNoteActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_new_checklist_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvA_cancel = dialog.findViewById(R.id.tvA_cancel);
        TextView tv_rename = dialog.findViewById(R.id.tv_rename);
        LinearLayout checklist_holder = dialog.findViewById(R.id.checklist_holder);
        LinearLayout dialog_show = dialog.findViewById(R.id.dialog_show);
        ImageView add_item = dialog.findViewById(R.id.add_item);
        createViewCheckList(lisChild, checklist_holder);
        tvA_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        List<CheckItem> checkItems = new ArrayList<>();
        ConfigUtils.darkLinearLayoutRadius(dialog_show);
        ConfigUtils.getConFigDark1(getApplicationContext(),tv_rename,tv_ok,tvA_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < lisChild.size(); i++) {
                    if (lisChild.get(i).getText().toString().length() != 0) {
                        checkItems.add(new CheckItem(i, false, lisChild.get(i).getText().toString()));
                    }
                }
                if (idNote != 0) {
                    checkItems.addAll(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValueChecklist());
                    AppDatabase.noteDB.getNoteDAO().updateListCheckList(checkItems, idNote);
                } else {
                    ConfigUtils.listCheckList.addAll(checkItems);

                }
                updateCheckList(checkItems);

                dialog.dismiss();

            }
        });
        add_item.setOnClickListener(view -> {
            createViewCheckList(lisChild, checklist_holder);

        });
        dialog.show();
    }

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
        text_note_view2 = findViewById(R.id.text_note_view2);
        text_note_view3 = findViewById(R.id.text_note_view3);
        tvViewNote = findViewById(R.id.tvViewNote);
        tvTime = findViewById(R.id.tvTime);
        ivDraw = findViewById(R.id.ivDraw);
        tvChoosePhoto = findViewById(R.id.tvChoosePhoto);
        tvTakePhoto = findViewById(R.id.tvTakePhoto);
        checklist_list = findViewById(R.id.checklist_list);
        rv_image = findViewById(R.id.rv_image);
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

        listImage = new ArrayList<>();

    }
    String target = "";

    private void initSearch(){
        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.hideKeyboard(AddNoteActivity.this);
                search_root.setVisibility(View.GONE);
                                    String highlighted = "<span style=\"background-color:#ffffff;\">" + text_note_view.getText().toString() + "</span>";

                text_note_view.setText(Html.fromHtml(highlighted));
//                changeTextView("",false);
            }
        });
        search_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextView(target,false);

            }
        });
        search_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextView(target,true);

            }
        });
        search_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    target = charSequence.toString();
                    changeTextView(target,true);
//                    String fullText = text_note_view.getText().toString();
//                    String highlighted = "<span style=\"background-color:#e4b645;\">" + charSequence + "</span>";
//                    fullText = fullText.replace( charSequence,highlighted);
//                    text_note_view.setText(Html.fromHtml(fullText));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
    int index=0;
    private void changeTextView(String target,boolean prev)
    {
        target = target.toLowerCase();
        String bString =text_note_view.getText().toString();
        int startSpan = 0, endSpan = 0;
        Spannable spanRange = new SpannableString(bString);
        bString = bString.toLowerCase();

        if(prev)
        {
            index -= target.length();
            index -= 1;
            if(index <= -1)
                index = bString.length()-1;
            index = bString.lastIndexOf(target, index);
            if(index <= -1)
                index = bString.lastIndexOf(target, bString.length()-1);
            index += target.length();
        }
        else
        {
            index = bString.indexOf(target, index);
            if(index == -1)
                index = bString.indexOf(target, 0);
            index += target.length();
        }

        while(true) {
            startSpan = bString.indexOf(target, endSpan);
            if(startSpan < 0)
                break;
            endSpan = startSpan + target.length();
            if(index == endSpan){
                spanRange.setSpan(
                        new BackgroundColorSpan(Color.parseColor("#e4b645")),
                        startSpan,
                        endSpan,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            else
                spanRange.setSpan(
                        new BackgroundColorSpan(Color.YELLOW),
                        startSpan,
                        endSpan,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        text_note_view.setText(spanRange);

    }

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                selectedImageUri = data.getData();

                String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
                File imageFile = new File(selectedImagePath);

                String des = ConfigUtils.copyFile(getApplicationContext(), imageFile, this);
            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                selectedImageUri = ConfigUtils.getImageUri(getApplicationContext(), imageBitmap);
                String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
                File imageFile = new File(selectedImagePath);
                String des = ConfigUtils.copyFile(getApplicationContext(), imageFile, this);
            }

        }

    }

    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null,
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

    private void setupListImage() {
        List<String> listImage = AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getListImage();
        if (listImage.size() == 0 &&  text_note_view3.length() == 0) {
            text_note_view3.setVisibility(View.GONE);
        } else {
            text_note_view3.setVisibility(View.VISIBLE);
        }
        rv_image.setLayoutManager(new GridLayoutManager(this, 2));
        imageNoteAdapter = new ImageNoteAdapter(this, listImage, this);
        rv_image.setAdapter(imageNoteAdapter);
    }
    private int fromPos = -1;
    private int toPos = -1;
    private void setUpListCheckList() {
        List<CheckItem> checkItems = AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValueChecklist();
        if ((checkItems == null || checkItems.size() == 0) &&  text_note_view2.length() == 0) {
            text_note_view2.setVisibility(View.GONE);
        } else {
            text_note_view2.setVisibility(View.VISIBLE);
            checklist_list.setLayoutManager(new GridLayoutManager(this, 1));
            checkListAdapter = new CheckListAdapter(this, checkItems, this);
            checklist_list.setAdapter(checkListAdapter);
            setTouchList(checkItems);
        }

    }
    private void setUpValue() {
        List<String> value = AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValue();
        if (value.size() ==1){
            text_note_view.setText(value.get(0));
        }else if (value.size() ==2){
            text_note_view2.setText(value.get(1));
            text_note_view2.setVisibility(View.VISIBLE);
        }if (value.size() ==3){

            text_note_view3.setText(value.get(2));
            text_note_view3.setVisibility(View.VISIBLE);

        }

    }
    private void setTouchList(List<CheckItem> checkItems){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( ItemTouchHelper.UP | ItemTouchHelper.DOWN , 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                toPos = target.getAdapterPosition();

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                switch(actionState){
                    case ItemTouchHelper.ACTION_STATE_DRAG:{
                        fromPos = viewHolder.getAdapterPosition();
                        break;
                    }

                    case ItemTouchHelper.ACTION_STATE_IDLE: {
                        //Execute when the user dropped the item after dragging.
                        if(fromPos != -1 && toPos != -1
                                && fromPos != toPos) {
                            moveItem(checkItems,fromPos, toPos);
                            fromPos = -1;
                            toPos = -1;
                        }
                        break;
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }
        });
        itemTouchHelper.attachToRecyclerView(checklist_list);

    }
    private void moveItem(List<CheckItem> checkItems,int oldPos, int newPos) {

        CheckItem temp = checkItems.get(oldPos);
        checkItems.set(oldPos, checkItems.get(newPos));
        checkItems.set(newPos, temp);
        checkListAdapter.notifyItemChanged(oldPos);
        checkListAdapter.notifyItemChanged(newPos);
        AppDatabase.noteDB.getNoteDAO().updateListCheckList(checkItems,idNote);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (idNote != 0) {
            setupListImage();
            setUpListCheckList();
            setUpValue();
        } else {
            if (ConfigUtils.listImageCache.size() == 0  &&  text_note_view3.length() == 0) {
                text_note_view3.setVisibility(View.GONE);
            } else {
                text_note_view3.setVisibility(View.VISIBLE);
            }
            if (ConfigUtils.listCheckList.size() == 0  &&  text_note_view2.length() == 0) {
                text_note_view2.setVisibility(View.GONE);
            } else {
                text_note_view2.setVisibility(View.VISIBLE);
            }
            rv_image.setLayoutManager(new GridLayoutManager(this, 2));
            imageNoteAdapter = new ImageNoteAdapter(this, ConfigUtils.listImageCache, this);
            rv_image.setAdapter(imageNoteAdapter);
            checklist_list.setLayoutManager(new GridLayoutManager(this, 1));
            checkListAdapter = new CheckListAdapter(this, ConfigUtils.listCheckList, this);
            checklist_list.setAdapter(checkListAdapter);
            setTouchList(ConfigUtils.listCheckList);
        }
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) ==true){
            main_note.setBackgroundColor(Color.BLACK);
        }else  main_note.setBackgroundColor(getResources().getColor(R.color.color_main));
        ;
    }

    @Override
    public void onFinish(List<String> data) {
        if (idNote != 0) {
            AppDatabase.noteDB.getNoteDAO().updateListImage(data, idNote);
            setupListImage();
        } else {
            ConfigUtils.listImageCache.clear();
            ConfigUtils.listImageCache.addAll(data);
            if (ConfigUtils.listImageCache.size() == 0  &&  text_note_view3.length() == 0) {
                text_note_view3.setVisibility(View.GONE);
            } else {
                text_note_view3.setVisibility(View.VISIBLE);
            }
            rv_image.setLayoutManager(new GridLayoutManager(this, 2));
            imageNoteAdapter = new ImageNoteAdapter(this, ConfigUtils.listImageCache, this);
            rv_image.setAdapter(imageNoteAdapter);

        }
    }

    @Override
    public void onProgress(String path) {
        if (idNote != 0) {
            List<String> listImage = AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getListImage();
            listImage.add(path);
            AppDatabase.noteDB.getNoteDAO().updateListImage(listImage, idNote);
        } else {
            ConfigUtils.listImageCache.add(path);
        }

    }
   private boolean ispnin(){
        if (tvPin.getText().toString().contains(getString(R.string.pins))){
            return false;
        }else return true;
   }
   private  int protectType(){
       if (tvLockNote.getText().toString().contains(getString(R.string.locks))){
           return 0;
       }else return 1;
   }
    @Override
    public void onBackPressed() {
        if (idNote != 0) {
            AppDatabase.noteDB.getNoteDAO().updateItem(edtTitle.getText().toString(), ConfigUtils.listValueCache, idNote);
            AppDatabase.noteDB.getNoteDAO().updatePinned(ispnin(),idNote);
            AppDatabase.noteDB.getNoteDAO().updateprotectionType(protectType(),idNote);
        } else if (edtTitle.getText().toString().length() != 0
                || ConfigUtils.listValueCache.size() > 0
                || ConfigUtils.listImageCache.size() > 0) {
            AppDatabase.noteDB.getNoteDAO().insert(
                    new Note(idFolder, ispnin(), ConfigUtils.listImageCache,  protectType(), System.currentTimeMillis(),
                            edtTitle.getText().toString(), 0, ConfigUtils.listValueCache, ConfigUtils.listCheckList,noteStyleCustom));
            ConfigUtils.listValueCache.clear();
            ConfigUtils.listImageCache.clear();
            ConfigUtils.listCheckList.clear();
        } else {
            ConfigUtils.listImageCache.clear();
            ConfigUtils.listCheckList.clear();
            ConfigUtils.listValueCache.clear();
        }

//        ap

        super.onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void showDialogConfirmDialog() {

        final Dialog dialog = new Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_pass);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText tv_rename = dialog.findViewById(R.id.edtFolder2);
        TextView tvCancelFolder2 = dialog.findViewById(R.id.tvCancelFolder2);
        TextView tvTitleFolder2 = dialog.findViewById(R.id.tvTitleFolder2);
        TextView tvTitleFolder22 = dialog.findViewById(R.id.tvTitleFolder22);
        RelativeLayout root_dl2 = dialog.findViewById(R.id.root_dl2);

        View view1_dl2 = dialog.findViewById(R.id.view1_dl2);
        View view2_dl2 = dialog.findViewById(R.id.view2_dl2);
        tvCancelFolder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_delete_note = dialog.findViewById(R.id.tvOkFolder2);
        ConfigUtils.darkRelativeRadius(root_dl2);
        ConfigUtils.getConFigDark(getApplicationContext(),tvCancelFolder2,tv_delete_note,tv_rename,tvTitleFolder2,tvTitleFolder22,view2_dl2,view1_dl2);

        tv_delete_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_rename.getText().toString().contains(ShareUtils.getStr(ShareUtils.PASSCODE, ""))) {
                    layoutLock.setVisibility(View.GONE);
                    tvLockNote.setText(getString(R.string.unlocks));
                    dialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.pass_not_connect), Toast.LENGTH_LONG).show();

                }
            }
        });

        dialog.show();

    }

    @Override
    public void onClick(View view) {

    }

    private void createViewCheckList(List<EditText> lisChild, LinearLayout checklist_holder) {
        View inflate = AddNoteActivity.this.getLayoutInflater().inflate(R.layout.item_add_checklist, null);
        EditText title = inflate.findViewById(R.id.title_edit_text);
        ConfigUtils.getConFigDark1(getApplicationContext(),title);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        lisChild.add(title);
        checklist_holder.addView(inflate);
        title.requestFocus();
        title.setCursorVisible(true);

    }

    @Override
    public void updateCheckList(List<CheckItem> data) {
        if (idNote != 0) {
            AppDatabase.noteDB.getNoteDAO().updateListCheckList(data, idNote);
            setUpListCheckList();
        } else {
            if (ConfigUtils.listCheckList.size() == 0 && text_note_view2.length() == 0) {
                text_note_view2.setVisibility(View.GONE);
            } else {
                text_note_view2.setVisibility(View.VISIBLE);
            }
            checklist_list.setLayoutManager(new GridLayoutManager(this, 1));
            checkListAdapter = new CheckListAdapter(this, ConfigUtils.listCheckList, this);
            checklist_list.setAdapter(checkListAdapter);
            setTouchList(ConfigUtils.listCheckList);

        }

    }
    private void showDialogCreatePassCode() {
        Context mContext = getApplicationContext();
        final Dialog dialog = new Dialog(AddNoteActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
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
    private void showDialogAlign() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.CustomBottomSheetDialogTheme);
        Context mContext = getApplicationContext();
        View dialog = LayoutInflater.from(mContext).inflate(R.layout.bottom_select_sheet, (ViewGroup) null);
        bottomSheetDialog.setContentView(dialog);
        TextView tvFormat = dialog.findViewById(R.id.tvFormat);
        TextView tvFormat1 = dialog.findViewById(R.id.tvFormat1);
        TextView tvFormat2 = dialog.findViewById(R.id.tvFormat2);
        TextView tvFormat3 = dialog.findViewById(R.id.tvFormat3);
        TextView tvFormat4 = dialog.findViewById(R.id.tvFormat4);
        TextView tvScale = dialog.findViewById(R.id.tvScale);
        TextView tvs1 = dialog.findViewById(R.id.tvs1);
        TextView tvs2 = dialog.findViewById(R.id.tvs2);
        TextView tvu1 = dialog.findViewById(R.id.tvu1);
        TextView tvu2 = dialog.findViewById(R.id.tvu2);
        TextView tvi1 = dialog.findViewById(R.id.tvi1);
        TextView tvi2 = dialog.findViewById(R.id.tvi2);
        TextView tvb2 = dialog.findViewById(R.id.tvb2);
        TextView tvSmall = dialog.findViewById(R.id.tvSmall);
        TextView tvlarge = dialog.findViewById(R.id.tvLarge);
        ImageView tvLeft = dialog.findViewById(R.id.tvLeft);
        ImageView tvCenter = dialog.findViewById(R.id.tvCenter);
        ImageView tvRight = dialog.findViewById(R.id.tvRight);
        tvs1.setPaintFlags(tvs1.getPaintFlags() | 16);
        tvs2.setPaintFlags(tvs2.getPaintFlags() | 16);
        tvu1.setPaintFlags(tvu1.getPaintFlags() | 8);
        tvu2.setPaintFlags(tvu2.getPaintFlags() | 8);
        close_bottom = dialog.findViewById(R.id.close_bottom2);
        close_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        tvs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteStyleCustom.isCheckSTitle()){
                    noteStyleCustom.setCheckSTitle(false);
                }else{
                    noteStyleCustom.setCheckSTitle(true);

                }
                ConfigUtils.getStyleTitle(noteStyleCustom,edtTitle);

                ConfigUtils.checkTextRight(noteStyleCustom.isCheckSTitle(),tvs1);
            }
        });
        ConfigUtils.checkTextRight(noteStyleCustom.isCheckSTitle(),tvs1);

        tvs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteStyleCustom.isCheckSContent()){
                    noteStyleCustom.setCheckSContent(false);
                }else {
                    noteStyleCustom.setCheckSContent(true);

                } ConfigUtils.getStyleContent(noteStyleCustom,text_note_view);
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view2);
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view3);
                ConfigUtils.checkTextRight(noteStyleCustom.isCheckSContent(),tvs2);


            }
        });
        ConfigUtils.checkTextRight(noteStyleCustom.isCheckSContent(),tvs2);

        tvi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteStyleCustom.isCheckITitle()){
                    noteStyleCustom.setCheckITitle(false);

                }else{
                    noteStyleCustom.setCheckITitle(true);

                }
                ConfigUtils.getStyleTitle(noteStyleCustom,edtTitle);
                ConfigUtils.checkText(noteStyleCustom.isCheckITitle(),tvi1);
            }
        });
        ConfigUtils.checkText(noteStyleCustom.isCheckITitle(),tvi1);

        tvi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteStyleCustom.isCheckIContent()){
                    noteStyleCustom.setCheckIContent(false);
                }else {
                    noteStyleCustom.setCheckIContent(true);

                }  ConfigUtils.getStyleContent(noteStyleCustom,text_note_view);
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view2);
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view3);
                ConfigUtils.checkText(noteStyleCustom.isCheckIContent(),tvi2);

            }
        });
        ConfigUtils.checkText(noteStyleCustom.isCheckIContent(),tvi2);

        tvu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteStyleCustom.isCheckUTitle()){
                    noteStyleCustom.setCheckUTitle(false);
                }else{
                    noteStyleCustom.setCheckUTitle(true);
                }
                ConfigUtils.getStyleTitle(noteStyleCustom,edtTitle);

                ConfigUtils.checkText(noteStyleCustom.isCheckUTitle(),tvu1);
            }
        });
        ConfigUtils.checkText(noteStyleCustom.isCheckUTitle(),tvu1);

        tvu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteStyleCustom.isCheckUContent()){
                    noteStyleCustom.setCheckUContent(false);
                }else {
                    noteStyleCustom.setCheckUContent(true);

                }
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view);
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view2);
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view3);
                ConfigUtils.checkText(noteStyleCustom.isCheckUContent(),tvu2);

            }
        });
        ConfigUtils.checkText(noteStyleCustom.isCheckUContent(),tvu2);

        tvb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteStyleCustom.isCheckBContent()){
                    noteStyleCustom.setCheckBContent(false);
                }else {
                    noteStyleCustom.setCheckBContent(true);

                }ConfigUtils.getStyleContent(noteStyleCustom,text_note_view);
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view2);
                ConfigUtils.getStyleContent(noteStyleCustom,text_note_view3);
                ConfigUtils.checkTextLeft(noteStyleCustom.isCheckBContent(),tvb2);
            }
        });
        ConfigUtils.checkTextLeft(noteStyleCustom.isCheckBContent(),tvb2);

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteStyleCustom.setGravityNote(0);
                tvLeft.setBackgroundResource(R.drawable.bg_radius_start_yellow);
                if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                    tvCenter.setBackgroundColor(Color.parseColor("#2c2c2e"));
                    tvRight.setBackgroundResource(R.drawable.bg_radius_end_while2);
                }else {
                    tvCenter.setBackgroundColor(Color.parseColor("#f2f1f6"));

                    tvRight.setBackgroundResource(R.drawable.bg_radius_end_while);

                }
                ConfigUtils.getStyleGravity(noteStyleCustom,edtTitle);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view2);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view3);
            }

        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteStyleCustom.setGravityNote(2);

                tvRight.setBackgroundResource(R.drawable.bg_radius_end_yellow);
                if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                    tvCenter.setBackgroundColor(Color.parseColor("#2c2c2e"));
                    tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while2);
                }else {
                    tvCenter.setBackgroundColor(Color.parseColor("#f2f1f6"));

                    tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while);

                }
                ConfigUtils.getStyleGravity(noteStyleCustom,edtTitle);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view2);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view3);
            }
        });
        tvCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteStyleCustom.setGravityNote(1);

                tvCenter.setBackgroundColor(Color.parseColor("#E4B645"));
                tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while2);
                tvRight.setBackgroundResource(R.drawable.bg_radius_end_while2);
                if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                    tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while2);

                    tvRight.setBackgroundResource(R.drawable.bg_radius_end_while2);
                }else {
                    tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while);

                    tvRight.setBackgroundResource(R.drawable.bg_radius_end_while);

                }
                ConfigUtils.getStyleGravity(noteStyleCustom,edtTitle);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view2);
                ConfigUtils.getStyleGravity(noteStyleCustom,text_note_view3);
            }
        });
        ConfigUtils.darkImage(mContext,tvLeft);
        ConfigUtils.darkImage(mContext,tvCenter);
        ConfigUtils.darkImage(mContext,tvRight);
        if (noteStyleCustom.getGravityNote() ==0){
            tvLeft.setBackgroundResource(R.drawable.bg_radius_start_yellow);
            if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                tvCenter.setBackgroundColor(Color.parseColor("#2c2c2e"));
                tvRight.setBackgroundResource(R.drawable.bg_radius_end_while2);
            }else {
                tvCenter.setBackgroundColor(Color.parseColor("#f2f1f6"));

                tvRight.setBackgroundResource(R.drawable.bg_radius_end_while);

            }

        }
        if (noteStyleCustom.getGravityNote() ==1){
            tvCenter.setBackgroundColor(Color.parseColor("#E4B645"));
            tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while2);
            tvRight.setBackgroundResource(R.drawable.bg_radius_end_while2);
            if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while2);

                tvRight.setBackgroundResource(R.drawable.bg_radius_end_while2);
            }else {
                tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while);

                tvRight.setBackgroundResource(R.drawable.bg_radius_end_while);

            }
        }
        if (noteStyleCustom.getGravityNote() ==2){
            tvRight.setBackgroundResource(R.drawable.bg_radius_end_yellow);
            if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)) {
                tvCenter.setBackgroundColor(Color.parseColor("#2c2c2e"));
                tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while2);
            }else {
                tvCenter.setBackgroundColor(Color.parseColor("#f2f1f6"));

                tvLeft.setBackgroundResource(R.drawable.bg_radius_start_while);

            }
        }
        tvSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.checkTextLeft(true,tvSmall);
                ConfigUtils.checkTextRight(false,tvlarge);
                ShareUtils.setBool(ShareUtils.CONFIG_SIZE_IMAGE,false);

            }
        });
        tvlarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.checkTextRight(true,tvlarge);
                ConfigUtils.checkTextLeft(false,tvSmall);
                ShareUtils.setBool(ShareUtils.CONFIG_SIZE_IMAGE,true);

            }
        });
        if (ShareUtils.getBool(ShareUtils.CONFIG_SIZE_IMAGE)){
            ConfigUtils.checkTextLeft(false,tvSmall);
            ConfigUtils.checkTextRight(true,tvlarge);
        }else {
            ConfigUtils.checkTextLeft(true,tvSmall);
            ConfigUtils.checkTextRight(false,tvlarge);
        }


        RelativeLayout root_dl = dialog.findViewById(R.id.rootBottomSheet);

        ConfigUtils.getConFigDark(mContext,tvFormat,tvFormat1,tvFormat2,tvFormat3,tvFormat4,tvi1,tvi2,tvs1,tvs2,tvu1,tvu2,tvb2,tvlarge, tvSmall,tvScale);
        ConfigUtils.darkRelativeTop(root_dl);
        bottomSheetDialog.show();

    }
}