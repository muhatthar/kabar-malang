package com.example.kabarmalang.upload;

import static com.example.kabarmalang.database.DBHelper.TABLE_NAME;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.kabarmalang.database.DBHelper;

import java.io.ByteArrayOutputStream;

import com.example.kabarmalang.R;
import com.example.kabarmalang.homepage.HomeActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class UploadActivity extends AppCompatActivity {

    DBHelper db;
    SQLiteDatabase sqLiteDatabase;
    EditText etTitle, etDesc;
    ImageView btnBack;
    ShapeableImageView imageBerita;
    AppCompatButton btnUpload;
    int id = 0;

    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;

    String[] cameraPermission;
    String[] storagePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        db = new DBHelper(this);
        etTitle = findViewById(R.id.ET_Judul);
        etDesc = findViewById(R.id.ET_Deskripsi);
        imageBerita = findViewById(R.id.upload_image);
        btnUpload = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(UploadActivity.this, HomeActivity.class);
            startActivity(back);
        });

        btnUpload.setOnClickListener(v -> {
            ContentValues cv = new ContentValues();
            cv.put("berita_title", etTitle.getText().toString());
            cv.put("berita_desc", etDesc.getText().toString());
            cv.put("berita_img", ImageViewToByte(imageBerita));

            sqLiteDatabase = db.getWritableDatabase();
            Long insert = sqLiteDatabase.insert(TABLE_NAME, null, cv);
            if (insert != null) {
                Toast.makeText(UploadActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();

                imageBerita.setImageResource(R.mipmap.ic_launcher);
                etTitle.setText("");
                etDesc.setText("");

                Intent back2 = new Intent(UploadActivity.this, HomeActivity.class);
                startActivity(back2);
            }
        });

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imagePick();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void imagePick() {
        imageBerita.setOnClickListener(v -> {
            int image = 0;
            if (image == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromGallery();
                }
            } else if (image == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                }
                
            } else {
                pickFromGallery();
            }
        });
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    private byte[] ImageViewToByte(ImageView imageBerita){
        Bitmap bitmap = ((BitmapDrawable) imageBerita.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST:{
                if (grantResults.length > 0) {
                    boolean camera_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accept && storage_accept) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{
                if (grantResults.length > 0) {
                    boolean storage_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storage_accept) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get().load(resultUri).into(imageBerita);
            }
        }
    }
}