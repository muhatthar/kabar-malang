package com.example.kabarmalang.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kabarmalang.R;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_pw;
    private Button login_btn;
    private TextView tv_daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}