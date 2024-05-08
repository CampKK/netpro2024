import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Calc2ScannerFile {

    public static void main(String args[]) {
        try {
            File file = new File("targetfile.txt");
            Scanner scan = new Scanner(file);
            // scan.useDelimiter("¥¥r¥¥n");

            int line = 1;
            int sum = 0;

            while (scan.hasNext()) {
                String str = scan.next();
                System.out.println(line + ":" + str);
                int num = Integer.parseInt(str.trim());
                sum += num;
                line++;
            }

            System.out.println(sum);

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
}

// 課題 ファイルから読み込むキーボードから2つの数字を打ち込む
// その足し算結果を、出力する。