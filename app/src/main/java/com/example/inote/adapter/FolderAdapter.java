package com.example.inote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Folder;
import com.example.inote.ui.NotesActivity;
import com.example.inote.view.IUpdate;

import java.util.Arrays;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private List<Folder> data;

    public FolderAdapter(List<Folder> data, IUpdate iUpdate) {
        this.iUpdate = iUpdate;
        this.data = data;
    }

    IUpdate iUpdate;

    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(FolderAdapter.ViewHolder holder, int position) {
        holder.tvNameFolder.setText(this.data.get(position).getTitle() + "");
        holder.sizeFolder.setText(AppDatabase.noteDB.getNoteDAO().getAllNoteFolder(this.data.get(position).getId()).size() + "");
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvNameFolder;
        private TextView sizeFolder;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.tvNameFolder = view.findViewById(R.id.tvNameFolder);
            this.sizeFolder = view.findViewById(R.id.size_folder);
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
            Intent intent = new Intent(itemView.getContext(), NotesActivity.class);
            intent.putExtra("idFolder", data.get(getAdapterPosition()).getId());
            itemView.getContext().startActivity(intent);
        }

        private void showDialogChoose() {

            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_info);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView tv_rename = dialog.findViewById(R.id.tv_rename);

            tv_rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogRename();
                    dialog.dismiss();
                }
            });

            TextView tv_delete_note = dialog.findViewById(R.id.tv_delete_note);
            tv_delete_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createdelete();
                    dialog.dismiss();

                }
            });

            dialog.show();

        }

        private void createdelete() {
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
                    AppDatabase.noteDB.getFolderDAO().delete(data.get(getAdapterPosition()).getId());
                    AppDatabase.noteDB.getNoteDAO().deleteAllNoteByFolder(data.get(getAdapterPosition()).getId());
                    dialog.dismiss();
                    iUpdate.onFinish();

                }
            });

            dialog.show();
        }

        private void dialogRename() {
            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_rename);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView tvA_cancel = dialog.findViewById(R.id.tvA_cancel);
            EditText tv_rename = dialog.findViewById(R.id.tv_rename);
            tv_rename.setText(data.get(getAdapterPosition()).getTitle());
            tv_rename.getBackground().setColorFilter(itemView.getContext().getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);
            tv_rename.requestFocus();
            InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            tvA_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tv_rename.getWindowToken(), 0);
                    dialog.dismiss();
                }
            });

            TextView tv_ok = dialog.findViewById(R.id.tv_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppDatabase.noteDB.getFolderDAO().update(data.get(getAdapterPosition()).getId(), tv_rename.getText().toString());

                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tv_rename.getWindowToken(), 0);
                    dialog.dismiss();
                    iUpdate.onFinish();


                }
            });

            dialog.show();
        }

    }

}