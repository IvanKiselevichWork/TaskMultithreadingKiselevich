package by.kiselevich.taskmultithreading.thread;

import by.kiselevich.taskmultithreading.entity.Matrix;
import by.kiselevich.taskmultithreading.exception.IndexOutOfMaxtrixBoundsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MatrixChangerThread extends Thread {

    private static final Logger LOG = LogManager.getLogger(MatrixChangerThread.class);

    private int id;
    private int sum;
    private CountDownLatch latchBeforeCalculateSum;
    private CyclicBarrier barrierBeforeWriteMatrixAndSum;
    private Matrix matrix;

    public MatrixChangerThread(int id, CountDownLatch latchBeforeCalculateSum, CyclicBarrier barrierBeforeWriteMatrixAndSum) {
        super(String.valueOf(id));
        this.id = id;
        this.latchBeforeCalculateSum = latchBeforeCalculateSum;
        this.barrierBeforeWriteMatrixAndSum = barrierBeforeWriteMatrixAndSum;
        matrix = Matrix.getInstance();
    }

    @Override
    public void run() {

        try {
            int diagonalIndex = setMatrixDiagonalValue();

            if (isRowChosen()) {
                setMatrixRowValue(diagonalIndex);
            } else {
                setMatrixColumnValue(diagonalIndex);
            }

            latchBeforeCalculateSum.countDown();
            latchBeforeCalculateSum.await();

            sum = calculateSumOfRowAndColumn(diagonalIndex);

            barrierBeforeWriteMatrixAndSum.await();

        } catch (IndexOutOfMaxtrixBoundsException e) {
            LOG.error(e);
        } catch (BrokenBarrierException e) {
            LOG.warn(e);
        } catch (InterruptedException e) {
            LOG.warn(e);
            Thread.currentThread().interrupt();
        }
    }

    private void setMatrixRowValue(int diagonalIndex) throws IndexOutOfMaxtrixBoundsException {
        int index = getRandomIndex(matrix.getSize());
        while (index == diagonalIndex || !matrix.trySetValue(diagonalIndex, index, id)) {
            index = getRandomIndex(matrix.getSize());
        }
    }

    private void setMatrixColumnValue(int diagonalIndex) throws IndexOutOfMaxtrixBoundsException {
        int index = getRandomIndex(matrix.getSize());
        while (index == diagonalIndex || !matrix.trySetValue(index, diagonalIndex, id)) {
            index = getRandomIndex(matrix.getSize());
        }
    }

    private boolean isRowChosen() {
        return new Random().nextBoolean();
    }

    private int setMatrixDiagonalValue() throws IndexOutOfMaxtrixBoundsException {
        int diagonalIndex = getRandomIndex(matrix.getSize());
        while (!matrix.trySetValue(diagonalIndex, diagonalIndex, id)) {
            diagonalIndex = getRandomIndex(matrix.getSize());
        }
        return diagonalIndex;
    }

    private int getRandomIndex(int maxValue) {
        return new Random().nextInt(maxValue);
    }

    private int calculateSumOfRowAndColumn(int diagonalIndex) {
        int rowSum = 0;
        int columnSum = 0;
        for (int i = 0; i < matrix.getSize(); i++) {
            try {
                rowSum += matrix.getValue(i, diagonalIndex);
                columnSum += matrix.getValue(diagonalIndex, i);
            } catch (IndexOutOfMaxtrixBoundsException e) {
                LOG.warn(e);
            }
        }
        return rowSum + columnSum - id;
    }

    public int getSum() {
        return sum;
    }
}
