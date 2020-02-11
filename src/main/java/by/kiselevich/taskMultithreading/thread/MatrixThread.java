package by.kiselevich.taskMultithreading.thread;

import by.kiselevich.taskMultithreading.entity.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MatrixThread extends Thread {

    private static final Logger LOG = LogManager.getLogger(MatrixThread.class);

    private int id;
    private int sum;

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


        int index = (int)(Math.random() * matrix.getN());
        if (isRowChosen()) {
            while (index == diagonalIndex || !matrix.setValue(diagonalIndex, index, id)) {
                LOG.trace("thread " + this.getName() + " trying ");
                index = (int)(Math.random() * matrix.getN());
            }
            LOG.trace("thread " + this.getName() + " choose " + diagonalIndex + ";" + index);
        } else {
            while (index == diagonalIndex || !matrix.setValue(index, diagonalIndex, id)) {
                LOG.trace("thread " + this.getName() + " trying ");
                index = (int)(Math.random() * matrix.getN());
            }
            LOG.trace("thread " + this.getName() + " choose " + index + ";" + diagonalIndex);
        }

        sum = calculateSumOfRowAndColumn(diagonalIndex);

        LOG.trace("thread " + getName() + " sum = " + sum);
    }

    private boolean isRowChosen() {
        return Math.random() < 0.5;
    }

    private int calculateSumOfRowAndColumn(int diagonalIndex) {
        int rowSum = 0;
        int columnSum = 0;
        Matrix matrix = Matrix.getInstance();
        for (int i = 0; i < matrix.getN(); i++) {
            rowSum += matrix.getValue(i, diagonalIndex);
            columnSum += matrix.getValue(diagonalIndex, i);
        }
        return rowSum + columnSum - matrix.getValue(diagonalIndex, diagonalIndex);
    }

    public int getSum() {
        return sum;
    }
}
