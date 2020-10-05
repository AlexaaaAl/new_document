package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));//прогрузка формы
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Алиса");
        stage.getIcons().add(
                new Image(Main.class.getResourceAsStream( "gerb.gif" )));
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
        Main.class.getClassLoader().getResource("src/main/image/gerb.gif");

    }
}
