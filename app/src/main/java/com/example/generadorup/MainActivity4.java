package com.example.generadorup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity4 extends AppCompatActivity {

    private static final String[] words = {
            "cielo", "tierra", "fuego", "agua", "sol", "luna", "estrella", "montaña", "río",
            "bosque", "nube", "trueno", "viento", "mar", "roca", "arena", "nieve", "lluvia",
            "relámpago", "árbol", "flor", "hoja", "piedra", "cascada", "camino", "sendero",
            "puente", "ciudad", "pueblo", "castillo", "torre", "puerta", "ventana", "casa",
            "edificio", "templo", "iglesia", "museo", "parque", "plaza", "playa", "desierto",
            "cueva", "volcán", "isla", "lago", "catarata", "selva", "jungla", "pradera",
            "valle", "colina", "glaciar", "oceano", "golfo", "bahía", "acantilado", "río",
            "arroyo", "pantano", "laguna", "delta", "fiordo", "península", "duna", "faro",
            "estanque", "charco", "barro", "catarata", "cascada", "viento", "trueno", "rayo",
            "neblina", "hielo", "granizo", "tormenta", "ciclón", "huracán", "tsunami",
            "terremoto", "avalancha", "deslizamiento", "erupción", "incendio", "resplandor",
            "crepúsculo", "amanecer", "anochecer", "aurora", "eclipse", "cometa", "meteoro",
            "asteroide", "galaxia", "universo", "cosmos", "átomo", "molécula", "célula",
            "neurona", "gen", "cromosoma", "ADN", "proteína", "enzima", "virus", "bacteria",
            "hormona", "ecosistema", "biosfera", "planeta", "estrella", "satélite", "órbita",
            "gravedad", "energía", "luz", "oscuridad", "fuerza", "potencia", "velocidad",
            "aceleración", "tiempo", "espacio", "dimensión"
    };

    private static final String symbols = "@#$%&*?!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        TextView passwordTextView = findViewById(R.id.generatedPasswordTextView);
        Button generateButton = findViewById(R.id.generatePasswordButton);
        Button copyButton = findViewById(R.id.copyButton);

        // Generar contraseña al hacer clic en el botón "Generar"
        generateButton.setOnClickListener(v -> passwordTextView.setText(generatePassword()));

        // Copiar contraseña al portapapeles al hacer clic en el botón "Copiar"
        copyButton.setOnClickListener(v -> copyPasswordToClipboard(passwordTextView.getText().toString()));
    }

    private String generatePassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        while (password.length() < 12) {
            String word = words[random.nextInt(words.length)];
            if (random.nextBoolean()) {
                word = capitalizeRandomly(word);
            }
            password.append(word);

            if (random.nextBoolean() && password.length() < 12) {
                password.append(random.nextInt(10));
            }

            if (random.nextBoolean() && password.length() < 12) {
                password.append(symbols.charAt(random.nextInt(symbols.length())));
            }
        }

        return password.substring(0, 12);
    }

    private String capitalizeRandomly(String word) {
        StringBuilder capitalizedWord = new StringBuilder();
        Random random = new Random();
        for (char c : word.toCharArray()) {
            if (random.nextBoolean()) {
                capitalizedWord.append(Character.toUpperCase(c));
            } else {
                capitalizedWord.append(c);
            }
        }
        return capitalizedWord.toString();
    }

    private void copyPasswordToClipboard(String password) {
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
