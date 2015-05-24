package com.lab3;


public class Blowfish
{
    private BlowfishValues values;

    public Blowfish () {
        values = new BlowfishValues();
    }

    public static void main(String[] args) {
        Blowfish myBlowfish = new Blowfish();
        String encode = myBlowfish.encrypt("hello", "hell");
        String decode = myBlowfish.decrypt(encode, "hell");
        System.out.println(encode);
        System.out.println(decode);
    }

    public String encrypt(String text, String key) {
        values.setValues();
        generateKeys(key.getBytes());
        byte[] msg = adjunction(text.getBytes(), ' ');
        encrypt(msg);
        return new String(msg);
    }

    public String decrypt(String text, String key) {
        values.setValues();
        generateKeys(key.getBytes());
        byte[] msg = text.getBytes();
        decrypt(msg);
        return new String(msg);
    }

    private void encrypt(byte[] data) {

        int blocks = data.length >> 3;

        for (int iter1 = 0; iter1 < blocks; iter1++) {

            int p;

            p = iter1 << 3;

            long leftSubBlock = bytesTo32bits(data, p);
            long rightSubBlock = bytesTo32bits(data, p + 4);
            long temp;

            for (int i = 0; i < 16; i++) {
                leftSubBlock = leftSubBlock ^ values.currentKeys[i];
                rightSubBlock = magicFunction(leftSubBlock) ^ rightSubBlock;
                temp = leftSubBlock;
                leftSubBlock = rightSubBlock;
                rightSubBlock = temp;
            }

            temp = leftSubBlock;
            leftSubBlock = rightSubBlock;
            rightSubBlock = temp;
            rightSubBlock ^= values.currentKeys[16];
            leftSubBlock ^= values.currentKeys[17];
            bits32ToBytes(leftSubBlock, data, p);
            bits32ToBytes(rightSubBlock, data, p + 4);
        }
    }

    private void decrypt(byte[] data) {

        int blocks = data.length >> 3;

        for (int iter1 = 0; iter1 < blocks; iter1++) {

            int p;

            p = iter1 << 3;

            long leftSubBlock = bytesTo32bits(data, p);
            long rightSubBlock = bytesTo32bits(data, p + 4);
            long temp;

            for (int i = 17; i > 1; i--) {
                leftSubBlock = leftSubBlock ^ values.currentKeys[i];
                rightSubBlock = magicFunction(leftSubBlock) ^ rightSubBlock;
                temp = leftSubBlock;
                leftSubBlock = rightSubBlock;
                rightSubBlock = temp;
            }

            temp = leftSubBlock;
            leftSubBlock = rightSubBlock;
            rightSubBlock = temp;
            rightSubBlock ^= values.currentKeys[1];
            leftSubBlock ^= values.currentKeys[0];
            bits32ToBytes(leftSubBlock, data, p);
            bits32ToBytes(rightSubBlock, data, p + 4);
        }
    }

    private long magicFunction(long x) {
        return (((values.currentTables[((int) x >>> 24)] + values.currentTables[256 | ((int) x >>> 16) & 0xff]) ^
                values.currentTables[512 | ((int) x >>> 8) & 0xff]) + values.currentTables[768 |(int) x & 0xff]);
    }

    private void generateKeys(byte[] key) {
        int data;
        int j = 0, i;
        for (i = 0; i < 18; ++i) {
            data = 0x00000000;
            for (int k = 0; k < 4; ++k) {
                data = (data << 8) | (key[j] & 0xFF);
                j++;
                if (j >= key.length) {
                    j = 0;
                }
            }
            values.currentKeys[i] ^= data;
        }
        byte[] b = new byte[8];
        for (i = 0; i < 18; i += 2) {
            encrypt(b);
            values.currentKeys[i] = bytesTo32bits(b, 0);
            values.currentKeys[i + 1] = bytesTo32bits(b, 4);
        }
        for (i = 0; i < 4; ++i) {
            for (j = 0; j < 256; j += 2) {
                encrypt(b);
                values.currentTables[i << 8 | j] = bytesTo32bits(b, 0);
                values.currentTables[i << 8 | (j + 1)] = bytesTo32bits(b, 4);
            }
        }
    }

    private int bytesTo32bits (byte[] byteArr, int num) {

        return ((byteArr[num] & 0xff) << 24) | ((byteArr[num + 1] & 0xff) << 16) | ((byteArr[num + 2] & 0xff) << 8) | ((byteArr[num + 3] & 0xff));
    }

    private void bits32ToBytes (long a, byte[] byteArr, int p) {
        byteArr[p + 3] = (byte) a;
        byteArr[p + 2] = (byte) (a >> 8);
        byteArr[p + 1] = (byte) (a >> 16);
        byteArr[p] = (byte) (a >> 24);
    }

    private byte[] adjunction (byte[] a, int p) {
        int l = (a.length | 7) + 1;
        byte[] b = new byte[l];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) p;
        }
        System.arraycopy(a, 0, b, 0, a.length);
        return b;
    }
}
