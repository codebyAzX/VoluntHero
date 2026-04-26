package com.example.volunthero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private TextInputEditText etUsername, etRegEmail, etRegPassword, etConfirmPassword;
    private TextInputLayout tilConfirmPassword;
    private TextView tvRegTitle, tvBackToLogin;
    private Button btnRegister;
    private CheckBox cbTerms;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        // Using explicit URL to ensure it connects to the correct European region
        mDatabase = FirebaseDatabase.getInstance("https://volunthero-cc4d8-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        tvRegTitle = findViewById(R.id.tvRegTitle);
        etUsername = findViewById(R.id.etUsername);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        cbTerms = findViewById(R.id.cbTerms);

        if (tvRegTitle != null) {
            String fullText = "VoluntHero";
            SpannableString spannable = new SpannableString(fullText);
            tvRegTitle.setText(spannable);

            tvRegTitle.post(() -> {
                if (tvRegTitle.getWidth() > 0) {
                    Shader shader = new LinearGradient(
                            0, 0, tvRegTitle.getWidth(), 0,
                            new int[]{Color.parseColor("#a86bff"), Color.parseColor("#38FFD7")},
                            null, Shader.TileMode.CLAMP);
                    tvRegTitle.getPaint().setShader(shader);
                    tvRegTitle.invalidate();
                }
            });
        }

        btnRegister.setOnClickListener(v -> registerUser());

        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String email = etRegEmail.getText() != null ? etRegEmail.getText().toString().trim() : "";
        String password = etRegPassword.getText() != null ? etRegPassword.getText().toString().trim() : "";
        String confirm = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            tilConfirmPassword.setError(getString(R.string.passwords_dont_match));
            return;
        } else {
            tilConfirmPassword.setError(null);
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fUser = mAuth.getCurrentUser();
                        if (fUser != null) {
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("uid", fUser.getUid());
                            userMap.put("username", username);
                            userMap.put("email", email);
                            userMap.put("status", "incomplete");

                            mDatabase.child("users").child(fUser.getUid())
                                    .setValue(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        SharedPreferences prefs = getSharedPreferences("VolunteerPrefs", MODE_PRIVATE);
                                        prefs.edit().putString("user_name", username).apply();

                                        fUser.sendEmailVerification();
                                        Toast.makeText(this, getString(R.string.verification_sent), Toast.LENGTH_LONG).show();
                                        
                                        Intent intent = new Intent(RegisterActivity.this, RoleSelectionActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Database Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(RegisterActivity.this, "Auth Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
