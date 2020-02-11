package by.kiselevich.taskMultithreading.thread;

import by.kiselevich.taskMultithreading.entity.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixWriterThread extends Thread {

    private static final Logger LOG = LogManager.getLogger(MatrixWriterThread.class);

    private File file;

    public MatrixWriterThread(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        Matrix matrix = Matrix.getInstance();

        if (file != null) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                bufferedWriter.write(matrix.toString());
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
