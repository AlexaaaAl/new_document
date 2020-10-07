package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import util.ConnectionUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DownloadDocument {
    @FXML
    private TextField pathSave;
    Connection connection;
    String path;
    File file;
    public void setPath(String path){
        this.path=path;
    }
    public int id ;
    public int getId()
    {
        return id;
    }
    public DownloadDocument(){
        connection = ConnectionUtil.connectdb();
    }
    public void initialize(){}
    public void ChoosePath(MouseEvent event ) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Выбор папки");
        file = chooser.showDialog(new Stage());
        //)  showOpenDialog(new Stage());
        pathSave.setText(file.getPath());

    }
    public void DownloadFile(MouseEvent event ) {
        try {
            FileUtils.copyFileToDirectory(new File(path), new File(file.getAbsolutePath()));
            //infoBox(file.getName(), "Файл был скачан в папку " + path, "Успешно");
        }catch (IOException throwables) {
            System.out.println("Ex "+throwables);
        }
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.setMaximized(true);
        stage.close();
    }
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

}
