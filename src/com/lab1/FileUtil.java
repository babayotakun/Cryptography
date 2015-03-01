package com.lab1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtil {

    public static final String KEY_FILENAME = "key.txt";
    public static final String IN_FILENAME = "in.txt";
    public static final String CRYPT_FILENAME = "crypt.txt";
    public static final String DECRYPT_FILENAME = "decrypt.txt";

    public static String readFile(String filename) {
        StringBuilder data = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public static void writeFile(String filename, String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(data);
            writer.flush();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int[] getNumbers(String chars) {
        int[] numbers = new int[chars.length()];
        for (int i = 0; i < chars.length(); i++) {
           if (chars.charAt(i) == ' ') {
                numbers[i] = Alphabet.space.getValue();
                continue;
            }
            numbers[i] = Alphabet.valueOf("" + chars.charAt(i)).getValue();
        }
        return numbers;
    }

    public static String getChars(int[] numbers) {
        StringBuilder chars = new StringBuilder();
        for (int number : numbers) {
            if (Alphabet.space.getValue() == number) {
                chars.append(" ");
                continue;
            }
            chars.append(Alphabet.getString(number));
        }
        return chars.toString();
    }
}
