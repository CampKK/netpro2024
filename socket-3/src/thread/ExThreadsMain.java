package thread;

public class ExThreadsMain {

    public static void main(String[] args) {
        // 3つのスレッドを作成して異なる計算を同時に実行する
        Runnable task1 = new CalculationTask("Task 1", 5);
        Runnable task2 = new CalculationTask("Task 2", 10);
        Runnable task3 = new CalculationTask("Task 3", 3);

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class CalculationTask implements Runnable {
    private String taskName;
    private int number;

    public CalculationTask(String taskName, int number) {
        this.taskName = taskName;
        this.number = number;
    }

    @Override
    public void run() {
        // 例として、単純な乗算を実行し、その結果を出力する
        for (int i = 1; i <= 10; i++) {
            System.out.println(taskName + ": " + number + " x " + i + " = " + (number * i));
            try {
                Thread.sleep(500); // 処理が速すぎるのを防ぐためにスリープ
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
