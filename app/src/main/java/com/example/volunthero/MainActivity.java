package com.example.volunthero;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class MainActivity extends BaseActivity {

    private boolean isOrganizer = false; //Firebase завтра

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadFragment(isOrganizer ? new OrganizerHomeFragment() : new VolunteerHomeFragment());
        }

        setupNavigation();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    private void setupNavigation() {
        findViewById(R.id.navHome).setOnClickListener(v -> {
            loadFragment(isOrganizer ? new OrganizerHomeFragment() : new VolunteerHomeFragment());
        });
    }
}