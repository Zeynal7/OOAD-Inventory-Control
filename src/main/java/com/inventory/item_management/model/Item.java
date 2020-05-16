package com.inventory.item_management.model;

import java.util.List;

public class Item {
    private int id;
    private String name;
    private String description;
    private String status;
    private String manufactureDate;
    private List<String> itemImageUrls;

    public Item(int id, String name, String description, String status, String manufactureDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.manufactureDate = manufactureDate;
    }

    public Item(int id, String name, String description, String status, String manufactureDate, List<String> itemImageUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.manufactureDate = manufactureDate;
        this.itemImageUrls = itemImageUrls;
    }

    public Item(int id, String name, String manufactureDate) {
        this.id = id;
        this.name = name;
        this.manufactureDate = manufactureDate;
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus() {
        this.status = status;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getItemImageUrls() {
        return itemImageUrls;
    }

    public void setItemImageUrls(List<String> itemImageUrls) {
        this.itemImageUrls = itemImageUrls;
    }
}
