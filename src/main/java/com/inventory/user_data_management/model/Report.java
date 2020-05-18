package com.inventory.user_data_management.model;

import com.inventory.item_management.model.Item;
import com.inventory.item_management.model.Stock;
import com.inventory.user_management.model.Employee;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class Report {
    private Map<Employee, List<Item>> employeeToItemsMap;
    private Stock currentStock;
    private String reportCreationDate;

    public Report(Map<Employee, List<Item>> employeeToItemsMap, Stock currentStock) {
        this.employeeToItemsMap = employeeToItemsMap;
        this.currentStock = currentStock;
        Date date = new Date(); // This object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.reportCreationDate = formatter.format(date);
    }

    public Report(Stock currentStock){
        this.currentStock = currentStock;
    }


    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("stock", currentStock.getJson());
        JSONArray employeeAssignedItems = new JSONArray();
        for (Map.Entry<Employee, List<Item>> entry : employeeToItemsMap.entrySet()) {
            JSONObject employeeJson = new JSONObject(entry.getKey());
            employeeJson.put("items", entry.getValue());
            employeeAssignedItems.put(employeeJson);
        }
        jsonObject.put("assignedItems", employeeAssignedItems);
        return jsonObject;
    }


    public Map<Employee, List<Item>> getEmployeeToItemsMap() {
        return employeeToItemsMap;
    }

    public void setEmployeeToItemsMap(Map<Employee, List<Item>> employeeToItemsMap) {
        this.employeeToItemsMap = employeeToItemsMap;
    }

    public Stock getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Stock currentStock) {
        this.currentStock = currentStock;
    }

    public String getReportCreationDate() {
        return reportCreationDate;
    }

    public void setReportCreationDate(String reportCreationDate) {
        this.reportCreationDate = reportCreationDate;
    }
}
