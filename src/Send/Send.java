package Send;

import controller.ChangeDocumentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.Document;
import util.ConnectionUtil;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Send implements Initializable {
    @FXML
    private ChoiceBox recipient;
    @FXML
    private ListView list_rec;
    Connection connection;
    private Document doc;
    ObservableList<String> recip=FXCollections.observableArrayList();
    int id;
    String subject = "Новый документ\n";
    String from =  "adzulaj@gmail.com";
    String pass = "Alex_j_s_4";
    public int getId()
    {
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public Document getDoc(){
        return doc;
    }
    public void setDoc(Document doc){
        this.doc=doc;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recipient.setItems(addColumn());
    }
    public Send(){
            connection = ConnectionUtil.connectdb();
    }
    public ObservableList<String> addColumn(){
        ObservableList<String> langs = FXCollections.observableArrayList();
        try {
            ResultSet rs=connection.createStatement().executeQuery("SELECT LAST_NAME FROM users");
            while (rs.next()) {
                langs.add(rs.getString(1));
            }
            /*ResultSet rec=connection.createStatement().executeQuery("SELECT first_name FROM users WHERE id="+
                    Integer.parseInt(doc.getId_recipient()));*/
        } catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
        return langs;
    }

    public void SendDoc(ActionEvent actionEvent) {
        try{
            //File file = new File("output.txt");
           //FileReader fr = new FileReader(file);
            //BufferedReader reader = new BufferedReader(fr);
            //int id_us = Integer.parseInt(reader.readLine());
            while (!recip.isEmpty()) {
                String r = recip.get(0);
                recip.remove(0);
                ResultSet id_recipient = connection.prepareStatement("SELECT id From users where LAST_NAME='" +
                        r + "'").executeQuery();
                id_recipient.next();
                ResultSet e_mail = connection.prepareStatement("SELECT e_mail From users where LAST_NAME='" +
                        r + "'").executeQuery();
                e_mail.next();
                String[] to= {e_mail.getString(1)};
                String body= "Номер"+doc.getNumber()+"\nДокумент"+doc.getOutline();
                sendFromGMail(from, pass, to, subject, body);
                if (doc.getDate() != null) {
                    connection.createStatement().executeUpdate("INSERT INTO `documents`" +
                            "    ( `number`,`outline`,`path`, `document`, `id_sender`, `id_recipient`,`date`,`comments`)" +
                            "    VALUES" +
                            "           (" + doc.getNumber() + ",'" + doc.getOutline() + "','" +
                            doc.getPath() + "','" + doc.getDocument() + "'," +
                            id + "," +
                            id_recipient.getInt(1) + ",'" +
                            doc.getDate() + "','" + doc.getComments() + "')");
                    infoBox("Файл доставлен", "Информация", doc.getDocument());
                } else {
                    connection.createStatement().executeUpdate("INSERT INTO `documents`" +
                            "    ( `number`,`outline`,`path`, `document`, `id_sender`, `id_recipient`,`comments`)" +
                            "    VALUES" +
                            "           (" + doc.getNumber() + ",'" + doc.getOutline() + "','" +
                            doc.getPath() + "','" + doc.getDocument() + "'," +
                            id + "," +
                            id_recipient.getInt(1) + ",'" +
                            doc.getComments() + "')");
                    infoBox("Файл доставлен", "Информация", doc.getDocument());
                }
            }
        }catch(SQLException ex){
            System.out.println("Exept!!"+ex);
        }
    }
   /*public void SendDoc(ActionEvent actionEvent) {
       try {
           ResultSet id_recipient = connection.prepareStatement("SELECT id From users where LAST_NAME='" +
                   recipient.getValue() + "'").executeQuery();
           id_recipient.next();
           id_rec.setText(id_recipient.getString(1));
       }catch(SQLException sq){

       }
       id_d.setText(doc.getDocument());
   }*/
    //после сохранение новых данных высветиться это окно
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public void DeleteRec(ActionEvent actionEvent) {
        recip.remove(recipient.getValue().toString());
        list_rec.setItems(recip);
        while (recip.isEmpty()){
            recip.get(1);}
    }

    public void AddRec(ActionEvent actionEvent) {
        recip.add(recipient.getValue().toString());
        list_rec.setItems(recip);
    }
    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        ChangeDocumentController.SendDocumentToMessage(from, pass, to, subject, body);
    }
}
