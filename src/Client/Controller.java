package Client;
import Server.ClientHandler;
import Server.MainServer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Controller  {
    @FXML
    public void exitApplication(ActionEvent event){
        Platform.exit();

    }

    @FXML
    TextArea textArea;

    @FXML
    TextField textField;

    @FXML
    Button btn1;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    @FXML
    HBox bottomPanel;

    @FXML
    HBox upperPanel;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passField;




    private boolean isAuthorized;

    final String IP_ADRESS = "localhost";
    final int PORT = 8001;


    public void setAuthorized(boolean isAuthorized){
        this.isAuthorized = isAuthorized;

        if(!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
        }
        if(isAuthorized) {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
        }

    }


    public void connect()  {
        try{
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run()  {
                    try{
                        while(true){
                            String str = in.readUTF();
                            if(str.startsWith("/authok")){
                                setAuthorized(true);
                                textArea.clear();
                                textArea.appendText("Вы успешно авторизовались!\n");

                                break;
                            } else {
                                textArea.appendText(str +  "\n");
                            }
                        }

                        while(true){

                            String str = in.readUTF();
                            if (str.equals("/serverClosed")){
                                break;
                            }

                            else textArea.appendText(str+"\n");
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    } finally {
                        try{
                            socket.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        setAuthorized(false);
                    }
                }
            }).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void tryToAuth(){
        if(socket == null || socket.isClosed()) connect();
        try{
            out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(){
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (RuntimeException e){
            e.printStackTrace();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
