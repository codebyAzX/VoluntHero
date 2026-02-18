package com.example.volunthero;

import android.content.Intent;
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

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //UI
        tvRegTitle = findViewById(R.id.tvRegTitle);
        etNickname = findViewById(R.id.etNickname);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        //градиент
        tvRegTitle.post(() -> {
            Shader shader = new LinearGradient(
                    0, 0, tvRegTitle.getWidth(), 0,
                    new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#38FFD7")},
                    null, Shader.TileMode.CLAMP);
            tvRegTitle.getPaint().setShader(shader);
            tvRegTitle.invalidate();
        });

        //4.Логика регистрации
        btnRegister.setOnClickListener(v -> {
            String email = etRegEmail.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String nickname = etNickname.getText().toString().trim();

            //проверка на пустые поля
            if (email.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            //проверка совпадения паролей
            if (!password.equals(confirmPassword)) {
                tilConfirmPassword.setError("Пароли не совпадают!");
                return;
            } else {
                tilConfirmPassword.setError(null);
            }

            //создание пользователя в Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                //отправка письма для верификации
                                user.sendEmailVerification()
                                        .addOnCompleteListener(verifyTask -> {
                                            if (verifyTask.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Письмо отправлено на " + email + ". Подтвердите его!",
                                                        Toast.LENGTH_LONG).show();

                                                //возвращаемся на экран логина
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Ошибка верификации: " + verifyTask.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            //ошибка Firebase (например, слабый пароль или почта уже занята)
                            Toast.makeText(RegisterActivity.this,
                                    "Ошибка: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        //"Назад к входу"
        tvBackToLogin.setOnClickListener(v -> finish());
    }
}