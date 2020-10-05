package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import util.ConnectionUtil;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private TextField last_name;
    @FXML
    private TextField position;
    @FXML
    private TextField department;
    @FXML
    private TextField server;
    @FXML
    private TextField mail;
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    Connection connection;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //MaskFormatter mf1 = new MaskFormatter('###.###.##');
        //JFormattedTextField s = new JFormattedTextField(mf1);
        //server.
    }
    public AddUserController() {
        connection = ConnectionUtil.connectdb();
    }

    public void AddUser(ActionEvent actionEvent) throws SQLException {
        connection.createStatement().executeUpdate("INSERT INTO `users`" +
                "    ( `FIRST_NAME`,`LAST_NAME`,`POSITION`, `DEPARTMENT`,`ip_server`, `E_MAIL`, `ROLE_ID`)" +
                "    VALUES" +
                "           ('" + name.getText() + "','" + last_name.getText() + "','" +
                position.getText() + "','" + department.getText() + "','" +
                server.getText() + "','" +
                mail.getText() + "'," +
                2 +")");
        ResultSet id_user = connection.prepareStatement("SELECT id From users where LAST_NAME='" +
                last_name.getText() + "'").executeQuery();
        id_user.next();
        connection.createStatement().executeUpdate("INSERT INTO `log`" +
                "    ( `id_user`,`login`,`password`)" +
                "    VALUES" +
                "           ("+id_user.getInt(1)+ ",'" + login.getText() + "','" + password.getText() +"')");
        infoBox("Пользователь добавлен! ",null,"Success" );

    }
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
