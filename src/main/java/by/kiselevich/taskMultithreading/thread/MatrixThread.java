package by.kiselevich.taskMultithreading.thread;

import by.kiselevich.taskMultithreading.entity.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MatrixThread extends Thread {

    private static final Logger LOG = LogManager.getLogger(MatrixThread.class);

    private int id;
    private int rowSum;
    private int columnSum;

    public MatrixThread(int id) {
        super(String.valueOf(id));
        this.id = id;
    }

    @Override
    public void run() {
        Matrix matrix = Matrix.getInstance();

        int diagonalIndex;
        diagonalIndex = (int)(Math.random() * matrix.getN());
        while (!matrix.setValue(diagonalIndex, diagonalIndex, id)) {
            diagonalIndex = (int)(Math.random() * matrix.getN());
        }
        //LOG.trace("thread " + this.getName() + " choose diag " + diagonalIndex);


        int index = (int)(Math.random() * matrix.getN());
        if (isRowChosen()) {
            while (index == diagonalIndex || !matrix.setValue(diagonalIndex, index, id)) {
                index = (int)(Math.random() * matrix.getN());
            }
            LOG.trace("thread " + this.getName() + " choose " + diagonalIndex + ";" + index);
        } else {
            while (index == diagonalIndex || !matrix.setValue(index, diagonalIndex, id)) {
                index = (int)(Math.random() * matrix.getN());
            }
            LOG.trace("thread " + this.getName() + " choose " + index + ";" + diagonalIndex);
        }

        for (int i = 0; i < matrix.getN(); i++) {
            rowSum += matrix.getValue(i, diagonalIndex);
            columnSum += matrix.getValue(diagonalIndex, i);
        }

        LOG.trace("thread " + getName() + " sum = " + (rowSum + columnSum));
    }

    private boolean isRowChosen() {
        return Math.random() < 0.5;
    }
}
