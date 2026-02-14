package com.example.volunthero;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private TextView tvLogo, tvGoToRegister, tvForgotPassword, tvLangSwitch;
    private ImageView ivFlag;
    private LinearLayout llLangSwitch;
    private Button btnContinue;
    private TextInputEditText etEmail, etPassword;
    private static boolean isInitialLocaleSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isInitialLocaleSet) {
            setInitialLocale("hy");
            isInitialLocaleSet = true;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvLogo = findViewById(R.id.tvLogo);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnContinue = findViewById(R.id.btnContinue);

        //լեզուներ
        tvLangSwitch = findViewById(R.id.tvLangSwitch);
        ivFlag = findViewById(R.id.ivFlag);
        llLangSwitch = findViewById(R.id.llLangSwitch);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        //градиент
        applyGradientToLogo();

        //for language որ փոխվեն էլի
        updateLanguageUI();

        llLangSwitch.setOnClickListener(v -> showLanguageMenu());

        tvForgotPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Введите email в поле логина");
                return;
            }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Письмо отправлено!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        //դեպի ռեգեստրացիա
        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Вход для " + email, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setInitialLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    private void updateLanguageUI() {
        String lang = getResources().getConfiguration().getLocales().get(0).getLanguage();
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

    //Հայերեան ֊> Րուսերեն -> Անգլեռեն
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
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
        finish();
    }

    private void applyGradientToLogo() {
        tvLogo.post(() -> {
            TextPaint paint = tvLogo.getPaint();
            float width = paint.measureText(tvLogo.getText().toString());
            Shader textShader = new LinearGradient(0, 0, width, 0,
                    new int[]{Color.parseColor("#7E57C2"), //фиол
                            Color.parseColor("#38FFD7")}, //бирюз
                    null, Shader.TileMode.CLAMP);
            paint.setShader(textShader);
            tvLogo.invalidate();
        });
    }
}