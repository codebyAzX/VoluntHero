package com.example.volunthero;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userRole = getIntent().getStringExtra("USER_ROLE");
        if (userRole == null) userRole = "volunteer";

        if (savedInstanceState == null) {
            loadHomeFragment();
        }

        setupNavigation();
    }

    private void setupNavigation() {
        //домой
        findViewById(R.id.nav_home).setOnClickListener(v -> loadHomeFragment());

        //фидбек
        findViewById(R.id.nav_feedback).setOnClickListener(v -> loadFragment(new FeedbackFragment()));

        //чат
        findViewById(R.id.nav_chat).setOnClickListener(v -> {
            if ("organizer".equals(userRole)) loadFragment(new OrganizerChatsFragment());
            else loadFragment(new ChatFragment());
        });

        //профиль
        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            if ("organizer".equals(userRole)) loadFragment(new OrganizerProfileFragment());
            else loadFragment(new ProfileFragment());
        });
    }

    private void loadHomeFragment() {
        if ("volunteer".equals(userRole)) loadFragment(new VolunteerHomeFragment());
        else loadFragment(new OrganizerHomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}