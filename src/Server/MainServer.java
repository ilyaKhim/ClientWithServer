package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

public class MainServer {
    private Vector<ClientHandler> clients;

//    public  Vector<ClientHandler> getClients(){
//        return clients;
//    }

    public MainServer() throws SQLException {
        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();
        try {
            AuthService.connect();

//            System.out.println(AuthService.getNiceByLoginAndPass("login1", "pass1"));
            server = new ServerSocket(8001, 10);
            System.out.println("Сервер запущен");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }


    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public void privateMsg(String msg, String nickFrom, String nickDest) {
        String message[] = msg.split(" ", 3); // разбил, чтобы можно было вставить двоеточие
        for (ClientHandler c : clients) {
            if (nickFrom.equals(nickDest)) {
                if (c.getNick().equals(nickDest)) {
                    c.sendMsg("Личное сообщение от " + nickFrom + " " + message[1] + ": " + message[2]);
                    break;
                }
            }
            if (c.getNick().equals(nickDest)) {
                c.sendMsg("Личное сообщение от " + nickFrom + " " + message[1] + ": " + message[2]);
            }
            if (c.getNick().equals(nickFrom)) {
                c.sendMsg("Личное сообщение от " + nickFrom + " " + message[1] + ": " + message[2]);
            }



        }
    }
}