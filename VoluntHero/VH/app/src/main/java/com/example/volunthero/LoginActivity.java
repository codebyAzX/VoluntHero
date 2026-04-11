package com.example.volunthero;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView tvLogo, tvGoToRegister;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //էլեմնտներ
        tvLogo = findViewById(R.id.tvLogo);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        btnContinue = findViewById(R.id.btnContinue);

        applyGradientToLogo();

        tvGoToRegister.setOnClickListener(v -> {
            //դեպի new window
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        //кнопка входа
        btnContinue.setOnClickListener(v -> {
            //сюда логику позже
        });
    }

    private void applyGradientToLogo() {
        tvLogo.post(() -> {
            TextPaint paint = tvLogo.getPaint();
            float width = paint.measureText(tvLogo.getText().toString());

            Shader textShader = new LinearGradient(0, 0, width, 0,
                    new int[]{
                            Color.parseColor("#7E57C2"), //лиловый
                            Color.parseColor("#38FFD7")  //бирюз
                    }, null, Shader.TileMode.CLAMP);

            paint.setShader(textShader);
            tvLogo.invalidate();
        });
    }
}