package by.kiselevich.taskmultithreading.thread;

import by.kiselevich.taskmultithreading.entity.Matrix;

public class MatrixInitiatorThread extends Thread {
    private int matrixSize;

    public MatrixInitiatorThread(int matrixSize) {
        this.matrixSize = matrixSize;
    }

    @Override
    public void run() {
        Matrix.getInstance().initMatrix(matrixSize);
    }
}
