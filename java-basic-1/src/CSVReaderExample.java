import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReaderExample {
    public static void main(String[] args) {
        String csvFile = "jusho.csv"; // ファイルのパスを指定してください
        String line = "";
        String csvSplitBy = ","; // CSVファイルの区切り文字を指定してください
        int skipLines = 1; // ヘッダー行を読み飛ばすための行数

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // ヘッダー行を読み飛ばす
            for (int i = 0; i < skipLines; i++) {
                br.readLine();
            }

            while ((line = br.readLine()) != null) {
                // 1行をカンマで分割して単語の配列を取得
                String[] words = line.split(csvSplitBy);

                // 区を出力
                System.out.println(words[3]); // 区のインデックスは3

            }//while end
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//main end
}//class end

