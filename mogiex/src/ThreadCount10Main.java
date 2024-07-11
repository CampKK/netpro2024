public class ThreadCount10Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            CountTenRunnableImpleC task = new CountTenRunnableImpleC(i, i);
            Thread thread = new Thread(task);
            thread.start();
        }
    }
}
