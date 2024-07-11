import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String userInput;
            while (true) {
                System.out.print("数字を入力 (終了するには 'q' または 'exit' を入力): ");
                userInput = stdIn.readLine();
                out.println(userInput);

                if (userInput.equalsIgnoreCase("q") || userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                String response = in.readLine();
                System.out.println("サーバからの応答: " + response);
            }
        } catch (Exception e) {
            System.out.println("クライアントエラー: " + e.getMessage());
        }
    }
}
