package com.example.kabarmalang;

import static com.example.kabarmalang.database.DBHelper.TABLE_NAME;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.kabarmalang.database.DBHelper;

import java.io.ByteArrayOutputStream;

public class UploadActivity extends AppCompatActivity {

    DBHelper db;
    SQLiteDatabase sqLiteDatabase;
    EditText etTitle, etDesc;
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
        btnUpload = findViewById(R.id.btnUpload);

        btnUpload.setOnClickListener(v -> {
            ContentValues cv = new ContentValues();
            cv.put("title", etTitle.getText().toString());
            cv.put("desc", etDesc.getText().toString());
            cv.put("imageBerita", ImageViewToByte(imageBerita));

            sqLiteDatabase = db.getWritableDatabase();
            Long insert = sqLiteDatabase.insert(TABLE_NAME, null, cv);
            if (insert != null) {
                Toast.makeText(UploadActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();

                imageBerita.setImageResource(R.id. );
                etTitle.setText("");
            }
        });

    }

    private byte[] ImageViewToByte(ImageView imageBerita){
        Bitmap bitmap = ((BitmapDrawable) imageBerita.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;

    }
}