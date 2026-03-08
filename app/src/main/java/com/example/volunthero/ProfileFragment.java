package com.example.volunthero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvEmail = view.findViewById(R.id.tvProfileEmail);
        if (tvEmail != null) {
            tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        return view;
    }
}