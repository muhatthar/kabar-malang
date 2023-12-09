package com.example.kabarmalang.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.example.kabarmalang.R;
import com.example.kabarmalang.homepage.HomeActivity;
import com.example.kabarmalang.login.LoginActivity;
import com.example.kabarmalang.model.userModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_nama, et_email, et_pw, et_repw;
    private TextView tv_login;
    private Button btn_daftar;
    private AppCompatButton google_btn;
    private FirebaseAuth mAuth;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private boolean isPwVisible = false;
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_nama = findViewById(R.id.daftar_nama);
        et_email = findViewById(R.id.daftar_email);
        et_pw = findViewById(R.id.daftar_pw);
        et_repw = findViewById(R.id.daftar_repw);
        btn_daftar = findViewById(R.id.daftar_button);
        tv_login = findViewById(R.id.daftar_login);
        google_btn = findViewById(R.id.daftar_google);

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

        et_repw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_repw.getRight() - et_repw.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        isPwVisible = !isPwVisible;

                        if (isPwVisible) {
                            et_repw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            et_repw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                            et_repw.setTypeface(customFont);
                            et_repw.setTextColor(Color.parseColor("#101828"));
                            et_repw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        } else {
                            et_repw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            et_repw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                            et_repw.setTypeface(customFont);
                            et_repw.setTextColor(Color.parseColor("#101828"));
                            et_repw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        }

                        et_repw.setSelection(et_repw.length());

                        return true;
                    }
                }
                return false;
            }
        });

        String oriLogin = getString(R.string.masuk);
        String targetLogin ="Masuk";
        int startIndex = oriLogin.indexOf(targetLogin);
        int endIndex = startIndex + targetLogin.length();

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(oriLogin);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
            }
        };

        spannableStringBuilder.setSpan(clickableSpan, startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_login.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        tv_login.setMovementMethod(LinkMovementMethod.getInstance());

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_btn.setOnClickListener(v -> {
            loginWithGoogle();
        });

        btn_daftar.setOnClickListener(v -> {
            register(et_email.getText().toString(), et_pw.getText().toString());
        });
    }

    private void loginWithGoogle() {
        Intent googleLogin = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleLogin, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(acc);
        } catch (ApiException e) {
            Toast.makeText(this, "Login Google gagal", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acc) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    String userId = user.getUid();
                    String nama = user.getDisplayName();

                    DatabaseReference userRef = database.child("Users").child(userId).child("UserData");
                    userModel userModel = new userModel(nama);
                    userRef.setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            Intent googleIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(googleIntent);
                        }
                    }). addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Login Google gagal", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(login);
        } else {
            Toast.makeText(this, "Daftar akun terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        boolean result = true;
        String password = et_pw.getText().toString();

        if (TextUtils.isEmpty(et_email.getText().toString())){
            Toast.makeText(this, "Isi Email Anda terlebih dahulu", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (TextUtils.isEmpty(et_nama.getText().toString())){
            Toast.makeText(this, "Isi Nama Anda terlebih dahulu", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (TextUtils.isEmpty(et_pw.getText().toString())){
            Toast.makeText(this, "Isi Password Anda terlebih dahulu", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (TextUtils.isEmpty(et_repw.getText().toString())){
            Toast.makeText(this, "Isi Konfirmasi Password Anda terlebih dahulu", Toast.LENGTH_SHORT).show();
            result = false;
        } else if (!TextUtils.isEmpty(et_repw.getText().toString()) && !et_repw.getText().toString().equals(et_pw.getText().toString())) {
            Toast.makeText(this, "Cek kembali Konfirmasi Password Anda", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (password.length() < 8) {
            et_pw.setError("Minimal 8 Karakter Diperlukan");
            result = false;
        }

        return result;
    }

    public void register(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                String userId = user.getUid();
                                String nama = et_nama.getText().toString();

                                DatabaseReference userRef = database.child("Users").child(userId).child("UserData");
                                userModel userModel = new userModel(nama);
                                userRef.setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(RegisterActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                        updateUI(user);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Email dan Password Anda tidak valid", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
}