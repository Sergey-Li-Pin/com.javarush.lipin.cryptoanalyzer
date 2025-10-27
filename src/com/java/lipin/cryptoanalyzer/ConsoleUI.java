package com.java.lipin.cryptoanalyzer;

import java.util.Scanner;

public class ConsoleUI {

    private Scanner consoleScanner;

    public ConsoleUI() {
        this.consoleScanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Криптоанализатор ===");
        System.out.println("Программа для шифрования, дешифрования и криптоанализа текстовых файлов");

        // Главный цикл приложения
        while (true) {
            printMenu();
            String choice = consoleScanner.nextLine();

            // Обработка выбора пользователя
            switch (choice) {
                case "1":
                    encryptFile();
                    break;
                case "2":
                    decryptFile();
                    break;
                case "3":
                    bruteForceFile();
                    break;
                case "4":
                    System.out.println("Выход из программы. До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите пункт от 1 до 4.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nВыберите пункт меню:");
        System.out.println("1 - Зашифровать файл");
        System.out.println("2 - Расшифровать файл (нужен ключ)");
        System.out.println("3 - Brute force атака (подбор ключа)");
        System.out.println("4 - Выход");
        System.out.print("Ваш выбор: ");
    }

    private void encryptFile() {
        try {
            System.out.print("Введите путь к файлу для шифрования: ");
            String filePath = consoleScanner.nextLine();
            String text = FileService.readFile(filePath);

            System.out.print("Введите ключ шифрование (целое число): ");
            int key = Integer.parseInt(consoleScanner.nextLine());
            String encryptedText = CaesarCipher.encrypt(text, key);
            String outputPath = getOutputPath(filePath, "encrypted");
            FileService.writeFile(outputPath, encryptedText);

            System.out.println("Файл успешно зашифрован: " + outputPath);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ключ должен быть целым числом.");
        } catch (Exception e) {
            System.out.println("Ошибка при шифровании: " + e.getMessage());
        }
    }

    private void decryptFile() {
        try {
            System.out.print("Введите путь к файлу для расшифрования: ");
            String filePath = consoleScanner.nextLine();
            String text = FileService.readFile(filePath);

            System.out.print("Введите ключ дешифровки (целое число): ");
            int key = Integer.parseInt(consoleScanner.nextLine());
            String decryptedText = CaesarCipher.decrypt(text, key);
            String outputPath = getOutputPath(filePath, "decrypted");
            FileService.writeFile(outputPath, decryptedText);

            System.out.println("Файл успешно расшифрован: " + outputPath);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ключ должен быть целым числом.");
        } catch (Exception e) {
            System.out.println("Ошибка при расшифровке: " + e.getMessage());
        }
    }

    private void bruteForceFile() {
        try {
            System.out.print("Введите путь к файлу для brute force атаки: " );
            String filePath = consoleScanner.nextLine();
            String text = FileService.readFile(filePath);
            BruteForce.decryptAllVariants(text, filePath);
        } catch (Exception e) {
            System.out.println("Ошибка при brute force: " + e.getMessage());
        }
    }

    private String getOutputPath(String originalPath, String suffix) {
        int dotIndex = originalPath.lastIndexOf('.');
        if (dotIndex != -1) {
            // Если есть расширение, вставляем суффикс перед точкой
            return originalPath.substring(0, dotIndex) + "_" + suffix + originalPath.substring((dotIndex));
        }
        // Если расширения нет, просто добавляем суффикс в конец
        return originalPath + "_" + suffix;
    }
}
