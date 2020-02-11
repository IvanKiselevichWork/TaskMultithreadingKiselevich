package by.kiselevich.taskMultithreading1.thread;

import by.kiselevich.taskMultithreading1.entity.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;

public class MatrixChangerThread extends Thread {

    private static final Logger LOG = LogManager.getLogger(MatrixChangerThread.class);

    private int id;
    private int sum;
    private CountDownLatch latch;

    public MatrixChangerThread(int id, CountDownLatch latch) {
        super(String.valueOf(id));
        this.id = id;
        this.latch = latch;
    }

    @Override
    public void run() {
        Matrix matrix = Matrix.getInstance();

        int diagonalIndex;
        diagonalIndex = (int)(Math.random() * matrix.getSize());
        while (!matrix.setValue(diagonalIndex, diagonalIndex, id)) {
            diagonalIndex = (int)(Math.random() * matrix.getSize());
        }


        int index = (int)(Math.random() * matrix.getSize());
        if (isRowChosen()) {
            while (index == diagonalIndex || !matrix.setValue(diagonalIndex, index, id)) {
                LOG.trace("thread " + this.getName() + " trying ");
                index = (int)(Math.random() * matrix.getSize());
            }
            LOG.trace("thread " + this.getName() + " choose " + diagonalIndex + ";" + index);
        } else {
            while (index == diagonalIndex || !matrix.setValue(index, diagonalIndex, id)) {
                LOG.trace("thread " + this.getName() + " trying ");
                index = (int)(Math.random() * matrix.getSize());
            }
            LOG.trace("thread " + this.getName() + " choose " + index + ";" + diagonalIndex);
        }

        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            LOG.warn(e);
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
        for (int i = 0; i < matrix.getSize(); i++) {
            rowSum += matrix.getValue(i, diagonalIndex);
            columnSum += matrix.getValue(diagonalIndex, i);
        }
        return rowSum + columnSum - matrix.getValue(diagonalIndex, diagonalIndex);
    }

    public int getSum() {
        return sum;
    }
}
