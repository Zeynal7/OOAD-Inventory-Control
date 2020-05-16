package com.inventory.user_management.model;

import com.inventory.item_management.model.Stock;
import com.inventory.user_data_management.model.Report;

import java.util.List;

public abstract class Person {
    private int id;
    private String fullname;
    private String email;

    public Person(int id, String fullname, String email) {
        this(id, fullname);
        this.email = email;
    }

    public Person(int id, String fullname) {
        this.id = id;
        this.fullname = fullname;
    }
//
//    public Stock getAvailableItems()
//        /// TODO: FIX
//        return Stock();
//    }


    public Report createReport(List<Employee> employeeList){
        // Get Report of items From DB
        return new Report();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
