package com.example.volunthero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VolunteerProfileFragment extends Fragment {

    private TextView tvName, tvSkills, tvBio, tvEmail;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvProfileName);
        tvSkills = view.findViewById(R.id.tvProfileSkills);
        tvBio = view.findViewById(R.id.tvProfileBio);
        tvEmail = view.findViewById(R.id.tvProfileEmail);

        //Firebase Auth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            tvEmail.setText(currentUser.getEmail());

            mDatabase = FirebaseDatabase.getInstance().getReference("users")
                    .child(currentUser.getUid()).child("profile");

            loadProfileData();
        }
    }

    private void loadProfileData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    VolunteerProfile profile = snapshot.getValue(VolunteerProfile.class);
                    if (profile != null) {
                        tvName.setText(profile.getName());
                        tvSkills.setText("Հմտություններ: Ծրագրավորում" + profile.getSkills());
                        tvBio.setText("Քո մասին: " + profile.getBio());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });
    }
}