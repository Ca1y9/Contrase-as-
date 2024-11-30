package com.example.generadorup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    private EditText nameEditText, favoriteSingerEditText, favoriteColorEditText, passwordLengthEditText;
    private CheckBox upperCaseCheckBox, lowerCaseCheckBox, numbersCheckBox, symbolsCheckBox;
    private TextView generatedPasswordTextView;
    private Button generateButton, copyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nameEditText = findViewById(R.id.nameEditText);
        favoriteSingerEditText = findViewById(R.id.favoriteSingerEditText);
        favoriteColorEditText = findViewById(R.id.favoriteColorEditText);
        passwordLengthEditText = findViewById(R.id.passwordLengthEditText);
        upperCaseCheckBox = findViewById(R.id.upperCaseCheckBox);
        lowerCaseCheckBox = findViewById(R.id.lowerCaseCheckBox);
        numbersCheckBox = findViewById(R.id.numbersCheckBox);
        symbolsCheckBox = findViewById(R.id.symbolsCheckBox);
        generatedPasswordTextView = findViewById(R.id.generatedPasswordTextView);
        generateButton = findViewById(R.id.generateButton);
        copyButton = findViewById(R.id.copyButton);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePassword();
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyPasswordToClipboard();
            }
        });
    }

    private void generatePassword() {
        String name = nameEditText.getText().toString().trim();
        String favoriteSinger = favoriteSingerEditText.getText().toString().trim();
        String favoriteColor = favoriteColorEditText.getText().toString().trim();
        String lengthInput = passwordLengthEditText.getText().toString().trim();

        // Validar longitud
        if (lengthInput.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa una longitud.", Toast.LENGTH_SHORT).show();
            return;
        }

        int length = Integer.parseInt(lengthInput);
        if (length < 8 || length > 20) {
            Toast.makeText(this, "La longitud debe estar entre 8 y 20 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || favoriteSinger.isEmpty() || favoriteColor.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si al menos un CheckBox está seleccionado
        if (!upperCaseCheckBox.isChecked() && !lowerCaseCheckBox.isChecked() &&
                !numbersCheckBox.isChecked() && !symbolsCheckBox.isChecked()) {
            Toast.makeText(this, "Seleccione al menos una opción de tipo de carácter.", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = generateCustomPassword(name, favoriteSinger, favoriteColor,
                upperCaseCheckBox.isChecked(), lowerCaseCheckBox.isChecked(),
                numbersCheckBox.isChecked(), symbolsCheckBox.isChecked(), length);

        generatedPasswordTextView.setText(password);
    }

    private String generateCustomPassword(String name, String singer, String color,
                                          boolean useUpper, boolean useLower, boolean useNumbers, boolean useSymbols, int length) {
        List<Character> passwordChars = new ArrayList<>();
        Random random = new Random();

        // Función de ayuda para añadir caracteres aleatorios
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()_+";

        // Garantizar al menos un carácter de cada tipo seleccionado
        if (useUpper) {
            passwordChars.add(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        }
        if (useLower) {
            passwordChars.add(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        }
        if (useNumbers) {
            passwordChars.add(numbers.charAt(random.nextInt(numbers.length())));
        }
        if (useSymbols) {
            passwordChars.add(symbols.charAt(random.nextInt(symbols.length())));
        }

        // Rellenar el resto de la contraseña de forma aleatoria
        while (passwordChars.size() < length) {
            if (useUpper && passwordChars.size() < length) {
                passwordChars.add(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
            }
            if (useLower && passwordChars.size() < length) {
                passwordChars.add(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
            }
            if (useNumbers && passwordChars.size() < length) {
                passwordChars.add(numbers.charAt(random.nextInt(numbers.length())));
            }
            if (useSymbols && passwordChars.size() < length) {
                passwordChars.add(symbols.charAt(random.nextInt(symbols.length())));
            }
        }

        // Mezclar los caracteres para que la distribución sea aleatoria
        Collections.shuffle(passwordChars);

        // Convertir a cadena final
        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }

    private void copyPasswordToClipboard() {
        String password = generatedPasswordTextView.getText().toString();
        if (!password.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Contraseña generada", password);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay contraseña generada para copiar", Toast.LENGTH_SHORT).show();
        }
    }
}
