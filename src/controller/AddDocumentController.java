package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Document;
import models.Documents;
import org.apache.commons.io.FileUtils;
import util.ConnectionUtil;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ResourceBundle;


public class AddDocumentController implements Initializable {

    @FXML
    private TextArea comments;
    @FXML
    private TextField outline;
    @FXML
    private ChoiceBox user;
    @FXML
    private DatePicker date;
    @FXML
    private Button choose_doc;
    @FXML
    private Button btnSave;
    @FXML
    private CheckBox chdate;
    @FXML
    private ListView list_v;
    @FXML
    private ListView document_path;
    @FXML
    private ListView document_name;
    @FXML
    private Label yy;
    @FXML
    private TableColumn<Documents,String> tblPath;
    @FXML
    private TableColumn<Documents,String> tblName;
    @FXML
    private ChoiceBox document_type;

    ObservableList<String> recipient=FXCollections.observableArrayList();
    ObservableList<String> path=FXCollections.observableArrayList();
    ObservableList<String> path_send=FXCollections.observableArrayList();
    ObservableList<String> doc_name=FXCollections.observableArrayList();
    ObservableList<Integer> id_file_user=FXCollections.observableArrayList();
    String input ="";
    String output ="";
    PreparedStatement preparedStatement;
    Connection connection;
    File file=null;
    String name= null;
    LocalDate disabledate= LocalDate.now();
    String from =  "adzulaj@gmail.com";
    String pass = "Alex_j_s_4";
    int id_f=0;
    @Override
    public void initialize(URL url, ResourceBundle rb) { //инициализация сцены
        // TODO
        comments.setWrapText(true);

        date.setShowWeekNumbers(true);
       try{
           File file = new File("output.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            name=line;
            while (line != null) {
                line = reader.readLine();
            }
       } catch (FileNotFoundException fileNotFoundException) {
           fileNotFoundException.printStackTrace();
       } catch (IOException exception) {
           exception.printStackTrace();
       }
       // lbl.setText("line");
        user.setItems(addColumn());
        date.setValue(LocalDate.now());
        date.setShowWeekNumbers(true);
    }

    //при открытии окна выбирает все данные из бд
    public AddDocumentController() {
        connection = ConnectionUtil.connectdb();
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


    //кнопка сохранения новых данных
    public void SaveAction(MouseEvent event) { //кнопка сохранения
        try {
            java.sql.Date d=null;
            ResultSet num=connection.prepareStatement("SELECT max(number) " +
                    "from documents;").executeQuery();
            num.next();
            ResultSet id_file=connection.prepareStatement("SELECT max(id) " +
                    "from document_file;").executeQuery();
            id_file.next();
            if ( id_file!=null){
                id_f=id_file.getInt(1)+1;
            }
            //находим id пользователя который зашел в программу
            ResultSet id_user = connection.prepareStatement("SELECT id_user from log WHERE login='" +
                    name + "'").executeQuery();
            id_user.next();
            //полное имя пользователя
            ResultSet user_name = connection.prepareStatement("SELECT Last_name,First_name,Middle_name " +
                    "from users WHERE id=" +
                    id_user.getInt(1)).executeQuery();
            user_name.next();
            //отдел в котором работает пользователь
            ResultSet DEPARTMENT = connection.prepareStatement("SELECT DEPARTMENT From users where id='" +
                    id_user.getInt(1) + "'").executeQuery();
            DEPARTMENT.next();
            //занесение данных в базу и в на сервер + отправка уведомления получателю
            while (!recipient.isEmpty()) {
                //данные получателя (одного)
                String r = recipient.get(0);
                recipient.remove(0);
                ResultSet id_recip = connection.prepareStatement("SELECT id From users where LAST_NAME='" +
                        /*user.getValue().toString()*/r + "'").executeQuery();
                ResultSet ip_server = connection.prepareStatement("SELECT ip_server From users where LAST_NAME='" +
                        /*user.getValue().toString()*/r + "'").executeQuery();
                ResultSet e_mail = connection.prepareStatement("SELECT E_MAIL From users where LAST_NAME='" +
                        /*user.getValue().toString()*/r + "'").executeQuery();
                e_mail.next();
                id_recip.next();
                ip_server.next();
                //добавляем номер к документу
                int numb = num.getInt(1) + 1;
                //почта получателя
                //добавление файла на сервер
                while(!path.isEmpty()) {
                     input = path.get(0); //путь к загружаемому файлу
                     output = "//" + ip_server.getString(1) + "/Программа/" +
                             DEPARTMENT.getString(1) + "/" +
                             user_name.getString(1) + " " +
                             user_name.getString(2) + " " +
                             user_name.getString(3) + "/" + LocalDate.now();//папка вывода
                    sendFile(new File(input), new File(output));
                    //path_send.add(path.get(0));

                    connection.createStatement().executeUpdate("INSERT INTO `document_file`" +
                            "    (`id` ,`path`, `file`)" + "    VALUES ("+id_f+",'" + output + "','" + doc_name.get(0) + "')");
                    path.remove(0);
                    doc_name.remove(0);
                    id_file_user.add(id_f);
                    id_f+=1;

                }
               /* while(!path_send.isEmpty()){
                    String p=path_send.get(0);

                }*/
                if (chdate.isSelected() == true) { //если выбрали ГАЛАЧКУ то добавим дату в таблицу
                    LocalDate i = date.getValue();
                    java.sql.Date sqlDate = java.sql.Date.valueOf(i);
                    connection.createStatement().executeUpdate("INSERT INTO `documents`" +
                            "    ( `number`,`outline`, `id_sender`, `id_recipient`,`date`,`comments`,`document_type`)" +
                            "    VALUES" +
                            "           (" + numb + ",'" + outline.getText() + "'," +
                            id_user.getInt(1) + "," +
                            id_recip.getInt(1) + ",'" +
                            sqlDate + "','" + comments.getText()  + "','"+
                            document_type.getValue().toString()+"')");
                    String[] to = {e_mail.getString(1)};
                    String subject = "Вам отправили документ\n";
                    String body = "Номер документа: "+ Integer.toString(numb);
                    //отправка письма
                    sendFromGMail(from, pass, to, subject, body);
                } else { //если не выбрали ГАЛАЧКУ то не добавим дату в таблицу
                    connection.createStatement().executeUpdate("INSERT INTO `documents`" +
                            "    ( `number`,`outline`, `id_sender`, `id_recipient`,`comments`,`document_type`)" +
                            "    VALUES" +
                            "           (" + numb + ",'" + outline.getText() + "'," +
                            id_user.getInt(1) + "," +
                            id_recip.getInt(1) + ",'" +
                            comments.getText() + "','"+
                            document_type.getValue().toString()+"')");
                    String[] to = {e_mail.getString(1)};
                    String subject = "Вам отправили документ\n";
                    String body = "Номер документа: "+ Integer.toString(numb);
                    //отправка письма
                    sendFromGMail(from, pass, to, subject, body);
                }

            }
            while(!id_file_user.isEmpty()){
                connection.createStatement().executeUpdate("INSERT INTO `all_one`" +
                        "    (`id_doc`, `id_file`)" + "    VALUES ("
                        + num.getInt(1) +","+id_file_user.get(0)+")");
                id_file_user.remove(0);
            }
            infoBox("Документ добавлен! ",null,"Success" );
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            //stage.setMaximized(true);
            stage.close();
            
        } catch (SQLException | IOException throwables) {
            System.err.println("Error!!!!!!!! "+throwables);
            throwables.printStackTrace();
        }
     }
    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        ChangeDocumentController.SendDocumentToMessage(from, pass, to, subject, body);
    }
    public void sendFile(File input,File output) throws IOException {
        FileUtils.copyFileToDirectory(input,
                output);
    }
    //кнопка выбора документа
    public void ButtonAction(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выбор документа");
        file = chooser.showOpenDialog(new Stage());
        path.add(file.getPath());
        doc_name.add(file.getName());
        //tblName.setCellValueFactory(); ;
        document_path.setItems(path);
        document_name.setItems(doc_name);
    }
    //после сохранение новых данных высветиться это окно
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public void DeleteRec(ActionEvent actionEvent) {
        recipient.remove(user.getValue().toString());
        list_v.setItems(recipient);
        while (recipient.isEmpty()){
            recipient.get(1);}
    }

    public void AddRec(ActionEvent actionEvent) {
        recipient.add(user.getValue().toString());
        list_v.setItems(recipient);
    }

    public void DeleteFile(MouseEvent mouseEvent) {
        document_name.setOnTouchPressed(document_path.getOnTouchPressed());
        document_name.setCursor(document_path.getCursor());
        ObservableList<String> u=document_path.getSelectionModel().getSelectedItems();
        ObservableList<String> r=document_name.getSelectionModel().getSelectedItems();
        /*document_name.set
                document_path.getEditingIndex();*/
        yy.setText(u.get(0));
        path.remove(yy.getText());
        File files=new File(yy.getText());
        doc_name.remove(files.getName());
        document_path.setItems(path);
        document_name.setItems(doc_name);
       /* while (path.isEmpty()){
            path.get(1);
        }*/
        String message="";
        for (String m: u){
            message+=m;
        }
        System.out.println(message);

    }
}
