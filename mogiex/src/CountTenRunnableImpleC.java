public class CountTenRunnableImpleC implements Runnable {
    private final int interval;
    private final int threadId;

    public CountTenRunnableImpleC(int threadId, int interval) {
        this.threadId = threadId;
        this.interval = interval;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Thread ID: " + threadId + ", Counter: " + i);
                Thread.sleep(interval * 1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread ID: " + threadId + " was interrupted.");
        }
    }
}
