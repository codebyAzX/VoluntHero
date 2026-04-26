package com.example.volunthero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VolunteerInfoActivity extends BaseActivity {

    private static final int FILE_SELECT_CODE = 999;
    private LinearLayout stepAge, stepInterests, stepSkills, stepDocs;
    private TextView tvStepTitle, tvCalculatedAge, tvSelectedFilesCount;
    private LinearProgressIndicator progressBar;
    private AppCompatButton btnNext, btnBack, btnUpload;
    private ChipGroup chipGroupInterests, chipGroupSkills;
    private int currentStep = 1;
    private EditText etSkills;
    private String userRole;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_info);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://volunthero-cc4d8-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        userRole = getIntent().getStringExtra("USER_ROLE");

        initViews();
        setupDatePicker();
        populateChips();
        updateUI();

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (currentStep > 1) {
                    currentStep--;
                    updateUI();
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(VolunteerInfoActivity.this)
                            .setTitle("Выход")
                            .setMessage("Вы уверены? Данные не сохранятся.")
                            .setPositiveButton("Да", (dialog, which) -> finish())
                            .setNegativeButton("Отмена", null)
                            .show();
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (isStepValid()) {
                if (currentStep < 4) {
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

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Docs"), FILE_SELECT_CODE);
        });

        findViewById(R.id.btnNoExperience).setOnClickListener(v -> finishForm());
    }

    private boolean isStepValid() {
        if (currentStep == 2 && chipGroupInterests.getCheckedChipIds().isEmpty()) {
            Toast.makeText(this, getString(R.string.select_interests_warning), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (currentStep == 3 && chipGroupSkills.getCheckedChipIds().isEmpty()) {
            Toast.makeText(this, getString(R.string.select_skills_warning), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
        etSkills = findViewById(R.id.etOtherSkill);
    }

    private void populateChips() {
        String[] interests = getResources().getStringArray(R.array.interests_array);
        if (interests != null) {
            for (String i : interests) chipGroupInterests.addView(createChip(i));
        }

        String[] skills = getResources().getStringArray(R.array.skills_array);
        if (skills != null) {
            for (String s : skills) chipGroupSkills.addView(createChip(s));
        }
    }

    private Chip createChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setClickable(true);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{}
        };

        int[] colors = new int[]{
                Color.parseColor("#a4f5e5"),
                Color.parseColor("#F5F5F5")
        };

        int[] textColors = new int[]{
                Color.BLACK,
                Color.parseColor("#1A1A1A")
        };

        chip.setChipBackgroundColor(new ColorStateList(states, colors));
        chip.setTextColor(new ColorStateList(states, textColors));

        chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
        chip.setChipStrokeWidth(1f);

        return chip;
    }

    private void setupDatePicker() {
        DatePicker dp = findViewById(R.id.datePicker);
        if (dp != null) {
            dp.init(2005, 0, 1, (view, y, m, d) -> {
                int age = 2026 - y;
                tvCalculatedAge.setText(age + " " + getString(R.string.years_unit));
            });
        }
    }

    private void updateUI() {
        stepAge.setVisibility(currentStep == 1 ? View.VISIBLE : View.GONE);
        stepInterests.setVisibility(currentStep == 2 ? View.VISIBLE : View.GONE);
        stepSkills.setVisibility(currentStep == 3 ? View.VISIBLE : View.GONE);
        stepDocs.setVisibility(currentStep == 4 ? View.VISIBLE : View.GONE);

        btnBack.setVisibility(currentStep == 1 ? View.GONE : View.VISIBLE);
        tvStepTitle.setText(getString(R.string.step) + " " + currentStep + " " + getString(R.string.of) + " 4");
        progressBar.setProgress(currentStep);
        btnNext.setText(currentStep == 4 ? getString(R.string.btn_finish) : getString(R.string.btn_next));
    }

    private void finishForm() {
        FirebaseUser fUser = mAuth.getCurrentUser();
        if (fUser == null) return;

        SharedPreferences prefs = getSharedPreferences("VolunteerPrefs", MODE_PRIVATE);
        String username = prefs.getString("user_name", "Anonymous");
        String email = fUser.getEmail();
        String uid = fUser.getUid();

        DatePicker dp = findViewById(R.id.datePicker);
        int year = dp.getYear();
        int calculatedAge = 2026 - year;

        String interests = getCheckedChipsText(chipGroupInterests);
        String skills = getCheckedChipsText(chipGroupSkills);
        String manualSkill = etSkills.getText().toString().trim();
        if (!manualSkill.isEmpty()) {
            skills += (skills.isEmpty() ? "" : ", ") + manualSkill;
        }

        User user = new User(uid, username, email, "volunteer");
        user.age = String.valueOf(calculatedAge);
        user.interests = interests;
        user.skills = skills;

        mDatabase.child("volunteers").child(uid).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, getString(R.string.welcome_hero), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("USER_ROLE", "volunteer");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getCheckedChipsText(ChipGroup group) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            if (chip.isChecked()) {
                if (result.length() > 0) result.append(", ");
                result.append(chip.getText().toString());
            }
        }
        return result.toString();
    }
}
