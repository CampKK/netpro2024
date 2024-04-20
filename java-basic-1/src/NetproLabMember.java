import java.util.Random;

public class NetproLabMember {
    public static final int years = 15;
    public static final int columns = 3;

    public static void main(String[] args) {
        int[][] members = new int[years][columns];
        Random random = new Random();
        double totalRatio = 1;
        for (int i = 0; i < years; i++) {
            //学生の総数
            members[i][0] = 120 + (random.nextInt(20) - 10);
            //女性の割合(%)
            members[i][1] = 20 + i; // 初期値20%から毎年1%上昇
            //岩井研の人数
            members[i][2] = 10 + (random.nextInt(7) - 3); // 定員10人に対して+-3のランダム性

            // 男性数を求める
            int men = members[i][0] - (int) (members[i][0] * ((float) members[i][1] / 100));
            // 総数から岩井研の人数を取り出す組み合わせ
            long cpsRatio = members[i][2] != 0 ? combination(members[i][0], members[i][2]) : 1;
            // 男性の中から岩井研のメンバーを取り出す組み合わせ
            long menRatio = members[i][2] != 0 ? combination(men, members[i][2]) : 1;
            // 岩井研の人数に男性しか入らない割合
            totalRatio *= (double) menRatio / cpsRatio;
        }
        System.out.println("15年間岩井研に女性の学生が来ない確率: " + totalRatio);
    }

    // Combinationを実装
    public static final long combination(final int n, int r) {
        // rが0の場合、組み合わせは1となる
        if (r == 0) {
            return 1;
        }

        long numerator = factorial(n);
        long denominator = factorial(r) * factorial(n - r);
        return numerator / denominator;
    }

    public static final long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}
