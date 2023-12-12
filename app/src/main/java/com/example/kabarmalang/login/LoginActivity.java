package com.example.kabarmalang.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.example.kabarmalang.R;
import com.example.kabarmalang.homepage.HomeActivity;
import com.example.kabarmalang.model.userModel;
import com.example.kabarmalang.register.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_pw;
    private Button login_btn;
    private TextView tv_daftar;
    private AppCompatButton google_btn;
    private FirebaseAuth mAuth;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private boolean isPwVisible = false;
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        et_email = findViewById(R.id.login_email);
        et_pw = findViewById(R.id.login_pw);
        login_btn = findViewById(R.id.login_button);
        tv_daftar = findViewById(R.id.login_daftar);
        google_btn = findViewById(R.id.login_google);
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_btn.setOnClickListener(v -> {
            loginWithGoogle();
        });

        login_btn.setOnClickListener(v -> {
            login(et_email.getText().toString(), et_pw.getText().toString());
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
                                Toast.makeText(LoginActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                updateUI(user);
                            }
                        }). addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login Google gagal", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
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