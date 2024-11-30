package com.example.generadorup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity8 extends AppCompatActivity {

    private EditText usernameEditText, currentPasswordEditText, newPasswordEditText;
    private Button submitChangeButton;
    private static final String TAG = "MainActivity8"; // Para la depuración de log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        usernameEditText = findViewById(R.id.usernameEditText);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        submitChangeButton = findViewById(R.id.submitChangeButton);

        submitChangeButton.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String username = usernameEditText.getText().toString().trim();
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        if (username.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedCurrentPassword = hashData(currentPassword);
        String hashedNewPassword = hashData(newPassword);

        if (hashedCurrentPassword == null || hashedNewPassword == null) {
            Toast.makeText(this, "Error al encriptar la contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        sendChangePasswordRequest(username, hashedCurrentPassword, hashedNewPassword);
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
            Log.e(TAG, "Error al generar el hash: " + e.getMessage());
            return null;
        }
    }

    private void sendChangePasswordRequest(final String username, final String hashedCurrentPassword, final String hashedNewPassword) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                // URL del endpoint PHP
                String urlString = "http:/192.168.43.240/movil/change_password.php";
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Tiempo de espera extendido
                conn.setConnectTimeout(20000); // 20 segundos para la conexión
                conn.setReadTimeout(20000); // 20 segundos para la lectura

                String data = "username=" + username + "&current_password=" + hashedCurrentPassword + "&new_password=" + hashedNewPassword;
                Log.d(TAG, "Datos enviados al servidor: " + data);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Código de respuesta del servidor: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response = in.readLine();
                    in.close();

                    Log.d(TAG, "Respuesta del servidor: " + response);

                    runOnUiThread(() -> {
                        if ("Contraseña actualizada".equals(response)) {
                            Toast.makeText(MainActivity8.this, "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if ("Usuario o contraseña actual incorrectos".equals(response)) {
                            Toast.makeText(MainActivity8.this, "Usuario o contraseña actual incorrectos", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity8.this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity8.this, "Error de conexión: Código " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error al conectar con el servidor: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(MainActivity8.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
    }
}
