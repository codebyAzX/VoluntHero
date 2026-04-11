package com.example.volunthero;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolunteerHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_volunteer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView textLogo = view.findViewById(R.id.tvLogoVol);

        RecyclerView rvMissions = view.findViewById(R.id.rvMissions);
        if (rvMissions != null) {
            rvMissions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            List<EventModel> forYouEvents = new ArrayList<>();
            forYouEvents.add(new EventModel("ENLIGHT Translator 2026", "Languages • Interpretation", "Yerevan", R.drawable.trans));

            rvMissions.setAdapter(new EventAdapter(forYouEvents, event -> showApplyDialog()));
        }

        RecyclerView rvPopular = view.findViewById(R.id.rvPopularMissions);
        if (rvPopular != null) {
            rvPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            List<EventModel> popularEvents = new ArrayList<>();
            popularEvents.add(new EventModel("Sevan Summit 2025", "Technology", "Sevan", R.drawable.sss25));
            popularEvents.add(new EventModel("Tech Week", "Networking", "Gyumri", R.drawable.techweek25));
            popularEvents.add(new EventModel("Digitec 2025", "Technology • Expo", "Yerevan", R.drawable.digitec25));

            rvPopular.setAdapter(new EventAdapter(popularEvents, event -> showApplyDialog()));
        }
    }


    private void sendApplicationToFirebase(String name, String lastName, String reason) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String volunteerId = FirebaseAuth.getInstance().getUid();

        String organizerId = "ID_ОРГАНИЗАТОРА";

        Map<String, Object> application = new HashMap<>();
        application.put("volunteerId", volunteerId);
        application.put("organizerId", organizerId);
        application.put("fullName", name + " " + lastName);
        application.put("reason", reason);
        application.put("status", "pending");
        application.put("timestamp", FieldValue.serverTimestamp());

        db.collection("applications").add(application)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(), "Заявка отправлена организатору!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void showApplyDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.dialog_apply_mission, null);
        dialog.setContentView(view);

        EditText etFirstName = view.findViewById(R.id.etApplyFirstName);
        EditText etLastName = view.findViewById(R.id.etApplyLastName);
        EditText etAge = view.findViewById(R.id.etApplyAge);
        EditText etEmail = view.findViewById(R.id.etApplyEmail);
        EditText etReason = view.findViewById(R.id.etApplyReason);
        Button btnSubmit = view.findViewById(R.id.btnSubmitApplication);

        SharedPreferences prefs = requireContext().getSharedPreferences("VolunteerPrefs", Context.MODE_PRIVATE);
        if (etFirstName != null) etFirstName.setText(prefs.getString("user_name", ""));
        if (etAge != null) etAge.setText(prefs.getString("user_age", ""));
        if (etEmail != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            etEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(v -> {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String reason = etReason.getText().toString().trim();

                if (reason.length() < 10) {
                    Toast.makeText(requireContext(), "Опишите причину подробнее", Toast.LENGTH_SHORT).show();
                } else {
                    //FIREBASE
                    sendApplicationToFirebase(firstName, lastName, reason);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }
}