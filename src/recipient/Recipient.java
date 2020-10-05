package recipient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import util.ConnectionUtil;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;

public class Recipient implements Initializable {
    @FXML
    ListView listv;
    @FXML
    ChoiceBox choise;
    @FXML
    Button add;
    @FXML
    Button delete;
    ObservableList<String> recipient=FXCollections.observableArrayList();
    Connection connection;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = ConnectionUtil.connectdb();
        choise.setItems(addColumn());
    }
    public void AddRecip(ActionEvent actionEvent) {
        recipient.add(choise.getValue().toString());
        listv.setItems(recipient);
    }

    public void DeleteRecip(ActionEvent actionEvent) {
        recipient.remove(choise.getValue().toString());
        listv.setItems(recipient);
    }
    public ObservableList<String> addColumn(){
        ObservableList<String> langs = FXCollections.observableArrayList();
        try {
            ResultSet rs=connection.createStatement().executeQuery("SELECT first_name FROM users");
            while (rs.next()) {
                langs.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
        return langs;
    }

}
