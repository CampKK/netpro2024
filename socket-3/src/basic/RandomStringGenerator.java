package basic;
import java.util.Random;

public class RandomStringGenerator {

    public static void main(String[] args) {
        // ランダムな文字列の長さ
        int length = 10;

        // ランダムな文字列を生成するための文字セット
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // ランダムな文字列を生成するためのStringBuilder
        StringBuilder sb = new StringBuilder();

        // ランダムな文字列を生成
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charset.length());
            char randomChar = charset.charAt(randomIndex);
            sb.append(randomChar);
        }
        String randomString = sb.toString();

        // 生成されたランダムな文字列を表示
        System.out.println("Generated Random String: " + randomString);

        // 生成されたランダムな文字列を処理する
        processString(randomString);
    }

    public static void processString(String str) {
        // 文字列の長さを表示
        System.out.println("Length of the string: " + str.length());

        // 文字列を大文字に変換して表示
        System.out.println("Uppercase string: " + str.toUpperCase());

        // 文字列を小文字に変換して表示
        System.out.println("Lowercase string: " + str.toLowerCase());

        // 文字列を逆順にして表示
        StringBuilder reversed = new StringBuilder(str).reverse();
        System.out.println("Reversed string: " + reversed);
    }
}
