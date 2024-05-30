package networking.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");

            while (true) {
                System.out.print("入力してね（終了なら exit）: ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                byte[] sendData = message.getBytes();

                // 送信パケットを作成
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9876);
                socket.send(sendPacket);

                // 受信データ用のバッファを作成
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // サーバからの返信を受信
                socket.receive(receivePacket);

                String modifiedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("サーバからの返信: " + modifiedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}