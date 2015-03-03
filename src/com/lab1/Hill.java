package com.lab1;

import java.math.BigInteger;

public class Hill {

    private static int[][] encriptionKey = new int[2][2];
    private static int[][] decriptionKey = new int[2][2];

    private static int mod = Alphabet.mod.getValue();

    private static void setupKey() {
        int[] keys = FileUtil.getNumbers(FileUtil.readFile(FileUtil.KEY_FILENAME));
        if (keys.length != 4) {
            return;
        }
        encriptionKey[0][0] = keys[0];
        encriptionKey[1][0] = keys[1];
        encriptionKey[0][1] = keys[2];
        encriptionKey[1][1] = keys[3];
    }

    private static int determinant(int[][] matrix)  {
        if (matrix[0].length == 2) {
            return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
        }
        return -1;
    }

    private static int modInverse(int determinant) {
        return BigInteger.valueOf(determinant).modInverse(BigInteger.valueOf(mod)).intValue();
    }

    private static int[] setupData(int[] data) {
        if (data.length % 2 == 0) {
            return data;
        }
        int[] newData = new int[data.length + 1];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        newData[data.length] = Alphabet.ъ.getValue();
        return newData;
    }

    private static void setupDecryptionKey() {

        decriptionKey[0][0] = encriptionKey[1][1];
        decriptionKey[1][1] = encriptionKey[0][0];
        decriptionKey[0][1] = -encriptionKey[0][1];
        decriptionKey[1][0] = -encriptionKey[1][0];

        int determinant = determinant(encriptionKey);
        int a = modInverse(determinant);

        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                int temp = 0;
                int x = (temp = decriptionKey[r][c] * a);
                if (x >= 0) {
                    temp /= mod;
                    temp *= mod;
                    decriptionKey[r][c] = x - temp;
                }
                else {
                    int mod2 = mod;
                    while (-1 * x > mod2) {
                        mod2 += mod;
                    }
                    decriptionKey[r][c] = x + mod2;
                }
            }
        }
    }



    private static int[] crypt(int[][] key, int[] numbers) {

        numbers = setupData(numbers);
        int[] cryptNumbers = new int[numbers.length];
        for (int sIndex = 0; sIndex < numbers.length; sIndex += 2) {
            for (int r = 0; r < 2; r++) {
                int[] block = new int[2];
                for (int c = 0; c < 2; c++) {
                    block[c] = key[r][c] * numbers[sIndex + c];
                }
                int sum = 0;
                for (int i : block) {
                    sum += i;
                }

                cryptNumbers[sIndex + r] = sum % mod;
            }
        }
        return cryptNumbers;
    }

    public static void main(String[] args) {
        setupKey();
        setupDecryptionKey();

        System.out.println(decriptionKey[0][0] + " " + decriptionKey[0][1] + "   " + encriptionKey[0][0] + " " + encriptionKey[0][1]);
        System.out.println(decriptionKey[1][0] + " " + decriptionKey[1][1] + "   " + encriptionKey[1][0] + " " + encriptionKey[1][1]);
        int[] crypt = crypt(encriptionKey, FileUtil.getNumbers(FileUtil.readFile(FileUtil.IN_FILENAME)));
        int[] decrypt = crypt(decriptionKey, crypt);

        FileUtil.writeFile(FileUtil.CRYPT_FILENAME, FileUtil.getChars(crypt));
        FileUtil.writeFile(FileUtil.DECRYPT_FILENAME, FileUtil.getChars(decrypt));
    }
}
