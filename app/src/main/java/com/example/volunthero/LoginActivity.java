package com.example.volunthero;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity { //BASE

    private TextView tvLogo, tvGoToRegister, tvForgotPassword, tvLangSwitch;
    private ImageView ivFlag;
    private LinearLayout llLangSwitch;
    private Button btnContinue;
    private TextInputEditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //View
        tvLogo = findViewById(R.id.tvLogo);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnContinue = findViewById(R.id.btnContinue);
        tvLangSwitch = findViewById(R.id.tvLangSwitch);
        ivFlag = findViewById(R.id.ivFlag);
        llLangSwitch = findViewById(R.id.llLangSwitch);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        //градиент
        if (tvLogo != null) {
            applyGradientToLogo();
        }

        updateLanguageUI();

        //язык
        if (llLangSwitch != null) {
            llLangSwitch.setOnClickListener(v -> showLanguageMenu());
        }

        //рег
        if (tvGoToRegister != null) {
            tvGoToRegister.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            });
        }

        //парол
        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v -> {
                if (etEmail != null) {
                    String email = etEmail.getText().toString().trim();
                    if (email.isEmpty()) {
                        etEmail.setError("Введите email");
                        return;
                    }
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Письмо отправлено!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }

        //ЛОГИКА ВХОДА
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    try {
                                        Intent intent = new Intent(LoginActivity.this, RoleSelectionActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (Exception e) {
                                        Toast.makeText(this, "Ошибка перехода: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(this, "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            });
        }
    }

    private void applyGradientToLogo() {
        tvLogo.post(() -> {
            try {
                TextPaint paint = tvLogo.getPaint();
                float width = paint.measureText(tvLogo.getText().toString());
                if (width > 0) {
                    Shader textShader = new LinearGradient(0, 0, width, 0,
                            new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#38FFD7")},
                            null, Shader.TileMode.CLAMP);
                    paint.setShader(textShader);
                    tvLogo.invalidate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateLanguageUI() {
        if (tvLangSwitch == null || ivFlag == null) return;
        String lang = LocaleHelper.getLanguage(this);
        if (lang.equals("hy")) {
            tvLangSwitch.setText("AM");
            ivFlag.setImageResource(R.drawable.flag_armenia);
        } else if (lang.equals("ru")) {
            tvLangSwitch.setText("RU");
            ivFlag.setImageResource(R.drawable.flag_russia);
        } else {
            tvLangSwitch.setText("EN");
            ivFlag.setImageResource(R.drawable.flag_usa);
        }
    }

    private void showLanguageMenu() {
        PopupMenu popupMenu = new PopupMenu(this, llLangSwitch);
        popupMenu.getMenu().add(0, 1, 0, "Հայերեն (AM)");
        popupMenu.getMenu().add(0, 2, 1, "Русский (RU)");
        popupMenu.getMenu().add(0, 3, 2, "English (EN)");

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1: setLocale("hy"); break;
                case 2: setLocale("ru"); break;
                case 3: setLocale("en"); break;
            }
            return true;
        });
        popupMenu.show();
    }

    private void setLocale(String lang) {
        //сохраняем выбор в настройки
        LocaleHelper.setLocale(this, lang);

        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
        finish();
    }
}