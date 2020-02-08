package by.kiselevich.taskMultithreading.entity;

import by.kiselevich.taskMultithreading.constant.ConstantValues;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Matrix {

    private int[][] data;
    private Lock[][] locks;
    private boolean[][] isUsed;
    private int n;

    private Matrix() {
        n = ConstantValues.N;
        data = new int[n][n];
        locks = new Lock[n][n];
        isUsed = new boolean[n][n];
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                locks[i][j] = new ReentrantLock();
                isUsed[i][j] = false;
            }
        }
    }

    public void resetUseControl() {
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                isUsed[i][j] = false;
            }
        }
    }

    private static class SingletonHolder {
        private static final Matrix INSTANCE = new Matrix();
    }

    public static Matrix getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int getN() {
        return n;
    }

    public boolean setValue(int i, int j, int value) {
        if (i < n && j < n) {
            if (!isUsed[i][j]) {
                isUsed[i][j] = true;
                data[i][j] = value;
                return true;
            }
        }
        return false;
    }

    public int getValue(int i, int j) {
        if (i < n && j < n) {
            return data[i][j];
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                stringBuilder.append(String.format("%4d", data[i][j]));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
