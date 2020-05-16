package com.inventory.user_management.model;

import com.inventory.item_request_processing.model.Request;

public class Admin extends Person {

    public Admin(int id, String fullname, String email) {
        super(id, fullname, email);
    }

    public void assignManager(User user){
        Manager manager = new Manager(user);
        // Add manager to DB
    }

    public void add(User user){
        // Add user to DB
    }
    public void verifyRequest(Request request){
        //Verify request for item
    }
}
