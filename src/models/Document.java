package models;

import java.sql.Date;
//класс документ с информацией о папке с документом , отправителем, полусчателем и т.д.
public class Document {
    private int id_document;
    private int number;
    private String path;
    private String document;
    private String id_sender;
    private String id_recipient;
    private String outline;
    private String comments;
    private String status;
    private String document_type;
    private Date date;
    private Date date_added;



    public Document(int id_document, int number,/*,String path, /*String document,*/ String id_sender,String id_recipient,
                    String outline,String comments,Date date,Date date_added, String status,String document_type) {
        this.id_document = id_document;
        this.number = number;
        //this.path=path;
        //this.document = document;
        this.id_sender = id_sender;
        this.id_recipient=id_recipient;
        this.outline=outline;
        this.comments=comments;
        this.date_added=date_added;
        this.date=date;
        this.status=status;
        this.document_type=document_type;
    }

    public Document() {
    }
    //id
    public int getId_document() {

        return id_document;
    }
    //number
    public int getNumber() {

        return number;
    }

    public void setNumber(int number) {

        this.number = number;
    }
    //папка
    public String getPath(){
       return path;
    }

    public void setPath(String path){
        this.path=path;
    }
    //document
    public String getDocument() {

        return document;
    }

    public void setDocument(String document) {

        this.document = document;
    }
    //sender
    public String getId_sender() {

        return id_sender;
    }

    public void setId_sender(String id_sender) {

        this.id_sender = id_sender;
    }
    //recipient
    public String getId_recipient() {

        return id_recipient;
    }
    //outline
    public String getOutline(){
        return outline;
    }

    public void setOutline(String outline){
        this.outline=outline;
    }

    public void setId_recipient (String id_recipient) {

        this.id_recipient=id_recipient;
    }
    //comments
    public String getComments() {

        return comments;
    }

    public void setComments (String comments) {

        this.comments = comments;
    }
    //status
    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }
    //document_type
    public String getDocument_type() {

        return document_type;
    }

    public void setDocument_type(String document_type) {

        this.document_type = document_type;
    }
    //date
    public Date getDate() {

        return date;
    }

    public void setDate(Date date) {

        this.date = date;
    }
    //date
    public Date getDate_added() {

        return date_added;
    }

    public void setDate_added(Date date_added) {

        this.date_added = date_added;
    }


   // @Override
   /* public String toString()
    {
        return "ClassUser [id = "+id+", login = "+login+", password = "+password+"]";
    }*/

}
