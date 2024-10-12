import java.util.concurrent.locks.Lock;


public class ControlCenter implements Runnable {
    static char[][] forest = Main.forest;
    static Lock lock = Main.lock;
    private volatile boolean running = true;


    public static void notifyControlCenter(int x, int y) {
        ControlCenter controlCenter = new ControlCenter();
        controlCenter.combatFire(x, y);
    }

    private void combatFire(int x, int y) {
        lock.lock();
        try {
            if (forest[x][y] == '@') {
                forest[x][y] = '/';
                System.out.printf("Fogo em (%d, %d) foi extinto.\n", x, y);
                Main.printForest();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (running) { // Usa a condição de saída
            lock.lock();
            try {
                for (int i = 0; i < Main.SIZE; i++) {
                    for (int j = 0; j < Main.SIZE; j++) {
                        if (forest[i][j] == '@' && isEdgeNode(i, j)) {
                            System.out.printf("Central de controle notificada sobre fogo em (%d, %d). Tomando ação...\n", i, j);
                            combatFire(i, j);
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private boolean isEdgeNode(int i, int j) {
        return i == 0 || i == Main.SIZE - 1 || j == 0 || j == Main.SIZE - 1;
    }
}