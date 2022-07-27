package com.example.inote.adapter;

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


public class ImageNoteAdapter extends RecyclerView.Adapter<ImageNoteAdapter.ViewHolder> {
    private List<String> data;
    Context context;

    public ImageNoteAdapter(Context context, List<String> data, ICopy iUpdate) {
        this.iUpdate = iUpdate;
        this.data = data;
        this.context = context;
    }

    ICopy iUpdate;

    @Override
    public ImageNoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(ImageNoteAdapter.ViewHolder holder, int position) {
        File file = new File(data.get(position));
        Uri uri = Uri.fromFile(file);
        Glide.with(context)
                .load(uri)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.image);
        holder.image.setBorderColor(Color.GRAY);


    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RoundedImageView image;
        ImageView ivDeleteImage;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.image = view.findViewById(R.id.avatarAlbum);
            this.ivDeleteImage = view.findViewById(R.id.ivDeleteImage);
            ivDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                new File(data.get(getAdapterPosition())).delete();
                    data.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    iUpdate.onFinish(data);

                }
            });
        }

        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(itemView.getContext(), NotesActivity.class);
//            intent.putExtra("idFolder",data.get(getAdapterPosition()).getId());
//            itemView.getContext().startActivity(intent);
        }

    }

}
