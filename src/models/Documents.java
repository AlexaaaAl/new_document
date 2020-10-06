package models;


public class Documents {
    private int id;
    private String path;
    private String document;


    public Documents(int id,String path, String document) {
        this.id = id;
        this.path=path;
        this.document = document;

    }

    public Documents() {
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
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
