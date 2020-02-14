package by.kiselevich.taskmultithreading.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class MatrixResultPerformerThread extends Thread {

    private static final Logger LOG = LogManager.getLogger(MatrixResultPerformerThread.class);

    private File file;
    private MatrixChangerThread[] threads;

    public MatrixResultPerformerThread(File file, MatrixChangerThread[] threads) {
        this.file = file;
        this.threads = threads;
    }

    @Override
    public void run() {
        Thread reseter = new MatrixReseterThread();
        reseter.start();
        Thread writer = new ResultsWriterThread(file, threads);
        writer.start();

        try {
            reseter.join();
            writer.join();
        } catch (InterruptedException e) {
            LOG.warn(e);
            Thread.currentThread().interrupt();
        }
    }
}
