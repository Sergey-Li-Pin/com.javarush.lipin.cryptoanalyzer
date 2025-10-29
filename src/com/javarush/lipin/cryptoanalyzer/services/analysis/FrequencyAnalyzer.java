package com.javarush.lipin.cryptoanalyzer.services.analysis;

import com.javarush.lipin.cryptoanalyzer.services.encryption.CaesarCipher;

import java.util.*;

public class FrequencyAnalyzer {

    // Эталонные частоты русских букв
    private static final Map<Character, Double> RUSSIAN_LETTER_FREQUENCIES = Map.ofEntries(
            Map.entry('о', 0.1097), Map.entry('е', 0.0845), Map.entry('а', 0.0801),
            Map.entry('и', 0.0735), Map.entry('н', 0.0670), Map.entry('т', 0.0626),
            Map.entry('с', 0.0547), Map.entry('р', 0.0473), Map.entry('в', 0.0454),
            Map.entry('л', 0.0440), Map.entry('к', 0.0349), Map.entry('м', 0.0321),
            Map.entry('д', 0.0298), Map.entry('п', 0.0281), Map.entry('у', 0.0262),
            Map.entry('я', 0.0201), Map.entry('ы', 0.0190), Map.entry('ь', 0.0174),
            Map.entry('г', 0.0170), Map.entry('з', 0.0165), Map.entry('б', 0.0159),
            Map.entry('ч', 0.0144), Map.entry('й', 0.0121), Map.entry('х', 0.0097),
            Map.entry('ж', 0.0094), Map.entry('ш', 0.0073), Map.entry('ю', 0.0064),
            Map.entry('ц', 0.0048), Map.entry('щ', 0.0036), Map.entry('э', 0.0032),
            Map.entry('ф', 0.0026), Map.entry('ъ', 0.0004), Map.entry('ё', 0.0004)
    );

    public static int findKeyByStatisticalAnalysis(String encryptedText) {
        String letterAlphabet = CaesarCipher.getAlphabet().substring(0, 33);

        int bestKey = 0;
        double bestScore = Double.MAX_VALUE;

        for (int key = 1; key < letterAlphabet.length(); key++) {
            String decryptedText = CaesarCipher.decrypt(encryptedText, key);
            double score = calculateFrequencyScore(decryptedText, letterAlphabet);

            if (score < bestScore) {
                bestScore = score;
                bestKey = key;
            }
        }

        return bestKey;
    }

    private static double calculateFrequencyScore(String text, String alphabet) {
        // Подсчитываем частоты букв в тексте
        Map<Character, Integer> letterCounts = new HashMap<>();
        int totalLetters = 0;

        for (char c : text.toCharArray()) {
            char lowerChar = Character.toLowerCase(c);
            if (alphabet.indexOf(lowerChar) >= 0) {
                letterCounts.put(lowerChar, letterCounts.getOrDefault(lowerChar, 0) + 1);
                totalLetters++;
            }
        }

        if (totalLetters == 0) return Double.MAX_VALUE;

        // Вычисляем метрику различия с эталонными частотами
        double score = 0.0;
        for (Map.Entry<Character, Double> entry : RUSSIAN_LETTER_FREQUENCIES.entrySet()) {
            char letter = entry.getKey();
            double expectedFrequency = entry.getValue();
            double actualFrequency = (double) letterCounts.getOrDefault(letter, 0) / totalLetters;

            // используем квадратичную разницу для штрафа больших отклонений
            score += Math.pow(expectedFrequency - actualFrequency, 2);
        }

        return score;
    }
}
