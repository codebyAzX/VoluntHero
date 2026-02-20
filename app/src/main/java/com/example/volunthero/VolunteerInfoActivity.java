package com.example.volunthero;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class VolunteerInfoActivity extends BaseActivity {

    private static final int FILE_SELECT_CODE = 999;
    private LinearLayout stepAge, stepInterests, stepSkills, stepDocs;
    private TextView tvStepTitle, tvCalculatedAge, tvSelectedFilesCount;
    private LinearProgressIndicator progressBar;
    private AppCompatButton btnNext, btnBack, btnUpload;
    private ChipGroup chipGroupInterests, chipGroupSkills;
    private int currentStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_info);

        initViews();
        setupDatePicker();
        populateChips();
        updateUI();

        btnNext.setOnClickListener(v -> {
            if (currentStep < 4) { currentStep++; updateUI(); }
            else { finishForm(); }
        });

        btnBack.setOnClickListener(v -> {
            if (currentStep > 1) { currentStep--; updateUI(); }
        });

        //выбора файл (кнопка)
        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Docs"), FILE_SELECT_CODE);
        });

        findViewById(R.id.btnNoExperience).setOnClickListener(v -> finishForm());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null) {
            int count = (data.getClipData() != null) ? data.getClipData().getItemCount() : 1;
            tvSelectedFilesCount.setText(getString(R.string.files_selected) + ": " + count);
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        tvStepTitle = findViewById(R.id.tvStepTitle);
        tvCalculatedAge = findViewById(R.id.tvCalculatedAge);
        tvSelectedFilesCount = findViewById(R.id.tvSelectedFilesCount);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnUpload = findViewById(R.id.btnUpload);
        chipGroupInterests = findViewById(R.id.chipGroupInterests);
        chipGroupSkills = findViewById(R.id.chipGroupSkills);
        stepAge = findViewById(R.id.stepAge);
        stepInterests = findViewById(R.id.stepInterests);
        stepSkills = findViewById(R.id.stepSkills);
        stepDocs = findViewById(R.id.stepDocs);
    }

    private void populateChips() {
        String[] interests = getResources().getStringArray(R.array.interests_array);
        for (String i : interests) chipGroupInterests.addView(createChip(i));

        String[] skills = getResources().getStringArray(R.array.skills_array);
        for (String s : skills) chipGroupSkills.addView(createChip(s));
    }

    private Chip createChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setTextColor(Color.WHITE);
        chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#333333")));
        chip.setChipStrokeWidth(2f);

        int[][] states = new int[][]{ new int[]{android.R.attr.state_checked}, new int[]{} };
        int[] colors = new int[]{ Color.parseColor("#38FFD7"), Color.parseColor("#1A1A1A") };
        int[] textColors = new int[]{ Color.BLACK, Color.WHITE };

        chip.setChipBackgroundColor(new ColorStateList(states, colors));
        chip.setTextColor(new ColorStateList(states, textColors));
        return chip;
    }

    private void setupDatePicker() {
        DatePicker dp = findViewById(R.id.datePicker);
        dp.init(2005, 0, 1, (view, y, m, d) -> {
            int age = 2026 - y;
            tvCalculatedAge.setText(age + " " + getString(R.string.years_unit));
        });
    }

    private void updateUI() {
        stepAge.setVisibility(currentStep == 1 ? View.VISIBLE : View.GONE);
        stepInterests.setVisibility(currentStep == 2 ? View.VISIBLE : View.GONE);
        stepSkills.setVisibility(currentStep == 3 ? View.VISIBLE : View.GONE);
        stepDocs.setVisibility(currentStep == 4 ? View.VISIBLE : View.GONE);

        btnBack.setVisibility(currentStep == 1 ? View.GONE : View.VISIBLE);
        tvStepTitle.setText(getString(R.string.step) + " " + currentStep + " " + getString(R.string.of) + " 4");

        //1/4, 2/4, 3/4, 4/4
        progressBar.setProgress(currentStep);

        btnNext.setText(currentStep == 4 ? getString(R.string.btn_finish) : getString(R.string.btn_next));
    }

    private void finishForm() {
        Toast.makeText(this, getString(R.string.welcome_hero), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}