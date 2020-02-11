package by.kiselevich.taskMultithreading1.thread;

import by.kiselevich.taskMultithreading1.entity.Matrix;

public class MatrixUseControlReseterThread extends Thread {

    @Override
    public void run() {
        Matrix.getInstance().resetUseControl();
    }
}
