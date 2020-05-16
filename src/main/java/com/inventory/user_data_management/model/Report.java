package com.inventory.user_data_management.model;

import com.inventory.item_management.model.Item;
import com.inventory.item_management.model.Stock;
import com.inventory.user_management.model.Employee;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
