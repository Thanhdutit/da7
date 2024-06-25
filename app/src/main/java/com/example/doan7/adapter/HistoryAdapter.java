package com.example.doan7.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan7.History;
import com.example.doan7.MyApplication;
import com.example.doan7.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<History> listHistory;
    private OnItemClickListener onItemClickListener;
    private Context context;
    String selectedLanguage, ten;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "goiy.db";

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(History item);
    }

//    public HistoryAdapter(List<History> history, OnItemClickListener onItemClickListener) {
//        this.listHistory = history;
//        this.onItemClickListener = onItemClickListener;
//    }
    public HistoryAdapter(Context context, List<History> history, OnItemClickListener onItemClickListener) {
        this.context = context;
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
        selectedLanguage = MyApplication.getSelectedLanguage();
        String tenBenhColumn;


        switch (selectedLanguage) {
            case "English":
                tenBenhColumn = "tenbenh_en";

                break;
            case "Japanese":
                tenBenhColumn = "tenbenhjp";

                break;
            case "Vietnamese":
            default:
                tenBenhColumn = "tenbenh";

                break;
        }
        database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String id =  history.getLop();
        Cursor cursor = database.query("tbgoiy", new String[]{"id", tenBenhColumn}, "id=?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            ten = cursor.getString(1);
        } else {
            Log.e("HistoryFragment", "No data found for id: " + id);
        }
        cursor.close();

            holder.tvDisease.setText(ten);
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
