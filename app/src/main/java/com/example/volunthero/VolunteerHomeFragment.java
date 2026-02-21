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
        return inflater.inflate(R.layout.fragment_volunteer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView textLogo = view.findViewById(R.id.tvLogoVol);

        if (textLogo != null) {
            textLogo.post(() -> {
                //градиент
                Shader textShader = new LinearGradient(
                        0, 0, textLogo.getWidth(), 0,
                        new int[]{
                                Color.parseColor("#7E57C2"), //фиол
                                Color.parseColor("#38FFD7")  //бирюз
                        },
                        null, Shader.TileMode.CLAMP);

                textLogo.getPaint().setShader(textShader);
                textLogo.invalidate();
            });
        }
    }
}