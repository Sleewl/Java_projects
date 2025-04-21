package Lecture;

import java.util.Scanner;
public class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите строку: ");
            String input = scanner.nextLine().toLowerCase();

            int[] englishLetterCount = new int[26];
            int[] russianLetterCount = new int[32];

            for (char ch : input.toCharArray()) {
                if (ch >= 'a' && ch <= 'z') {
                    englishLetterCount[ch - 'a']++;
                } else if (ch >= 'а' && ch <= 'я') {
                    russianLetterCount[ch - 'а']++;
                }
            }

            System.out.println("Вхождения английских букв:");
            for (int i = 0; i < englishLetterCount.length; i++) {
                if (englishLetterCount[i] > 0) {
                    System.out.println("Буква '" + (char) (i + 'a') + "' встречается " + englishLetterCount[i] + " раз(а)");
                }
            }

            System.out.println("Вхождения русских букв:");
            for (int i = 0; i < russianLetterCount.length; i++) {
                if (russianLetterCount[i] > 0) {
                    System.out.println("Буква '" + (char) (i + 'а') + "' встречается " + russianLetterCount[i] + " раз(а)");
                }
            }
            scanner.close();
        }
    }


