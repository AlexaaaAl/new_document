package controller;

import Send.Send;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Answer;
import models.Document;
import util.ConnectionUtil;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/*
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;*/

public class ViewDocController {
    @FXML
    private Label idD; //id документа
    @FXML
    private Label numb; //номер документа
    @FXML
    private Label docum; //название документа
    @FXML
    private Label outline; //краткое описание
    @FXML
    private TextArea comments; //коментарий к документу
    @FXML
    private Button down; //кнопка "скачать потдтвержденный документ
    @FXML
    private Button upload; //кнопка "скачать потдтвержденный докумен
    @FXML
    private Button confirm_doc;
    @FXML
    private Button view_file;

    Connection connection;
    private ObservableList<Document> data;
    private ObservableList<Answer> answer;
    private Document document;
    private Answer document_ans;
    int id_document;
    int id_us;
    String server;
    public void setServer(String server){
        this.server=server;
    }

    public ViewDocController () {
        connection = ConnectionUtil.connectdb();
        //view();
    }
    public void initialize() {
        try {
            File file = new File("doc.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            id_document = Integer.parseInt(reader.readLine());
            id_us = Integer.parseInt(reader.readLine());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        idD.setText(Integer.toString(id_document));
        numb.setText(Integer.toString(id_us));
        try {
            data=FXCollections.observableArrayList();
            ResultSet rs=connection.createStatement().executeQuery("SELECT doc.id_document,doc.number," +
                    " us.LAST_NAME , us1.LAST_NAME," +
                    " doc.outline,doc.comments,doc.date_added,doc.date,doc.status,doc.document_type" +
                    " FROM documents doc" +
                    " INNER JOIN users us" +
                    " on doc.id_sender = us.ID" +
                    " INNER JOIN users us1" +
                    " on doc.id_recipient = us1.ID WHERE doc.id_document=" +
                    id_document);
            while (rs.next()) {
                data.add(new Document(rs.getInt(1), rs.getInt(2),
                        rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6),
                        rs.getDate(7), rs.getDate(8),
                        rs.getString(9), rs.getString(10)));
            }
            document=data.get(0);
            numb.setText(Integer.toString(document.getNumber()));
            docum.setText(document.getDocument());
            outline.setText(document.getOutline());
            comments.setText(document.getComments());
            upload.setDisable(true);
            confirm_doc.setDisable(true);
            String st="подтверждено";
             ResultSet id_rec=connection.createStatement().executeQuery("SELECT id_recipient " +
                     "FROM documents doc " +
                     "WHERE doc.id_document=" + id_document);
             id_rec.next();
            int t=id_rec.getInt(1);
            if (t==id_us){
                upload.setDisable(false);
                confirm_doc.setDisable(false);
            }
            if (!document.getStatus().equals(st)){
                down.setDisable(true);
            }else{
                answer=FXCollections.observableArrayList();
                try {
                    ResultSet confirm = connection.createStatement().executeQuery("SELECT answer.id_doc," +
                            "answer.path, answer.document , answer.date" +
                            " FROM answer_recirient answer" +
                            " WHERE answer.id_doc=" + id_document);
                    //confirm.next();
                    //pathb.setText(confirm.getString(2));
                    // doc.setText(confirm.getString(3));
                    confirm.next();
                    answer.add(new Answer(confirm.getInt(1),
                            confirm.getString(2),
                            confirm.getString(3),
                            confirm.getDate(4)));
                    /*while (confirm.next()) {
                        answer.add(new Answer(confirm.getInt(1),
                                confirm.getString(2), confirm.getString(3), confirm.getDate(4)));
                        //pathb.setText(confirm.getString(2));
                        //doc.setText(confirm.getString(3));
                    }*/
                    document_ans = answer.get(0);
                }catch (SQLException ex) {
                    System.err.println("Error 1 1  "+ex);
                }

            }

        } catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
        //view();
    }

    //изменить документ
    public void ChangeDocument(MouseEvent event) throws IOException {
        int id=Integer.parseInt(idD.getText());
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangeDocument/ChangeDocument.fxml"));
        Parent root = loader.load();
        ChangeDocumentController controllerEditBook = loader.getController(); //получаем контроллер для второй формы
        controllerEditBook.setId(id); // передаем необходимые параметры
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Изменить документ");
        stage.show();
    }
    //показать файлы
    public void ViewFile(MouseEvent event) throws IOException {
        //int id=Integer.parseInt(idD.getText());
        try {
            //добавление в документ id doc и id user
            PrintWriter writers = new PrintWriter("numb.txt", "UTF-8");
            writers.println(numb.getText());
            writers.close();
        }
        catch (IOException exception){
            System.err.println(exception.getMessage());
        }
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Document/ViewDocuments.fxml"));
        Parent root = loader.load();
        //ViewDocuments controllerEditBook = loader.getController(); //получаем контроллер для второй формы
        //controllerEditBook.setNumb(Integer.parseInt(numb.getText())); // передаем необходимые параметры
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Просмотр файлов");
        stage.show();

    }

    //скачать документ
    public void DownloadFile(MouseEvent event) throws IOException {
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Document/DownloadDocument.fxml"));
        Parent root = loader.load();
        DownloadDocument controllerEditBook = loader.getController(); //получаем контроллер для второй формы
        controllerEditBook.setPath(document.getPath()+"/"+document.getDocument()); // передаем необходимые параметры
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Скачать документ");
        stage.show();
    }
    //загрузить документ
    public void UploadFile(MouseEvent event) throws IOException {
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Document/UploadDocument.fxml"));
        Parent root = loader.load();
        UploadDocument controllerEditBook = loader.getController(); //получаем контроллер для второй формы
        controllerEditBook.setPath(document.getPath()); // передаем необходимые параметры
        controllerEditBook.setDocument(document.getDocument());
        controllerEditBook.setId(id_document);
       // controllerEditBook.setId_user(id_user);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Загругить документ");
        stage.show();
    }
    //скачать подтвержденный документ
    public void DownloadConfirmFile(MouseEvent event) throws IOException {
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Document/DownloadDocument.fxml"));
        Parent root = loader.load();
        DownloadDocument controllerEditBook = loader.getController(); //получаем контроллер для второй формы
        controllerEditBook.setPath(document_ans.getPath()+"/"+document_ans.getDocument()); // передаем необходимые параметры
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Скачать подписанный документ");
        stage.show();
    }
    //Удаление документа
    public void handleButtonActionDelete(MouseEvent event) {
        showConfirmationDelete(id_document);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.setMaximized(true);
        stage.close();

    }
    //удаление документа и всех записей
    private void showConfirmationDelete(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление файла");
        alert.setHeaderText("Вы точно хотите удалить документ ?");
        /*try {
            ResultSet path_d = connection.prepareStatement("SELECT path From documents where id_document=" +
                    id_document).executeQuery();
            ResultSet doc_d = connection.prepareStatement("SELECT document From documents where id_document=" +
                    id_document).executeQuery();
            // option != null.
            path_d.next();
            doc_d.next();
            String j=path_d.getString(1)+doc_d.getString(1);
            System.out.println(j);
            File file = new File(j);
            file.delete();
        }catch (SQLException ex){
            System.err.println("ER "+ex);
        }
        try {
            ResultSet path_d  = connection.prepareStatement("SELECT path From answer_recirient where id_doc=" +
                id_document).executeQuery();
            ResultSet  doc_d = connection.prepareStatement("SELECT document From answer_recirient where id_doc=" +
                id_document).executeQuery();
            path_d.next();
            doc_d.next();
            File file = new File(path_d.getString(1)+doc_d.getString(1));
            file.delete();
        }catch (SQLException ex){
            System.err.println("E "+ex);
        }*/
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            //this.label.setText("No selection!");
        } else if (option.get() == ButtonType.OK) {
            try {
                connection.createStatement().executeUpdate("DELETE From documents where id_document=" + id);
            }catch (SQLException ex) {
                System.err.println("Error!!!!!!!! "+ex);
            }
        } else if (option.get() == ButtonType.CANCEL) {
            //this.label.setText("Cancelled!");
        } else {
            //this.label.setText("-");
        }

    }

    public void Confirm(ActionEvent actionEvent) throws SQLException {
        connection.createStatement().executeUpdate("UPDATE documents " +
                "set status='подтверждено'" +
                " where id_document=" + id_document +
                " AND id_recipient="+id_us);
    }
    //пересылка документа
    public void SendMessage(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            ResultSet all = connection.prepareStatement("SELECT * From documents where id_document =" +
                    id_document).executeQuery();
            all.next();
            Document doc = new Document();
            doc.setNumber(all.getInt(2));
            doc.setDocument(all.getString(4));
            doc.setId_recipient(all.getString(6));
            doc.setOutline(all.getString(7));
            doc.setComments(all.getString(8));
            doc.setPath(all.getString(3));
            doc.setDate(all.getDate(10));
            doc.setStatus(all.getString(11));
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Send/Send.fxml"));
            Parent root = loader.load();
            Send controllerEditBook = loader.getController(); //получаем контроллер для второй формы
            controllerEditBook.setDoc(doc); // передаем необходимые параметры
            controllerEditBook.setId(id_us);
            // controllerEditBook.setId_user(id_user);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Переслать документ");
            stage.show();
        }catch(SQLException | IOException  ex){
            System.err.println("Error!!!!!!!! "+ex);
        }


    }
}
