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
import org.apache.commons.io.FileUtils;
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
    private void showConfirmationAdd(String path,String name,int numb) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Добавление файла");
        alert.setHeaderText("Добавить документ ?");
        int id_f=0;
        try {
            ResultSet id_user = connection.prepareStatement("SELECT DISTINCT(id_sender) from documents WHERE number='" +
                    numb + "'").executeQuery();
            id_user.next();
            //полное имя пользователя
            ResultSet user_name = connection.prepareStatement("SELECT Last_name,First_name,Middle_name " +
                    "from users WHERE id=" +
                    id_user.getInt(1)).executeQuery();
            user_name.next();
            //отдел в котором работает пользователь
            ResultSet DEPARTMENT = connection.prepareStatement("SELECT DEPARTMENT From users where id=" +
                    id_user.getInt(1)).executeQuery();
            DEPARTMENT.next();
            ResultSet id_file=connection.prepareStatement("SELECT max(id) " +
                    "from document_file;").executeQuery();
            id_file.next();
            ResultSet ip_server = connection.prepareStatement("SELECT ip_server From users where id=" +
                    id_user.getInt(1) ).executeQuery();
            ip_server.next();
            if ( id_file!=null){
                id_f=id_file.getInt(1)+1;
            }
            String input = path; //путь к загружаемому файлу
            String output = "//" + ip_server.getString(1) + "/Программа/" +
                    DEPARTMENT.getString(1) + "/" +
                    user_name.getString(1) + " " +
                    user_name.getString(2) + " " +
                    user_name.getString(3) + "/" + LocalDate.now();//папка вывода
            sendFile(new File(input), new File(output));
            connection.createStatement().executeUpdate("INSERT INTO `document_file`" +
                    "    (`id` ,`path`, `file`)" + "    VALUES ("+id_f+",'" + output + "','" + name + "')");
            connection.createStatement().executeUpdate("INSERT INTO `all_one`" +
                    "    (`id_doc`, `id_file`)" + "    VALUES ("
                    + numb +","+id_f+")");
        }catch (SQLException | IOException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
    }
    public void sendFile(File input,File output) throws IOException {
        FileUtils.copyFileToDirectory(input,
                output);
    }
}
