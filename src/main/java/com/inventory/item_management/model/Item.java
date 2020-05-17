package com.inventory.item_management.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Item {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "LG Monitor 15\"")
    private String name;
    @Schema(example = "15 inch LG monitor")
    private String description;
    @Schema(example = "Out of stock")
    private String status;
    @Schema(example = "2020-05-11")
    private String manufactureDate;
    @Schema(example = "[\"lg-monitor-1.png\",\"lg-monitor-2.png\"]")
    private List<String> itemImageUrls;

    public Item(Integer id, String name, String description, String status, String manufactureDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.manufactureDate = manufactureDate;
    }

    public Item(Integer id, String name, String description, String status, String manufactureDate, List<String> itemImageUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.manufactureDate = manufactureDate;
        this.itemImageUrls = itemImageUrls;
    }

    public Item(Integer id, String name, String manufactureDate) {
        this.id = id;
        this.name = name;
        this.manufactureDate = manufactureDate;
    }

    public Item(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(){}

    public Item(Object[] row){
        this.id = Integer.parseInt(row[0].toString());
        this.name = Objects.toString(row[1].toString(), "");
        this.manufactureDate = Objects.toString(row[2].toString(), "N/A");
        this.status = Objects.toString(row[3].toString(), "N/A");
        this.description = Objects.toString(row[4].toString(), "N/A");
        String imageUrlsConcatted = Objects.toString(row[5], null);
        if (imageUrlsConcatted != null)
            this.itemImageUrls = Arrays.asList(imageUrlsConcatted.split(","));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public List<String> getItemImageUrls() {
        return itemImageUrls;
    }

    public void setItemImageUrls(List<String> itemImageUrls) {
        this.itemImageUrls = itemImageUrls;
    }
}
