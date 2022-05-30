package com.example.contact_manager_new.db.entity;

public class Contact {

    //1-constants for Database
    public static final String TABLE_NAME="contacts";
    public static final String COLUMN_ID="contact_id";
    public static final String COLUMN_NAME="column_name";
    public static final  String COLUMN_EMAIL="contact_email";

    //2-variables
    private String name;
    private String email;
    private int id;

    //3-constructors
    public Contact(){

    }

    public Contact(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //SQL QUERY: creating the table

    public static final String CREATE_TABLE=
            "CREATE TABLE"+TABLE_NAME+"("+COLUMN_ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_NAME+"TEXT,"+
                    COLUMN_EMAIL+"DATETIME DEFAULT CURRENT_TIMESTAMP"+")";
}
