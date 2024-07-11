import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("サーバがポート12345で起動しました...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.equalsIgnoreCase("q") || inputLine.equalsIgnoreCase("exit")) {
                            break;
                        }
                        try {
                            int number = Integer.parseInt(inputLine);
                            if (isPrime(number)) {
                                out.println(number + " is Prime");
                            } else {
                                out.println(number + " is non-Prime");
                            }
                        } catch (NumberFormatException e) {
                            out.println("Invalid input, please enter a number.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("クライアント接続エラー: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("サーバエラー: " + e.getMessage());
        }
    }

    private static boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}
