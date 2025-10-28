package com.javarush.lipin.cryptoanalyzer;

import com.javarush.lipin.cryptoanalyzer.controllers.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // Создание и запуск консольного интерфейса
        ConsoleUI consoleUI = new ConsoleUI();
        consoleUI.start();
    }
}
