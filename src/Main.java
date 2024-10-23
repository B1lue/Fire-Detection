import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    static final int SIZE = 30;
    static char[][] forest = new char[SIZE][SIZE];
    static Lock lock = new ReentrantLock();
    static Condition[][] conditions = new Condition[SIZE][SIZE];
    static Random random = new Random();

    public static void main(String[] args) {
        initializeForest();


        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                new Thread(new SensorNode(i, j)).start();
            }
        }


        new Thread(new FireGenerator()).start();


        new Thread(new ControlCenter()).start();
    }

    private static void initializeForest() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                forest[i][j] = '-';
                conditions[i][j] = lock.newCondition();
            }
        }

        int sensorX = random.nextInt(SIZE);
        int sensorY = random.nextInt(SIZE);
        forest[sensorX][sensorY] = 'T';
    }


    public static void printForest() {
        lock.lock();
        try {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    System.out.print(forest[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        } finally {
            lock.unlock();
        }
    }
}
