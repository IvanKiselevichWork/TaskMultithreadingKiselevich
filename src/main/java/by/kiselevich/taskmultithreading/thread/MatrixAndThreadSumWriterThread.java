package by.kiselevich.taskmultithreading.thread;

import by.kiselevich.taskmultithreading.entity.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixAndThreadSumWriterThread extends Thread {

    private static final Logger LOG = LogManager.getLogger(MatrixAndThreadSumWriterThread.class);

    private File file;
    private MatrixChangerThread[] threads;

    public MatrixAndThreadSumWriterThread(File file, MatrixChangerThread[] threads) {
        this.file = file;
        this.threads = threads;
    }

    @Override
    public void run() {
        Matrix matrix = Matrix.getInstance();

        if (file != null) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
                bufferedWriter.write(matrix.toString());
                for(MatrixChangerThread thread : threads) {
                    bufferedWriter.write("thread " + thread.getName()
                            + " sum = " + thread.getSum()
                            + System.lineSeparator());
                }

            } catch (IOException e) {
                LOG.warn(e);
            }
        } else {
            LOG.warn("No file to write matrix");
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
