package com.example.volunthero;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //роль из Intent(RoleSelection/InfoActivity)
        String userRole = getIntent().getStringExtra("USER_ROLE");

        //եթե հանկարծ դերը չի փոխանցվել
        if (userRole == null) {
            //ժամանակավոռ
            userRole = "volunteer";
        }

        if (savedInstanceState == null) {
            if (userRole.equals("volunteer")) {
                loadFragment(new VolunteerHomeFragment());
            } else if (userRole.equals("organizer")) {
                loadFragment(new OrganizerHomeFragment());
            }
        }
    }

    //метод для замены фрагмента в контейнере!
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}