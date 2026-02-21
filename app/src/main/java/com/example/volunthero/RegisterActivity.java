package com.example.volunthero;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends BaseActivity { //наследуем от BaseActivity

    private TextInputEditText etNickname, etRegEmail, etRegPassword, etConfirmPassword;
    private TextInputLayout tilConfirmPassword;
    private TextView tvRegTitle, tvBackToLogin;
    private Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        tvRegTitle = findViewById(R.id.tvRegTitle);
        etNickname = findViewById(R.id.etNickname);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        //градиент
        tvRegTitle.post(() -> {
            Shader shader = new LinearGradient(
                    0, 0, tvRegTitle.getWidth(), 0,
                    new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#38FFD7")},
                    null, Shader.TileMode.CLAMP);
            tvRegTitle.getPaint().setShader(shader);
            tvRegTitle.invalidate();
        });

        btnRegister.setOnClickListener(v -> {
            String email = etRegEmail.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String nickname = etNickname.getText().toString().trim();

            //верификация
            if (email.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                tilConfirmPassword.setError(getString(R.string.passwords_dont_match));
                return;
            } else {
                tilConfirmPassword.setError(null);
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(verifyTask -> {
                                            if (verifyTask.isSuccessful()) {
                                                //mail֊ի պահը
                                                String msg = getString(R.string.verification_sent) + " " + email;
                                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    getString(R.string.error_prefix) + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvBackToLogin.setOnClickListener(v -> finish());
    }
}