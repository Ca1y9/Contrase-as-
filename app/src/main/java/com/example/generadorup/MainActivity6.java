package com.example.generadorup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity6 extends AppCompatActivity {

    private EditText loginUsernameEditText, loginPasswordEditText;
    private Button loginSubmitButton, cp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        loginUsernameEditText = findViewById(R.id.loginUsernameEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        loginSubmitButton = findViewById(R.id.loginSubmitButton);
        cp = findViewById(R.id.cp);

        loginSubmitButton.setOnClickListener(v -> loginUser());

        cp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity6.this, MainActivity8.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = loginUsernameEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Encriptar la contraseña antes de enviarla (no es necesario en algunos casos)
        String hashedPassword = hashData(password);

        // Enviar los datos al servidor
        sendLoginRequest(username, hashedPassword);
    }

    private String hashData(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendLoginRequest(final String username, final String hashedPassword) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.43.240/movil/login_user.php"); // Cambia la IP si es necesario
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String data = "username=" + username + "&password=" + hashedPassword; // Asegúrate de que se espera un hash en el servidor

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response = in.readLine();
                    in.close();

                    runOnUiThread(() -> {
                        if ("Login exitoso".equals(response)) {
                            Toast.makeText(MainActivity6.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity6.this, MainActivity7.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity6.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity6.this, "Error de conexión", Toast.LENGTH_SHORT).show());
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity6.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
