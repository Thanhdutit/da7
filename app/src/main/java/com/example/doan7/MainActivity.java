package com.example.doan7;

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
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan7.ml.QuantizedModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String[] diseases = {"benh1", "benh2", "benh3"};
    private ImageButton imgBtDelete;
    private ImageView imgView, ibBackHome, imgAdd;
    private Button predict, suggestion;
    private TextView tvBenhChanDoan, tvAddImg1, tvAddImg2;
    private Bitmap image;
    private SQLiteDatabase database,database1;
    private String DATABASE_NAME = "goiy.db";
    private String DATABASE_NAME1 = "camnang.db";
    private String DB_PATH_SUFFIX = "/databases/";
    private String data = "";
    private String data1 = "";
    private String data2 = "";
    int imageSize = 128;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private String currentPhotoPath;
    private CardView cardView;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDatabase1();
        initViews();
        setupDatabase();
        setupPredictButton();
        setupSuggestionButton();

        ibBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        imgBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPreviousNotPrediction();
            }
        });



    }

    private void showImagePickerDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_image, null);


        Button btnCamera = view.findViewById(R.id.btnCamera);
        Button btnGallery = view.findViewById(R.id.btnGallery);

//        AlertDialog dialog = new AlertDialog.Builder(this)
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.TransparentDialog)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                            Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                    }

            } else if (requestCode == REQUEST_GALLERY) {
                clearPreviousPrediction();
                Uri selectedImage = data != null ? data.getData() : null;
                // Xử lý ảnh từ thư viện
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
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
        tvBenhChanDoan.setText("Bệnh Chuẩn đoán");

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
        tvBenhChanDoan.setText("Bệnh chuẩn đoán");
    }

    private void initViews() {
        imgView = findViewById(R.id.imageView);
        tvBenhChanDoan = findViewById(R.id.tvBenhChanDoan);
        predict = findViewById(R.id.btPredict);
        suggestion = findViewById(R.id.btSuggestion);
        ibBackHome = findViewById(R.id.ibBackHome);
        cardView = findViewById(R.id.cardView);
        tvAddImg1 = findViewById(R.id.tvAddImg1);
        tvAddImg2 = findViewById(R.id.tvAddImag2);
        imgAdd = findViewById(R.id.imgAdd);
        imgBtDelete = findViewById(R.id.imgBtDelete);

    }

    private void setupDatabase() {
        processCopy(DATABASE_NAME);
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

    }
    private void setupDatabase1() {
        processCopy(DATABASE_NAME1);
        database1 = openOrCreateDatabase(DATABASE_NAME1, MODE_PRIVATE, null);

    }

    private void setupSuggestionButton() {
        suggestion.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, Suggestion.class);
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
            QuantizedModel model = QuantizedModel.newInstance(getApplicationContext());

                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
                byteBuffer.order(ByteOrder.nativeOrder());

                int [] intValues = new int[imageSize * imageSize];
                image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

                // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
                int pixel = 0;
                for(int i = 0; i < imageSize; i++){
                    for(int j = 0; j < imageSize; j++){
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
                updateUIWithPrediction(maxIndex);
            }


            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCustomNotification(String message) {
        // Tạo và cấu hình cửa sổ thông báo tùy chỉnh
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
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

    private void updateUIWithPrediction(int maxIndex) {
        String[] selectionArgs = {String.valueOf(maxIndex)};
        Cursor c = database.query("tbgoiy", null, "id=?", selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            tvBenhChanDoan.setText(c.getString(1));
            data = c.getString(1);
            data1 = c.getString(2);
            data2 = c.getString(3);
        }

        c.close();
        suggestion.setVisibility(View.VISIBLE);
    }

    private void processCopy(String databaseName) {
        File dbFile = new File(getDatabasePath1(databaseName));
        if (!dbFile.exists()) {
            try {
                copyDatabaseFromAsset(databaseName);
                Toast.makeText(this, "Copying success from Assets folder", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath1(String databaseName) {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + databaseName;
    }

    private void copyDatabaseFromAsset(String databaseName) {
            try {
                InputStream myInput = getAssets().open(databaseName);
                String outFileName = getDatabasePath1(databaseName);
                File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
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