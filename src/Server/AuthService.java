package Server;

import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement stat;


    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:DBUsers.db");
            stat = connection.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getNiceByLoginAndPass(String login, String pass){
        String sql = String.format("SELECT nickname FROM main where login = '%s' and password = '%s'", login, pass);
        try {
            ResultSet rs = stat.executeQuery(sql);
            if(rs.next()){
                return rs.getString(1);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void connectUser(String nick){
        String sql = String.format("UPDATE main SET status = '1' where nickname = '%s'", nick);
        try{
            stat.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void disconnectUser(String nick){
        String sql = String.format("UPDATE main SET status = '0' where nickname = '%s'", nick);
        try{
            stat.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean isAlreadyAuth(String login, String pass){
        String sql = String.format("SELECT status FROM main where login = '%s' and password = '%s'", login, pass);
        try{
            ResultSet rs = stat.executeQuery(sql);
            if(rs.next()){
                return rs.getBoolean("status");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
