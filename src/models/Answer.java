package models;

import java.sql.Date;
//класс для документа-ответа..
public class Answer {
    private int id_doc;
    private String path;
    private String document;
    private Date date;

    public Answer(  int id_doc, String path, String document, Date date){
        this.id_doc=id_doc;
        this.path=path;
        this.document=document;
        this.date=date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId_doc() {
        return id_doc;
    }

    public void setId_doc(int id_doc) {
        this.id_doc = id_doc;
    }

    public String getDocument() {
        return document;
    }

    public String getPath() {
        return path;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
