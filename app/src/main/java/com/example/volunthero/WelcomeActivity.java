package com.example.volunthero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

public class WelcomeActivity extends BaseActivity {

    private TextView tvLangSwitch, tvIHaveAccount, tvWelcomeTitle;
    private ImageView ivFlag;
    private LinearLayout llLangSwitch;
    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tvLangSwitch = findViewById(R.id.tvLangSwitch);
        tvWelcomeTitle = findViewById(R.id.tvWelcomeTitle);
        ivFlag = findViewById(R.id.ivFlag);
        llLangSwitch = findViewById(R.id.llLangSwitch);
        btnGetStarted = findViewById(R.id.btnGetStarted);
        tvIHaveAccount = findViewById(R.id.tvIHaveAccount);



        updateLanguageUI();

        llLangSwitch.setOnClickListener(v -> showLanguageMenu());

        btnGetStarted.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
        });

        tvIHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        });
    }

    private void updateLanguageUI() {
        String lang = LocaleHelper.getLanguage(this);

        if (lang.equals("hy")) {
            tvLangSwitch.setText("AM");
            ivFlag.setImageResource(R.drawable.flag_armenia);
        } else if (lang.equals("ru")) {
            tvLangSwitch.setText("RU");
            ivFlag.setImageResource(R.drawable.flag_russia);
        } else {
            tvLangSwitch.setText("EN");
            ivFlag.setImageResource(R.drawable.flag_usa);
        }
    }

    private void showLanguageMenu() {
        PopupMenu popupMenu = new PopupMenu(this, llLangSwitch);
        popupMenu.getMenu().add(0, 1, 0, "Հայերեն (AM)");
        popupMenu.getMenu().add(0, 2, 1, "Русский (RU)");
        popupMenu.getMenu().add(0, 3, 2, "English (EN)");

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1: setLocale("hy"); break;
                case 2: setLocale("ru"); break;
                case 3: setLocale("en"); break;
            }
            return true;
        });
        popupMenu.show();
    }

    private void setLocale(String lang) {
        LocaleHelper.setLocale(this, lang);
        android.util.Log.d("LANGUAGE_TEST", "Язык установлен на: " + lang);

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}