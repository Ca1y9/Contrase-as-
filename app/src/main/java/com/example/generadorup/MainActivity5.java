package com.example.generadorup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity5 extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private CheckBox showPasswordCheckBox;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        registerButton = findViewById(R.id.registerButton);

        // Mostrar/ocultar contraseña al marcar el CheckBox
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            passwordEditText.setSelection(passwordEditText.length());
        });

        // Registrar usuario al hacer clic en el botón "Registrar"
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Solo hashear la contraseña
        String hashedPassword = hashData(password);

        // Verificar si el hash de la contraseña se generó correctamente antes de enviar los datos
        if (hashedPassword != null) {
            sendUserDataToDatabase(username, hashedPassword);  // Solo pasar el nombre de usuario sin hashear
        } else {
            Toast.makeText(this, "Error al encriptar la contraseña.", Toast.LENGTH_SHORT).show();
        }
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

    private void sendUserDataToDatabase(final String username, final String hashedPassword) {
        new Thread(() -> {
            try {
                URL url = new URL("http:/192.168.43.240/movil/insert_user.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = "username=" + username + "&password=" + hashedPassword;  // Usar el nombre de usuario sin hashear

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String response = in.readLine();
                    in.close();

                    runOnUiThread(() -> {
                        if ("Registro exitoso".equals(response)) {
                            Toast.makeText(MainActivity5.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                        } else if (response.contains("Error: El usuario ya existe")) {
                            Toast.makeText(MainActivity5.this, "El usuario ya existe. Intenta con otro nombre.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity5.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity5.this, "Error de conexión", Toast.LENGTH_SHORT).show());
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity5.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
