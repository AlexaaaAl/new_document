package models;

import java.sql.Date;
//пользователи
public class User {
    private String login;
    private int id_user;

    public User(String login, int id_user){
        this.login= login;
        this.id_user=id_user;
    }


    public User() {
    }
    //id
    public int getId_user() {

        return id_user;
    }
    public void setId_user(int id_user) {

        this.id_user = id_user;
    }
    //document
    public String getLogin() {

        return login;
    }

    public void setLogin(String login) {

        this.login = login;
    }
}
