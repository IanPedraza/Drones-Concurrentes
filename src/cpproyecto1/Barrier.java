package cpproyecto1;

public class Barrier {

    private final int maxNumThreads;
    private int numCalls = 0;

    public Barrier(int numThreads) {
        this.maxNumThreads = numThreads;
    }

    public synchronized void call() {
        numCalls++;
        
        if (numCalls == maxNumThreads) {
            this.notifyAll();
            numCalls = 0;
        } else {
            try {
                this.wait();
            } catch (InterruptedException ignore) {
            }
        }
    }
}
