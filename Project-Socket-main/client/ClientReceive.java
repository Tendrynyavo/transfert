package client;

import java.io.DataOutputStream;
import java.net.Socket;

public class ClientReceive {
    
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8089);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String fileName = "compile.sh";
            out.writeInt(fileName.length());
            out.write(fileName.getBytes());
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
