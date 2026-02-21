package com.example.volunthero;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Пытаемся получить роль из Intent (если пришли из RoleSelection или InfoActivity)
        String userRole = getIntent().getStringExtra("USER_ROLE");

        // 2. Если роль не передана (например, запустили приложение напрямую)
        if (userRole == null) {
            // Можешь временно поменять на "organizer", чтобы потестить другой экран
            userRole = "volunteer";
        }

        // 3. Загружаем нужный фрагмент только при первом создании Activity
        if (savedInstanceState == null) {
            if (userRole.equals("volunteer")) {
                loadFragment(new VolunteerHomeFragment());
            } else if (userRole.equals("organizer")) {
                loadFragment(new OrganizerHomeFragment());
            }
        }
    }

    /**
     * Метод для замены фрагмента в контейнере
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}