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

            double score = 4.71 * (numOfCharacters / numOfWords) + 0.5 * (numOfWords / numOfSentences) - 21.43;

            printResult(numOfWords, numOfSentences, numOfCharacters, score);

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
                words = text.split("\\s");
                return words.length;

            case "characters":
                char[] characters;
                double numOfCharacters = 0;
                characters = text.toCharArray();
                for (char character : characters) {
                    if (String.valueOf(character).matches("\\S")) {
                        numOfCharacters += 1;
                    }
                }
                return numOfCharacters;

            default:
                return 0;
        }
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
