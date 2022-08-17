package com.example.inote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.inote.models.Recent;
import com.example.inote.ui.AddNoteActivity;
import com.example.inote.ui.MainActivity;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.IUpdate;
import com.example.inote.view.ShareUtils;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<Note> data;
    private Context mContext;
    IUpdate iUpdate;

    public NoteAdapter(Context context, List<Note> data, IUpdate iUpdate) {
        this.data = data;
        this.mContext = context;
        this.iUpdate = iUpdate;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem;
        if (ShareUtils.getBool(ShareUtils.VIEW_AS_GALLERY)){
             rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_gallery, parent, false);
        }else {
            rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        }
        return new ViewHolder(rowItem);
    }

    public void setFilter(List<Note> newList) {
        data = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note note = data.get(position);
        Date date = new Date(note.getTimeEdit());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);

        holder.tvTitle.setText(note.getTitle() + "");
        if (this.data.get(position).getIdFolder() == 0) {
            holder.tvNoteSmall.setText(mContext.getResources().getString(R.string.notes));
        } else {
            holder.tvNoteSmall.setText(AppDatabase.noteDB.getFolderDAO().getItemFolder(note.getIdFolder()).getTitle());

        }
        if (note.getValue().size() != 0) {
            holder.tvValueNote.setText(dateFormat.format(date) + ", " + note.getValue().get(0) + "");

        } else {
            holder.tvValueNote.setText(dateFormat.format(date) + ", " + mContext.getResources().getString(R.string.no_content));

        }
        holder.tvTime.setText(dateFormat.format(date));
        holder.tvTitleNote1.setText(note.getTitle());
        if (note.getProtectionType() == 1) {
            holder.ivLockHome.setVisibility(View.VISIBLE);
            holder.tvValueNote.setText(mContext.getResources().getString(R.string.locked_string));

        } else {
            holder.ivLockHome.setVisibility(View.GONE);

        }

        if (data.get(position).getListImage().size() > 0) {
            if (data.get(position).getListImage().get(0).contains("storage")){
                holder.image_note2.setImageURI(Uri.fromFile(new File(data.get(position).getListImage().get(data.get(position).getListImage().size() - 1))));
            }else {
                ConfigUtils.convertBase64toImage(holder.image_note2,data.get(position).getListImage().get(data.get(position).getListImage().size() - 1));
            }
        }
        if (position == data.size()-1){
            holder.view_main.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle,tvTime,tvTitleNote1;
        private TextView tvValueNote;
        private TextView tvNoteSmall;
        private RoundedImageView image_note2;
        private ImageView ivLockHome;
        private View view_main;
        RelativeLayout root_note;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.tvTitle = view.findViewById(R.id.tvTitleNote);
            this.tvValueNote = view.findViewById(R.id.tvValueNote);
            this.tvNoteSmall = view.findViewById(R.id.tvNoteSmall);
            this.image_note2 = view.findViewById(R.id.image_note2);
            this.ivLockHome = view.findViewById(R.id.ivLockHome);
            this.view_main = view.findViewById(R.id.view_main);
            this.tvTime = view.findViewById(R.id.tvTime);
            this.tvTitleNote1 = view.findViewById(R.id.tvTitleNote1);
            this.root_note = view.findViewById(R.id.rl_main);
            ConfigUtils.getConFigDark(mContext,view_main,tvTitle,tvTime,tvTitleNote1,tvValueNote);
            ConfigUtils.darkRelativeRadius(root_note);
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
            RelativeLayout dialog2 = dialog.findViewById(R.id.dialog2);
            LinearLayout rl_bottom = dialog.findViewById(R.id.rl_bottom);
            TextView tvName = dialog.findViewById(R.id.tvName);
            TextView tvPinDl = dialog.findViewById(R.id.tvPinDl);
            TextView tvEdit = dialog.findViewById(R.id.tvEdit);
            TextView tvFolder = dialog.findViewById(R.id.tvFolder);
            View viewM = dialog.findViewById(R.id.viewM);
            View viewM3 = dialog.findViewById(R.id.viewM3);
            View viewM4 = dialog.findViewById(R.id.viewM4);
            View viewM2 = dialog.findViewById(R.id.viewM2);
            tvName.setText(data.get(getAdapterPosition()).getTitle());
            if (data.get(getAdapterPosition()).isPinned()) {
                tvPinDl.setText(mContext.getResources().getString(R.string.unpin));
            }
            rlPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(getAdapterPosition()).isPinned()) {
                        AppDatabase.noteDB.getNoteDAO().updatePinned(false, data.get(getAdapterPosition()).getId());
                    } else {
                        AppDatabase.noteDB.getNoteDAO().updatePinned(true, data.get(getAdapterPosition()).getId());
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
            ConfigUtils.getConFigDark(mContext,rl_bottom,tvName,tvPinDl,tvFolder,tvEdit,tvFavorites,viewM3,viewM,viewM4,viewM2,dialog2);


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
                    } else {
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
            LinearLayout dialog_show = dialog.findViewById(R.id.dialog_show);

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
            ConfigUtils.getConFigDark(dialog.getContext(),tv_rename,tvA_cancel,tv_ok,dialog_show);

            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppDatabase.noteDB.getRecentDao().insert(
                            new Recent(data.get(getAdapterPosition()).getIdFolder(),false,data.get(getAdapterPosition()).getListImage(),data.get(getAdapterPosition()).getProtectionType(),
                                    System.currentTimeMillis(),
                            data.get(getAdapterPosition()).getTitle(),0,
                            data.get(getAdapterPosition()).getValue(), data.get(getAdapterPosition()).getValueChecklist(),data.get(getAdapterPosition()).getNoteStyle()
                            ));
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
            ConfigUtils.getConFigDark(mContext,tvCancelFolder2,tv_delete_note,tv_rename,tvTitleFolder2,tvTitleFolder22,view2_dl2,view1_dl2,root_dl2);
            tv_delete_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tv_rename.getText().toString().contains(ShareUtils.getStr(ShareUtils.PASSCODE, ""))) {
                        if (checkdelete) {
                            dialogDeleteNote();
                        } else {
                            if (data.get(getAdapterPosition()).getProtectionType() == 1) {
                                AppDatabase.noteDB.getNoteDAO().updateprotectionType(0, data.get(getAdapterPosition()).getId());
                            } else {
                                AppDatabase.noteDB.getNoteDAO().updateprotectionType(1, data.get(getAdapterPosition()).getId());
                            }
                            iUpdate.onFinish();
                        }
                        dialog.dismiss();


                    } else {
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

            for (int i = 0; i < AppDatabase.noteDB.getFolderDAO().getAllFolder().size(); i++) {
                RadioButton rb = new RadioButton(itemView.getContext()); // dynamically creating RadioButton and adding to RadioGroup.
                rb.setText(AppDatabase.noteDB.getFolderDAO().getAllFolder().get(i).getTitle());
                rb.setId(AppDatabase.noteDB.getFolderDAO().getAllFolder().get(i).getId());
                rb.setWidth(400);
                rb.setTextSize(17);
                rb.setPadding(20, 0, 0, 0);
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