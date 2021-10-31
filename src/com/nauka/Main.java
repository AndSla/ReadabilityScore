package com.nauka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
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
            Map<String, Double> scores = getScores(numOfWords, numOfSentences, numOfCharacters, numOfSyllables, numOfPolysyllables);

            printQuantities(numOfWords, numOfSentences, numOfCharacters, numOfSyllables, numOfPolysyllables);
            String chosenAlgorithm = getUserInput();
            printResult(chosenAlgorithm, scores);

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

    public static Map<String, Double> getScores(double words,
                                                double sentences,
                                                double characters,
                                                double syllables,
                                                double polysyllables) {

        double ariScore = 4.71 * (characters / words) + 0.5 * (words / sentences) - 21.43;
        double fkScore = 0.39 * (words / sentences) + 11.8 * (syllables / words) - 15.59;
        double smogScore = 1.043 * Math.sqrt(polysyllables * (30 / sentences)) + 3.1291;
        double l = (100 * characters) / words;
        double s = (100 * sentences) / words;
        double clScore = 0.0588 * l - 0.296 * s - 15.8;
        double average = (ariScore + fkScore + smogScore + clScore) / 4;

        Map<String, Double> scores = new HashMap<>();
        scores.put("ARI", ariScore);
        scores.put("FK", fkScore);
        scores.put("SMOG", smogScore);
        scores.put("CL", clScore);
        scores.put("all", average);

        return scores;

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

        System.out.println();
        return input;

    }

    public static int getAge(double score) {
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

    public static void printResult(String chosenAlgorithm, Map<String, Double> scores) {
        DecimalFormat df = new DecimalFormat("#.##");

        double score = scores.get(chosenAlgorithm);
        int age = getAge(score);

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
            case "all":
                System.out.println("Automated Readability Index: " + df.format(scores.get("ARI")) + " (about " + getAge(scores.get("ARI")) + "-year-olds).");
                System.out.println("Flesch–Kincaid readability tests: " + df.format(scores.get("FK")) + " (about " + getAge(scores.get("FK")) + "-year-olds).");
                System.out.println("Simple Measure of Gobbledygook: " + df.format(scores.get("SMOG")) + " (about " + getAge(scores.get("SMOG")) + "-year-olds).");
                System.out.println("Coleman–Liau index: " + df.format(scores.get("CL")) + " (about " + getAge(scores.get("CL")) + "-year-olds).");
                System.out.println();
                System.out.println("This text should be understood in average by " + df.format(scores.get(chosenAlgorithm)) + "-year-olds.");
        }

    }

}
