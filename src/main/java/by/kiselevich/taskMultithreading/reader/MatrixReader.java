package by.kiselevich.taskMultithreading.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MatrixReader {

    private static final Logger LOG = LogManager.getLogger(MatrixReader.class);
    private static final int N_INDEX = 0;
    private static final int Y_INDEX = 1;

    private String path;
    private int n;
    private int y;

    public MatrixReader(String path) {
        this.path = path;
        readTwoNumbers();
    }

    public void setPath(String path) {
        readTwoNumbers();
    }

    public int getN() {
        return n;
    }

    public int getY() {
        return y;
    }

    private void readTwoNumbers() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            if (line != null) {
                String[] numbersStringArray = line.split(";");
                if (numbersStringArray.length >= 2) {
                    n = Integer.parseInt(numbersStringArray[N_INDEX]);
                    y = Integer.parseInt(numbersStringArray[Y_INDEX]);
                    return;
                }
            }
            LOG.warn("Cant read numbers, string = <" + line + ">");
        } catch (IOException e) {
            LOG.warn(e);
        }
    }
}
