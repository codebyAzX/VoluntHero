package com.example.volunthero;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class OrganizerInfoActivity extends BaseActivity {

    private LinearLayout stepOrgMain, stepOrgBio, stepOrgVerify;
    private TextView tvStepTitle;
    private LinearProgressIndicator progressBar;
    private AppCompatButton btnNext, btnBack;
    private EditText etOrgName, etOrgBio, etOrgWorkEmail, etOtherOrgType;
    private ChipGroup chipGroupOrgType;
    private int currentStep = 1;

    // 1. Добавляем переменную для роли
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_info);

        // 2. Получаем роль из интента сразу
        userRole = getIntent().getStringExtra("USER_ROLE");

        initViews();
        populateOrgTypes();
        updateUI();

        btnNext.setOnClickListener(v -> {
            if (isStepValid()) {
                if (currentStep < 3) {
                    currentStep++;
                    updateUI();
                } else {
                    finishForm();
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            if (currentStep > 1) {
                currentStep--;
                updateUI();
            }
        });
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        tvStepTitle = findViewById(R.id.tvStepTitle);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        stepOrgMain = findViewById(R.id.stepOrgMain);
        stepOrgBio = findViewById(R.id.stepOrgBio);
        stepOrgVerify = findViewById(R.id.stepOrgVerify);
        etOrgName = findViewById(R.id.etOrgName);
        etOrgBio = findViewById(R.id.etOrgBio);
        etOrgWorkEmail = findViewById(R.id.etOrgWorkEmail);
        etOtherOrgType = findViewById(R.id.etOtherOrgType);
        chipGroupOrgType = findViewById(R.id.chipGroupOrgType);
    }

    private void populateOrgTypes() {
        String[] types = getResources().getStringArray(R.array.org_types_array);
        for (String type : types) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            chip.setChipBackgroundColor(null);
            chip.setBackgroundResource(R.drawable.card_background);

            int[][] states = new int[][]{ new int[]{android.R.attr.state_checked}, new int[]{} };
            int[] colors = new int[]{ Color.parseColor("#7E57C2"), Color.WHITE };
            chip.setTextColor(new ColorStateList(states, colors));

            chipGroupOrgType.addView(chip);
        }
    }

    private boolean isStepValid() {
        if (currentStep == 1) {
            if (etOrgName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, getString(R.string.error_fill_name), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (chipGroupOrgType.getCheckedChipId() == View.NO_ID && etOtherOrgType.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, getString(R.string.error_select_type), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (currentStep == 2) {
            if (etOrgBio.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, getString(R.string.error_fill_bio), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (currentStep == 3) {
            String email = etOrgWorkEmail.getText().toString().trim();
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void updateUI()  {
        stepOrgMain.setVisibility(currentStep == 1 ? View.VISIBLE : View.GONE);
        stepOrgBio.setVisibility(currentStep == 2 ? View.VISIBLE : View.GONE);
        stepOrgVerify.setVisibility(currentStep == 3 ? View.VISIBLE : View.GONE);

        btnBack.setVisibility(currentStep == 1 ? View.GONE : View.VISIBLE);

        String stepText = getString(R.string.step) + " " + currentStep + " " + getString(R.string.of) + " 3";
        tvStepTitle.setText(stepText);

        progressBar.setProgress(currentStep);
        btnNext.setText(currentStep == 3 ? getString(R.string.btn_finish) : getString(R.string.btn_next));
    }

    private void finishForm() {
        Toast.makeText(this, "Welcome to VoluntHero!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);

        // 3. ПЕРЕДАЕМ РОЛЬ В MAIN
        intent.putExtra("USER_ROLE", userRole);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}