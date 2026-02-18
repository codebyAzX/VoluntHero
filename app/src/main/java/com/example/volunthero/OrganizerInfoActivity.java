package com.example.volunthero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OrganizerInfoActivity extends BaseActivity {

    private ProgressBar progressBar;
    private TextView tvStepTitle;
    private Button btnNext, btnBack;
    private LinearLayout stepOrgMain, stepOrgRole, stepOrgEmail;
    private int currentStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_info);

        progressBar = findViewById(R.id.progressBar);
        tvStepTitle = findViewById(R.id.tvStepTitle);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        stepOrgMain = findViewById(R.id.stepOrgMain);
        stepOrgRole = findViewById(R.id.stepOrgRole);
        stepOrgEmail = findViewById(R.id.stepOrgEmail);

        btnNext.setOnClickListener(v -> {
            if (currentStep < 3) {
                currentStep++;
                updateUI();
            } else {
                Toast.makeText(this, "Профиль организации создан!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrganizerInfoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> {
            if (currentStep > 1) {
                currentStep--;
                updateUI();
            }
        });

        updateUI();
    }

    private void updateUI() {
        progressBar.setProgress(currentStep);
        btnBack.setVisibility(currentStep > 1 ? View.VISIBLE : View.GONE);
        tvStepTitle.setText("ОРГАНИЗАЦИЯ: ШАГ " + currentStep + " ИЗ 3");

        stepOrgMain.setVisibility(View.GONE);
        stepOrgRole.setVisibility(View.GONE);
        stepOrgEmail.setVisibility(View.GONE);

        switch (currentStep) {
            case 1: stepOrgMain.setVisibility(View.VISIBLE); break;
            case 2: stepOrgRole.setVisibility(View.VISIBLE); break;
            case 3: stepOrgEmail.setVisibility(View.VISIBLE); break;
        }
    }
}