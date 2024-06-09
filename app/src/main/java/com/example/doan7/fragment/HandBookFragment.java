package com.example.doan7.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.doan7.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.regex.Pattern;


public class HandBookFragment extends Fragment {

    private TextView tvYeuCauChamSoc1, tvHuongDanTrong1, tvSauBenhVaDieuTri1, tvTen;
    private ImageButton imgBtCachua,imgBtNgo,imgBtLua,imgBtTao,imgBtOt,imgBtKhoaiTay,imgBtDauNanh,imgBtNho,imgBtDuaLeo,ImgBtDauTay;
    private SQLiteDatabase database;
    private ImageView img1,img2,img3;
    private SearchView searchView;
    private String DATABASE_NAME = "camnang.db";
    private String DB_PATH_SUFFIX = "/databases/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_handbook, container, false);
        initViews(view);
        setupDatabase();
        setupSearchView();
        updateUIWithPrediction("9");

        imgBtCachua.setOnClickListener(v ->{
            updateUIWithPrediction("1");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.cachua);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);

        });
        imgBtLua.setOnClickListener(v ->{
            updateUIWithPrediction("2");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.lua);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });
        imgBtDauNanh.setOnClickListener(v ->{
            updateUIWithPrediction("3");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.daunanh);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });
        ImgBtDauTay.setOnClickListener(v ->{
            updateUIWithPrediction("4");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.dautay);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });
        imgBtDuaLeo.setOnClickListener(v ->{
            updateUIWithPrediction("5");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.dualeo);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });
        imgBtKhoaiTay.setOnClickListener(v ->{
            updateUIWithPrediction("6");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.khoaitay);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });
        imgBtNho.setOnClickListener(v ->{
            updateUIWithPrediction("7");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.nho);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });
        imgBtOt.setOnClickListener(v ->{
            updateUIWithPrediction("8");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.otchuong);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });
        imgBtTao.setOnClickListener(v ->{
            updateUIWithPrediction("9");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.tao);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });
        imgBtNgo.setOnClickListener(v ->{
            updateUIWithPrediction("10");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ngo);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        });

        return view;
    }
    private void initViews(View view) {
        tvHuongDanTrong1 = view.findViewById(R.id.tvHuongDanTrong1);
        tvYeuCauChamSoc1 = view.findViewById(R.id.tvYeuCauChamSoc1);
        tvSauBenhVaDieuTri1 = view.findViewById(R.id.tvSauBenhVaDieuTri1);
        tvTen = view.findViewById(R.id.tvTen);
        imgBtCachua = view.findViewById(R.id.imgBtCachua);
        imgBtLua = view.findViewById(R.id.imgBtLua);
        imgBtDauNanh = view.findViewById(R.id.imgBtDauNanh);
        ImgBtDauTay = view.findViewById(R.id.imgBtDau);
        imgBtDuaLeo = view.findViewById(R.id.imgBtDuaLeo);
        imgBtKhoaiTay = view.findViewById(R.id.imgBtKhoaiTay);
        imgBtNho = view.findViewById(R.id.imgBtNho);
        imgBtOt = view.findViewById(R.id.imgBtOt);
        imgBtTao = view.findViewById(R.id.imgBtTao);
        imgBtNgo = view.findViewById(R.id.imgBtNgo);
        img1 = view.findViewById(R.id.imageView1);
        img2 = view.findViewById(R.id.imageView2);
        img3 = view.findViewById(R.id.imageView3);
        searchView = view.findViewById(R.id.searchView);


    }
    public void onResume() {
        super.onResume();
        // Đặt văn bản của SearchView thành rỗng
        searchView.setQuery("", false);
        // Clear focus của SearchView để không hiển thị bàn phím khi Fragment được tải lại
        searchView.clearFocus();
    }
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDatabase(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDatabase(newText);
                return true;
            }
        });
    }

    // Phương thức này sẽ được gọi khi người dùng thực hiện tìm kiếm
    private void searchDatabase(String query) {
        // Truy vấn cơ sở dữ liệu để tìm kiếm dựa trên 'ten'
        Cursor c = database.query("tb_camnang",
               null, // Chọn các cột cụ thể
                "ten LIKE ?", // Điều kiện tìm kiếm
                new String[]{"%" + query + "%"}, // Giá trị tìm kiếm
                null, // Không nhóm theo
                null, // Không có điều kiện nhóm
                null); // Không sắp xếp

        if (c.moveToFirst()) {
            // Lấy id và các giá trị khác từ Cursor
            int id = c.getInt(c.getColumnIndexOrThrow("id"));
            String ten = c.getString(c.getColumnIndexOrThrow("ten"));
            String yeucauchamsoc = c.getString(c.getColumnIndexOrThrow("yeucauchamsoc"));
            String huongdantrong = c.getString(c.getColumnIndexOrThrow("huongdantrong"));
            String saubenhvadieutri = c.getString(c.getColumnIndexOrThrow("saubenhvadieutri"));

            // Hiển thị kết quả tìm kiếm
            tvTen.setText(ten);
            tvYeuCauChamSoc1.setText(formatTextWithNewline(yeucauchamsoc));
            tvHuongDanTrong1.setText(formatTextWithNewline(huongdantrong));
            tvSauBenhVaDieuTri1.setText(formatTextWithNewline(saubenhvadieutri));
            updateImage(ten);

            // Hiển thị id của mục tìm kiếm
//            Toast.makeText(requireActivity(), "ID của kết quả tìm kiếm: " + id, Toast.LENGTH_LONG).show();
        } else {
            // Nếu không tìm thấy dữ liệu, hiển thị thông báo
            tvTen.setText("Không tìm thấy");
            tvYeuCauChamSoc1.setText("");
            tvHuongDanTrong1.setText("");
            tvSauBenhVaDieuTri1.setText("");
            img1.setImageResource(R.drawable.unnamed); // `default_image` là hình ảnh mặc định của bạn
            img2.setImageResource(R.drawable.unnamed);
            img3.setImageResource(R.drawable.unnamed);

        }

        // Đóng Cursor sau khi sử dụng
        c.close();
    }

    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    private void updateImage(String ten) {
        // Tạo tên tài nguyên dựa trên kết quả tìm kiếm
        String drawableName = removeAccents(ten).toLowerCase().replace(" ", ""); // Biến đổi tên thành dạng thích hợp cho tên tài nguyên

        // Lấy ID tài nguyên từ tên tài nguyên
        int drawableId = getResources().getIdentifier(drawableName, "drawable", getContext().getPackageName());

        // Kiểm tra xem tài nguyên có tồn tại hay không
        if (drawableId != 0) {
            // Nếu tồn tại, cập nhật hình ảnh
            Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
            img1.setImageDrawable(drawable);
            img2.setImageDrawable(drawable);
            img3.setImageDrawable(drawable);


        } else {
            // Nếu không tồn tại, bạn có thể hiển thị một hình ảnh mặc định hoặc thông báo lỗi
            img1.setImageResource(R.drawable.unnamed); // `default_image` là hình ảnh mặc định của bạn
            img2.setImageResource(R.drawable.unnamed);
            img3.setImageResource(R.drawable.unnamed);
        }
    }


    private void updateUIWithPrediction(String id) {
        Cursor c = database.query("tb_camnang", null, "id=?", new String[]{id}, null, null, null);
        if (c.moveToFirst()) {
            tvTen.setText(c.getString(1));
            tvYeuCauChamSoc1.setText(formatTextWithNewline(c.getString(2)));
            tvHuongDanTrong1.setText(formatTextWithNewline(c.getString(3)));
            tvSauBenhVaDieuTri1.setText(formatTextWithNewline(c.getString(4)));
        }
        c.close();
    }

    private SpannableString formatTextWithNewline(String text) {
        // Bỏ khoảng trắng đầu và cuối chuỗi nếu có

        text = text.trim();

        // Thêm dấu '-' vào đầu đoạn văn bản

        // Nếu bắt đầu với dấu gạch đầu dòng, giữ nguyên dấu gạch đầu dòng đầu tiên

        text = text.replace("-", "\n-");

        if (text.endsWith("\n-")) {
            text = text.substring(0, text.length() - 2);
        }
        text = "-" + text;
        return new SpannableString(text);
    }


    private void setupDatabase() {
        processCopy();
        database = requireContext().openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
    }

    private void processCopy() {
        File dbFile = requireContext().getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDatabaseFromAsset();
                Toast.makeText(requireActivity(), "Copying success from Assets folder", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return requireContext().getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    private void copyDatabaseFromAsset() {
        try {
            InputStream myInput = requireContext().getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File f = new File(requireContext().getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            } else {
                File oldFile = new File(outFileName);
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}