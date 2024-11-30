package com.example.generadorup;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity9 extends AppCompatActivity {

    private EditText passwordEditText;
    private TextView analysisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        passwordEditText = findViewById(R.id.passwordEditText);
        analysisTextView = findViewById(R.id.analysisTextView);

        findViewById(R.id.analyzeButton).setOnClickListener(view -> {
            String password = passwordEditText.getText().toString();
            PasswordEvaluator.PasswordAnalysis analysis = PasswordEvaluator.analyzePassword(password);

            String result = "Fortaleza: " + analysis.getStrength() + "\n"
                    + "Puntuación: " + analysis.getScore() + "/11\n"
                    + "Recomendaciones:\n" + analysis.getRecommendations();

            analysisTextView.setText(result);
        });
    }
}