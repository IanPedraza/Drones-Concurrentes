package cpproyecto1;

import static cpproyecto1.SynchronizeOptions.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    
    private int x;
    private int y;

    private final Semaphore semaphore;
    private final Lock mutex;

    private final ArrayList<Double> datosX;
    private final ArrayList<Double> datosY;
    
    public Drone(Map map, Graphic graphic, DroneNumber droneNumber, Lock mutex, Semaphore semaphore, int x, int y, int minWidth, int minHeight, int maxWidth, int maxHeight, ArrayList<Double> datosX, ArrayList<Double> datosY) {
        this.alive = false;
        
        this.map = map;
        this.graphic = graphic;
        
        //this.semaphore = new Semaphore(1);
        //this.mutex = new ReentrantLock();
        
        this.semaphore = semaphore;
        this.mutex = mutex;
        
        this.datosX = datosX;
        this.datosY = datosY;
        
        this.droneNumber = droneNumber;
        this.x = x;
        this.y = y;

        this.minWidth = minWidth;
        this.minHeight = minHeight;

        this.maxWidth = maxWidth - padding;
        this.maxHeight = maxHeight - padding;
        
        //this.index = 0;
    }

    @Override
    public synchronized void run() {
        alive = true;
        
        while (alive) {
            if (map.isPause()) {
                try { Thread.sleep((int) (Math.random() * map.getSpeed())); }
                catch (Exception e) {}
            } else {
                performAction();
            }
        }

    }
    
    private void performAction() {
        switch(map.getSynchronizeOption()) {
            case NONE:
                none();
                break;
            
            case MUTEX: 
                mutex();
                break;
                
            case SEMAPHORE: 
                semaphore();
                break;
                
            case VC: 
                break;
                
            case MONITORS: 
                break;
                
            case GATES: 
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
        
        if (map.isGraphicEnable()) graphic();
        
        map.repaint();
        
        try { Thread.sleep((int) (Math.random() * map.getSpeed())); }
        catch (Exception e) {}
    }
    
    
    private void graphic() {
        //datosX.add((double) x);
        //datosY.add((double) y);
        graphic.addToGraphic(droneNumber.getNumber());
        //graphic.actualizar(datosX, datosY);
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
