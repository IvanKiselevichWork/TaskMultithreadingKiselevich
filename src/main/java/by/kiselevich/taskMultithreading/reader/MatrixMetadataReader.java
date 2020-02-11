package by.kiselevich.taskMultithreading.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MatrixMetadataReader {

    private static final Logger LOG = LogManager.getLogger(MatrixMetadataReader.class);
    private static final int N_INDEX = 0;
    private static final int Y_INDEX = 1;
    private static final int DATA_ARRAY_LENGTH = 2;

    private File file;
    private int n;
    private int y;

    public MatrixMetadataReader(File file) {
        this.file = file;
        readTwoNumbers();
    }

    public void setPath(File file) {
        this.file = file;
        readTwoNumbers();
    }

    public int getN() {
        return n;
    }

    public int getY() {
        return y;
    }

    private void readTwoNumbers() {
        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                if (line != null) {
                    String[] numbersStringArray = line.split(";");
                    if (numbersStringArray.length >= DATA_ARRAY_LENGTH) {
                        n = Integer.parseInt(numbersStringArray[N_INDEX]);
                        y = Integer.parseInt(numbersStringArray[Y_INDEX]);
                    } else {
                        LOG.warn("Cant read matrix metadata, string = <" + line + ">");
                    }
                } else {
                    LOG.warn("Matrix metadata is empty");
                }
            } catch (IOException e) {
                LOG.warn(e);
            }
        } else {
            LOG.warn("No file to read matrix metadata");
        }
    }
}
