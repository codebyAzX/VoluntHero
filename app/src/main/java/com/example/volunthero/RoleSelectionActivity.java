package com.example.volunthero;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;

public class RoleSelectionActivity extends BaseActivity {

    private MaterialCardView cardVolunteer, cardOrganizer;
    private TextView tvRoleTitle;
    private TextView tvVolTitle, tvVolDesc, tvOrgTitle, tvOrgDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        //инициализация
        tvRoleTitle = findViewById(R.id.tvRoleTitle);
        cardVolunteer = findViewById(R.id.cardVolunteer);
        cardOrganizer = findViewById(R.id.cardOrganizer);
        tvVolTitle = findViewById(R.id.tvVolunteerTitle);
        tvVolDesc = findViewById(R.id.tvVolunteerDesc);
        tvOrgTitle = findViewById(R.id.tvOrganizerTitle);
        tvOrgDesc = findViewById(R.id.tvOrganizerDesc);

        setupLocalizedTexts();

        //градиент
        if (tvRoleTitle != null) {
            applyGradientToTitle();
        }

        //клик-вол
        if (cardVolunteer != null) {
            cardVolunteer.setOnClickListener(v -> {
                startActivity(new Intent(this, VolunteerInfoActivity.class));
            });
        }

        //клик-орг
        if (cardOrganizer != null) {
            cardOrganizer.setOnClickListener(v -> {
                startActivity(new Intent(this, OrganizerInfoActivity.class));
            });
        }
    }

    private void setupLocalizedTexts() {
        if (tvRoleTitle != null) tvRoleTitle.setText(R.string.role_title);
        if (tvVolTitle != null) tvVolTitle.setText(R.string.role_volunteer);
        if (tvVolDesc != null) tvVolDesc.setText(R.string.role_volunteer_desc);
        if (tvOrgTitle != null) tvOrgTitle.setText(R.string.role_organizer);
        if (tvOrgDesc != null) tvOrgDesc.setText(R.string.role_organizer_desc);
    }

    private void applyGradientToTitle() {
        tvRoleTitle.post(() -> {
            int width = tvRoleTitle.getWidth();
            if (width > 0) {
                Shader textShader = new LinearGradient(0, 0, width, 0,
                        new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#38FFD7")},
                        null, Shader.TileMode.CLAMP);
                tvRoleTitle.getPaint().setShader(textShader);
                tvRoleTitle.invalidate();
            }
        });
    }
}