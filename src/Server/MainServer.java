package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

public class MainServer {
    private Vector<ClientHandler> clients;

    public MainServer() throws SQLException {
        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();
        try {
            AuthService.connect();

//            System.out.println(AuthService.getNiceByLoginAndPass("login1", "pass1"));
            server = new ServerSocket(8100, 10);
            System.out.println("Сервер запущен");

            while(true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                subscribe(new ClientHandler(socket, this));
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                socket.close();

            } catch (IOException e){
                e.printStackTrace();
            }

            try{
                server.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }

    public void subscribe(ClientHandler client){
        clients.add(client);
    }

    public void unsubscribe(ClientHandler client){
        clients.remove(client);
    }

    public void broadcastMsg(String msg){
        for(ClientHandler o: clients){
            o.sendMsg(msg);
        }
    }

}
