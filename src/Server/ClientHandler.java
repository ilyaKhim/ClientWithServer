package Server;

import Client.Main;
import Server.MainServer.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private MainServer server;
    public String nick;

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
                            if(str.startsWith("/auth")){
                                String[] tokens = str.split(" ");
                                String newNick = AuthService.getNiceByLoginAndPass(tokens[1], tokens[2]);
                                boolean authStatus = AuthService.isAlreadyAuth(tokens[1], tokens[2]);
                                if(newNick != null && authStatus == false){
                                    sendMsg("/authok");
                                    nick = newNick;
                                    setNick(newNick);
                                    server.subscribe(ClientHandler.this);
                                    AuthService.connectUser(nick);
                                    break;
                                } else if (newNick == null){
                                    sendMsg("Неверный логин/пароль");
                                } else if (authStatus == true){
                                    sendMsg("Пользователь уже авторизован в системе");
                                }
                            }

                        }
                        while (true){
                            String str = in.readUTF();
//                            getNick();
                            if(str.equals("/end")){
                                out.writeUTF("/serverClosed");
                                break;
                            }
                            if(str.startsWith("/w ")){
                                String[] temp = str.split(" ");
                                String tempNick = temp[1];

                                server.privateMsg(str, nick, tempNick);
                            }
                            else server.broadcastMsg("Client "+ nick+ ": " + str);


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
                        AuthService.disconnectUser(nick);

                    }
                }
            }).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void setNick(String nick){
        this.nick = nick;
    }

    public  String getNick(){
        return nick;
    }


    public void sendMsg(String msg){
        try{
            out.writeUTF(msg);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}