package com.nauka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = args[0];

        try {
            String text = getTextFromFile(".\\src\\com\\nauka\\" + filePath);
            double numOfSentences = getNumOf("sentences", text);
            double numOfWords = getNumOf("words", text);
            double numOfCharacters = getNumOf("characters", text);
            double numOfSyllables = getNumOf("syllables", text);
            double numOfPolysyllables = getNumOf("polysyllables", text);

            printQuantities(numOfWords, numOfSentences, numOfCharacters, numOfSyllables, numOfPolysyllables);

            String chosenAlgorithm = getUserInput();
            System.out.println();

            if (chosenAlgorithm != null) {
                double ariScore = getARIScore(numOfWords, numOfSentences, numOfCharacters);
                double fkScore = getFKScore(numOfWords, numOfSentences, numOfSyllables);
                double smogScore = getSMOGScore(numOfSentences, numOfPolysyllables);
                double clScore = getCLScore(numOfWords, numOfSentences, numOfCharacters);
                int average = (getResult(ariScore) + getResult(fkScore) + getResult(smogScore) + getResult(clScore)) / 4;
                DecimalFormat df = new DecimalFormat("#.##");

                switch (chosenAlgorithm) {
                    case "all":
                        printResult("ARI", ariScore, getResult(ariScore));
                        printResult("FK", fkScore, getResult(fkScore));
                        printResult("SMOG", smogScore, getResult(smogScore));
                        printResult("CL", clScore, getResult(clScore));
                        System.out.println();
                        System.out.println("This text should be understood in average by " + average + "-year-olds.");
                        break;
                    case "ARI":
                        printResult("ARI", ariScore, getResult(ariScore));
                        break;
                    case "FK":
                        printResult("FK", fkScore, getResult(fkScore));
                        break;
                    case "SMOG":
                        printResult("SMOG", smogScore, getResult(smogScore));
                        break;
                    case "CL":
                        printResult("CL", clScore, getResult(clScore));
                }
            }

        } catch (NoSuchFileException e) {
            System.out.println("File not found!");
        }

    }

    public static String getTextFromFile(String filePath) throws IOException {
        String text = Files.readAllLines(Paths.get(filePath)).toString();
        text = text.substring(1, text.length() - 1); // cut off first and last characters in order to get rid of [ and ] brackets from converting list to string
        return text;
    }

    public static double getNumOf(String what, String text) {
        switch (what) {
            case "sentences":
                String[] sentences;
                sentences = text.split("[.?!]");
                return sentences.length;

            case "words":
                String[] words;
                words = text.split("[.,?!]*\\s");  // split on .,? or ! with whitespace
                return words.length;

            case "characters":
                char[] characters;
                double numOfCharacters = 0;
                characters = text.toCharArray();
                for (char character : characters) {
                    if (String.valueOf(character).matches("\\S")) { // checks if char is non-whitespace
                        numOfCharacters += 1;
                    }
                }
                return numOfCharacters;

            case "syllables":
                words = text.split("[.,?!]*\\s");
                int numOfSyllables = 0;

                for (String word : words) {
                    numOfSyllables += getNumOfSyllables(word);
                }

                return numOfSyllables;

            case "polysyllables":
                words = text.split("[.,?!]*\\s");
                int numOfPolysyllables = 0;

                for (String word : words) {
                    if (getNumOfSyllables(word) > 2) {
                        numOfPolysyllables += 1;
                    }
                }

                return numOfPolysyllables;

            default:
                return 0;
        }
    }

    public static double getNumOfSyllables(String word) {
        String[] chars = word.split("");
        int numOfVowels = 0;
        int numOfSyllables = 0;

        for (int i = 0; i < word.length(); i++) {
            if (chars[i].matches("[aAeEiIoOuUyY]")) {   // counting vowels in words
                numOfVowels += 1;
                if (i < word.length() - 1 &&
                        chars[i + 1].matches("[aAeEiIoOuUyY]")) {   // get rid of vowels that or next to each other and forms one syllable
                    numOfVowels -= 1;
                }
            }
        }

        numOfSyllables += numOfVowels;

        if (numOfVowels == 0) {
            numOfSyllables += 1;
        } else if (numOfVowels > 1 && word.matches("\\w*[eE]")) {   // don't count e in the end of word as the syllable
            numOfSyllables -= 1;
        }

        return numOfSyllables;

    }

    public static void printQuantities(
            double words,
            double sentences,
            double characters,
            double syllables,
            double polysyllables) {

        DecimalFormat df = new DecimalFormat("#");

        String result = "Words: " + df.format(words) + "\n" +
                "Sentences: " + df.format(sentences) + "\n" +
                "Characters: " + df.format(characters) + "\n" +
                "Syllables: " + df.format(syllables) + "\n" +
                "Polysyllables: " + df.format(polysyllables);

        System.out.println(result);
    }

    // Automated readability index
    public static double getARIScore(double words, double sentences, double characters) {
        return 4.71 * (characters / words) + 0.5 * (words / sentences) - 21.43;
    }

    // Flesch–Kincaid readability test
    public static double getFKScore(double words, double sentences, double syllables) {
        return 0.39 * (words / sentences) + 11.8 * (syllables / words) - 15.59;
    }

    // Simple Measure of Gobbledygook
    public static double getSMOGScore(double sentences, double polysyllables) {
        return 1.043 * Math.sqrt(polysyllables * (30 / sentences)) + 3.1291;
    }

    // Coleman–Liau index
    public static double getCLScore(double words, double sentences, double characters) {
        double l = (100 * characters) / words;
        double s = (100 * sentences) / words;
        return 0.0588 * l - 0.296 * s - 15.8;
    }

    public static String getUserInput() {
        Scanner sc = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            input = sc.next();

            if (input.matches("ARI|FK|SMOG|CL|all")) {
                break;
            } else if ("exit".equals(input)) {
                break;
            } else {
                System.out.println("Wrong algorithm, try again or type \"exit\".");
            }

        }

        return input;

    }

    public static int getResult(double score) {
        int roundedScore = (int) Math.ceil(score);
        int age;

        switch (roundedScore) {
            case 1:
                age = 6;
                break;
            case 2:
                age = 7;
                break;
            case 3:
                age = 9;
                break;
            case 4:
                age = 10;
                break;
            case 5:
                age = 11;
                break;
            case 6:
                age = 12;
                break;
            case 7:
                age = 13;
                break;
            case 8:
                age = 14;
                break;
            case 9:
                age = 15;
                break;
            case 10:
                age = 16;
                break;
            case 11:
                age = 17;
                break;
            case 12:
                age = 18;
                break;
            case 13:
            case 14:
                age = 24;
                break;
            default:
                age = 0;
        }

        return age;
    }

    public static void printResult(String chosenAlgorithm, double score, int age) {
        DecimalFormat df = new DecimalFormat("#.##");

        switch (chosenAlgorithm) {
            case "ARI":
                System.out.println("Automated Readability Index: " + df.format(score) + " (about " + age + "-year-olds).");
                break;
            case "FK":
                System.out.println("Flesch–Kincaid readability tests: " + df.format(score) + " (about " + age + "-year-olds).");
                break;
            case "SMOG":
                System.out.println("Simple Measure of Gobbledygook: " + df.format(score) + " (about " + age + "-year-olds).");
                break;
            case "CL":
                System.out.println("Coleman–Liau index: " + df.format(score) + " (about " + age + "-year-olds).");
                break;
        }
    }

}
