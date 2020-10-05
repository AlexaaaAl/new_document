package controller;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Document;
import org.apache.commons.io.FileUtils;
import util.ConnectionUtil;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class FXMLMenuController implements Initializable {
    @FXML
    TableView tblData;
    @FXML
    private Label login;
    @FXML
    private Label login1;
    @FXML
    private TableColumn<Document,Integer> tblDateAdded;
    @FXML
    private TableColumn<Document,Integer> tblNumber;
    @FXML
    private TableColumn<Document,String> tblDocument;
    @FXML
    private TableColumn<Document,String> tblSender;
    @FXML
    private TableColumn<Document,String> tblRecipient;
    @FXML
    private TableColumn<Document,String> tblStatus;
    @FXML
    private TableColumn<Document, Date> tblDate;
    @FXML
    private TableColumn<Document,String> tblComments;
    @FXML
    private Button Button;
    @FXML
    private TextField numberDoc;

    Scene scene;
    int id_user;
    private ObservableList<Document>data;
    private ObservableList<Document> filteredData = FXCollections.observableArrayList();
    String name= null;
    String server= null;
    PreparedStatement preparedStatement;
    Connection connection;
    String notification;
    static int seconds = 1;
    static boolean flag = true; // Можете где-нибудь потом сделать false и остановить поток
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fetColumnList();
        try {
            File file = new File("output.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            login.setText(line);
            name=line;
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
        try{
            ResultSet id=connection.createStatement().executeQuery("select id_user from log where login ='"+
                    login.getText()+"'");
            id.next();
            ResultSet id_role=connection.createStatement().executeQuery("select role_id from users where id ="+
                    id.getInt(1));
            id_role.next();
            if (id_role.getInt(1)==2){

            }
            ResultSet name=connection.createStatement().executeQuery("select FIRST_NAME,LAST_NAME,MIDDLE_NAME " +
                    "from users where id ="+
                    id.getInt(1));
            name.next();
            login1.setText(name.getString(2)+" "+name.getString(1)+" "+name.getString(3));
        }catch(SQLException ex){

        }

        flag = true;
        //постоянное одновление таблицы в потоке
        new Thread(() -> {
            try {
                while(flag) {
                    Thread.sleep(seconds * 3000);
                    fetColumnList(); // Вызываем метод из потока
                }
             } catch (InterruptedException v) {
             }
        }).start();
        filteredData.addAll(tblData.getItems());
        tblStatus.setCellFactory(column -> {
            return new TableCell<Document, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        Document auxPerson = getTableView().getItems().get(getIndex());
                        if (auxPerson.getStatus().equals("в ожидании")) {
                            setTextFill(Color.RED);
                            setStyle("-fx-background-color: yellow;-fx-alignment: CENTER");

                        } else {

                            if(auxPerson.getStatus().equals("подтверждено")) {
                                setTextFill(Color.WHITE);
                                setStyle("-fx-background-color: green;-fx-alignment: CENTER");

                            }
                            else {
                                setTextFill(Color.BLACK);
                                setStyle("-fx-background-color: cornflowerblue;-fx-alignment: CENTER");

                            }
                        }
                    }
                }
            };
        });
        try {
            ResultSet rs=connection.createStatement().executeQuery("select id_user from log where login ='"+
                    login.getText()+"'");
            rs.next();
            id_user= rs.getInt(1);
            /*ResultSet serv=connection.createStatement().executeQuery("select ip_server from users where id ="+
                    id_user);
            rs.next();
            server= serv.getString(1);*/
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public FXMLMenuController() {
        connection = ConnectionUtil.connectdb();
    }
    //вывод статусов документа
    public ObservableList<String> addColumn(String column){
        ObservableList<String> langs = FXCollections.observableArrayList();
        try {
            ResultSet rs=connection.createStatement().executeQuery("SELECT distinct "+column+" FROM documents");
            while (rs.next()) {
                langs.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }

        return langs;
    }
    //заполнение таблицы при запуске программы
    private void fetColumnList() {
        try {
            data=FXCollections.observableArrayList();
            ResultSet rs=connection.createStatement().executeQuery("SELECT doc.id_document,doc.number, us.LAST_NAME , us1.LAST_NAME," +
                    " doc.comments,doc.date,doc.status,doc.outline,doc.date_added" +
                    " FROM documents doc" +
                    " INNER JOIN users us" +
                    " on doc.id_sender = us.ID" +
                    " INNER JOIN users us1" +
                    " on doc.id_recipient = us1.ID WHERE doc.id_sender=" +
                    "(select id_user from log where login='"+name +"') or  " +
                    "id_recipient=(select id_user from log where login='"+name+"')");
            while (rs.next()) {

                data.add(new Document(rs.getInt(1),rs.getInt(2),
                        rs.getString(3), rs.getString(4),rs.getString(8),
                        rs.getString(5), rs.getDate(6),
                        rs.getDate(9), rs.getString(7)/*Date.valueOf(LocalDate.now())*/));
                if (rs.getDate(6)!=null){
                    java.util.Date r=rs.getDate(6);
                    java.util.Date now=Date.valueOf(LocalDate.now());
                    //long milliseconds =now - r;
                    //if ()
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
        tblDateAdded.setCellValueFactory(new PropertyValueFactory("date_added"));
        tblNumber.setCellValueFactory(new PropertyValueFactory("number"));
        tblDocument.setCellValueFactory(new PropertyValueFactory("document"));
        tblSender.setCellValueFactory(new PropertyValueFactory("id_sender"));
        tblRecipient.setCellValueFactory(new PropertyValueFactory("id_recipient"));
        tblComments.setCellValueFactory(new PropertyValueFactory("outline"));
        tblStatus.setCellValueFactory(new PropertyValueFactory("status"));
        tblDate.setCellValueFactory(new PropertyValueFactory("date"));
        tblData.setItems(null);
        tblData.setItems(data);
    }
    //Вызов окна добавления документа
    public void handleButtonAction() {
        try {
            Stage stage=new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/AddDocument/AddDocument.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Добавление документа");
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        fetColumnList();

    }
    //изменение строки документа
    public void handleButtonActionChange(MouseEvent mouse) throws IOException{
        if (mouse.getClickCount() == 2) {
            int row = tblData.getSelectionModel().getSelectedIndex();
            Document document = (Document) tblData.getSelectionModel().getSelectedItem();
            int iddocument = document.getId_document();
            try {
               /* ResultSet rs=connection.createStatement().executeQuery("select id_user from log where login ='"+
                        login.getText()+"'");
                rs.next();
                id_user= rs.getInt(1);*/
                connection.createStatement().executeUpdate("UPDATE documents " +
                        "set status='выполняется'" +
                        "where id_document=" + iddocument +
                        " AND id_recipient="+id_user+
                        " AND status <> 'подтверждено'");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                //добавление в документ id doc и id user
                PrintWriter writers = new PrintWriter("doc.txt", "UTF-8");
                writers.println(iddocument);
                writers.println(id_user);
                writers.close();
            }
            catch (IOException exception){
                System.err.println(exception.getMessage());
            }
            Stage stage=new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Document/ViewDoc.fxml"));
            Parent root = loader.load();
            ViewDocController controllerEditBook = loader.getController(); //получаем контроллер для второй формы
            //controllerEditBook.setId(iddocument); // передаем необходимые параметры
            //controllerEditBook.setId_user(id_user);
            // Parent view = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Просмотр данных");
            stage.show();

        }
    }
    public void handleButtonActionFilter(){
            addTextFilter(data, numberDoc, tblData);
    }
    //фильтрация текста
    public static void addTextFilter(ObservableList<Document> allData,
                                     TextField filterField, TableView<Document> table) {
        final List<TableColumn<Document, ?>> columns = table.getColumns();
        FilteredList<Document> filteredData = new FilteredList<>(allData);
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String text="" ;
            //if(choice.getValue()!= null) {
            //text= choice.
            //lbl.setText(text);
            //text = choice.getValue().toString();//filterField.getText();
           // }
           // if(filterField.getText()!= null) {
           text = filterField.getText();
           // }
            if (text == null || text.isEmpty()) {
                return null;
            }
            final String filterText = text.toLowerCase();
            return o -> {
                for (TableColumn<Document, ?> col : columns) {
                    ObservableValue<?> observable = col.getCellObservableValue(o);
                    if (observable != null) {
                        Object value = observable.getValue();
                        if (value != null && value.toString().toLowerCase().equals(filterText)) {
                            return true;
                        }
                    }
                }
                return false;
            };
        }, filterField.textProperty()));
        SortedList<Document> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }
    //выход
    public void GoOut(ActionEvent actionEvent) throws IOException {
        File file = new File("\\\\output.txt");
        if(file.delete()){
            System.out.println("output.txt файл был удален с корневой папки проекта");
        }else System.out.println("Файл output.txt не был найден в корневой папке проекта");
        File file1 = new File("\\\\doc.txt");
        if(file1.delete()){
            System.out.println("doc.txt файл был удален с корневой папки проекта");
        }else System.out.println("Файл doc.txt не был найден в корневой папке проекта");
        flag=false;
        Platform.exit();
        System.exit(0);

    }


    public void AddUser(ActionEvent actionEvent) throws IOException {
        Stage stage=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/AddUser/AddUser.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setTitle("Добавление пользователя");
        stage.show();

    }
}