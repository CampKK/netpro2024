import java.io.Serializable;

public class TaskObject implements Serializable , ITask{
    int x;
    int result;



    public void setExecNumber(int x){
        this.x = x;
    } // クライアントで最初に計算させる数字を入力しておく関数

    public void exec(){
        this.result = -1;

        if (x < 2) {
            return;
        }

        for (int i = x; i >= 2; i--) {
            boolean isPrime = true;
            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                result = i;
                break;
            }
        }
    } // サーバで計算を実行をさせる関数...計算アルゴリズムが記載される。下記アルゴリズムを参照のこと

    public int getResult(){
        return result;
    } // クライアントで結果を取り出す関数

}