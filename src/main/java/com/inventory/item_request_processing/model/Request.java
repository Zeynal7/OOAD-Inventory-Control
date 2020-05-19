package com.inventory.item_request_processing.model;

import com.inventory.helper.Enums;
import com.inventory.item_management.model.Item;
import com.inventory.user_management.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {
    @Schema(example = "1")
    private Integer id;
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
        this.status = Enums.ItemStatus.WAITING_APPROVAL.getStatus();
    }

    public Request(Integer id, Employee employee, Item item, String requestDate, String status) {
        this.id = id;
        this.employee = employee;
        this.item = item;
        this.requestDate = requestDate;
        this.status = status;
    }

    public Request(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getRequestDate() {
        if(requestDate == null || requestDate.isEmpty()){
            Date date = new Date(); // This object contains the current date value
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            return formatter.format(date);
        }
        return requestDate;
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
