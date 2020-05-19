package com.inventory.user_management.model;

import java.util.Objects;

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

    public Employee(int id, String fullname, String department) {
        super(id, fullname);
        this.department = department;
    }

    public Employee(int id){
        super(id, "");
    }

    public Employee(){
    }

    public Employee(Object[] row){
        super(Integer.parseInt(row[0].toString()),
                Objects.toString(row[1].toString(), ""),
                Objects.toString(row[2].toString(), ""));
        this.department = Objects.toString(row[3].toString(), "");
        int supervisorId = Integer.parseInt(Objects.toString(row[4].toString(), "0"));
        this.supervisor = new Employee(supervisorId);
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
