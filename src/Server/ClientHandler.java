package Server;

import sun.applet.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private MainServer server;

    public ClientHandler(Socket socket, MainServer server){
        try{
            this.socket = socket;
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                   try{
                       while (true){
                           String str = in.readUTF();
                           if(str.equals("/end")){
                               break;
                           }
                           server.broadcastMsg("Client: " + str);
                       }
                   } catch (IOException e){
                       e.printStackTrace();
                   } finally {
                       try{
                           in.close();
                       } catch (IOException e){
                           e.printStackTrace();
                       }
                       try {
                           out.close();
                       } catch (IOException e){
                           e.printStackTrace();
                       }
                       try {
                           socket.close();
                       } catch (IOException e){
                           e.printStackTrace();
                       }
                       server.unsubscribe(ClientHandler.this);

                   }
                }
            }).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }





    public void sendMsg(String msg){
        try{
            out.writeUTF(msg);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
