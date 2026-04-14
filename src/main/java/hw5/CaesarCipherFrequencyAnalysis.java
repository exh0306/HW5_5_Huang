package hw5;

import java.util.*;

public class CaesarCipherFrequencyAnalysis {

    private static final char[] ARABIC_LETTERS = {
            'ا','ب','ت','ث','ج','ح','خ','د','ذ','ر','ز','س','ش','ص',
            'ض','ط','ظ','ع','غ','ف','ق','ك','ل','م','ن','ه','و','ي'
    };

    private static final double[] ARABIC_FREQUENCIES = {
            11.6, 4.8, 3.7, 1.1, 2.8, 2.6, 1.1, 3.5, 1.0, 4.7,
            0.9, 6.5, 3.0, 2.9, 1.5, 1.7, 0.7, 3.9, 1.0, 3.0,
            2.7, 3.6, 5.3, 3.1, 7.2, 2.5, 6.0, 6.7
    };

    public static String encryptWithShift(String text, int shift) {
        StringBuilder encryptedText = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = indexOfArabicLetter(c);

            if (index != -1) {
                int newIndex = (index + shift) % ARABIC_LETTERS.length;
                encryptedText.append(ARABIC_LETTERS[newIndex]);
            } else {
                encryptedText.append(c);
            }
        }

        return encryptedText.toString();
    }

    public static String decryptWithShift(String text, int shift) {
        StringBuilder decryptedText = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = indexOfArabicLetter(c);

            if (index != -1) {
                int newIndex = (index - shift) % ARABIC_LETTERS.length;
                if (newIndex < 0) {
                    newIndex += ARABIC_LETTERS.length;
                }
                decryptedText.append(ARABIC_LETTERS[newIndex]);
            } else {
                decryptedText.append(c);
            }
        }

        return decryptedText.toString();
    }

    public static String decryptUsingFrequencyAnalysis(String ciphertext) {
        String bestDecryption = "";
        double lowestChiSquare = Double.MAX_VALUE;

        for (int shift = 0; shift < ARABIC_LETTERS.length; shift++) {
            String decryptedText = decryptWithShift(ciphertext, shift);
            double chiSquare = calculateChiSquare(decryptedText);

            if (chiSquare < lowestChiSquare) {
                lowestChiSquare = chiSquare;
                bestDecryption = decryptedText;
            }
        }

        return bestDecryption;
    }

    public static double calculateChiSquare(String text) {
        int[] letterCounts = new int[ARABIC_LETTERS.length];
        int totalLetters = 0;

        for (char c : text.toCharArray()) {
            int index = indexOfArabicLetter(c);
            if (index != -1) {
                letterCounts[index]++;
                totalLetters++;
            }
        }

        double chiSquare = 0.0;

        for (int i = 0; i < ARABIC_LETTERS.length; i++) {
            double observed = letterCounts[i];
            double expected = totalLetters * ARABIC_FREQUENCIES[i] / 100.0;

            if (expected > 0) {
                chiSquare += Math.pow(observed - expected, 2) / expected;
            }
        }

        return chiSquare;
    }

    public static void printLetterCounts(String text) {
        int[] letterCounts = new int[ARABIC_LETTERS.length];
        int totalLetters = 0;

        for (char c : text.toCharArray()) {
            int index = indexOfArabicLetter(c);
            if (index != -1) {
                letterCounts[index]++;
                totalLetters++;
            }
        }

        System.out.println("\nArabic Frequency Analysis:");
        System.out.println("Total Arabic letters counted: " + totalLetters);

        for (int i = 0; i < ARABIC_LETTERS.length; i++) {
            double observedPercent = totalLetters == 0 ? 0 : (letterCounts[i] * 100.0 / totalLetters);

            System.out.printf("%c -> Count: %d, Observed: %.2f%%, Expected: %.2f%%%n",
                    ARABIC_LETTERS[i], letterCounts[i], observedPercent, ARABIC_FREQUENCIES[i]);
        }
    }

    private static int indexOfArabicLetter(char c) {
        for (int i = 0; i < ARABIC_LETTERS.length; i++) {
            if (ARABIC_LETTERS[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
