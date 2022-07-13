package com.example.inote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inote.R;
import com.example.inote.models.Folder;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private List<Folder> data;
    public FolderAdapter (List<Folder> data){
        this.data = data;
    }

    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(FolderAdapter.ViewHolder holder, int position) {
        holder.tvNameFolder.setText(this.data.get(position).getTitle()+"");
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvNameFolder;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.tvNameFolder = view.findViewById(R.id.tvNameFolder);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position : " + getLayoutPosition() + " text : " + this.tvNameFolder.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}