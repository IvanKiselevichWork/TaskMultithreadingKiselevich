package by.kiselevich.taskMultithreading.runner;

import by.kiselevich.taskMultithreading.entity.Matrix;
import by.kiselevich.taskMultithreading.reader.MatrixMetadataReader;
import by.kiselevich.taskMultithreading.thread.MatrixAndThreadSumWriterThread;
import by.kiselevich.taskMultithreading.thread.MatrixChangerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class Runner {
    private final static Logger LOG = LogManager.getLogger(Runner.class);
    private final static String OUTPUT_FILENAME = "output.txt";
    private final static String INPUT_FILENAME = "input.txt";

    private static File outputFile = new File(OUTPUT_FILENAME);
    private static File inputFile = null;
    static {
        try {
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
        } catch (IOException e) {
            LOG.warn(e);
        }

        URL inputFileUrl = Runner.class.getClassLoader().getResource(INPUT_FILENAME);
        if (inputFileUrl != null) {
            inputFile = new File(inputFileUrl.getFile());
        } else {
            LOG.warn("No input file for matrix metadata");
        }
    }

    public static void main(String[] args) {

        MatrixMetadataReader matrixMetadataReader = new MatrixMetadataReader(inputFile);
        int n = matrixMetadataReader.getN();
        int y = matrixMetadataReader.getY();

        Matrix.getInstance().initMatrix(n);

        MatrixChangerThread[] threads = new MatrixChangerThread[n];

        int id = 0;
        for (int i = 0; i < y; i++) {

            CountDownLatch latch = new CountDownLatch(n);
            for (int j = 0; j < n; j++) {
                threads[j] = new MatrixChangerThread(id++, latch);
                threads[j].start();
            }

            for (int j = 0; j < n; j++) {
                try {
                    threads[j].join();
                } catch (InterruptedException e) {
                    LOG.warn(e);
                }
            }

            LOG.trace(Matrix.getInstance().toString());
            Matrix.getInstance().resetUseControl();

            Thread writer = new MatrixAndThreadSumWriterThread(outputFile, threads);
            writer.start();
            try {
                writer.join();
            } catch (InterruptedException e) {
                LOG.warn(e);
            }
        }

    }
}
