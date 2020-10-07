package util;

import java.sql.*;
import javax.swing.*;

public class ConnectionUtil {

    public static Connection connectdb()
    {
        Connection conn = null;
        try{
           /* String url = "jdbc:mysql://localhost:3306/document_circulation?useLegacyDatetimeCode=false&serverTimezone=" +
                    "Europe/Moscow"; //TIMEZONE обязательно!!!!!!!!!!!!!!!!*/
            String url = "jdbc:mysql://192.168.50.10:3306/new_document_circulation?useLegacyDatetimeCode=false&serverTimezone=" +
                    "Europe/Moscow";
            String username = "root";
            String password = "password";
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance(); //jar библиотека
            /*try (Connection conn = DriverManager.getConnection(url, username, password)){ //попытка подключения к бд
                return conn;0
            }*/
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex); //показать ошибку подключения
            System.exit(0);//закрыть приложение
            return null;
        }
    }

}
