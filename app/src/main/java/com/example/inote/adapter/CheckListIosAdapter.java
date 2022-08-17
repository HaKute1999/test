package com.example.inote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import android.widget.Toast;

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


public class CheckListIosAdapter extends RecyclerView.Adapter<CheckListIosAdapter.ViewHolder> {
    private List<CheckItem> data;
    Context context;

    public CheckListIosAdapter(Context context, List<CheckItem> data, ICheckList iUpdate) {
        this.iUpdate = iUpdate;
        this.data = data;
        this.context = context;
    }

    ICheckList iUpdate;

    @Override
    public CheckListIosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist_ios, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(CheckListIosAdapter.ViewHolder holder, int position) {
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
        ImageView checklist_image;
        EditText checklist_title;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.checklist_image = view.findViewById(R.id.checklist_image);

            this.checklist_title = view.findViewById(R.id.checklist_title);
            ConfigUtils.getConFigDark(context,checklist_title);
            this.checklist_title.setOnEditorActionListener(
                    new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            // Identifier of the action. This will be either the identifier you supplied,
                            // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                 iUpdate.updateCheckList(data);
                                Toast.makeText(context, checklist_title.getText(), Toast.LENGTH_SHORT).show();//do here your stuff f
                                return true;
                            }
                            return false;

                    }
                    });
        }

        @Override
        public void onClick(View view) {

        }

    }

}
