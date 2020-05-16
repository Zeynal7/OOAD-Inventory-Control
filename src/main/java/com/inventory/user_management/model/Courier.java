package com.inventory.user_management.model;

import com.inventory.item_management.model.Item;

public class Courier extends Person {

    public Courier(int id, String fullname, String email) {
        super(id, fullname, email);
    }

    public Courier(int id, String fullname) {
        super(id, fullname);
    }

    public void deliver(Item item, Employee employee){
        // Deliver item to the employee
    }
}
