package com.example.volunthero;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FeedbackFragment extends Fragment {

    private EditText etFeedbackMessage;
    private Button btnSendFeedback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //XML
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        etFeedbackMessage = view.findViewById(R.id.etFeedbackMessage);
        btnSendFeedback = view.findViewById(R.id.btnSendFeedback);

        btnSendFeedback.setOnClickListener(v -> {
            String message = etFeedbackMessage.getText().toString().trim();

            if (!message.isEmpty()) {
                Log.d("FEEDBACK_LOG", "Получен отзыв: " + message);

                Toast.makeText(getActivity(), "Спасибо! Твой отзыв отправлен.", Toast.LENGTH_SHORT).show();

                etFeedbackMessage.setText("");
            } else {
                Toast.makeText(getActivity(), "Пожалуйста, напиши что-нибудь", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}