package com.example.volunthero;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganizerProfileFragment extends Fragment {

    private TextView tvOrgName, tvOrgEmail;
    private AppCompatButton btnLogout;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //XML с дизайном профиля
        return inflater.inflate(R.layout.fragment_organizer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //инициализация UI элементов
        tvOrgName = view.findViewById(R.id.tvOrgNameProfile);
        tvOrgEmail = view.findViewById(R.id.tvOrgEmailProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        //инициализация Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //данные текущего организатора
        loadUserData();

        //логика кнопки Выхода
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        String userId = mAuth.getUid();

        if (userId != null) {
            // Firebase Firestore в коллекцию "users"
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("orgName");
                            String email = documentSnapshot.getString("orgEmail");

                            if (name != null) tvOrgName.setText(name);
                            if (email != null) tvOrgEmail.setText(email);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
