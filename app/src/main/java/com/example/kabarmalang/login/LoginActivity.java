package com.example.kabarmalang.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kabarmalang.R;
import com.example.kabarmalang.homepage.HomeActivity;
import com.example.kabarmalang.model.userModel;
import com.example.kabarmalang.register.RegisterActivity;
import com.example.kabarmalang.upload.UploadActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_pw;
    private Button login_btn;
    private TextView tv_daftar;
    private FirebaseAuth mAuth;
    private userModel userModel;
    private boolean isPwVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        et_email = findViewById(R.id.login_email);
        et_pw = findViewById(R.id.login_pw);
        login_btn = findViewById(R.id.login_button);
        tv_daftar = findViewById(R.id.login_daftar);
        mAuth = FirebaseAuth.getInstance();

        Typeface customFont = ResourcesCompat.getFont(this, R.font.satoshi_regular);

        et_pw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_pw.getRight() - et_pw.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        isPwVisible = !isPwVisible;

                        if (isPwVisible) {
                            et_pw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            et_pw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                            et_pw.setTypeface(customFont);
                            et_pw.setTextColor(Color.parseColor("#101828"));
                            et_pw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        } else {
                            et_pw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            et_pw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                            et_pw.setTypeface(customFont);
                            et_pw.setTextColor(Color.parseColor("#101828"));
                            et_pw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        }

                        et_pw.setSelection(et_pw.length());

                        return true;
                    }
                }
                return false;
            }
        });

        String oriDaftar = getString(R.string.daftar);
        String targetDaftar ="Daftar";
        int startIndex = oriDaftar.indexOf(targetDaftar);
        int endIndex = startIndex + targetDaftar.length();

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(oriDaftar);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent daftar = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(daftar);
            }
        };

        spannableStringBuilder.setSpan(clickableSpan, startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_daftar.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        tv_daftar.setMovementMethod(LinkMovementMethod.getInstance());

        login_btn.setOnClickListener(v -> {
            login(et_email.getText().toString(), et_pw.getText().toString());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent login = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(login);
        } else {
            Toast.makeText(this, "Lakukan Login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validateForm() {
        boolean result = true;
        String password = et_pw.getText().toString();

        if (TextUtils.isEmpty(et_email.getText().toString())) {
            Toast.makeText(this, "Isi Email Anda terlebih dahulu", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (TextUtils.isEmpty(et_pw.getText().toString())) {
            Toast.makeText(this, "Isi Password Anda terlebih dahulu", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (password.length() < 8) {
            et_pw.setError("Minimal 8 karakter diperlukan");
            result = false;
        }

        return result;
    }

    public void login(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Email dan Password Anda tidak valid", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
}