package thread;

// Runnable インターフェースを実装することで、このクラスのインスタンスはスレッドとして実行可能になります。
public class CountTenRunnable implements Runnable {
    String myAlfabetStr="noalfabet";

    // main メソッドはプログラムのエントリーポイントです。
    public static void main(String[] args){
        // 2つの文字を初期化します。
        char c1 = 97; // ASCII値 97 は 'a' です
        char c2 = (char)(c1 + 1); // c1 に 1 を足すと ASCII値 98 になり、'b' になります
        char c3 = (char)(c2 + 1);
        // 初期化した文字をコンソールに出力します。
        System.out.println(c1); // 出力: a
        System.out.println(c2); // 出力: b
        System.out.println(c3);
        // CountTenRunnable クラスのインスタンスを作成します。
        CountTenRunnable ct1 = new CountTenRunnable();
        CountTenRunnable ct2 = new CountTenRunnable();
        CountTenRunnable ct3 = new CountTenRunnable();
        ct1.setAlfabet(c1+"-thread chan");
        ct2.setAlfabet(c2+"-thread chan");
        ct3.setAlfabet(c3+"-thread chan");

        // ct を実行する新しいスレッドを作成します。
        Thread th1 = new Thread(ct1);
        Thread th2 = new Thread(ct2);
        Thread th3 = new Thread(ct3);

        // スレッドを開始します。これにより、CountTenRunnable の run メソッドが呼び出されます。

        System.out.println("th1.getName()"+th1.getName());
        th1.start();

        System.out.println("th2.getName()"+th2.getName());
        th2.start();

        System.out.println("th2.getName()"+th3.getName());
        th3.start();
        

        // この try-catch ブロックは、0 から 9 までの値を 500 ミリ秒間隔で出力するループを実行します。
        try {
            for(int i = 0; i < 10; i++) {
                System.out.println("main:i=" + i);

                // メインスレッドを 500 ミリ秒間一時停止します。
                Thread.sleep(500);  // ミリ秒単位のスリープ時間
            }
        }
        catch(InterruptedException e) {
            // スレッドが中断された場合は、例外を出力します。
            System.err.println(e);
        }
    }

    public void setAlfabet(String alfabetstr) {
        myAlfabetStr = alfabetstr;
    }

    // run メソッドは、新しいスレッドが実行するコードを含みます。
    public void run() {
        // この try-catch ブロックは、0 から 9 までの値を 1000 ミリ秒間隔で出力するループを実行します。
        try {
            for(int i = 0; i < 10; i++) {
                System.out.println(myAlfabetStr+"runnable thread:i=" + i);

                // スレッドを 1000 ミリ秒間一時停止します。
                Thread.sleep(1000);  // ミリ秒単位のスリープ時間
            }
        }
        catch(InterruptedException e) {
            // スレッドが中断された場合は、例外を出力します。
            System.err.println(e);
        }
    }
}