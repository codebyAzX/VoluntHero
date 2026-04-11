package com.example.volunthero;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

public class OrganizerHomeFragment extends Fragment {

    public OrganizerHomeFragment() {
        super(R.layout.fragment_organizer_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final TextView textLogo = view.findViewById(R.id.tvLogoOrg);
        if (textLogo != null) {
            applyGradient(textLogo);
        }

        AppCompatButton btnCreateEvent = view.findViewById(R.id.btnCreateEvent);
        if (btnCreateEvent != null) {
            btnCreateEvent.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Opening event creator...", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void applyGradient(TextView tv) {
        tv.post(() -> {
            Shader textShader = new LinearGradient(0, 0, tv.getWidth(), 0,
                    new int[]{Color.parseColor("#A86BFF"), Color.parseColor("#7E57C2")},
                    null, Shader.TileMode.CLAMP);
            tv.getPaint().setShader(textShader);
            tv.invalidate();
        });
    }
}