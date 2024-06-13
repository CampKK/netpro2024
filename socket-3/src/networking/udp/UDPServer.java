package networking.udp;
<<<<<<< HEAD
import java.net.DatagramPacket;
import java.net.DatagramSocket;
=======

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
>>>>>>> e7723f5c4db321a48162b3d6ee95d454e599de1d

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];

            while (true) {
                // 受信パケットを作成
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("受信: " + message);
<<<<<<< HEAD
=======

                // メッセージを大文字に変換
                String upperCaseMessage = message.toUpperCase();
                byte[] sendData = upperCaseMessage.getBytes();

                // クライアントのアドレスとポートを取得
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // 返信パケットを作成
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(sendPacket);

                System.out.println("返信: " + upperCaseMessage);
>>>>>>> e7723f5c4db321a48162b3d6ee95d454e599de1d
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
