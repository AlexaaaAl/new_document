package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
                data.add(new Documents(rs.getString(2), rs.getString(3)));
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
}
