package com.example.inote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inote.R;
import com.example.inote.models.Folder;
import com.example.inote.models.Note;
import com.example.inote.ui.AddNoteActivity;
import com.example.inote.ui.MainActivity;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<Note> data;
    private static Context mContext;
    public NoteAdapter (Context context,List<Note> data){
        this.data = data;
        this.mContext = context;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Date date = new Date(this.data.get(position).getTimeEdit());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);

        holder.tvTitle.setText(this.data.get(position).getTitle()+"");
        holder.tvValueNote.setText(dateFormat.format(date)+", " + this.data.get(position).getValue()+"");
        holder.tvNoteSmall.setText("Ghi chú");
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvValueNote;
        private TextView tvNoteSmall;
        private RoundedImageView image_note2;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.tvTitle = view.findViewById(R.id.tvTitleNote);
            this.tvValueNote = view.findViewById(R.id.tvValueNote);
            this.tvNoteSmall = view.findViewById(R.id.tvNoteSmall);
            this.image_note2 = view.findViewById(R.id.image_note2);
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
            itemView.getContext().startActivity(i);

        }

        private void showDialogChoose(){
            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_longclick);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RelativeLayout rlPin = dialog.findViewById(R.id.rlPin);

            rlPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            RelativeLayout rlLock = dialog.findViewById(R.id.rlLock);
            rlLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                }
            });

            dialog.show();

        }

    }


}