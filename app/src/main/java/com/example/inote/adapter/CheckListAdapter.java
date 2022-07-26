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

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.checklist_image = view.findViewById(R.id.checklist_image);
            this.ivRename = view.findViewById(R.id.ivRename);
            this.checklist_drag_handle = view.findViewById(R.id.checklist_drag_handle);
            this.checklist_delete = view.findViewById(R.id.checklist_delete);
            this.checklist_title = view.findViewById(R.id.checklist_title);
            view.setOnClickListener(new View.OnClickListener() {
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
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ivRename.setVisibility(View.VISIBLE);
                    checklist_delete.setVisibility(View.VISIBLE);
                    checklist_drag_handle.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(context.getResources().getColor(R.color.activated_item_foreground));
                    return false;
                }
            });
            checklist_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public void onClick(View view) {

        }

    }

}
