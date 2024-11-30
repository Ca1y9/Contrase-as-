package com.example.generadorup;

public class PasswordEvaluator {
    public static PasswordAnalysis analyzePassword(String password) {
        int score = 0;
        StringBuilder recommendations = new StringBuilder();

        // Longitud de la contraseña
        if (password.length() >= 12) {
            score += 3; // Contraseña muy larga (más segura)
        } else if (password.length() >= 8) {
            score += 2; // Contraseña suficientemente larga
            recommendations.append("La contraseña tiene una longitud adecuada (al menos 8 caracteres).\n");
        } else {
            recommendations.append("La contraseña debería tener al menos 8 caracteres.\n");
        }

        // Uso de mayúsculas
        if (password.matches(".*[A-Z].*")) {
            score += 2;  // Aumenta la complejidad con mayúsculas
        } else {
            recommendations.append("Incluye al menos una letra mayúscula.\n");
        }

        // Uso de minúsculas
        if (password.matches(".*[a-z].*")) {
            score += 2;  // Aumenta la complejidad con minúsculas
        } else {
            recommendations.append("Incluye al menos una letra minúscula.\n");
        }

        // Uso de números
        if (password.matches(".*\\d.*")) {
            score += 2;  // Aumenta la complejidad con números
        } else {
            recommendations.append("Incluye al menos un número.\n");
        }

        // Uso de caracteres especiales
        if (password.matches(".*[\\W_].*")) {
            score += 3;  // Aumenta la complejidad con caracteres especiales
            recommendations.append("La contraseña contiene caracteres especiales.\n");
        } else {
            recommendations.append("Incluye al menos un carácter especial (e.g., !@#$%^&*()).\n");
        }

        // Evaluación de la fortaleza de la contraseña
        String strength;
        if (score >= 12) {
            strength = "Fuerte";
        } else if (score >= 8) {
            strength = "Moderada";
        } else {
            strength = "Débil";
        }

        // Resultados finales
        return new PasswordAnalysis(strength, score, recommendations.toString());
    }

    // Clase para encapsular el análisis de la contraseña
    public static class PasswordAnalysis {
        private final String strength;
        private final int score;
        private final String recommendations;

        public PasswordAnalysis(String strength, int score, String recommendations) {
            this.strength = strength;
            this.score = score;
            this.recommendations = recommendations;
        }

        public String getStrength() {
            return strength;
        }

        public int getScore() {
            return score;
        }

        public String getRecommendations() {
            return recommendations;
        }
    }
}
