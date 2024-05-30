package thread;
public class MyRunnable implements Runnable {
    public void run() {
        // スレッドが実行する処理を記述
        System.out.println("MyRunnable is running");
    }

    public static void main(String[] args) {
        // Runnable インタフェースを実装したクラスのインスタンスを作成
        MyRunnable myRunnable = new MyRunnable();

        // 新しいスレッドを作成して開始
        Thread thread = new Thread(myRunnable);
        thread.start();
    }
}
