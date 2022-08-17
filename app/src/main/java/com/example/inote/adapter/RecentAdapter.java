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

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {
    private List<Recent> data;
    private Context mContext;
    IUpdate iUpdate;

    public RecentAdapter(Context context, List<Recent> data, IUpdate iUpdate) {
        this.data = data;
        this.mContext = context;
        this.iUpdate = iUpdate;
    }

    @Override
    public RecentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(rowItem);
    }

    public void setFilter(List<Recent> newList) {
        data = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecentAdapter.ViewHolder holder, int position) {
        Recent recent = data.get(position);
        Date date = new Date(recent.getTimeEdit());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);

        holder.tvTitle.setText(recent.getTitle() + "");
        if (this.data.get(position).getIdFolder() == 0) {
            holder.tvNoteSmall.setText(mContext.getResources().getString(R.string.notes));
        } else {
            holder.tvNoteSmall.setText(AppDatabase.noteDB.getFolderDAO().getItemFolder(recent.getIdFolder()).getTitle());

        }
        if (recent.getValue().size() != 0) {
            holder.tvValueNote.setText(dateFormat.format(date) + ", " + recent.getValue().get(0) + "");

        } else {
            holder.tvValueNote.setText(dateFormat.format(date) + ", " + mContext.getResources().getString(R.string.no_content));

        }

        if (data.get(position).getListImage().size() > 0) {
            holder.image_note2.setImageURI(Uri.fromFile(new File(data.get(position).getListImage().get(data.get(position).getListImage().size() - 1))));
        }
        if (data.get(position).getProtectionType()==1){
            holder.ivLockHome.setVisibility(View.VISIBLE);
        }else holder.ivLockHome.setVisibility(View.GONE);
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
        private View view_main;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.tvTitle = view.findViewById(R.id.tvTitleNote);
            this.tvValueNote = view.findViewById(R.id.tvValueNote);
            this.tvNoteSmall = view.findViewById(R.id.tvNoteSmall);
            this.image_note2 = view.findViewById(R.id.image_note2);
            this.ivLockHome = view.findViewById(R.id.ivLockHome);
            this.view_main = view.findViewById(R.id.view_main);
            ConfigUtils.getConFigDark(mContext,view_main,tvTitle);

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
            showDialogChoose();

        }
        private void showDialogChoose() {
            final Dialog dialog = new Dialog(itemView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_recent_click);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            RelativeLayout rlDelete = dialog.findViewById(R.id.rlDelete);
            RelativeLayout dialog2 = dialog.findViewById(R.id.dialog2);
            LinearLayout dialog21 = dialog.findViewById(R.id.rl_bottom);
            RelativeLayout rlMove = dialog.findViewById(R.id.rlFolder);
            TextView tvName = dialog.findViewById(R.id.tvName);
            TextView tvFolder = dialog.findViewById(R.id.tvFolder);
            ImageView ivFolder = dialog.findViewById(R.id.ivFolder);
            View viewM2 = dialog.findViewById(R.id.viewM2);
            tvName.setText(data.get(getAdapterPosition()).getTitle());
             ConfigUtils.getConFigDark(mContext,tvName,tvFolder,dialog21,dialog2,viewM2);
             ConfigUtils.darkImage(mContext,ivFolder);
            rlDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   AppDatabase.noteDB.getRecentDao().deleteItemRecent(data.get(getAdapterPosition()).getId());
                   iUpdate.onFinish();
                    dialog.dismiss();


                }
            });
            rlMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppDatabase.noteDB.getNoteDAO().insert(
                            new Note(0,false,data.get(getAdapterPosition()).getListImage(),data.get(getAdapterPosition()).getProtectionType(),
                                    System.currentTimeMillis(),
                                    data.get(getAdapterPosition()).getTitle(),0,
                                    data.get(getAdapterPosition()).getValue(), data.get(getAdapterPosition()).getValueChecklist(), data.get(getAdapterPosition()).getNoteStyle()));
                    AppDatabase.noteDB.getRecentDao().deleteItemRecent(data.get(getAdapterPosition()).getId());
                    dialog.dismiss();
                    iUpdate.onFinish();

                }
            });

            dialog.show();

        }
    }

}
