package by.kiselevich.taskmultithreading.runner;

import by.kiselevich.taskmultithreading.entity.Matrix;
import by.kiselevich.taskmultithreading.reader.MatrixMetadataReader;
import by.kiselevich.taskmultithreading.thread.MatrixAndThreadSumWriterThread;
import by.kiselevich.taskmultithreading.thread.MatrixChangerThread;
import by.kiselevich.taskmultithreading.thread.MatrixInitiatorThread;
import by.kiselevich.taskmultithreading.thread.MatrixUseControlReseterThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

public class Runner {
    private static final Logger LOG = LogManager.getLogger(Runner.class);
    private static final String OUTPUT_FILENAME = "output.txt";
    private static final String INPUT_FILENAME = "input.txt";

    private static File outputFile = new File(OUTPUT_FILENAME);
    private static File inputFile = null;
    static {
        try {
            Files.write(Paths.get(outputFile.toURI()), "".getBytes(), StandardOpenOption.CREATE_NEW);
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

        MatrixInitiatorThread matrixInitiator = new MatrixInitiatorThread(n);
        matrixInitiator.start();
        try {
            matrixInitiator.join();
        } catch (InterruptedException e) {
            LOG.warn(e);
            Thread.currentThread().interrupt();
        }

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
                    Thread.currentThread().interrupt();
                }
            }

            LOG.trace(Matrix.getInstance().toString());
            MatrixUseControlReseterThread matrixReseter = new MatrixUseControlReseterThread();
            matrixReseter.start();
            try {
                matrixReseter.join();
            } catch (InterruptedException e) {
                LOG.warn(e);
                Thread.currentThread().interrupt();
            }

            Thread writer = new MatrixAndThreadSumWriterThread(outputFile, threads);
            writer.start();
            try {
                writer.join();
            } catch (InterruptedException e) {
                LOG.warn(e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
