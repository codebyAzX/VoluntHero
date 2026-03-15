package com.example.volunthero;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends BaseActivity {

    private TextView tvGoToRegister, tvForgotPassword;
    private Button btnLogin;
    private TextInputEditText etLoginEmail, etLoginPassword;
    private ImageButton btnGoogle, btnFacebook, btnLinkedIn;
    private FirebaseAuth mAuth;
    private TextView tvWelcomeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnLinkedIn = findViewById(R.id.btnLinkedIn);

        tvWelcomeTitle = findViewById(R.id.tvLoginTitle);
        String titleText = getString(R.string.welcome_title);
        setupGradientTitle(tvWelcomeTitle, titleText);



        //сброс пароля
        tvForgotPassword.setOnClickListener(v -> {
            String email = etLoginEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etLoginEmail.setError("Enter your email first");
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //логика входа
        btnLogin.setOnClickListener(v -> {
            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(this, RoleSelectionActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        //Google Sign-In Client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // ID из google-services.json
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 101); // 101 - код запроса
        });


        btnFacebook.setOnClickListener(v -> Toast.makeText(this, "Facebook Login coming soon", Toast.LENGTH_SHORT).show());
        btnLinkedIn.setOnClickListener(v -> Toast.makeText(this, "LinkedIn Login coming soon", Toast.LENGTH_SHORT).show());
    }

    private void setupGradientTitle(TextView textView, String text) {
        SpannableString spannable = new SpannableString(text);

        int index = text.indexOf("!");
        if (index != -1) {
            spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC),
                    index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#38FFD7")),
                    index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setText(spannable);

        //градиент
        textView.post(() -> {
            TextPaint paint = textView.getPaint();
            float width = paint.measureText(textView.getText().toString());

            Shader textShader = new LinearGradient(0, 0, width, 0,
                    new int[]{Color.parseColor("#a86bff"), Color.parseColor("#38FFD7")},
                    null, Shader.TileMode.CLAMP);

            paint.setShader(textShader);
            textView.invalidate();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, RoleSelectionActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Auth Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}