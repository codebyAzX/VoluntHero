package com.example.volunthero;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView tvLogo = findViewById(R.id.tvLogo);

//градиент
        Shader textShader = new LinearGradient(0, 0, tvLogo.getPaint().measureText("VoluntHero"), 0,
                new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#26C6DA")},
                null, Shader.TileMode.CLAMP);
        tvLogo.getPaint().setShader(textShader);

    }
}