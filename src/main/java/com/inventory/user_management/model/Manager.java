package com.inventory.user_management.model;

import com.inventory.item_management.model.Item;

public class Manager extends User {


    public Manager(int id, String fullname, String email) {
        super(id, fullname, email);
    }

    public Manager(User user) {
        super(user.getId(), user.getFullname(), user.getEmail());
    }

    public void assign(Item item, Employee employee) throws Exception {
        // Assign item to employee in DB
    }

    public void addToStock(Item item){
        // Add/Update item to DB
    }
}
