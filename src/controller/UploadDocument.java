package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import util.ConnectionUtil;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UploadDocument {
    @FXML
    private TextField pathSave;

    Connection connection;
    File file;
    String line;
    String path;
    public void setPath(String path){
        this.path=path;
    }
    String document;
    public void setDocument(String document){
        this.document=document;
    }
    int id ;
    public void setId(int id)
    {
        this.id=id;
    }
    int id_user ;
    public void setId_user(int id_user)
    {
        this.id_user=id_user;
    }
    public UploadDocument(){
        connection = ConnectionUtil.connectdb();
    }
    public void initialize(){
        try {
            File file = new File("output.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                // считываем остальные строки в цикле
                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void ChoosePath(MouseEvent event ) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выбор документа");
        file = chooser.showOpenDialog(new Stage());
        pathSave.setText(file.getPath());

    }
    public void DownloadFile(MouseEvent event ) throws IOException, SQLException {
        File input=new File(file.getAbsolutePath());
        File output=new File(path/*+"\\Подтвержденный"+document*/);

        FileUtils.copyFileToDirectory(input,output);
            connection.createStatement().executeUpdate("INSERT INTO `answer_recirient`" +
                    "    ( `id_doc`,`path`, `document`)" +
                    "    VALUES" +
                    "           (" +id+",'"+
                    path+"','"+file.getName()+"')");

        infoBox(file.getName(), "Файл был загружен в папку " + path, "Успешно");
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
