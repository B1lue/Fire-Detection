import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class FireGenerator implements Runnable {

    static char[][] forest = Main.forest;
    static Lock lock = Main.lock;
    static Condition[][] conditions = Main.conditions;
    static Random random = Main.random;

    public FireGenerator() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this, 0, 3, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        int x = random.nextInt(Main.SIZE);
        int y = random.nextInt(Main.SIZE);

        lock.lock();
        try {
            if (forest[x][y] == '-') {
                forest[x][y] = '@';
                conditions[x][y].signal();
                System.out.printf("Fogo gerado em (%d, %d)\n", x, y);
                Main.printForest();
            }
        } finally {
            lock.unlock();
        }
    }
}