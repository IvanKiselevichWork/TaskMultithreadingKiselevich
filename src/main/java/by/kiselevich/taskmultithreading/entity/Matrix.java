package by.kiselevich.taskmultithreading.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Matrix {

    private static class Element {
        private int value;
        private Lock lock;
        private boolean isUsed;

        private Element() {
            value = 0;
            lock = new ReentrantLock();
            isUsed = false;
        }
    }

    private Element[][] cells;
    private int size;

    private Matrix() {

    }

    public void initMatrix(int matrixSize) {
        this.size = matrixSize;
        cells = new Element[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Element();
            }
        }
    }

    public void resetUseControl() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].isUsed = false;
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

    public boolean trySetValue(int i, int j, int value) {
        if (i < size && j < size && cells[i][j].lock.tryLock()) {
            if (!cells[i][j].isUsed) {
                cells[i][j].isUsed = true;
                cells[i][j].value = value;
                cells[i][j].lock.unlock();
                return true;
            }
            cells[i][j].lock.unlock();
        }
        return false;
    }

    public int getValue(int i, int j) {
        if (i < size && j < size) {
            return cells[i][j].value;
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
                stringBuilder.append(String.format("%4d", cells[i][j].value));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
