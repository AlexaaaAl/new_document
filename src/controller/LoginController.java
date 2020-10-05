package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import util.ConnectionUtil;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private Label lblErrors;
    @FXML
    private TextField textEmail; //берем данные с формы - логин
    @FXML
    private PasswordField textPassword; //берем данные с формы - пароль
    @FXML
    private Button btnSignin;
    @FXML
    private ImageView image;
    Connection con = null;
    PreparedStatement preparedStatement = null; // для выполнения запросов
    ResultSet resultSet = null;

    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == btnSignin) {

            if (logIn().equals("Success")) {
                try {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu/FXMLMenu.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);//FXMLLoader.load(getClass().getResource("FXMLMenu.fxml"))
                    stage.setTitle( "Алиса");
                    stage.setMaximized(true);
                    stage.setResizable(true);
                    stage.setScene(scene);
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent t) {
                            Platform.exit();
                            System.exit(0);
                        }
                    });
                    stage.show();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {  //проверка подключения к бд
        // TODO
        this.image=new ImageView(new Image(getClass().getResourceAsStream("gerb.gif")));
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check"); //если подключения нет
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go"); //если подключения есть
        }
    }
    public LoginController() {
        con = ConnectionUtil.connectdb();
    }
    private String logIn() {
        String status = "Success";
        String email = textEmail.getText();
        String password = textPassword.getText();
        if(email.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
            status = "Error";
        } else {
            String sql = "SELECT * FROM log WHERE login=? AND password=?"; //запрос на логин
            try {
                preparedStatement = con.prepareStatement(sql); //выполнение запроса
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    /*infoBox("Please enter correct Email and Password", null, "Failed"); //или это*/
                    setLblError(Color.TOMATO, "Enter Correct Email/Password"); //или это
                    status = "Error";
                } else {
                    try {
                        PrintWriter writers = new PrintWriter("output.txt", "UTF-8");
                        writers.println(email);
                        writers.close();
                    }
                    catch (IOException exception){
                        //System.err.println(exception.getMessage());
                    }
                    //infoBox("Добро пожаловать "+email,null,"Success" );
                    setLblError(Color.GREEN, "Login Successful..Redirecting..");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        return status;
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }

}

