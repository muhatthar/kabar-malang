package com.example.kabarmalang.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kabarmalang.R;
import com.example.kabarmalang.homepage.HomeActivity;
import com.google.android.material.imageview.ShapeableImageView;

public class DetailActivity extends AppCompatActivity {

    ShapeableImageView detailImage;
    TextView tvDetailTitle, tvDetailDesc, tvDetailAuthor, tvDetailDate;
    ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailImage = findViewById(R.id.detail_image);
        tvDetailTitle = findViewById(R.id.detail_title);
        tvDetailDesc = findViewById(R.id.detail_deskripsi);
        tvDetailAuthor = findViewById(R.id.detail_author);
        tvDetailDate = findViewById(R.id.detail_date);
        btnClose = findViewById(R.id.btnClose);

        if (getIntent().getBundleExtra("beritaDetails") != null) {
            Bundle bundle = getIntent().getBundleExtra("beritaDetails");
            Integer id = bundle.getInt("row_id");
            String title = bundle.getString("title");
            String desc = bundle.getString("desc");
            String date = bundle.getString("date");
            byte[] bytes = bundle.getByteArray("img");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            tvDetailTitle.setText(title);
            tvDetailDesc.setText(desc);
            tvDetailAuthor.setText("");
            tvDetailDate.setText(date);
            detailImage.setImageBitmap(bitmap);
        }

        btnClose.setOnClickListener(v -> {
            Intent close = new Intent(DetailActivity.this, HomeActivity.class);
        });

    }
}