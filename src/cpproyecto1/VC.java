package cpproyecto1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class VC {

    private boolean available;

    private final Lock mutex, lock;
    private final Condition condition;

    VC() {
        this.mutex = new ReentrantLock();
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
        this.available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public void await() throws InterruptedException {
        condition.await();
    }

    public void signal() {
        condition.signalAll();
    }
    
    public void lock() {
        mutex.lock();
    }
    
    public boolean trylock() {
        return mutex.tryLock();
    }
    
    public void unlock() {
        mutex.unlock();
    }
    
}
