package Client;

import Server.AuthService;
import Server.ClientHandler;
import Server.MainServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("My Client");
        Scene scene = new Scene(root, 370,370);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // подскажите, как достать текущий ник для того, чтобы его поле "status", которое говорит о том,
                // авторизован пользователь или нет, изменить? Т.е значение 0 - не авторизован, 1 - авторизован
                // Вся проблема в том, что в данной программе мы не можем зайти в аккаунт, если мы не прописали /end
                // А просто вышли из него нажав крестик. Хочу чтобы это отлавливалось.
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
