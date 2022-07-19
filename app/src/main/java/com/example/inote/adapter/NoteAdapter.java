package com.example.inote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Folder;
import com.example.inote.models.Note;
import com.example.inote.ui.AddNoteActivity;
import com.example.inote.ui.MainActivity;
import com.example.inote.view.IUpdate;
import com.example.inote.view.ShareUtils;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<Note> data;
    private  Context mContext;
    IUpdate iUpdate;
    public NoteAdapter (Context context, List<Note> data, IUpdate iUpdate){
        this.data = data;
        this.mContext = context;
        this.iUpdate = iUpdate;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(rowItem);
    }
    public void setFilter(List<Note> newList){
        data = newList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note note =  data.get(position);
        Date date = new Date(note.getTimeEdit());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);

        holder.tvTitle.setText(note.getTitle()+"");
        if (this.data.get(position).getIdFolder() == 0){
            holder.tvNoteSmall.setText(mContext.getResources().getString(R.string.notes));
        }else {
            holder.tvNoteSmall.setText(AppDatabase.noteDB.getFolderDAO().getItemFolder(note.getIdFolder()).getTitle());

        }
        if (note.getValue().length() !=0){
            holder.tvValueNote.setText(dateFormat.format(date)+", " + note.getValue()+"");

        }else {
            holder.tvValueNote.setText(dateFormat.format(date)+", " + mContext.getResources().getString(R.string.no_content));

        }
        if (note.getProtectionType() ==1){
            holder.ivLockHome.setVisibility(View.VISIBLE);
            holder.tvValueNote.setText(mContext.getResources().getString(R.string.locked_string));

        }else {
            holder.ivLockHome.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvValueNote;
        private TextView tvNoteSmall;
        private RoundedImageView image_note2;
        private ImageView ivLockHome;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.tvTitle = view.findViewById(R.id.tvTitleNote);
            this.tvValueNote = view.findViewById(R.id.tvValueNote);
            this.tvNoteSmall = view.findViewById(R.id.tvNoteSmall);
            this.image_note2 = view.findViewById(R.id.image_note2);
            this.ivLockHome = view.findViewById(R.id.ivLockHome);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showDialogChoose();
                    return false;
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(itemView.getContext(), AddNoteActivity.class);
            i.putExtra("idNote", data.get(getAdapterPosition()).getId());
            itemView.getContext().startActivity(i);

        }

        private void showDialogChoose() {
            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_longclick);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RelativeLayout rlPin = dialog.findViewById(R.id.rlPin);
            RelativeLayout rlEdit = dialog.findViewById(R.id.rlEdit);
            RelativeLayout rlDelete = dialog.findViewById(R.id.rlDelete);
            RelativeLayout rlMove = dialog.findViewById(R.id.rlFolder);
            TextView tvName = dialog.findViewById(R.id.tvName);
            TextView tvPinDl = dialog.findViewById(R.id.tvPinDl);
            tvName.setText(data.get(getAdapterPosition()).getTitle());
            if (data.get(getAdapterPosition()).isPinned()){
                tvPinDl.setText(mContext.getResources().getString(R.string.unpin));
            }
            rlPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(getAdapterPosition()).isPinned()){
                        AppDatabase.noteDB.getNoteDAO().updatePinned(false,data.get(getAdapterPosition()).getId());
                    }else {
                        AppDatabase.noteDB.getNoteDAO().updatePinned(true,data.get(getAdapterPosition()).getId());
                    }
                    iUpdate.onFinish();


                    dialog.dismiss();
                }
            });

            RelativeLayout rlLock = dialog.findViewById(R.id.rlLock);
            TextView tvFavorites = dialog.findViewById(R.id.tvFavorites);
            if (data.get(getAdapterPosition()).getProtectionType() == 1) {
                tvFavorites.setText(mContext.getString(R.string.unlocks));
            }
            rlLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(getAdapterPosition()).getProtectionType() == 1) {
                        showDialogConfirmDialog(false);


                    } else {
                        if (ShareUtils.getStr(ShareUtils.PASSCODE, "").length() == 0) {
                            showDialogCreatePassCode();
                        } else {
                            showDialogConfirmDialog(false);

                        }

                    }

                    dialog.dismiss();

                }
            });
            rlEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(itemView.getContext(), AddNoteActivity.class);
                    i.putExtra("idNote", data.get(getAdapterPosition()).getId());
                    itemView.getContext().startActivity(i);
                    dialog.dismiss();
                }
            });
            rlDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.get(getAdapterPosition()).getProtectionType() == 1) {
                        showDialogConfirmDialog(true);
                    }else {
                        dialogDeleteNote();
                    }
                    dialog.dismiss();


                }
            });
            rlMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRadioButtonDialog();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

        private void dialogDeleteNote() {
            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_delete);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView tvA_cancel = dialog.findViewById(R.id.tvA_cancel);
            TextView tv_rename = dialog.findViewById(R.id.tv_rename);
            String str = itemView.getContext().getResources().getString(R.string.delete_note_prompt_message);
            String format = String.format(str, Arrays.copyOf(new Object[]{data.get(getAdapterPosition()).getTitle()}, 1));
            tv_rename.setText(format);
            tvA_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            TextView tv_ok = dialog.findViewById(R.id.tv_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppDatabase.noteDB.getNoteDAO().deleteItemNote(data.get(getAdapterPosition()).getId());
                    dialog.dismiss();
                    iUpdate.onFinish();

                }
            });

            dialog.show();
        }
        private void showDialogCreatePassCode() {
            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_create_pass);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            EditText edtPasscode = dialog.findViewById(R.id.edtPasscode);
            EditText edtRePasscode = dialog.findViewById(R.id.edtRePasscode);
            EditText edtQuestion = dialog.findViewById(R.id.edtQuestion);
            TextView tvOk = dialog.findViewById(R.id.tvOk);
            TextView tvCancel = dialog.findViewById(R.id.tvCancel);
            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtPasscode.getText().toString().length() < 6 && edtRePasscode.getText().toString().length() < 6) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.must_6), Toast.LENGTH_LONG).show();

                    } else if (edtPasscode.getText().toString() == edtRePasscode.getText().toString()) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.no_match), Toast.LENGTH_LONG).show();
                    } else if (edtQuestion.getText().toString().length() == 0) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.must_empty), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.locks), Toast.LENGTH_LONG).show();
                        ShareUtils.setStr(ShareUtils.PASSCODE, edtPasscode.getText().toString());
