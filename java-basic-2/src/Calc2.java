import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Calc2 {

	public static void main(String[] args) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			// BufferedReader というのは、データ読み込みのクラス(型)
			// クラスの変数を作るには、new を使う。

			// readLine() は、入出力エラーの可能性がある。エラー処理がないとコンパイルできない。
			//  Java では、 try{ XXXXXXXX }  catch(エラーの型 変数) { XXXXXXXXXXXXXXXXXX} と書く
		try {
			System.out.print("一つ目の数字は?:");
			String line = reader.readLine();
			int first = Integer.parseInt(line);
			System.out.print("二つ目の数字は?:");
			line = reader.readLine();
			int second = Integer.parseInt(line);
			System.out.println(first + second);
		}
		catch(IOException e) {
			System.out.println(e);
		}


	}
}

//  課題    キーボードから2つの数字を打ち込む
//     その足し算結果を、出力する。
//  できたら、第02回の提出フォルダへ Calc2.java を置く。
//  解答は次回、出す。自力でコンパイルエラーを取る練習をしてください。