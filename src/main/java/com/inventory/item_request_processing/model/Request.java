package com.inventory.item_request_processing.model;

import com.inventory.item_management.model.Item;
import com.inventory.user_management.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {
    @Schema(example = "1")
    private int id;
    private Employee employee;
    private Item item;
    @Schema(example = "2020-05-11")
    private String requestDate;
    @Schema(example = "Waiting Delivery")
    private String status;

    public Request(Employee employee, Item item) {
        this.employee = employee;
        this.item = item;
        Date date = new Date(); // This object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.requestDate = formatter.format(date);
    }

    public Employee getEmployee() {
        return employee;
    }

    public Item getItem() {
        return item;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String status() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
