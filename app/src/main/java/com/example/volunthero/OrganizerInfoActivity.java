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
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class OrganizerInfoActivity extends BaseActivity {

    private LinearLayout stepOrgMain, stepOrgBio;
    private TextView tvStepTitle;
    private LinearProgressIndicator progressBar;
    private AppCompatButton btnNext, btnBack;
    private EditText etOrgName, etOrgBio, etOtherOrgType;
    private ChipGroup chipGroupOrgType;
    private TextView tvCharCount;
    private int currentStep = 1;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_info);

        userRole = getIntent().getStringExtra("USER_ROLE");

        initViews();
        populateOrgTypes();
        updateUI();

        etOrgBio.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                tvCharCount.setText(length + "/600");

                if (length >= 550) {
                    tvCharCount.setTextColor(Color.parseColor("#A86BFF"));
                } else {
                    tvCharCount.setTextColor(Color.parseColor("#9F9F9F"));
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (currentStep > 1) {
                    currentStep--;
                    updateUI();
                } else {
                    showExitDialog();
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (isStepValid()) {
                if (currentStep < 2) {
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
        etOrgName = findViewById(R.id.etOrgName);
        etOrgBio = findViewById(R.id.etOrgBio);
        etOtherOrgType = findViewById(R.id.etOtherOrgType);
        chipGroupOrgType = findViewById(R.id.chipGroupOrgType);
        tvCharCount = findViewById(R.id.tvCharCount);

        if (progressBar != null) progressBar.setMax(2);
    }

    private void populateOrgTypes() {
        String[] types = getResources().getStringArray(R.array.org_types_array);
        int[][] states = new int[][]{ new int[]{android.R.attr.state_checked}, new int[]{} };
        int[] colors = new int[]{ Color.parseColor("#d194ff"), Color.parseColor("#F5F5F5") };
        int[] textColors = new int[]{ Color.BLACK, Color.BLACK };

        for (String type : types) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            chip.setChipBackgroundColor(new ColorStateList(states, colors));
            chip.setTextColor(new ColorStateList(states, textColors));
            chip.setChipStrokeWidth(0f);
            chipGroupOrgType.addView(chip);
        }
    }

    private boolean isStepValid() {
        if (currentStep == 1) {
            if (etOrgName.getText().toString().trim().isEmpty()) {
                showToast(R.string.error_fill_name);
                return false;
            }
            if (chipGroupOrgType.getCheckedChipId() == View.NO_ID && etOtherOrgType.getText().toString().trim().isEmpty()) {
                showToast(R.string.error_select_type);
                return false;
            }
            if (etOrgBio.getText().toString().length() > 600) {
                Toast.makeText(this, "Описание слишком длинное (макс. 600 симв.)", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (currentStep == 2) {
            if (etOrgBio.getText().toString().trim().isEmpty()) {
                showToast(R.string.error_fill_bio);
                return false;
            }
        }
        return true;
    }

    private void updateUI() {
        stepOrgMain.setVisibility(currentStep == 1 ? View.VISIBLE : View.GONE);
        stepOrgBio.setVisibility(currentStep == 2 ? View.VISIBLE : View.GONE);
        btnBack.setVisibility(currentStep == 1 ? View.GONE : View.VISIBLE);

        String stepText = getString(R.string.step) + " " + currentStep + " " + getString(R.string.of) + " 2";
        tvStepTitle.setText(stepText);

        progressBar.setProgress(currentStep);
        btnNext.setText(currentStep == 2 ? getString(R.string.btn_finish) : getString(R.string.btn_next));
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Выход")
                .setMessage("Прервать регистрацию?")
                .setPositiveButton("Да", (d, w) -> finish())
                .setNegativeButton("Нет", null)
                .show();
    }

    private void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    private void finishForm() {


        Toast.makeText(this, "Welcome to VoluntHero!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER_ROLE", userRole);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}