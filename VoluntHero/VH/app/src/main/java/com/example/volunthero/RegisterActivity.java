package com.example.volunthero;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth; // Библиотека Firebase

public class RegisterActivity extends AppCompatActivity {

    private TextView tvRegisterLogo;
    private EditText etRegEmail, etRegPass;
    private Button btnSignUp;
    private FirebaseAuth mAuth; //Объект для работы с регистрацией

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        tvRegisterLogo = findViewById(R.id.tvRegisterLogo);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPass = findViewById(R.id.etRegPass);
        btnSignUp = findViewById(R.id.btnSignUp);

        applyGradientToLogo();

        btnSignUp.setOnClickListener(v -> {
            String email = etRegEmail.getText().toString().trim();
            String password = etRegPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            //Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this,
                                                    "Регистрация успешна! Проверьте почту " + email,
                                                    Toast.LENGTH_LONG).show();
                                            finish(); //зкрываем окно и возвращаемся к входу
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Ошибка: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void applyGradientToLogo() {
        tvRegisterLogo.post(() -> {
            TextPaint paint = tvRegisterLogo.getPaint();
            float width = paint.measureText(tvRegisterLogo.getText().toString());
            Shader textShader = new LinearGradient(0, 0, width, 0,
                    new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#38FFD7")},
                    null, Shader.TileMode.CLAMP);
            paint.setShader(textShader);
            tvRegisterLogo.invalidate();
        });
    }
}