package com.example.doan7.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan7.History;
import com.example.doan7.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<History> listHistory;
    private OnItemClickListener onItemClickListener;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(History item);
    }

    public HistoryAdapter(List<History> history, OnItemClickListener onItemClickListener) {
        this.listHistory = history;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = listHistory.get(position);
        holder.tvDisease.setText(history.getName());
        holder.imgHistory.setImageResource(history.getImageId());

        // Gán sự kiện click cho item
        holder.cardViewHistory.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(history);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDisease;
        private ImageView imgHistory;
        private CardView cardViewHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDisease = itemView.findViewById(R.id.tvDisease);
            imgHistory = itemView.findViewById(R.id.ImgHistory);
            cardViewHistory = itemView.findViewById(R.id.cardViewHistory);
        }
    }
}
