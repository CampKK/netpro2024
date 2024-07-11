import java.util.Random;

public class CenterKamoku {
    private String name;
    private int score;
    private static final Random rand = new Random();

    public CenterKamoku(String name) {
        this.name = name;
        this.score = rand.nextInt(101); // 0から100の間のランダムな整数を生成
    }

    public void printNameAndScore() {
        System.out.println(name + ":" + score);
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
