package by.kiselevich.taskmultithreading.thread;

import by.kiselevich.taskmultithreading.entity.Matrix;

public class MatrixUseControlReseterThread extends Thread {

    @Override
    public void run() {
        Matrix.getInstance().resetUseControl();
    }
}
