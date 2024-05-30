package thread;
public class AnonymousClassExample {
    public static void main(String[] args) {
        // Runnableインターフェースを無名クラスで実装
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Running in an anonymous class");
            }
        };

        // 新しいスレッドで無名クラスの実行
        new Thread(runnable).start();
    }
}
