package com.example.bizly1.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bizly1.R;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Uri> imageUris;
    private OnImageDeleteListener deleteListener;

    public interface OnImageDeleteListener {
        void onDelete(int position);
    }

    public ImageAdapter(List<Uri> imageUris, OnImageDeleteListener deleteListener) {
        this.imageUris = imageUris;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_upload, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = imageUris.get(position);
        // In a real app, use Glide or Picasso to load Uri
        holder.ivImage.setImageURI(uri);
        holder.btnDelete.setVisibility(View.VISIBLE);
        
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageButton btnDelete;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            btnDelete = itemView.findViewById(R.id.btnDeleteImage);
        }
    }
}

