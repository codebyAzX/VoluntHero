package com.example.volunthero;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VolunteerHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_volunteer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //градиент
        final TextView textLogo = view.findViewById(R.id.tvLogoVol);
        if (textLogo != null) applyGradient(textLogo);

        RecyclerView rvMissions = view.findViewById(R.id.rvMissions);
        if (rvMissions != null) {
            rvMissions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            List<EventModel> featuredEvents = new ArrayList<>();
            featuredEvents.add(new EventModel("Sevan Summit 2025", "Technology", "Sevan", R.drawable.sss25));
            featuredEvents.add(new EventModel("Digitec 2025", "Technology", "Yerevan", R.drawable.digitec25));
            featuredEvents.add(new EventModel("Tech Week", "Networking", "Gyumri", R.drawable.techweek25));

            rvMissions.setAdapter(new EventAdapter(featuredEvents));
        }

        RecyclerView rvPopular = view.findViewById(R.id.rvPopularMissions);
        if (rvPopular != null) {
            rvPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            List<EventModel> popularEvents = new ArrayList<>();
            popularEvents.add(new EventModel("Sevan Summit 2025", "Technology", "Sevan", R.drawable.sss25));
            popularEvents.add(new EventModel("Digitec 2025", "Technology", "Yerevan", R.drawable.digitec25));
            popularEvents.add(new EventModel("Tech Week", "Networking", "Vanadzor", R.drawable.techweek25));

            rvPopular.setAdapter(new EventAdapter(popularEvents));
        }
    }

    private void applyGradient(TextView tv) {
        tv.post(() -> {
            Shader textShader = new LinearGradient(0, 0, tv.getWidth(), 0,
                    new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#38FFD7")},
                    null, Shader.TileMode.CLAMP);
            tv.getPaint().setShader(textShader);
            tv.invalidate();
        });
    }
}