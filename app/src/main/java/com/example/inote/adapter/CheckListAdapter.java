package com.example.inote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.CheckItem;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.ICheckList;
import com.example.inote.view.drawingview.ICopy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inote.R;

import com.example.inote.view.IUpdate;
import com.example.inote.view.drawingview.ICopy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    private List<CheckItem> data;
    Context context;

    public CheckListAdapter(Context context, List<CheckItem> data, ICheckList iUpdate) {
        this.iUpdate = iUpdate;
        this.data = data;
        this.context = context;
    }

    ICheckList iUpdate;

    @Override
    public CheckListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(CheckListAdapter.ViewHolder holder, int position) {
        CheckItem checkItem = data.get(position);
        if (checkItem.isDone()) {
            holder.checklist_image.setImageDrawable(context.getDrawable(R.drawable.ic_checked_true2));
        } else {
            holder.checklist_image.setImageDrawable(context.getDrawable(R.drawable.circle_bg_note));

        }
        holder.checklist_title.setText(checkItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView checklist_image, ivRename, checklist_drag_handle, checklist_delete;
        TextView checklist_title;
        RelativeLayout checklist_holder;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.checklist_image = view.findViewById(R.id.checklist_image);
            this.checklist_holder = view.findViewById(R.id.checklist_holder);
            this.ivRename = view.findViewById(R.id.ivRename);
            this.checklist_drag_handle = view.findViewById(R.id.checklist_drag_handle);
            this.checklist_delete = view.findViewById(R.id.checklist_delete);
            this.checklist_title = view.findViewById(R.id.checklist_title);
            ConfigUtils.getConFigDark(context,checklist_title);
            checklist_holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.get(getAdapterPosition()).isDone()) {
                        data.get(getAdapterPosition()).setDone(false);
                    } else {
                        data.get(getAdapterPosition()).setDone(true);
                    }
                    iUpdate.updateCheckList(data);

                }
            });
            checklist_holder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ivRename.setVisibility(View.VISIBLE);
                    checklist_delete.setVisibility(View.VISIBLE);
                    checklist_drag_handle.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(context.getResources().getColor(R.color.activated_item_foreground));
                    return true;
                }
            });
            checklist_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.remove(getAdapterPosition());
                    iUpdate.updateCheckList(data);
                }
            });
            ivRename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogRename();
                }
            });


        }

        @Override
        public void onClick(View view) {

        }
        private void dialogRename() {
            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_rename);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView tvA_cancel = dialog.findViewById(R.id.tvA_cancel);
            EditText tv_rename = dialog.findViewById(R.id.tv_rename);
            LinearLayout dialog_show = dialog.findViewById(R.id.dialog_show);
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
            ConfigUtils.getConFigDark1(itemView.getContext(),tv_rename,tvA_cancel,tv_ok,dialog_show);

            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.get(getAdapterPosition()).setTitle(tv_rename.getText().toString());
                    AppDatabase.noteDB.getNoteDAO().updateListCheckList(data,data.get(getAdapterPosition()).getId() );

                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tv_rename.getWindowToken(), 0);
                    dialog.dismiss();
                    iUpdate.updateCheckList(data);


                }
            });

            dialog.show();
        }
    }

}
