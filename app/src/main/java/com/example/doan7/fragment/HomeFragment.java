package com.example.doan7.fragment;
import static android.content.Context.MODE_PRIVATE;

import com.example.doan7.MyApplication;
import com.example.doan7.fragment.HistoryFragment;
import com.example.doan7.adapter.HistoryAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import org.tensorflow.lite.DataType;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan7.Home;
import com.example.doan7.ml.QuantizedModel;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.ByteOrder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.ViewGroup;

import com.example.doan7.MainActivity;
import com.example.doan7.R;
import com.example.doan7.Suggestion;


public class HomeFragment extends Fragment {
    private String[] diseases = {"benh1", "benh2", "benh3"};
    private ImageButton imgBtDelete;
    private ImageView imgView, ibBackHome, imgAdd;
    private Button predict, suggestion,btnCamera, btnGallery;
    private TextView tvBenhChanDoan, tvAddImg1, tvAddImg2,tv,tv7,tv9,tvChoseImg;
    private Bitmap image;
    private SQLiteDatabase database;
    private String DATABASE_NAME = "goiy.db";
    private String DB_PATH_SUFFIX = "/databases/";
    private String data = "";
    private String data1 = "";
    private String data2 = "";
    private String data3="" ;
    int imageSize = 128;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private String currentPhotoPath;
    private CardView cardView;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    String selectedLanguage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for requireActivity() fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        selectedLanguage = MyApplication.getSelectedLanguage();
        // Initialize views
        initViews(view);

        // Setup database
        setupDatabase();

        // Setup predict button
        setupPredictButton();

        // Setup suggestion button
        setupSuggestionButton();

        updateTextView(selectedLanguage);

        // Setup image picker dialog
        imgAdd.setOnClickListener(v -> showImagePickerDialog());

        // Setup delete button
        imgBtDelete.setOnClickListener(v -> clearPreviousNotPrediction());



        ibBackHome.setOnClickListener(v -> {
//                Intent myIntent = new Intent(requireActivity(), Home.class);
//                startActivity(myIntent);
                getActivity().finish();
            });


        return view;
    }

    private void initViews(View view) {
        imgView = view.findViewById(R.id.imageView);
        tvBenhChanDoan = view.findViewById(R.id.tvBenhChanDoan);
        predict = view.findViewById(R.id.btPredict);
        suggestion = view.findViewById(R.id.btSuggestion);
        ibBackHome = view.findViewById(R.id.ibBackHome);
        tvAddImg1 = view.findViewById(R.id.tvAddImg1);
        tvAddImg2 = view.findViewById(R.id.tvAddImag2);
        imgAdd = view.findViewById(R.id.imgAdd);
        imgBtDelete = view.findViewById(R.id.imgBtDelete);
        tv = view.findViewById((R.id.textView));
        tv7 = view.findViewById((R.id.textView7));
        tv9 = view.findViewById((R.id.textView9));


    }

    private void showImagePickerDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireActivity());
        View view = inflater.inflate(R.layout.dialog_image, null);
