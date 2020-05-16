package com.inventory.helper;

import java.io.File;

public class Enums {

    private static final String mainFolder = "ada_mobile";

    public enum Credentials {
        // Local - For testing from local computer
        URL("jdbc:mysql://localhost:3307/testezz?characterEncoding=utf8"),
        USER("username"),
        PASSWORD("password");

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

}