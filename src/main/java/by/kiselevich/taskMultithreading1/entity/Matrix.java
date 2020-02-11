package by.kiselevich.taskMultithreading1.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Matrix {

    private int[][] data;
    private Lock[][] locks;
    private boolean[][] isUsed;
    private int size;

    private Matrix() {

    }

    public void initMatrix(int matrixSize) {
        this.size = matrixSize;
        data = new int[size][size];
        locks = new Lock[size][size];
        isUsed = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                locks[i][j] = new ReentrantLock();
                isUsed[i][j] = false;
            }
        }
    }

    public void resetUseControl() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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

    public int getSize() {
        return size;
    }

    public boolean setValue(int i, int j, int value) {
        if (i < size && j < size && locks[i][j].tryLock()) {
            if (!isUsed[i][j]) {
                isUsed[i][j] = true;
                data[i][j] = value;
                locks[i][j].unlock();
                return true;
            }
            locks[i][j].unlock();
        }
        return false;
    }

    public int getValue(int i, int j) {
        if (i < size && j < size) {
            return data[i][j];
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                stringBuilder.append(String.format("%4d", data[i][j]));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
