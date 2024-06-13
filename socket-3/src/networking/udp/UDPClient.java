package networking.udp;
<<<<<<< HEAD
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
=======

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
>>>>>>> e7723f5c4db321a48162b3d6ee95d454e599de1d

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket socket = null;
<<<<<<< HEAD
        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            String message = "Hello, server!";
            byte[] sendData = message.getBytes();

            // 送信パケットを作成
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9876);
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
=======
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
>>>>>>> e7723f5c4db321a48162b3d6ee95d454e599de1d
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> e7723f5c4db321a48162b3d6ee95d454e599de1d
