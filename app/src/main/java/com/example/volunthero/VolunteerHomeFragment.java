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

public class VolunteerHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //МАКЕТ ВОЛОНТЕРА
        return inflater.inflate(R.layout.fragment_volunteer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ID
        final TextView textLogo = view.findViewById(R.id.tvLogoVol);
        if (textLogo != null) {
            applyGradient(textLogo);
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