package com.example.volunthero;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends BaseActivity {

    private TextInputEditText etUsername, etRegEmail, etRegPassword, etConfirmPassword;
    private TextInputLayout tilConfirmPassword;
    private TextView tvRegTitle, tvBackToLogin;
    private Button btnRegister;
    private CheckBox cbTerms;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        tvRegTitle = findViewById(R.id.tvRegTitle);
        etUsername = findViewById(R.id.etUsername);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        cbTerms = findViewById(R.id.cbTerms);
        String fullText = "hello!";

        SpannableString spannable = new SpannableString(fullText);

        spannable.setSpan(new StyleSpan(Typeface.ITALIC), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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

        btnRegister.setOnClickListener(v -> registerUser());
        tvBackToLogin.setOnClickListener(v -> finish());
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

        //Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification().addOnCompleteListener(verifyTask -> {
                                Toast.makeText(this, getString(R.string.verification_sent), Toast.LENGTH_LONG).show();
                                finish();
                            });
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}