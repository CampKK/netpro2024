package networking.udp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastClient {
    public static void main(String[] args) {
        try {
            // マルチキャストグループのIPアドレスとポート番号を指定
            InetAddress group = InetAddress.getByName("239.0.0.1");
            int port = 12345;

            // データ送信用のソケットを作成
            DatagramSocket socket = new DatagramSocket();

            // 送信するコマンドを設定
<<<<<<< HEAD
            String command = "SOME_COMMAND";
=======
        String command = "TUSHINN";
>>>>>>> e7723f5c4db321a48162b3d6ee95d454e599de1d

            // コマンドをバイト配列に変換して DatagramPacket を作成し、マルチキャストグループに送信
            byte[] buffer = command.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);

            System.out.println("Command sent: " + command);

            // ソケットを閉じる
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
