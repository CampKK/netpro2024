package thread;

public class CountAZTenRunnable implements Runnable {
    String myAlfabetStr = "noalfabet";
    // main メソッドはプログラムのエントリーポイントです。
    public static void main(String[] args){

        CountAZTenRunnable[] cts = new CountAZTenRunnable[26];
        // 2つの文字を初期化します。
        char c1 = 97; // ASCII値 97 は 'a' です
        char c2 = (char)(c1 + 1); // c1 に 1 を足すと ASCII値 98 になり、'b' になります
        char c3 = (char)(c1 + 2); // c1 に 2 を足すと ASCII値 99 になり、'c' になります
        /*for(int i = 97; i < 97 + 24; i++){
            
        }*/

        // 初期化した文字をコンソールに出力します。
        System.out.println(c1); // 出力: a
        System.out.println(c2); // 出力: b
        System.out.println(c3); // 出力: c

        // CountAZTenRunnable クラスのインスタンスを作成します。
        CountAZTenRunnable ct1 = new CountAZTenRunnable();
        CountAZTenRunnable ct2 = new CountAZTenRunnable();
        CountAZTenRunnable ct3 = new CountAZTenRunnable();
        ct1.setAlfabet(c1 + "-thread chan");
        ct2.setAlfabet(c2 + "-thread chan");
        ct3.setAlfabet(c3 + "-thread chan");
        for(int i = 0; i < 26; i++){
            cts[i] = new CountAZTenRunnable();
            String name_with_alfabet = (char)(97 + i) + "-chan thread";
            cts[i].setAlfabet(name_with_alfabet);
        }

        for(CountAZTenRunnable ct:cts){
            new Thread(ct).start();
        }

        // ct を実行する新しいスレッドを作成します。
        //Thread th1 = new Thread(ct1);
        //Thread th2 = new Thread(ct2);
        //Thread th3 = new Thread(ct3);
        // スレッドを開始します。これにより、CountAZTenRunnable の run メソッドが呼び出されます。
        //System.out.println("th1.getName();" + th1.getName());
        //th1.start();
        //.out.println("th2.getName();" + th2.getName());
        //th2.start();
        //System.out.println("th3.getName();" + th3.getName());
        //th3.start();

        // この try-catch ブロックは、0 から 9 までの値を 500 ミリ秒間隔で出力するループを実行します。
        try {
            for(int i = 0; i <= 10; i++) {
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
            for(int i = 0; i <= 10; i++) {
                System.out.println(myAlfabetStr + "runnable thread:i=" + i);

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