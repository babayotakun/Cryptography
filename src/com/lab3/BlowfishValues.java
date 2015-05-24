package com.lab3;

import java.util.Random;

public class BlowfishValues {

    public long [] currentKeys = new long [18];

    public long [] currentTables = new long [1024];

    private static final long [] initKeys = new long[18];

    private static final long[] initTables = new long[1024];

    static {
        Random random = new Random();
        for (int i = 0; i < 18; i++) {
            initKeys[i] = random.nextLong();
        }
        for (int i = 0; i < 1024; i++) {
            initTables[i] = random.nextLong();
        }
    }

    public void setValues () {
        System.arraycopy(initKeys, 0, currentKeys, 0, 18);
        System.arraycopy(initTables, 0, currentTables, 0, 1024);
    }
}