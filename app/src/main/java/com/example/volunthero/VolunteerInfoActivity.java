package com.example.volunthero;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.slider.Slider;

public class VolunteerInfoActivity extends BaseActivity { // ИЗМЕНЕНО: теперь наследуемся от BaseActivity

    private ProgressBar progressBar;
    private TextView tvStepTitle, tvAgeDisplay;
    private Slider ageSlider;
    private Button btnNext;

    //контейнеры шагов
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

        stepAge = findViewById(R.id.stepAge);
        stepInterests = findViewById(R.id.stepInterests);
        stepSkills = findViewById(R.id.stepSkills);
        stepDocs = findViewById(R.id.stepDocs);

        //слушатель слайдера
        ageSlider.addOnChangeListener((slider, value, fromUser) -> {
            tvAgeDisplay.setText(getString(R.string.your_age, (int) value));
        });

        //неьт
        btnNext.setOnClickListener(v -> {
            if (currentStep < 4) {
                currentStep++;
                updateUI();
            } else {
                Toast.makeText(this, getString(R.string.btn_finish), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        updateUI();
    }

    private void updateUI() {
        progressBar.setProgress(currentStep);

        tvStepTitle.setText(getString(R.string.step_format, currentStep, 4));

        stepAge.setVisibility(View.GONE);
        stepInterests.setVisibility(View.GONE);
        stepSkills.setVisibility(View.GONE);
        stepDocs.setVisibility(View.GONE);

        switch (currentStep) {
            case 1:
                stepAge.setVisibility(View.VISIBLE);
                btnNext.setText(getString(R.string.btn_next));
                tvAgeDisplay.setText(getString(R.string.your_age, (int) ageSlider.getValue()));
                break;
            case 2:
                stepInterests.setVisibility(View.VISIBLE);
                btnNext.setText(getString(R.string.btn_next));
                break;
            case 3:
                stepSkills.setVisibility(View.VISIBLE);
                btnNext.setText(getString(R.string.btn_next));
                break;
            case 4:
                stepDocs.setVisibility(View.VISIBLE);
                btnNext.setText(getString(R.string.btn_finish));
                break;
        }
    }
}