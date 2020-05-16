package com.inventory.user_management.model;

import com.inventory.item_management.model.Item;

public class Employee extends User {
    private String department;
    private Employee supervisor;


    public Employee(int id, String fullname, String email, String department, Employee supervisor) {
        super(id, fullname, email);
        this.department = department;
        this.supervisor = supervisor;
    }

    public Employee(int id, String fullname, String department, Employee supervisor) {
        super(id, fullname);
        this.department = department;
        this.supervisor = supervisor;
    }

    public void request(Item item){
        //request for item
    }
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }
}
