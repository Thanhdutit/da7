

package com.example.doan7.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan7.History;
import com.example.doan7.MyApplication;
import com.example.doan7.R;
import com.example.doan7.Suggestion;
import com.example.doan7.adapter.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;
//
//public class HistoryFragment extends Fragment {
//
//    private RecyclerView recyclerView;
//    private SQLiteDatabase database;
//    private String data, data1, data2;
//    private String DATABASE_NAME = "goiy.db";
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_history, container, false);
//
//        List<History> historyList = new ArrayList<>();
//        historyList.add(new History("1", "Cà chua", R.drawable.cachua));
//        historyList.add(new History("2", "Đậu nành", R.drawable.daunanh));
//        historyList.add(new History("3", "Dưa hấu", R.drawable.delete));
//        historyList.add(new History("4", "Táo", R.drawable.tao));
//        historyList.add(new History("5", "Ớt chuông", R.drawable.otchuong));
//
//        recyclerView = view.findViewById(R.id.recyclerViewHistory);
//
//        HistoryAdapter historyAdapter = new HistoryAdapter(historyList, item -> {
//            String id = item.getId(); // Lấy id từ item
//            fetchHistoryDetails(id); // Lấy chi tiết lịch sử dựa trên id
//            Intent intent = new Intent(requireContext(), Suggestion.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("tenbenh", data);
//            bundle.putString("trieuchung", data1);
//            bundle.putString("dieutri", data2);
//            intent.putExtra("mypackage", bundle);
//            startActivity(intent);
//        });
//
//        recyclerView.setAdapter(historyAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
//        return view;
//    }
//
//    // Hàm để lấy chi tiết lịch sử từ database dựa trên ID
//    private void fetchHistoryDetails(String id) {
//        database = requireContext().openOrCreateDatabase(DATABASE_NAME, android.content.Context.MODE_PRIVATE, null);
//        Cursor cursor = database.query("tbgoiy", null, "id=?", new String[]{id}, null, null, null);
//        if (cursor.moveToFirst()) {
//            data = cursor.getString(1); // Cột thứ 2 trong bảng
//            data1 = cursor.getString(2); // Cột thứ 3 trong bảng
//            data2 = cursor.getString(3); // Cột thứ 4 trong bảng
//            Log.d("HistoryFragment", "Data: " + data + ", Data1: " + data1 + ", Data2: " + data2);
//        } else {
//            Log.e("HistoryFragment", "No data found for id: " + id);
//        }
//        cursor.close();
//    }
//}
public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private SQLiteDatabase database;
    private String data, data1, data2, data3;
    private String DATABASE_NAME = "goiy.db";
    private SharedPreferences sharedPreferences;
    String selectedLanguage;
    private TextView tvHistory;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewHistory);

        tvHistory = view.findViewById(R.id.tvHistory);
        selectedLanguage= MyApplication.getSelectedLanguage();
        updateView(selectedLanguage);
        ImageButton imgBtDelete = view.findViewById(R.id.imgBtDelete);

        imgBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức xóa dữ liệu trong SharedPreferences khi button được click
                deleteDataFromSharedPreferences();
            }
        });
        return view;
    }

    private void deleteDataFromSharedPreferences() {
        // Khởi tạo một Editor để chỉnh sửa SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Xóa dữ liệu trong SharedPreferences với key được chỉ định
        editor.clear(); // Xóa toàn bộ dữ liệu
        // Hoặc nếu bạn chỉ muốn xóa một phần dữ liệu, bạn có thể sử dụng phương thức remove với key tương ứng
        // editor.remove("your_key_to_delete");

        // Lưu thay đổi
        editor.apply();

        // Thông báo hoặc cập nhật giao diện nếu cần
        Toast.makeText(requireContext(), "Dữ liệu đã được xóa", Toast.LENGTH_SHORT).show();
        onResume();

        // Sau khi xóa dữ liệu, bạn có thể cập nhật giao diện hoặc làm bất kỳ công việc nào khác cần thiết ở đây
    }

    private void updateView(String language) {
        switch (language) {
            case "English":
                tvHistory.setText(getString(R.string.history_text_en));
                break;
            case "Vietnamese":
               tvHistory.setText(getString(R.string.history_text_vn));
                break;
            case "Japanese":
                tvHistory.setText(getString(R.string.history_text_jp));
                break;
            default:
                tvHistory.setText(getString(R.string.history_text_vn));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật danh sách lịch sử mỗi khi Fragment hiển thị lại
        updateHistoryList();
    }

    private void updateHistoryList() {
        List<History> historyList = new ArrayList<>();

        // Đọc dữ liệu từ SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("history_prefs", getContext().MODE_PRIVATE);
        int historyCount = sharedPreferences.getInt("history_count", 0);

        for (int i = 0; i < historyCount; i++) {
            String id = sharedPreferences.getString("id_" + i, "");
            String tenbenh = sharedPreferences.getString("tenbenh_" + i, "");
            int imageResource = sharedPreferences.getInt("image_resource_" + i, R.drawable.cachua); // Thay R.drawable.default_image bằng hình ảnh mặc định
            String lop = sharedPreferences.getString("lop", "");
            historyList.add(new History(id, tenbenh, imageResource, lop));
        }

        // Cập nhật RecyclerView với danh sách lịch sử mới
        HistoryAdapter historyAdapter = new HistoryAdapter(historyList, item -> {
            String id = item.getLop();
            fetchHistoryDetails(id);
            Intent intent = new Intent(requireContext(), Suggestion.class);
            Bundle bundle = new Bundle();
            bundle.putString("tenbenh", data);
            bundle.putString("trieuchung", data1);
            bundle.putString("dieutri", data2);
            intent.putExtra("mypackage", bundle);
            startActivity(intent);
        });



        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
    }

    // Hàm để lấy chi tiết lịch sử từ database dựa trên ID
    private void fetchHistoryDetails(String id) {
        database = requireContext().openOrCreateDatabase(DATABASE_NAME, android.content.Context.MODE_PRIVATE, null);
        Cursor cursor = database.query("tbgoiy", null, "id=?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            data3 = cursor.getString(0);
            data = cursor.getString(1); // Cột thứ 2 trong bảng
            data1 = cursor.getString(2); // Cột thứ 3 trong bảng
            data2 = cursor.getString(3); // Cột thứ 4 trong bảng
            Log.d("HistoryFragment", "Data: " + data + ", Data1: " + data1 + ", Data2: " + data2);
        } else {
            Log.e("HistoryFragment", "No data found for id: " + id);
        }
        cursor.close();
    }
}
