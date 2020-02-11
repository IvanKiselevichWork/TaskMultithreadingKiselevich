package by.kiselevich.taskMultithreading.runner;

import by.kiselevich.taskMultithreading.constant.ConstantValues;
import by.kiselevich.taskMultithreading.entity.Matrix;
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
    private final static String OUTPUT_FILE = "output.txt";
    private final static String INPUT_FILE = "input.txt";

    private static File outputFile = new File(OUTPUT_FILE);
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

        URL inputFileUrl = Runner.class.getClassLoader().getResource(INPUT_FILE);
        if (inputFileUrl != null) {
            inputFile = new File(inputFileUrl.getFile());
        } else {
            LOG.warn("No input file for matrix metadata");
        }
    }

    public static void main(String[] args) {
        MatrixChangerThread[] threads = new MatrixChangerThread[ConstantValues.N];
        int id = 0;
        for (int i = 0; i < ConstantValues.Y; i++) {

            CountDownLatch latch = new CountDownLatch(ConstantValues.N);
            for (int j = 0; j < ConstantValues.N; j++) {
                threads[j] = new MatrixChangerThread(id++, latch);
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
