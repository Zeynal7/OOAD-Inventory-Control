package com.inventory.user_management.model;

import com.inventory.item_management.model.Stock;

import static com.inventory.item_management.ItemResources.getCurrentStock;

public abstract class Person {
    private int id;
    private String fullName;
    private String email;

    public Person(int id, String fullName, String email) {
        this(id, fullName);
        this.email = email;
    }

    public Person(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Stock getAvailableItems(){
        try {
            return getCurrentStock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Stock();
    }


//    public Report createReport(List<Employee> employeeList){
//        // Get Report of items From DB
//        return new Report();
//    }
//// TODO : FIX

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
