import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class MyExceptionHoliday {

	public static void main(String[] args) {

		new MyExceptionHoliday();

	}

	MyExceptionHoliday() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// BufferedReader というのは、データ読み込みのクラス(型)
		// クラスの変数を作るには、new を使う。
		while (true) {
			// readLine() は、入出力エラーの可能性がある。エラー処理がないとコンパイルできない。
			// Java では、 try{ XXXXXXXX } catch(エラーの型 変数) { XXXXXXXXXXXXXXXXXX} と書く
			try {
				System.out.println("何日ですか?");
				String line = reader.readLine();
				int theday = Integer.parseInt(line);
				System.out.println("日付" + theday + "日ですね。");

				test(theday);

			} catch (IOException e) {
				System.out.println(e);
			} catch (NumberFormatException e) {
				System.out.println("数字を入力してください。");
			} catch (NoHolidayException e) {
				e.printStackTrace();
			}
		}
	}

	void test(int theday) throws NoHolidayException {
		LocalDate date = LocalDate.of(2024, 5, theday);
		DayOfWeek dayOfWeek = date.getDayOfWeek();

		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			System.out.println(theday + "日は休日です。");
		} else {
			throw new NoHolidayException(/*theday + "日は休日ではありません。"*/);
		}

	}

}