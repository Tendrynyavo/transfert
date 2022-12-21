package serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import fichier.Fichier;

public class Download {
    
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8089);
            Socket client = server.accept();
            DataInputStream input = new DataInputStream(client.getInputStream());
            int lengthName = input.readInt();
            byte[] b = new byte[lengthName];
            input.read(b, 0, lengthName);
            String name = new String(b);
            Fichier file = Fichier.findByName(name);
            sendEachServer(file);
            server.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }    
    }

    public static void sendEachServer(Fichier file) throws Exception {
        DataOutputStream out = null;
        Socket socket = null;
        // Socket receiver = new Socket("localhost", 8089);
        // DataOutputStream outReceiver = new DataOutputStream(receiver.getOutputStream());
        for (int increment = 1; increment <= 3; increment++) {
            socket = new Socket("localhost", 8090 + increment);
            out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(file.getIdFile().length());
            out.write(file.getIdFile().getBytes());
            ServerSocket server = new ServerSocket(8089);
            Socket client = server.accept();
            System.out.println("Mety ve");
            // DataInputStream input = new DataInputStream(client.getInputStream());
            // byte[] content = new byte[4899];
            // input.read(content);
            // outReceiver.write(content);
            out.close();
            socket.close();
            server.close();
        }
    }
}
