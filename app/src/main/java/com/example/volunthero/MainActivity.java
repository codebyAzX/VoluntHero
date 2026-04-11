package com.example.volunthero;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private String userRole;
    private com.google.firebase.auth.FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        userRole = getIntent().getStringExtra("USER_ROLE");
        if (userRole == null) userRole = "volunteer";

        android.view.View btnAdd = findViewById(R.id.myRoundButton);
        android.view.View spacer = findViewById(R.id.nav_spacer);

        if ("volunteer".equalsIgnoreCase(userRole)) {
            if (btnAdd != null) btnAdd.setVisibility(android.view.View.GONE);
            if (spacer != null) spacer.setVisibility(android.view.View.GONE);
        } else {
            if (btnAdd != null) btnAdd.setVisibility(android.view.View.VISIBLE);
            if (spacer != null) spacer.setVisibility(android.view.View.VISIBLE);
        }

        if (savedInstanceState == null) {
            androidx.fragment.app.Fragment initialFragment;

            if ("organizer".equalsIgnoreCase(userRole)) {
                initialFragment = new OrganizerHomeFragment();
            } else {
                initialFragment = new VolunteerHomeFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, initialFragment)
                    .commit();
        }

        setupNavigation();

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
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
        findViewById(R.id.nav_menu_myplaces).setOnClickListener(v -> {
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

    private void openProfile() {
        String role = getIntent().getStringExtra("USER_ROLE");
        Fragment profileFragment;

        if ("ORGANIZER".equals(role)) {
            profileFragment = new ProfileFragment(); //для организатора
        } else {
            profileFragment = new VolunteerProfileFragment(); //дял волонтера
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new VolunteerProfileFragment())
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            //если юзер не залогинен - сразу на логин
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}