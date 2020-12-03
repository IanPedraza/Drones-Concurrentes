package cpproyecto1;

import static cpproyecto1.SynchronizeOptions.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CyclicBarrier;
import javax.swing.JOptionPane;

public class Drone extends Thread {

    private boolean alive;

    private int pasoX = 6;
    private int pasoY = 6;

    private final double speed = 1.0;
    private final int padding = 20;
    //int index;

    private int maxWidth;
    private int maxHeight;

    private int minWidth;
    private int minHeight;

    private final Map map;
    private final DroneNumber droneNumber;
    private final Graphic graphic;
    private final Settings settings;

    private int x;
    private int y;

    private final Semaphore semaphore;
    private final Lock mutex;
    private final VC vc;
    private final Barrier barrier;

    public Drone(Settings settings, Map map, Graphic graphic, DroneNumber droneNumber, Lock mutex, Semaphore semaphore, VC vc, Barrier barrier) {
        this.alive = false;

        this.map = map;
        this.graphic = graphic;
        this.settings = settings;

        /*
         this.semaphore = new Semaphore(1);
         this.mutex = new ReentrantLock();
         this.vc = new VC();
         this.barrier = new Barrier(1);
         */
        this.semaphore = semaphore;
        this.mutex = mutex;
        this.vc = vc;
        this.barrier = barrier;

        this.droneNumber = droneNumber;
        this.x = 0;
        this.y = 0;

        this.minWidth = 0;
        this.minHeight = 0;

        this.maxWidth = 0 - padding;
        this.maxHeight = 0 - padding;

        //this.index = 0;
    }

    @Override
    public void run() {
        alive = true;

        while (alive) {
            if (settings.isPause()) {
                try {
                    Thread.sleep((int) (Math.random() * settings.getSpeed()));
                } catch (Exception e) {
                }
            } else {
                performAction();
            }
        }

    }

    private void performAction() {
        switch (settings.getSynchronizeOption()) {
            case NONE:
                none();
                break;

            case MUTEX:
                mutex();
                break;

            case SEMAPHORE:
                semaphore();
                break;

            case CONDITIONS:
                condition();
                break;

            case MONITORS:
                monitors();
                break;

            case BARRIER:
                barrier();
                break;

        }
    }

    private void move() {
        if (x < minWidth || x >= maxWidth) {
            pasoX *= -1;
        }

        if (y < minHeight || y >= maxHeight) {
            pasoY *= -1;
        }

        x += pasoX;
        y += pasoY;

        if (settings.isGraphicEnable()) {
            try {
                graphic();
            } catch (Exception e) {
                settings.setPause(true);
                JOptionPane.showMessageDialog(null, "Dos o más hilos entrarón en sección crítica");
            }
        }

        map.repaint();

        try {
            Thread.sleep((int) (200 + Math.random() * settings.getSpeed()));
        } catch (Exception e) {
        }
    }

    private void graphic() {
        graphic.addToGraphic(droneNumber.getNumber());
    }

    private void none() {
        move();
    }

    private void semaphore() {
        if (semaphore.tryAcquire()) {
            move();
            semaphore.release();
        }
    }

    private void mutex() {
        if (mutex.tryLock()) {
            try {
                move();
            } finally {
                mutex.unlock();
            }
        }
    }

    private void condition() {
        if (vc.trylock()) {
            try {
                while (!vc.isAvailable()) {
                    try {
                        vc.await();
                    } catch (Exception ignore) {
                    }
                }

                vc.setAvailable(false);
                move();
                vc.signal();
            } catch (Exception ignore) {

            } finally {
                vc.setAvailable(true);
                vc.unlock();
            }
        }
    }

    private void monitors() {
        synchronized(this){
            move();
        }
    }

    private void barrier() {
        barrier.call();
        move();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDroneNumber() {
        return droneNumber.getNumber();
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth - padding;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight - padding;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void kill() {
        alive = false;
        this.interrupt();
    }

}
