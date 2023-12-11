package com.example.kabarmalang.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kabarmalang.R;
import com.example.kabarmalang.edit.EditActivity;
import com.example.kabarmalang.googleMaps.GoogleMapsDetailActivity;
import com.example.kabarmalang.googleMaps.GoogleMapsEditActivity;
import com.example.kabarmalang.homepage.HomeActivity;
import com.google.android.material.imageview.ShapeableImageView;

public class DetailActivity extends AppCompatActivity {

    ShapeableImageView detailImage;
    TextView tvDetailTitle, tvDetailDesc, tvDetailAuthor, tvDetailDate, tvKoordinat, tvLokasi;
    ImageButton btnClose, detailMap;
    private static final int MAP_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailImage = findViewById(R.id.detail_image);
        tvDetailTitle = findViewById(R.id.detail_title);
        tvDetailDesc = findViewById(R.id.detail_deskripsi);
        tvDetailAuthor = findViewById(R.id.detail_author);
        tvDetailDate = findViewById(R.id.detail_date);
        tvLokasi = findViewById(R.id.tv_lokasi);
        tvKoordinat = findViewById(R.id.tv_koordinat);
        detailMap = findViewById(R.id.detail_map);
        btnClose = findViewById(R.id.btnClose);

        if (getIntent().getBundleExtra("beritaDetails") != null) {
            Bundle bundle = getIntent().getBundleExtra("beritaDetails");
            Integer id = bundle.getInt("row_id");
            String title = bundle.getString("title");
            String desc = bundle.getString("desc");
            String date = bundle.getString("date");
            byte[] bytes = bundle.getByteArray("img");
            String location = bundle.getString("loc");
            String latitude = bundle.getString("lat");
            String longitude = bundle.getString("lng");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            tvDetailTitle.setText(title);
            tvDetailDesc.setText(desc);
            tvDetailAuthor.setText("");
            tvDetailDate.setText(date);
            tvLokasi.setText(location);
            tvKoordinat.setText(latitude + ", " + longitude);
            detailImage.setImageBitmap(bitmap);
        }

        detailMap.setOnClickListener(v -> {
            String koordinat = tvKoordinat.getText().toString();
            String[] koordinatSplit = koordinat.split(", ");
            String latitude = koordinatSplit[0];
            String longitude = koordinatSplit[1];

            Bundle bundle = new Bundle();
            Intent editMap = new Intent(DetailActivity.this, GoogleMapsDetailActivity.class);
            bundle.putString("lat", latitude);
            bundle.putString("lng", longitude);
            editMap.putExtra("beritaLatLng", bundle);
            startActivityForResult(editMap, MAP_REQUEST_CODE);
        });

        btnClose.setOnClickListener(v -> {
            Intent close = new Intent(DetailActivity.this, HomeActivity.class);
        });

    }
}