package com.example.volunthero;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private ImageView ivAvatar;

    public ProfileFragment() {
        super(R.layout.fragment_volunteer_profile);    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Инициализация UI
        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvAge = view.findViewById(R.id.tvProfileAge);
        TextView tvBio = view.findViewById(R.id.tvProfileBio);
        TextView tvSkills = view.findViewById(R.id.tvProfileSkills);
        TextView tvCerts = view.findViewById(R.id.tvProfileCertificates);
        ivAvatar = view.findViewById(R.id.ivProfileAvatar);
        CardView cvAvatar = view.findViewById(R.id.cvAvatar);
        ImageView btnLogout = view.findViewById(R.id.btnLogout);

        // 2. Получение данных из SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("VolunteerPrefs", Context.MODE_PRIVATE);

        String name = prefs.getString("user_name", "Имя не задано");
        String age = prefs.getString("user_age", "17");
        String bio = prefs.getString("user_bio", "Добавьте информацию о себе в настройках.");
        String skills = prefs.getString("user_skills", "Навыки не указаны");
        String certs = prefs.getString("user_certs", "Сертификатов пока нет");

        // 3. Установка данных в View
        tvName.setText(name);
        tvAge.setText(age + " лет • Yerevan");
        tvBio.setText(bio);
        tvSkills.setText(skills);
        tvCerts.setText(certs);

        // 4. Логика кнопки LogOut
        btnLogout.setOnClickListener(v -> {
            // Очищаем данные пользователя
            prefs.edit().clear().apply();
            Toast.makeText(getContext(), "Вы вышли из системы", Toast.LENGTH_SHORT).show();
            // Закрываем активити (приложение)
            getActivity().finish();
        });

        // 5. Логика выбора фото аватара
        cvAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                ivAvatar.setImageURI(selectedImage);
                // Сохранение Uri в SharedPreferences для постоянного отображения
                getActivity().getSharedPreferences("VolunteerPrefs", Context.MODE_PRIVATE)
                        .edit().putString("user_avatar_uri", selectedImage.toString()).apply();
            }
        }
    }
}