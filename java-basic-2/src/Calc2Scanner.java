import java.util.Scanner;

public class Calc2Scanner {

	public static void main(String[] args) {
		int i=0;
		while(i<10) {
			Scanner scan = new Scanner(System.in);

			String str = scan.next();
			System.out.println("最初のトークンは: " + str);
			int num01 = Integer.parseInt(str);
						str = scan.next();
			System.out.println("次のトークンは  : " + str);
			i++;
			int num02 = Integer.parseInt(str);
			System.out.println("足すと"+(num01+num02));
		}

	}

		}


// 課題 キーボードから2つの数字を打ち込む
// その足し算結果を、出力する。