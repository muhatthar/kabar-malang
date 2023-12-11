package com.example.kabarmalang.upload;

import static com.example.kabarmalang.database.DBHelper.TABLE_NAME;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.kabarmalang.R;
import com.example.kabarmalang.database.DBHelper;
import com.example.kabarmalang.googleMaps.GoogleMapsActivity;
import com.example.kabarmalang.homepage.HomeActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

public class UploadActivity extends AppCompatActivity {

    DBHelper db;
    SQLiteDatabase sqLiteDatabase;
    EditText etTitle, etDesc;
    ImageButton btnBack, upload_map;
    TextView tvKoordinat, tvLokasi;
    ShapeableImageView imageBerita;
    AppCompatButton btnUpload;
    int id = 0;

    private static final int MAP_REQUEST_CODE = 102;
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;

    private LatLng selectedLatLng;

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
        upload_map = findViewById(R.id.upload_map);
        tvKoordinat = findViewById(R.id.tv_koordinat);
        tvLokasi = findViewById(R.id.tv_lokasi);

        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(UploadActivity.this, HomeActivity.class);
            startActivity(back);
        });

        upload_map.setOnClickListener(v -> {
            Intent mapIntent = new Intent(UploadActivity.this, GoogleMapsActivity.class);
            startActivityForResult(mapIntent, MAP_REQUEST_CODE);
        });

        btnUpload.setOnClickListener(v -> {

            String getTitle = etTitle.getText().toString();
            String getDesc = etDesc.getText().toString();
            String getLocation = tvLokasi.getText().toString();
            String getLatLng = tvKoordinat.getText().toString();

            if (getTitle.isEmpty()) {
                etTitle.setError("Masukkan Judul Berita");
            } else if (getDesc.isEmpty()) {
                etDesc.setError("Masukkan Deskripsi Berita");
            } else if (getLatLng.isEmpty()) {
                etDesc.setError("Masukkan Lokasi");
            } else if (getLocation.isEmpty()) {
                etDesc.setError("Masukkan Lokasi");
            } else if (imageBerita.getDrawable() == null || getBitmapFromImageView(imageBerita) == null) {
                Toast.makeText(UploadActivity.this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
            } else {
                String koordinat = tvKoordinat.getText().toString();
                String[] koordinatSplit = koordinat.split(", ");
                String latitude = koordinatSplit[0];
                String longitude = koordinatSplit[1];

                ContentValues cv = new ContentValues();
                cv.put("berita_title", getTitle);
                cv.put("berita_desc", getDesc);
                cv.put("berita_img", ImageViewToByte(imageBerita));
                cv.put("berita_location", getLocation);
                cv.put("berita_latitude", latitude);
                cv.put("berita_longitude", longitude);

                sqLiteDatabase = db.getWritableDatabase();
                Long insert = sqLiteDatabase.insert(TABLE_NAME, null, cv);
                if (insert != null) {
                    Toast.makeText(UploadActivity.this, "Upload Berhasil", Toast.LENGTH_SHORT).show();

                    imageBerita.setImageResource(R.mipmap.ic_launcher);
                    etTitle.setText("");
                    etDesc.setText("");
                    tvLokasi.setText("");
                    tvKoordinat.setText("");

                    Intent back2 = new Intent(UploadActivity.this, HomeActivity.class);
                    startActivity(back2);
                }
            }

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String storedTitle = sharedPreferences.getString("title", "");
            String storedDesc = sharedPreferences.getString("desc", "");
        });

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imagePick();
    }

    private Bitmap getBitmapFromImageView(ImageView imageView) {
        if (imageView.getDrawable() instanceof BitmapDrawable) {
            return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void imagePick() {
        imageBerita.setOnClickListener(v -> {
            if (!checkCameraPermission() || !checkStoragePermission()) {
                requestPermissions(cameraPermission, CAMERA_REQUEST);
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
        Drawable drawable = imageBerita.getDrawable();

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            return stream.toByteArray();
        } else if (drawable instanceof VectorDrawable) {
            // Handle VectorDrawable conversion to a byte array if needed
            // For example, you can convert it to a bitmap first
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] bytes = stream.toByteArray();
            return bytes;
        }
        return new byte[0];
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
                        Toast.makeText(this, "Hidupkan Akses Kamera & Storage", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Hidupkan Akses Storage", Toast.LENGTH_SHORT).show();
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

        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            LatLng selectedLatLng = data.getParcelableExtra("selectedLatLng");
            String placeName = data.getStringExtra("placeName");

            if (selectedLatLng != null) {
                String getLatitude = String.valueOf(selectedLatLng.latitude);
                String getLongitude = String.valueOf(selectedLatLng.longitude);

                tvKoordinat.setText(getLatitude + ", " + getLongitude);
                tvLokasi.setText(placeName);

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("title", etTitle.getText().toString());
                editor.putString("desc", etDesc.getText().toString());
                editor.apply();
            }
        }
    }
}