//                        data.get(getAdapterPosition()).setProtectionType(1);
                        AppDatabase.noteDB.getNoteDAO().updateprotectionType(1, data.get(getAdapterPosition()).getId());
                        notifyDataSetChanged();
                        iUpdate.onFinish();
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

        private void showDialogConfirmDialog(boolean checkdelete) {

            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_pass);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            EditText tv_rename = dialog.findViewById(R.id.edtFolder2);
            TextView tvCancelFolder2 = dialog.findViewById(R.id.tvCancelFolder2);

            tvCancelFolder2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            TextView tv_delete_note = dialog.findViewById(R.id.tvOkFolder2);
            tv_delete_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tv_rename.getText().toString().contains(ShareUtils.getStr(ShareUtils.PASSCODE,""))){
                        if (checkdelete){
                            dialogDeleteNote();
                        }else {
                            if (data.get(getAdapterPosition()).getProtectionType()  ==1){
                                AppDatabase.noteDB.getNoteDAO().updateprotectionType(0, data.get(getAdapterPosition()).getId());
                            }else {
                                AppDatabase.noteDB.getNoteDAO().updateprotectionType(1, data.get(getAdapterPosition()).getId());
                            }
                            iUpdate.onFinish();
                        }
                        dialog.dismiss();


                    }else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.pass_not_connect), Toast.LENGTH_LONG).show();

                    }

                }
            });

            dialog.show();

        }
        private void showRadioButtonDialog() {

            // custom dialog
            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_group_folder);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.dialog_radio_group);

            for(int i=0;i<AppDatabase.noteDB.getFolderDAO().getAllFolder().size();i++){
                RadioButton rb=new RadioButton(itemView.getContext()); // dynamically creating RadioButton and adding to RadioGroup.
                rb.setText(AppDatabase.noteDB.getFolderDAO().getAllFolder().get(i).getTitle());
                rb.setId(AppDatabase.noteDB.getFolderDAO().getAllFolder().get(i).getId());
                rb.setWidth(400);
                rb.setTextSize(17);
                rb.setPadding(20,0,0,0);
                rb.setTextColor(Color.BLACK);
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]
                                {
                                        new int[]{-android.R.attr.state_enabled}, // Disabled
                                        new int[]{android.R.attr.state_enabled}   // Enabled
                                },
                        new int[]
                                {
                                        Color.BLACK, // disabled
                                        mContext.getResources().getColor(R.color.main_color)   // enabled
                                }
                );

                rb.setButtonTintList(colorStateList);
                rg.addView(rb);
            }
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    int childCount = radioGroup.getChildCount();
                    for (int x = 0; x < childCount; x++) {
                        RadioButton btn = (RadioButton) radioGroup.getChildAt(x);
                        if (btn.getId() == checkedId) {
                            AppDatabase.noteDB.getNoteDAO().updateFolder(btn.getId(), data.get(getAdapterPosition()).getId());
                            iUpdate.onFinish();
                            dialog.dismiss();
                        }
                    }
                }
            });
            dialog.show();

        }
    }

}