import java.util.Arrays;
import java.util.Random;

public class HeikinCKadai {
    public static final int N = 100;
    Kamoku[] kamoku = new Kamoku[N];
    static String kamokuname = "数学";

    public static void main(String[] args) {
        HeikinCKadai heikinb = new HeikinCKadai(kamokuname);
        heikinb.initalizeScores();
        heikinb.printAverage();
        heikinb.gokakusha();

    }

    HeikinCKadai(String s) {
        this.kamokuname = s;

    }

    void initalizeScores() {
        Random r = new Random();

        for (int i = 0; i < N; i++) {
            int score = r.nextInt(N + 1);
            kamoku[i] = new Kamoku(score);

        }

    }

    void printAverage() {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            sum += kamoku[i].getScore();

        }
        System.out.println("平均点は" + sum / N);

    }

    void gokakusha() {
        int gokakuCount = 0;
        for (int i = 0; i < N; i++) {
            if (kamoku[i].getScore() >= 80) {
                gokakuCount++;
            }
        }

        int[] gokakuScores = new int[gokakuCount];
        int index = 0;
        for (int i = 0; i < N; i++) {
            if (kamoku[i].getScore() >= 80) {
                gokakuScores[index++] = kamoku[i].getScore();
            }
        }

        Arrays.sort(gokakuScores);

        System.out.println("*以下合格者の点数です。*");
        System.out.println("");

        for (int score : gokakuScores) {
            System.out.println("*" + score + "*");
            System.out.println("");
        }

    }// student idと点数をソートしてだせ。＞＝８０以上

}

class Kamoku {
    String name;
    int score;
    private int studentid;

    Kamoku(int s) { // これがコンストラクタ。特殊なメソッド。クラス名と同じ。
        score = s;
    }

    // setScore というメソッドを定義する。
    // score に値を設定する。
    public void setScore(int num) {
        score = num;
    }

    // getScore というメソッドを定義する。
    // scoreを返す。
    public int getScore() {
        return score;
    }
}

// メソッド 関数のこと
// public 返り値(戻り値return value) 関数名() {
// 中に具体的な処理を書く
// }
