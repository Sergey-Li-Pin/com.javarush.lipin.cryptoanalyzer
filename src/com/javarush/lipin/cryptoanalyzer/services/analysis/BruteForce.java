package com.javarush.lipin.cryptoanalyzer.services.analysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.javarush.lipin.cryptoanalyzer.services.encryption.CaesarCipher;
import com.javarush.lipin.cryptoanalyzer.services.file.FileService;

public class BruteForce {

    public static void decryptAllVariants(String encryptedText, String originalFilePath) throws IOException {
        // Создаем уникальное имя папки с timestamp для организации результатов
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));
        String resultsDirName = "brute_force_results_" + timestamp;

        // Определяем, где создавать папку с результатами
        // Пытаемся создать папку в той же директории, что и исходный файл
        Path originalPath = Paths.get(originalFilePath);
        Path parentDir = originalPath.getParent();

        // Если не удалось определить родительскую директорию (допустим, файл в корне диска),
        // используем текущую директорию
        if (parentDir == null) {
            parentDir = Paths.get("");
        }

        // Создаем полный путь к папке результатов
        Path resultsDirPath = parentDir.resolve(resultsDirName);
        Files.createDirectories(resultsDirPath);

        // Получаем абсолютный путь к папке результатов
        Path absoluteresultPath = resultsDirPath.toAbsolutePath();

        int alphabetLength = CaesarCipher.getAlphabetLength();
        int totalKeys = alphabetLength - 1; // Ключи от 1 до alphabetLength - 1

        System.out.println("Начинаем перебор " + totalKeys + " ключей...");
        System.out.println("Результаты будут сохранены в папке: " + absoluteresultPath);

        // Перебираем все возможные ключи (исключая 0 и alphabetLength)
        for (int key = 1; key < alphabetLength; key++) {

            // Дешифруем текст текущим ключом
            String decryptedText = CaesarCipher.decrypt(encryptedText, key);

            // Создаем имя файла с указанием использованного ключа
            String outputFileName = "decrypted_key_" + key + ".txt";
            Path outputFilePath = resultsDirPath.resolve(outputFileName);

            // Сохраняем результат дешифрования
            FileService.writeFile(outputFilePath.toString(), decryptedText);

            // Выводим прогресс каждые 10 ключей для информирования пользователя
            if (key % 10 == 0) {
                System.out.println("Обработан ключ: " + key + " из " + totalKeys);
            }
        }

        System.out.println("Brute force завершен! Все " + totalKeys + " вариантов сохранены в папке: " + absoluteresultPath);
        System.out.println("Для нахождения правильного текста просмотрите файлы и найдите необходимый.");
    }

    public static String decryptedWithStatisticalanalysis(String encryptedText) {
        System.out.println("Запуск статистического анализа...");

        int probableKey = FrequencyAnalyzer.findKeyByStatisticalAnalysis(encryptedText);
        System.out.println("Найден наиболее вероятный ключ: " + probableKey);

        String decryptedText = CaesarCipher.decrypt(encryptedText, probableKey);


        System.out.println("Текст успешно расшифрован  с помощью статистического анализа!");
        return decryptedText;
    }
}
