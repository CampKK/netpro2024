package callback;

// Worker.java
public class Worker {
    public void doWork(Callback callback) {
        System.out.println("Doing some work...");

        // Simulating some work with a thread sleep
        try {
            Thread.sleep(2000); // 2 seconds delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Work is done, now call the callback method
        callback.onComplete("Work is complete!");
    }
}
