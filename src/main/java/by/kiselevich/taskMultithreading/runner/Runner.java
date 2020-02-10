package by.kiselevich.taskMultithreading.runner;

import by.kiselevich.taskMultithreading.constant.ConstantValues;
import by.kiselevich.taskMultithreading.entity.Matrix;
import by.kiselevich.taskMultithreading.thread.MatrixThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Runner {
    private final static Logger LOG = LogManager.getLogger(Runner.class);

    public static void main(String[] args) {
        Thread[] threads = new Thread[ConstantValues.N];
        int id = 0;
        for (int i = 0; i < ConstantValues.Y; i++) {
            for (int j = 0; j < ConstantValues.N; j++) {
                threads[j] = new MatrixThread(id++);
            }
            for (int j = 0; j < ConstantValues.N; j++) {
                threads[j].start();
            }
            for (int j = 0; j < ConstantValues.N; j++) {
                try {
                    threads[j].join();
                } catch (InterruptedException e) {
                    LOG.warn(e);
                }
            }
            LOG.trace(Matrix.getInstance().toString());
            Matrix.getInstance().resetUseControl();
        }

    }
}
