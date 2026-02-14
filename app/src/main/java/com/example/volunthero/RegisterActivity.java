package com.example.volunthero;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etNickname, etRegEmail, etRegPassword, etConfirmPassword;
    private TextInputLayout tilConfirmPassword;
    private TextView tvRegTitle, tvBackToLogin;
    private Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // UI
        tvRegTitle = findViewById(R.id.tvRegTitle);
        etNickname = findViewById(R.id.etNickname);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // градиент
        tvRegTitle.post(() -> {
            Shader shader = new LinearGradient(
                    0, 0, tvRegTitle.getWidth(), 0,
                    new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#38FFD7")},
                    null, Shader.TileMode.CLAMP);
            tvRegTitle.getPaint().setShader(shader);
            tvRegTitle.invalidate();
        });

        // кнопка рег.
        btnRegister.setOnClickListener(v -> {
            String email = etRegEmail.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String nickname = etNickname.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                tilConfirmPassword.setError("Пароли не совпадают!");
                return;
            } else {
                tilConfirmPassword.setError(null);
            }

            // Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // письма для верификации
                                user.sendEmailVerification()
                                        .addOnCompleteListener(verifyTask -> {
                                            if (verifyTask.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Письмо отправлено на " + email + ". Проверьте почту!",
                                                        Toast.LENGTH_LONG).show();
                                                finish(); // հետ на экран логина
                                            } else {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Ошибка отправки: " + verifyTask.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Ошибка: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // հետ դեպի մուտք
        tvBackToLogin.setOnClickListener(v -> finish());
    }
}