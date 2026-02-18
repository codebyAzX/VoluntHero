package com.example.volunthero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.slider.Slider;

public class VolunteerInfoActivity extends BaseActivity {

    private ProgressBar progressBar;
    private TextView tvStepTitle, tvAgeDisplay;
    private Slider ageSlider;
    private Button btnNext, btnBack, btnUpload;

    private LinearLayout stepAge, stepInterests, stepSkills, stepDocs;

    private int currentStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_info);

        //View
        progressBar = findViewById(R.id.progressBar);
        tvStepTitle = findViewById(R.id.tvStepTitle);
        tvAgeDisplay = findViewById(R.id.tvAgeDisplay);
        ageSlider = findViewById(R.id.ageSlider);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnUpload = findViewById(R.id.btnUpload);

        stepAge = findViewById(R.id.stepAge);
        stepInterests = findViewById(R.id.stepInterests);
        stepSkills = findViewById(R.id.stepSkills);
        stepDocs = findViewById(R.id.stepDocs);

        ageSlider.addOnChangeListener((slider, value, fromUser) -> {
            tvAgeDisplay.setText("Ваш возраст: " + (int) value);
        });

        btnNext.setOnClickListener(v -> {
            if (currentStep < 4) {
                currentStep++;
                updateUI();
            } else {
                //ФИНАЛЬНЫЙ ПЕРЕХОД
                Toast.makeText(this, "Профиль волонтера готов!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(VolunteerInfoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //назад
        btnBack.setOnClickListener(v -> {
            if (currentStep > 1) {
                currentStep--;
                updateUI();
            }
        });

        if (btnUpload != null) {
            btnUpload.setOnClickListener(v -> {
                Toast.makeText(this, "Загрузка скоро будет доступна!", Toast.LENGTH_SHORT).show();
            });
        }

        updateUI();
    }

    private void updateUI() {
        progressBar.setProgress(currentStep);

        btnBack.setVisibility(currentStep > 1 ? View.VISIBLE : View.GONE);

        tvStepTitle.setText("ШАГ " + currentStep + " ИЗ 4");

        stepAge.setVisibility(View.GONE);
        stepInterests.setVisibility(View.GONE);
        stepSkills.setVisibility(View.GONE);
        stepDocs.setVisibility(View.GONE);

        switch (currentStep) {
            case 1:
                stepAge.setVisibility(View.VISIBLE);
                btnNext.setText("ДАЛЕЕ");
                break;
            case 2:
                stepInterests.setVisibility(View.VISIBLE);
                btnNext.setText("ДАЛЕЕ");
                break;
            case 3:
                stepSkills.setVisibility(View.VISIBLE);
                btnNext.setText("ДАЛЕЕ");
                break;
            case 4:
                stepDocs.setVisibility(View.VISIBLE);
                btnNext.setText("ЗАВЕРШИТЬ");
                break;
        }
    }
}