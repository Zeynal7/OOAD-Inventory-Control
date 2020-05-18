package com.inventory.helper;

import java.io.File;

public class Enums {

    private static final String mainFolder = "inventory_control";

    public enum Credentials {
        // Local - For testing from local computer
        //URL("jdbc:mysql://localhost:3307/inventory_control?characterEncoding=utf8"),
        // Global
        URL("jdbc:mysql://31.171.108.153:3306/inventory_control?characterEncoding=utf8"),
        USER("test_user"),
        PASSWORD("R2iCH8HK3V!jrmfZ");

        private final String data;

        Credentials(String data) {
            this.data = data;
        }

        public String data() {
            return data;
        }
    }


    public enum FilePath {
        WINDOWS("C:\\"),
        MAC("/Users/zeynal/"),
        LINUX("/home/ada_app/");

        private final String path;

        FilePath(String path) {
            this.path = path;
        }

        public String path(FileLocation location) {
            return path + location.location() + File.separatorChar;
        }
    }


    public enum FileLocation {
        PROFILE_PHOTOS("profile_photos"),
        ITEM_IMAGES("item_images");

        private final String location;


        FileLocation(String location) {
            this.location = location;
        }

        String location() {
            return mainFolder + File.separatorChar + location;
        }
    }

    public enum ItemStatus {
        AVAILABLE("Available"),
        OUT_OF_STOCK("Out of Stock"),
        WAITING_DELIVERY("Waiting Delivery"),
        DELIVERED("Delivered"), // Delivered By Courier
        REJECTED("Rejected"), // Rejected By Admin
        WAITING_APPROVAL("Waiting Approval"),// Approval Of Admin
        APPROVED("Approved");// Approval Of Admin

        private final String status;


        ItemStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public static ItemStatus valueFromString(String status){
            switch (status.toLowerCase()){
                case "available":        return AVAILABLE;
                case "out of stock":     return OUT_OF_STOCK;
                case "waiting delivery": return WAITING_DELIVERY;
                case "delivered":        return DELIVERED;
                case "rejected":         return REJECTED;
                case "waiting approval": return WAITING_APPROVAL;
                case "approved":         return APPROVED;
                default:                 return AVAILABLE;

            }
        }
    }


    public enum ReportType {
        EMPLOYEE(1),
        MANAGER(2),
        COURIER(3),
        ADMIN(4);

        private final int type;


        ReportType(int type) {
            this.type = type;
        }

        public int getRawValue() {
            return type;
        }

        public static ReportType valueFromInt(int type) throws ApiException {
            switch (type){
                case 1:        return EMPLOYEE;
                case 2:        return MANAGER;
                case 3:        return COURIER;
                case 4:        return ADMIN;
            }
            throw new ApiException(ApiException.ExceptionCodes.UNKNOWN);
        }
    }



}