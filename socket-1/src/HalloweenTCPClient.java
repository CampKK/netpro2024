import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HalloweenTCPClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("ポート番号を入力してください (例: 5000) → ");
            int port = scanner.nextInt();

            try (Socket socket = new Socket("localhost", port);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {

                System.out.println("サーバーに接続しました。");

                while (true) {
                    // ユーザーからの入力を受け取る
                    System.out.print("ほしいお菓子を入力してください: ");
                    String okashi = scanner.next();
                    System.out.print("メッセージを入力してください: ");
                    String message = scanner.next();

                    // ゴーストオブジェクトを作成し、サーバーに送信
                    MyHalloweenGhost ghost = new MyHalloweenGhost(okashi, message);
                    oos.writeObject(ghost);
                    oos.flush();

                    // サーバーからの応答を受け取る
                    MyHalloweenGhost modifiedGhost = (MyHalloweenGhost) ois.readObject();
                    modifiedGhost.printGhostInfo();

                    // ユーザーに継続するか終了するかを確認
                    System.out.print("別のメッセージを送信するには 'continue' と入力し、終了するには 'quit' と入力してください: ");
                    String command = scanner.next();
                    osw.write(command + "\n");
                    osw.flush();

                    if ("quit".equalsIgnoreCase(command) || "exit".equalsIgnoreCase(command)) {
                        System.out.println("クライアントをシャットダウンします...");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
