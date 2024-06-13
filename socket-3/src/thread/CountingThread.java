package thread;
public class CountingThread extends Thread {
    private int count = 0;

    @Override
    public void run() {
        // スレッドが実行中にカウントを増加させる
        for (int i = 0; i < 5; i++) {
            try {
                // 1秒間待機
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            customMethod("outer main");
        }
    }

    // countを増加させるカスタムメソッド
    public synchronized void customMethod(String s) {
        count++;
        System.out.println("Count increased to: " + count + "from" + s);
    }

    public static void main(String[] args) {
        CountingThread thread = new CountingThread();

        // スレッドを開始
        thread.start();

        // メインスレッドでもcustomMethodを呼び出す
        for (int i = 0; i < 5; i++) {
            try {
                // 1秒間待機
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            thread.customMethod("outer main");
        }
    }
}
