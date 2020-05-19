package com.inventory.user_management.model;

public class User extends Person{


    public User(int id, String fullname, String email) {
        super(id, fullname, email);
    }

    public User(int id, String fullname) {
        super(id, fullname);
    }

    public User(){}
}
