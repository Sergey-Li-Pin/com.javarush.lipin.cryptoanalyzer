package com.javarush.lipin.cryptoanalyzer.services.encryption;

public class CaesarCipher {

    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,\":-!? ";

    public static String encrypt(String text, int key) {
        return processText(text, key, true);
    }

    public static String decrypt(String text, int key) {
        return processText(text, key, false);
    }

    private static String processText(String text, int key, boolean encrypt) {
        // Обработка крайних случаев
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        int alphabetLength = ALPHABET.length();

        // Обрабатываем каждый символ входного текста
        for (char character : text.toCharArray()) {

            // Определяем, был ли символ в верхнем регистре (только для букв)
            boolean wasUpperCase = Character.isUpperCase(character) && isRussianLetter(character);

            // Преобразуем к нижнему регистру для поиска в алфавите
            char lowerChar = Character.toLowerCase(character);
            int index = ALPHABET.indexOf(lowerChar);

            if (index != -1) {
                // Символ найден в алфавите - применяем преобразование
                int newIndex;
                if (encrypt) {
                    newIndex = (index + key) % alphabetLength;
                } else {
                    newIndex = (index - key + alphabetLength) % alphabetLength;
                }

                char proccessedChar = ALPHABET.charAt(newIndex);

                // Востанавливаем регистр, если исходный символ был в верхнем регистре
                if (wasUpperCase) {
                    proccessedChar = Character.toUpperCase(proccessedChar);
                }

                result.append(proccessedChar);
            } else {
                // Символ не найден в алфавите - оставляем без изменений
                result.append(character);
            }
        }
        return result.toString();
    }

    private static boolean isRussianLetter(char c) {
        // Русские буквы в диапазоне от 'А' до 'я', включая 'Ё' и 'ё'
        return (c >= 'А' && c <= 'я') || c == 'Ё' || c == 'ё';
    }

    public static int getAlphabetLength() {
        return ALPHABET.length();
    }

}
