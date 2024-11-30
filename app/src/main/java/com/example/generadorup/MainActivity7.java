package com.example.generadorup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity7 extends AppCompatActivity {

    private Button backToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        backToMainButton = findViewById(R.id.backToMainButton);

        // Configurar el botón para regresar a MainActivity3
        backToMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity7.this, MainActivity3.class);
            startActivity(intent);
            finish();
        });
    }
}
