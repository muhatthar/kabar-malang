package com.example.kabarmalang.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kabarmalang.R;
import com.example.kabarmalang.profil.ProfilFragment;
import com.example.kabarmalang.upload.UploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    private int selectedTab = 1;
    private LinearLayout navBarLayout;
    private int selectedTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        replaceFragment(new HomeFragment());

        final LinearLayout homePageLayout = findViewById(R.id.home_page_layout);
        final LinearLayout profilLayout = findViewById(R.id.profil_layout);

        final ImageView homePageImage = findViewById(R.id.iv_home);
        final FloatingActionButton addButton = findViewById(R.id.btn_add);
        final ImageView profilPageImage = findViewById(R.id.iv_profil);

        final TextView homePageText = findViewById(R.id.tv_home);
        final TextView profilPageText = findViewById(R.id.tv_profil);

        navBarLayout = findViewById(R.id.navbar_layout);

        selectedTextColor = homePageText.getCurrentTextColor();

        addButton.setOnClickListener(v -> {
            Intent tambahBerita = new Intent(HomeActivity.this, UploadActivity.class);
            startActivity(tambahBerita);

            selectedTab = 2;
        });

        homePageLayout.setOnClickListener(v -> {
            if (selectedTab != 1) {
                profilPageImage.setImageResource(R.drawable.ic_profil);
                profilPageText.setTextColor(getColor(R.color.neutral_400));

                homePageImage.setImageResource(R.drawable.ic_home_solid);
                homePageText.setTextColor(getColor(R.color.primary_500));

                replaceFragment(new HomeFragment());

                selectedTab = 1;
            }
        });

        profilLayout.setOnClickListener(v -> {
            if (selectedTab != 3) {
                homePageImage.setImageResource(R.drawable.ic_home);
                homePageText.setTextColor(getColor(R.color.neutral_400));

                profilPageImage.setImageResource(R.drawable.ic_profil_solid);
                profilPageText.setTextColor(getColor(R.color.primary_500));

                replaceFragment(new ProfilFragment());

                selectedTab = 3;
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (selectedTab == 1) {
            finishAffinity();
        } else {
            if (!handleFragmentBackPressed()) {
                super.onBackPressed();
            }
        }
    }

    private boolean handleFragmentBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (fragment instanceof HomeFragment) {
            return ((HomeFragment) fragment).onBackPressed();
        }

        return false;
    }
}