import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorNode implements Runnable {
    private static final Logger logger = Logger.getLogger(SensorNode.class.getName());
    int x, y;
    static char[][] forest = Main.forest;
    static Lock lock = Main.lock;
    static Condition[][] conditions = Main.conditions;
    private volatile boolean running = true;

    public SensorNode(int x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public void run() {
        while (running) {
            lock.lock();
            try {
                if (forest[x][y] == '@') {
                    System.out.printf("Sensor em (%d, %d) detectou fogo! Notificando vizinhos...\n", x, y);
                    notifyNeighbors(x, y, "Path: (" + x + ", " + y + ")");
                    forest[x][y] = '/';
                    Main.printForest();
                }
                conditions[x][y].await();
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Thread interrupted", e);
                running = false;  // Atualiza a condição de saída
            } finally {
                lock.unlock();
            }
        }
    }

    private void notifyNeighbors(int x, int y, String path) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isValid(nx, ny)) {
                lock.lock();
                try {
                    if (forest[nx][ny] == 'T') {
                        System.out.printf("Notificando central de controle a partir de (%d, %d)\n", nx, ny);
                        System.out.println(path + " -> (" + nx + ", " + ny + ")");
                        ControlCenter.notifyControlCenter(nx, ny);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < Main.SIZE && y >= 0 && y < Main.SIZE;
    }
}