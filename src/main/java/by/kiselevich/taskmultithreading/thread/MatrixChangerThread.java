package by.kiselevich.taskmultithreading.thread;

import by.kiselevich.taskmultithreading.entity.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
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
        diagonalIndex = new Random().nextInt(matrix.getSize());
        while (!matrix.setValue(diagonalIndex, diagonalIndex, id)) {
            diagonalIndex = new Random().nextInt(matrix.getSize());
        }


        int index = new Random().nextInt(matrix.getSize());
        if (isRowChosen()) {
            while (index == diagonalIndex || !matrix.setValue(diagonalIndex, index, id)) {
                LOG.trace("thread " + this.getName() + " trying ");
                index = new Random().nextInt(matrix.getSize());
            }
            LOG.trace("thread " + this.getName() + " choose " + diagonalIndex + ";" + index);
        } else {
            while (index == diagonalIndex || !matrix.setValue(index, diagonalIndex, id)) {
                LOG.trace("thread " + this.getName() + " trying ");
                index = new Random().nextInt(matrix.getSize());
            }
            LOG.trace("thread " + this.getName() + " choose " + index + ";" + diagonalIndex);
        }

        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            LOG.warn(e);
            Thread.currentThread().interrupt();
        }
        sum = calculateSumOfRowAndColumn(diagonalIndex);

        LOG.trace("thread " + getName() + " sum = " + sum);
    }

    private boolean isRowChosen() {
        return new Random().nextDouble() < 0.5;
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
