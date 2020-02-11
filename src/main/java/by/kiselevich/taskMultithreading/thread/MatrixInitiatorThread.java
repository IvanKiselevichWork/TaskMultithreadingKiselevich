package by.kiselevich.taskMultithreading.thread;

import by.kiselevich.taskMultithreading.entity.Matrix;

public class MatrixInitiatorThread extends Thread {
    private int n;

    public MatrixInitiatorThread(int N) {
        this.n = N;
    }

    @Override
    public void run() {
        Matrix.getInstance().initMatrix(n);
    }
}
