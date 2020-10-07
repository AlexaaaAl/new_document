package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Document;
import models.Documents;
import util.ConnectionUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ViewDocuments {
    @FXML
    private TableView tblFile;
    @FXML
    private Label g;
    @FXML
    private TableColumn<Documents, String>  id_file;
    @FXML
    private TableColumn<Documents, String>  name_file;

    int numb;
    private ObservableList<Documents> data;

    Connection connection;
    public void setNumb(int numb){
        this.numb=numb;
    }
    public ViewDocuments () {connection = ConnectionUtil.connectdb();}
    public void initialize() {
        try {
            File file = new File("numb.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            numb = Integer.parseInt(reader.readLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fetColumnList();
    }
    private void fetColumnList() {
        try {
            data= FXCollections.observableArrayList();
            ResultSet id_file=connection.createStatement().executeQuery("SELECT id_file FROM all_one WHERE id_doc="+
                    numb);
            while ( id_file.next()) {

                ResultSet rs=connection.createStatement().executeQuery("SELECT id,path,file " +
                        "FROM document_file " +
                        "WHERE id=" +id_file.getInt(1));
                rs.next();
                data.add(new Documents(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
        id_file.setCellValueFactory(new PropertyValueFactory("path"));
        name_file.setCellValueFactory(new PropertyValueFactory("document"));
        tblFile.setItems(null);
        tblFile.setItems(data);
    }

    public void Gui(MouseEvent mouseEvent) {
        fetColumnList();
    }
    public void RemoveFile(MouseEvent mouseEvent){
        int row = tblFile.getSelectionModel().getSelectedIndex();
        Documents document = (Documents) tblFile.getSelectionModel().getSelectedItem();
        String pathDelete = document.getPath()+"/"+document.getDocument();
        g.setText(pathDelete);
        showConfirmationDelete(document.getId(),pathDelete);
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.setMaximized(true);
        stage.close();
    }
    public void AddFile(MouseEvent mouseEvent){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выбор документа");
        File file = chooser.showOpenDialog(new Stage());
        String path =file.getPath();
        String doc_name =file.getName();
        showConfirmationAdd(path,doc_name,numb);
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.setMaximized(true);
        stage.close();
    }
    public void DownloadFile(MouseEvent mouseEvent) throws IOException {
        int row = tblFile.getSelectionModel().getSelectedIndex();
        Documents document = (Documents) tblFile.getSelectionModel().getSelectedItem();
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Document/DownloadDocument.fxml"));
        Parent root = loader.load();
        DownloadDocument controllerEditBook = loader.getController(); //получаем контроллер для второй формы
        controllerEditBook.setPath(document.getPath()+"/"+document.getDocument()); // передаем необходимые параметры
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Скачать подписанный документ");
        stage.show();
    }
    private void showConfirmationDelete(int id,String path) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление файла");
        alert.setHeaderText("Вы точно хотите удалить документ ?");
        File file = new File(path);
        file.delete();
        try {
            connection.createStatement().executeUpdate("DELETE From document_file where id=" + id);
        }catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
    }
    private void showConfirmationAdd(String id,String path,int numb) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Добавление файла");
        alert.setHeaderText("Добавить документ ?");

        try {
            connection.createStatement().executeUpdate("DELETE From document_file where id=" + id);
        }catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
    }
}
