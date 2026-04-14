package hw5;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create Scanner to read user input from keyboard
        Scanner scanner = new Scanner(System.in, "UTF-8");

        // Prompt user for Google API key
        System.out.print("Enter the API Key: ");
        String apiKey = scanner.nextLine();

        // Prompt user for text to translate
        System.out.print("Enter the plaintext: ");
        String plaintext = scanner.nextLine();

        System.out.print("Enter Caesar shift key: ");
        int shift = Integer.parseInt(scanner.nextLine());

        String targetLanguage = "ar"; // Arabic

        try {
            String translatedText = GoogleTranslate.translateText(apiKey, plaintext, targetLanguage);

            System.out.println("\nTranslated Arabic text:");
            System.out.println(translatedText);

            String encryptedText = CaesarCipherFrequencyAnalysis.encryptWithShift(translatedText, shift);

            System.out.println("\nEncrypted Arabic text:");
            System.out.println(encryptedText);

            CaesarCipherFrequencyAnalysis.printLetterCounts(encryptedText);

            String bestGuess = CaesarCipherFrequencyAnalysis.decryptUsingFrequencyAnalysis(encryptedText);

            System.out.println("\nBest guess decryption using frequency analysis:");
            System.out.println(bestGuess);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
