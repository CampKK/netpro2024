import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HalloweenTCPServer {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("ポート番号を入力してください (例: 5000) → ");
            int port = scanner.nextInt();

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("ポート " + port + " で待機中...");

                while (true) {
                    // クライアントからの接続を待つ
                    try (Socket clientSocket = serverSocket.accept();
                        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                        InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)) {

                        System.out.println("クライアントが接続されました。");

                        // クライアントからオブジェクトを受け取る
                        MyHalloweenGhost ghost = (MyHalloweenGhost) ois.readObject();
                        ghost.printGhostInfo();


                        // 変更後のオブジェクトをクライアントに返す
                        oos.writeObject(ghost);
                        oos.flush();

                        // クライアントからの終了コマンドを確認
                        Scanner commandScanner = new Scanner(isr);
                        String command = commandScanner.nextLine();
                        if ("quit".equalsIgnoreCase(command) || "exit".equalsIgnoreCase(command)) {
                            System.out.println("サーバーをシャットダウンします...");
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
