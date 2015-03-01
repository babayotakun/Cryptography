package com.lab1;

import java.math.BigInteger;

public class Affine {

    private static int alpha;
    private static int beta;
    private static int mod = Alphabet.mod.getValue();
    private static int modInverse;


    private static void setupKey() {
        int[] keys = FileUtil.getNumbers(FileUtil.readFile(FileUtil.KEY_FILENAME));
        if (keys.length != 2) {
            return;
        }
        alpha = keys[0];
        beta = keys[1];
        modInverse = modInverse();
    }

    public static int encryptNumber(int number) {
        return (alpha * number + beta) % mod;
    }

    public static int decryptNumber(int number) {
        return ((number - beta) + mod) * modInverse % mod;
    }

    public static int[] encrypt(int[] numbers) {
        int[] encrypt = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            encrypt[i] = encryptNumber(numbers[i]);
        }
        return encrypt;
    }

    public static int[] decrypt(int[] numbers) {
        int[] decrypt = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            decrypt[i] = decryptNumber(numbers[i]);
        }
        return decrypt;
    }

    private static int modInverse() {
        return BigInteger.valueOf(alpha).modInverse(BigInteger.valueOf(mod)).intValue();
    }


    public static void main(String[] args) {
        setupKey();
        String in = FileUtil.readFile(FileUtil.IN_FILENAME);
        String crypt = FileUtil.getChars(encrypt(FileUtil.getNumbers(in)));
        String decrypt = FileUtil.getChars(decrypt(FileUtil.getNumbers(crypt)));
        System.out.println("MESSAGE: " + in);
        System.out.println("ALPHA: " + alpha + "; BETA: " + beta + "; ALPHA INVERSE: " + modInverse);
        System.out.println("CRYPT: " + crypt);
        System.out.println("DECRYPT: " + decrypt);
        FileUtil.writeFile(FileUtil.CRYPT_FILENAME, crypt);
        FileUtil.writeFile(FileUtil.DECRYPT_FILENAME, decrypt);
    }
}
