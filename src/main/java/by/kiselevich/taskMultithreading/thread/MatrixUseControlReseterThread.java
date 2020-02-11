package by.kiselevich.taskMultithreading.thread;

import by.kiselevich.taskMultithreading.entity.Matrix;

public class MatrixUseControlReseterThread extends Thread {

    @Override
    public void run() {
        Matrix.getInstance().resetUseControl();
    }
}
