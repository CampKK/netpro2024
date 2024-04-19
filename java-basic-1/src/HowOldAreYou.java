// C言語では、#include に相当する
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HowOldAreYou {

	public static void main(String[] args) { 

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			// BufferedReader というのは、データ読み込みのクラス(型)
			// クラスの変数を作るには、new を使う。

			// readLine() は、入出力エラーの可能性がある。エラー処理がないとコンパイルできない。
			//  Java では、 try{ XXXXXXXX }  catch(エラーの型 変数) { XXXXXXXXXXXXXXXXXX} と書く
            while(true)
		try {
			System.out.println("何歳ですか?(終了するためには'q'または'e'を入力してください)");
			String line = reader.readLine();
            if(line.equals("q")||line.equals("e")){
                System.out.println("終了します");
                break;
            }
			int age = Integer.parseInt(line);
            if(age>=120||age<0){
                System.out.println("もう一度入力をしてください");
                continue;
            }
			System.out.println("あなたは" + age + "歳ですね。");
			System.out.println("2030年、" + (age + 2030-2024) + "歳ですね。");
            if(age<=5){
                System.out.println("令和"+(6-age)+"年生まれ");
            }else if(6<=age&&age<=35){
                System.out.println("平成"+(36-age)+"年生まれ");
            }else if(36<=age&&age<=98){
                System.out.println("昭和"+(99-age)+"年生まれ");
            }else if(99<=age&&age<=112){
                System.out.println("大正"+(113-age)+"年生まれ");
            }else if(113<=age&&age<=156){
                System.out.println("明治"+(157-age)+"年生まれ");
            }
		}
		catch(IOException e) {
			System.out.println(e);
		}


	}
}

//  課題    キーボードから数字を打ち込む
//  その結果、 あなたは、???歳ですね、と画面に表示させる。
//  その後、あなたは10年後、????歳ですね、と画面に表示させる。

