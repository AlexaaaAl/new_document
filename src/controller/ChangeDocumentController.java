package controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Document;
import org.apache.commons.io.FileUtils;
import util.ConnectionUtil;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ChangeDocumentController implements Initializable {
    @FXML
    private TextField document;
    @FXML
    private TextField path_doc;
    @FXML
    private TextArea comments;
    @FXML
    private TextField outline;
    @FXML
    private DatePicker date;
    @FXML
    private Button choose_doc;
    @FXML
    private Button btnSave;
    @FXML
    private CheckBox chdate;


    PreparedStatement preparedStatement;
    Connection connection;
    File file=null;
    String name= null;
    LocalDate disabledate= LocalDate.now();
    String subject = "Изменение документа\n";
    String from =  "adzulaj@gmail.com";
    String pass = "Alex_j_s_4";
    private ObservableList<Document> data;
    private Document documents;
    int id_document;
    int id_us;
    public int id ;
    boolean flag=false;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public ChangeDocumentController () {
        connection = ConnectionUtil.connectdb();
    }
    public void initialize(URL url, ResourceBundle rb) {

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
        try {
            data = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery("SELECT doc.id_document,doc.number," +
                    " us.FIRST_NAME , us1.FIRST_NAME," +
                    " doc.comments,doc.date,doc.status,doc.outline,doc.date_added,doc.document_type" +
                    " FROM documents doc" +
                    " INNER JOIN users us" +
                    " on doc.id_sender = us.ID" +
                    " INNER JOIN users us1" +
                    " on doc.id_recipient = us1.ID WHERE doc.id_document=" +
                    id_document);
            while (rs.next()) {
                data.add(new Document(rs.getInt(1), rs.getInt(2),
                        rs.getString(3), rs.getString(4), rs.getString(8),
                        rs.getString(5), rs.getDate(6),
                        rs.getDate(9), rs.getString(7),
                        rs.getString(10)));
            }
            documents = data.get(0);
            document.setText( documents.getDocument());
            path_doc.setText(documents.getPath() );
            outline.setText(documents.getOutline());
            comments.setText(documents.getComments());

            if (documents.getDate() != null) {
                LocalDate d = documents.getDate().toLocalDate();
                date.setValue(d);
                chdate.setSelected(true);
            }


        } catch (SQLException ex) {
            System.err.println("Error!!!!!!!! " + ex);
        }
        //view();
    }
    public void ButtonActionChoose(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выбор документа");
        file = chooser.showOpenDialog(new Stage());
        path_doc.setText(file.getPath());
        document.setText(file.getName());
        flag=true;

    }
    public void SaveAction(MouseEvent event) {
        showConfirmationChange(id_document);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        //написать апдейт для документа
        //при изменении документа сохранять в папку с ip из таблицы и отдел +год
        //ЗАМЕНИТЬ ФАЙЛ да или нет?????????????
    }
    private void showConfirmationChange(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Изменение файла");
        alert.setHeaderText("Вы точно хотите изменить документ ?");
        // option != null.
        ResultSet ip_server = null;
        ResultSet id_recip = null;
        ResultSet departament = null;
        String output=documents.getPath();
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            //this.label.setText("No selection!");
        } else if (option.get() == ButtonType.OK) {
            try {
                /*departament = connection.prepareStatement("SELECT DEPARTMENT From users where LAST_NAME='" +
                        user.getValue().toString() + "'").executeQuery();
                ip_server = connection.prepareStatement("SELECT ip_server From users where LAST_NAME='" +
                        user.getValue().toString() + "'").executeQuery();*/
                id_recip = connection.prepareStatement("SELECT id_recipient From documents where id_document=" +
                        id ).executeQuery();
                /*departament.next();
                ip_server.next();*/

                id_recip.next();
            }catch (SQLException ex) {
                System.err.println("Error!!!!  "+ex);
            }
            try{
                //добавление файла на сервер
                /*String input = documents.getPath() + documents.getDocument();
                if (flag) {
                    input = path_doc.getText(); //путь к загружаемому файлу

                    output= "//"+ip_server.getString(1)+"/сетевая/server/"+
                            departament.getString(1)+"/"+LocalDate.now().getYear();//папка вывода
                    sendFile(new File(input),new File(output));
                }*/


                if (chdate.isSelected()==true) {
                    LocalDate i = date.getValue();
                    java.sql.Date sqlDate =java.sql.Date.valueOf(i);
                    connection.createStatement().executeUpdate("UPDATE documents " +
                            /*"set document='" + document.getText() + "'," +
                            "path='" + output + "'," +*/
                            "set outline='" + outline.getText() + "'," +
                            "date='" + sqlDate + "'," +
                            "comments='" + comments.getText() + "'" +
                            "where id_document=" + id_document);
                    ResultSet e_mail = connection.prepareStatement("SELECT E_MAIL From users where id=" +
                            id_recip.getInt(1)).executeQuery();
                    e_mail.next();
                    ResultSet numb = connection.prepareStatement("SELECT number From documents where  id_document=" +
                            id_document).executeQuery();
                    numb.next();
                    String[] to = {e_mail.getString(1)};
                    String body = "Номер документа: "+ numb.getString(1)+ "\nДокумент: "+document.getText();
                    SendDocumentToMessage(from, pass, to, subject, body);
                }else{
                    connection.createStatement().executeUpdate("UPDATE documents " +
                            /*"set document='" + document.getText() + "'," +
                            "path='" + output + "'," +*/

                            "set outline='" + outline.getText() + "'," +
                            "comments='" + comments.getText() + "'" +
                            "where id_document=" + id_document);
                    ResultSet e_mail = connection.prepareStatement("SELECT E_MAIL From users where id=" +
                            id_recip.getInt(1)).executeQuery();
                    e_mail.next();
                    ResultSet numb = connection.prepareStatement("SELECT number From documents where  id_document=" +
                            id_document).executeQuery();
                    numb.next();
                    String[] to = {e_mail.getString(1)};
                    String body = "Номер документа: "+ numb.getString(1)+ "\nДокумент: "+document.getText();
                    SendDocumentToMessage(from, pass, to, subject, body);

                }
            }catch (SQLException /*| IOException*/ ex) {
                System.err.println("Errorgui  "+ex);
            }
        } else if (option.get() == ButtonType.CANCEL) {
            //this.label.setText("Cancelled!");
        } else {
            //this.label.setText("-");
        }

    }
    public void sendFile(File input,File output) throws IOException {
        FileUtils.copyFileToDirectory(input,
                output);
    }

    public static void SendDocumentToMessage(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //choisebox для выбора фамилии пользователя
    public ObservableList<String> addColumn(){
        ObservableList<String> langs = FXCollections.observableArrayList();
        try {
            ResultSet rs=connection.createStatement().executeQuery("SELECT LAST_NAME FROM users");
            while (rs.next()) {
                langs.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            System.err.println("Error!!!!!!!! "+ex);
        }
        return langs;
    }
}
