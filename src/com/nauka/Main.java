package com.nauka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.DecimalFormat;

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

            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");

            System.out.println(getARIScore(numOfWords, numOfSentences, numOfCharacters));
            System.out.println(getFKScore(numOfWords, numOfSentences, numOfSyllables));
            System.out.println(getSMOGScore(numOfSentences, numOfPolysyllables));
            System.out.println(getCLScore(numOfWords, numOfSentences, numOfCharacters));

            //double score = 4.71 * (numOfCharacters / numOfWords) + 0.5 * (numOfWords / numOfSentences) - 21.43;

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

    public static void printResult(double words, double sentences, double characters, double score) {
        int roundedScore = (int) Math.ceil(score);
        String ageRange;

        switch (roundedScore) {
            case 1:
                ageRange = "5-6";
                break;
            case 2:
                ageRange = "6-7";
                break;
            case 3:
                ageRange = "7-9";
                break;
            case 4:
                ageRange = "9-10";
                break;
            case 5:
                ageRange = "10-11";
                break;
            case 6:
                ageRange = "11-12";
                break;
            case 7:
                ageRange = "12-13";
                break;
            case 8:
                ageRange = "13-14";
                break;
            case 9:
                ageRange = "14-15";
                break;
            case 10:
                ageRange = "15-16";
                break;
            case 11:
                ageRange = "16-17";
                break;
            case 12:
                ageRange = "17-18";
                break;
            case 13:
                ageRange = "18-24";
                break;
            case 14:
                ageRange = "24+";
                break;
            default:
                ageRange = "";
        }

        DecimalFormat df1 = new DecimalFormat("#");
        DecimalFormat df2 = new DecimalFormat("#.##");

        String result = "Words: " + df1.format(words) + "\n" +
                "Sentences: " + df1.format(sentences) + "\n" +
                "Characters: " + df1.format(characters) + "\n" +
                "The score is: " + df2.format(score) + "\n" +
                "This text should be understood by " + ageRange + "-year-olds.";

        System.out.println(result);

    }

}
