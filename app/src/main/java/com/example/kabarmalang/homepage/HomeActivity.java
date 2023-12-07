package com.example.kabarmalang.homepage;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kabarmalang.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    private int selectedTab = 1;
    private LinearLayout navBarLayout;
    private int selectedTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final LinearLayout homePageLayout = findViewById(R.id.home_page_layout);
        final LinearLayout addLayout = findViewById(R.id.add_layout);
        final LinearLayout profilLayout = findViewById(R.id.profil_layout);

        final ImageView homePageImage = findViewById(R.id.iv_home);
        final FloatingActionButton addButton = findViewById(R.id.btn_add);
        final ImageView profilPageImage = findViewById(R.id.iv_profil);

        final TextView homePageText = findViewById(R.id.tv_home);
        final TextView profilPageText = findViewById(R.id.tv_profil);

        navBarLayout = findViewById(R.id.navbar_layout);

        selectedTextColor = homePageText.getCurrentTextColor();

        

    }
}