//        AlertDialog dialog = new AlertDialog.Builder(requireActivity())

        tvChoseImg = view.findViewById((R.id.tvChoseImg));
        btnCamera = view.findViewById(R.id.btnCamera);
        btnGallery = view.findViewById(R.id.btnGallery);

        updateDialog(selectedLanguage);


        AlertDialog dialog = new AlertDialog.Builder(requireActivity(), R.style.TransparentDialog)
                .setView(view)
                .create();

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                dialog.dismiss();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else if (requestCode == REQUEST_GALLERY && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                //xóa dữ liệu trước đó
                clearPreviousPrediction();
                Bundle extras = data != null ? data.getExtras() : null;
                if (extras != null && extras.containsKey("data")) {
                    image = (Bitmap) extras.get("data");
                    if (image != null) {
                        imgView.setImageBitmap(image);
                        image = Bitmap.createScaledBitmap(image, 128, 128, true);
                        // Xử lý ảnh từ camera
                    } else {
                        Toast.makeText(requireActivity(), "Failed to capture image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireActivity(), "Failed to capture image", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == REQUEST_GALLERY) {
                clearPreviousPrediction();
                Uri selectedImage = data != null ? data.getData() : null;
                // Xử lý ảnh từ thư viện
                try {
                    image = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                    imgView.setImageBitmap(image);
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearPreviousPrediction() {

        tvAddImg1.setVisibility(View.INVISIBLE);
        tvAddImg2.setVisibility(View.INVISIBLE);
        imgAdd.setImageDrawable(null);
        imgBtDelete.setVisibility(View.VISIBLE);
        // Ẩn nút gợi ý
        suggestion.setVisibility(View.GONE);
        //tvBenhChanDoan.setText("Bệnh Chuẩn đoán");
        updateTextView(selectedLanguage);

    }

    private void clearPreviousNotPrediction() {
        imgBtDelete.setVisibility(View.INVISIBLE);
        imgView.setImageDrawable(null);
        imgView.setImageResource(R.drawable.img3);
        image = null;
        imgAdd.setImageResource(R.drawable.photo);
        tvAddImg1.setVisibility(View.VISIBLE);
        tvAddImg2.setVisibility(View.VISIBLE);
        suggestion.setVisibility(View.INVISIBLE);
        //tvBenhChanDoan.setText("Bệnh chuẩn đoán");
        updateTextView(selectedLanguage);
    }


    private void setupDatabase() {
        processCopy();
        database = requireContext().openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
    }

    private void setupSuggestionButton() {
        suggestion.setOnClickListener(v -> {
            Intent myIntent = new Intent(requireActivity(), Suggestion.class);
            Bundle myBundle = new Bundle();
            myBundle.putString("tenbenh", data);
            myBundle.putString("trieuchung", data1);
            myBundle.putString("dieutri", data2);
            myIntent.putExtra("mypackage", myBundle);
            startActivity(myIntent);
        });
    }

    private void makePrediction(Bitmap image) {
        try {
            QuantizedModel model = QuantizedModel.newInstance(requireContext().getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            QuantizedModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Print all prediction values
            float[] outputArray = outputFeature0.getFloatArray();
            Log.e("Prediction", "All Prediction Values:");
            for (int i = 0; i < outputArray.length; i++) {
                Log.e("Prediction", "Index " + i + ": " + outputArray[i]);
            }


            int maxIndex = getMaxIndex(outputArray);
            float maxPrediction = outputArray[maxIndex];

            if (maxPrediction < 0.8) {

                showCustomNotification("Không nhận diện được bệnh, ảnh chụp không đúng hoặc bệnh không có trong dữ liệu, vui lòng chụp lại");
                clearPreviousNotPrediction();
            } else {
                updateUIWithPrediction(maxIndex, maxPrediction);

            }


            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showCustomNotification(String message) {
        // Tạo và cấu hình cửa sổ thông báo tùy chỉnh
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle("Thông báo");
        builder.setMessage(message);
        builder.setCancelable(true);


        // Tạo và hiển thị cửa sổ thông báo
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setupPredictButton() {
        predict.setOnClickListener(v -> {
            if (image != null) {
                makePrediction(image);
            } else {
                Toast.makeText(requireActivity(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getMaxIndex(float[] outputArray) {
        int maxIndex = 0;
        float maxPrediction = outputArray[0];
        for (int i = 1; i < outputArray.length; i++) {
            if (outputArray[i] > maxPrediction) {
                maxPrediction = outputArray[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }


private void updateUIWithPrediction(int maxIndex, float maxPrediction) {
    // Lấy ngôn ngữ đã chọn từ SharedPreferences hoặc biến toàn cục


    // Xác định tên các cột dựa trên ngôn ngữ đã chọn
    String tenBenhColumn;
    String trieuChungColumn;
    String cachDieuTriColumn;

    switch (selectedLanguage) {
        case "English":
            tenBenhColumn = "tenbenh_en";
            trieuChungColumn = "trieuchung_en";
            cachDieuTriColumn = "chuatri_en";
            break;
        case "Japanese":
            tenBenhColumn = "tenbenhjp";
            trieuChungColumn = "trieuchung_jp";
            cachDieuTriColumn = "chuatri_jp";
            break;
        case "Vietnamese":
        default:
            tenBenhColumn = "tenbenh";
            trieuChungColumn = "trieuchung";
            cachDieuTriColumn = "chuatri";
            break;
    }

    String[] selectionArgs = {String.valueOf(maxIndex)};
    Cursor c = database.query("tbgoiy", new String[]{"id", tenBenhColumn, trieuChungColumn, cachDieuTriColumn}, "id=?", selectionArgs, null, null, null);

    if (c.moveToFirst()) {
        data3 = c.getString(0); // ID
        data = c.getString(1);  // Tên bệnh theo ngôn ngữ đã chọn
        data1 = c.getString(2); // Triệu chứng theo ngôn ngữ đã chọn
        data2 = c.getString(3); // Cách điều trị theo ngôn ngữ đã chọn

        // Lưu dữ liệu vào SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("history_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int historyCount = sharedPreferences.getInt("history_count", 0);
        String id = "" + historyCount;

        // Lưu thông tin của lần nhận diện vào SharedPreferences
        editor.putString("id_" + historyCount, id);
        editor.putString("tenbenh_" + historyCount, data);
        editor.putString("trieuchung_" + historyCount, data1);
        editor.putString("dieutri_" + historyCount, data2);
        if (Integer.valueOf(data3)>= 0 && Integer.valueOf(data3) <= 3) {
            editor.putInt("image_resource_" + historyCount, R.drawable.ngo); // Cập nhật hình ảnh đúng
        } else if (Integer.valueOf(data3)==4) {
            editor.putInt("image_resource_" + historyCount, R.drawable.bi);
        } else if (Integer.valueOf(data3) >=5 && Integer.valueOf(data3) <=14) {
            editor.putInt("image_resource_" + historyCount, R.drawable.cachua);
        } else if (Integer.valueOf(data3) >=15 && Integer.valueOf(data3) <=18) {
            editor.putInt("image_resource_" + historyCount, R.drawable.daunanh);
        } else if (Integer.valueOf(data3) >=19 && Integer.valueOf(data3) <=20) {
            editor.putInt("image_resource_" + historyCount, R.drawable.dautay);
        } else if (Integer.valueOf(data3) >=21 && Integer.valueOf(data3) <=22) {
            editor.putInt("image_resource_" + historyCount, R.drawable.dualeo);
        } else if (Integer.valueOf(data3) >=23 && Integer.valueOf(data3) <=25) {
            editor.putInt("image_resource_" + historyCount, R.drawable.khoaitay);
        } else if (Integer.valueOf(data3) >=26 && Integer.valueOf(data3) <=29) {
            editor.putInt("image_resource_" + historyCount, R.drawable.lua);
        } else if (Integer.valueOf(data3) >=30 && Integer.valueOf(data3) <=33) {
            editor.putInt("image_resource_" + historyCount, R.drawable.nho);
        } else if (Integer.valueOf(data3) >=34 && Integer.valueOf(data3) <=35) {
            editor.putInt("image_resource_" + historyCount, R.drawable.otchuong);
        } else {
            // Trường hợp mặc định nếu id không nằm trong các khoảng trên
            editor.putInt("image_resource_" + historyCount, R.drawable.cachua);
        }


        editor.putString("lop_" + historyCount, data3);

        // Tăng số lần nhận diện lên 1
        editor.putInt("history_count", historyCount + 1);
        editor.apply();

        tvBenhChanDoan.setText(data);
    }

    c.close();
    suggestion.setVisibility(View.VISIBLE);
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
    private void updateTextView(String language) {
        switch (language) {
            case "English":
                tv.setText(getString(R.string.home_text_en));
                tv7.setText(getString(R.string.home_text1_en));
                tv9.setText(getString(R.string.home_text2_en));
                tvAddImg1.setText(getString(R.string.home_text3_en));
                tvAddImg2.setText(getString(R.string.home_text4_en));
                predict.setText(getString(R.string.home_text5_en));
                tvBenhChanDoan.setText(getString(R.string.home_text6_en));
                suggestion.setText(getString(R.string.home_text7_en));


                break;
            case "Vietnamese":
                tv.setText(getString(R.string.home_text_vn));
                tv7.setText(getString(R.string.home_text1_vn));
                tv9.setText(getString(R.string.home_text2_vn));
                tvAddImg1.setText(getString(R.string.home_text3_vn));
                tvAddImg2.setText(getString(R.string.home_text4_vn));
                predict.setText(getString(R.string.home_text5_vn));
                tvBenhChanDoan.setText(getString(R.string.home_text6_vn));
                suggestion.setText(getString(R.string.home_text7_vn));


                break;
            case "Japanese":
                tv.setText(getString(R.string.home_text_jp));
                tv7.setText(getString(R.string.home_text1_jp));
                tv9.setText(getString(R.string.home_text2_jp));
                tvAddImg1.setText(getString(R.string.home_text3_jp));
                tvAddImg2.setText(getString(R.string.home_text4_jp));
                predict.setText(getString(R.string.home_text5_jp));
                tvBenhChanDoan.setText(getString(R.string.home_text6_jp));
                suggestion.setText(getString(R.string.home_text7_jp));



                break;
            default:
                tv.setText(getString(R.string.home_text_vn));
                tv7.setText(getString(R.string.home_text1_vn));
                tv9.setText(getString(R.string.home_text2_vn));
                tvAddImg1.setText(getString(R.string.home_text3_vn));
                tvAddImg2.setText(getString(R.string.home_text4_vn));
                predict.setText(getString(R.string.home_text5_vn));
                tvBenhChanDoan.setText(getString(R.string.home_text6_vn));
                suggestion.setText(getString(R.string.home_text7_vn));

                break;
        }
    }

    private void updateDialog(String language) {
        switch (language) {
            case "English":
                btnCamera.setText(getString(R.string.dialogImage_Camera_en));
                btnGallery.setText(getString(R.string.dialogImage_Library_en));
                tvChoseImg.setText(getString(R.string.dialogImage_en));
                break;
            case "Vietnamese":
                btnCamera.setText(getString(R.string.dialogImage_Camera_vn));
                btnGallery.setText(getString(R.string.dialogImage_Library_vn));
                tvChoseImg.setText(getString(R.string.dialogImage_vn));
                break;
            case "Japanese":
                btnCamera.setText(getString(R.string.dialogImage_Camera_jp));
                btnGallery.setText(getString(R.string.dialogImage_Library_jp));
                tvChoseImg.setText(getString(R.string.dialogImage_jp));
                break;
            default:
                btnCamera.setText(getString(R.string.dialogImage_Camera_vn));
                btnGallery.setText(getString(R.string.dialogImage_Library_vn));
                tvChoseImg.setText(getString(R.string.dialogImage_vn));
                break;
        }
    }
}