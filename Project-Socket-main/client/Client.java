package client;

import formulaire.Button;
import formulaire.Champ;
import formulaire.Formulaire;
import listener.DownloadListener;
import listener.Upload;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    Socket socket;
    DataOutputStream out;
    DataInputStream input;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getInput() {
        return input;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setInput(DataInputStream input) {
        this.input = input;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public Client(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        setSocket(socket);
        setInput(new DataInputStream(socket.getInputStream()));
        setOut(new DataOutputStream(socket.getOutputStream()));
    }

    public Client(Socket socket) throws Exception {
        setSocket(socket);
        setInput(new DataInputStream(socket.getInputStream()));
        setOut(new DataOutputStream(socket.getOutputStream()));
    }

    public static void main(String[] args) {
        try {
            //File file = new File("./page1.html");
            Client client = new Client("localhost", 8090);
            //client.sendFile(file);
            //client.receive("./page1.html");
            Formulaire form = Formulaire.createFormulaire(new Client());
            for (Champ champ : form.getListeChamp()) {
                champ.setVisible(false, "");
            }
            form.addButtons(new Button[] {new Button(new Upload(client), "upload"), new Button(new DownloadListener(client), "download")});
            form.setPosition();
            form.initFrame(new JFrame());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client() {}

    public void sendFile(File file) throws Exception {
        sendNameFile("send");
        sendNameFile(file.getName());
        sendFileContent(file);
    }

    public void sendNameFile(String name) throws Exception {
        getOut().writeUTF(name);
    }
    
    public void sendFileContent(File file) throws Exception {
        FileInputStream input = new FileInputStream(file);
        byte[] content = new byte[(int) file.length()];
        getOut().writeInt((int) file.length());
        input.read(content);
        getOut().write(content);
    }

    public void receive(String filename) throws Exception {
        sendNameFile("receive");
        sendNameFile(filename);
        File fileToDownload = new File(filename);
        FileOutputStream fileOutputStream = new FileOutputStream("receive/" + fileToDownload);
        byte[] fileContentBytes = new byte[getInput().readInt()];
        getInput().read(fileContentBytes);
        fileOutputStream.write(fileContentBytes);
    }
}