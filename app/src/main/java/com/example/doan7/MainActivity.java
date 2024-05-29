package com.example.doan7;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan7.ml.QuantizedModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

        private String[] diseases = {"benh1", "benh2", "benh3"};
        private ImageView imgView;
        private Button  predict, suggestion;
        private TextView tv;
        private Bitmap img;
        private SQLiteDatabase database;
        private String DATABASE_NAME = "goiy.db";
        private String DB_PATH_SUFFIX = "/databases/";
        private String data1 = "";
        private String data2 = "";

        private ImageButton imgCam, imgAnh;
        private ImageView fullscreen_image;
        private ActivityResultLauncher<String> requestCameraPermissionLauncher;
        private ActivityResultLauncher<Intent> cameraActivityResultLauncher;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            initViews();
            setupDatabase();
            setupImagePicker();
            setupPredictButton();
            setupImageCapture();
            setupSuggestionButton();
        }

        private void initViews() {
            imgView = findViewById(R.id.imageView);
            tv = findViewById(R.id.textView);
            fullscreen_image = findViewById(R.id.fullscreen_image);

            predict = findViewById(R.id.button2);
            suggestion = findViewById(R.id.suggestionButton);
            imgCam = findViewById(R.id.imgCam);
            imgAnh = findViewById(R.id.imageAnh);
        }

        private void setupDatabase() {
            processCopy();
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        }

        private void setupImagePicker() {
            ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();
                            imgView.setImageURI(selectedImageUri);

                            try {
                                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );

            imgAnh.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            });
        }

    private void setupImageCapture() {
        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        launchCamera();
                    } else {
                        Toast.makeText(MainActivity.this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Initialize the camera result launcher
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Get the captured photo
                        Bundle extras = result.getData().getExtras();
                        if (extras != null && extras.containsKey("data")) {
                            Bitmap photo = (Bitmap) extras.get("data");
                            // Convert the photo to ARGB_8888 format
                            Bitmap argbPhoto = convertBitmap(photo);
                            imgView.setImageBitmap(argbPhoto);
                            img = argbPhoto;  // Set the captured photo to img

                            // Trigger the prediction process with the captured photo
                            if (img != null) {
                                img = Bitmap.createScaledBitmap(img, 128, 128, true);

                            } else {
                                Toast.makeText(this, "Please capture an image first", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        imgCam.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            } else {
                launchCamera();
            }
        });
    }

    private Bitmap convertBitmap(Bitmap inputBitmap) {
        return inputBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }


    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraActivityResultLauncher.launch(cameraIntent);
        }
    }

        private void setupPredictButton() {
            predict.setOnClickListener(v -> {
                if (img != null) {
                    img = Bitmap.createScaledBitmap(img, 128, 128, true);
                    makePrediction();
                } else {
                    Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void setupSuggestionButton() {
            suggestion.setOnClickListener(v -> {
                Intent myIntent = new Intent(MainActivity.this, Suggestion.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("trieuchung", data1);
                myBundle.putString("dieutri", data2);
                myIntent.putExtra("mypackage", myBundle);
                startActivity(myIntent);
            });
        }

        private void makePrediction() {
            try {
                QuantizedModel model = QuantizedModel.newInstance(getApplicationContext());

                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);

                TensorImage tensorImage = new TensorImage(DataType.FLOAT32);

                tensorImage.load(img);
                ByteBuffer byteBuffer = tensorImage.getBuffer();
                inputFeature0.loadBuffer(byteBuffer);

                QuantizedModel.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                model.close();

                float[] outputArray = outputFeature0.getFloatArray();
                int maxIndex = getMaxIndex(outputArray);

                updateUIWithPrediction(maxIndex);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
                tv.setText(c.getString(1));
                data1 = c.getString(2);
                data2 = c.getString(3);
            }

            c.close();
            suggestion.setVisibility(View.VISIBLE);
        }

        private void processCopy() {
            File dbFile = getDatabasePath(DATABASE_NAME);
            if (!dbFile.exists()) {
                try {
                    copyDatabaseFromAsset();
                    Toast.makeText(this, "Copying success from Assets folder", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }

        private String getDatabasePath() {
            return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
        }

        private void copyDatabaseFromAsset() {
            try {
                InputStream myInput = getAssets().open(DATABASE_NAME);
                String outFileName = getDatabasePath();
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