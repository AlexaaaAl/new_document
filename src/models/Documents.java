package models;


public class Documents {
    private String path;
    private String document;


    public Documents(String path, String document) {

        this.path=path;
        this.document = document;

    }

    public Documents() {
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


}
