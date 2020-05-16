package com.inventory.helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DbConnection {

    public static ArrayList<Object[]> selectFromDB(String query, Object[] parameters) throws Exception {
        System.out.println("Requested Query");
        System.out.println(query);
        System.out.println("Params");
        System.out.println(Arrays.toString(parameters));
        ArrayList<Object[]> result = new ArrayList<>();

        // Connecting to Database
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(Enums.Credentials.URL.data(), Enums.Credentials.USER.data(), Enums.Credentials.PASSWORD.data());
        PreparedStatement stmt = conn.prepareStatement(query);

        for (int i=0; i<parameters.length; i++) {
            stmt.setObject(i+1, parameters[i]);
        }

        // Executing Query and getting Column Count
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();


        // Adding Row Data to an Object Array
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                rowData[i] = rs.getString(i + 1);
            }
            result.add(rowData);
        }

        // Closing statement and connection
        try {
            stmt.close();
        } catch (SQLException ignored) {
        }

        try {
            conn.close();
        } catch (SQLException ignored) {
        }

        return result;
    }


    // Number of parameters is required in case there are multiple insert/update statements.
    // When there are multiple requests, with same query but different params, we need number of params, to divide them
    // within query.
    public static void insertUpdateToDB(String query, ArrayList<Object> parametersArray, int numberOfParameters) throws Exception {
        System.out.println("Requested Query");
        System.out.println(query);
        System.out.println("Params");
        System.out.println(parametersArray);

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(Enums.Credentials.URL.data(), Enums.Credentials.USER.data(), Enums.Credentials.PASSWORD.data());

        PreparedStatement stmt = conn.prepareStatement(query);

        // Adding query and its parameters to batch
        if(numberOfParameters != 0)
            for(int i = 0; i < parametersArray.size()/numberOfParameters; i++){
                for(int j=0; j<numberOfParameters; j++) {
                    stmt.setObject(j + 1, parametersArray.get( i * numberOfParameters + j));
                }
                stmt.addBatch();
            }

        if (stmt != null) {
            stmt.executeBatch();
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ignored) {
        }

        try {
            conn.close();
        } catch (SQLException ignored) {
        }


    }

    public static void directQuery(String query) throws Exception{
        System.out.println("Requested Direct Query");
        System.out.println(query);

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(Enums.Credentials.URL.data(), Enums.Credentials.USER.data(), Enums.Credentials.PASSWORD.data());

        Statement stmt = conn.createStatement();

        stmt.execute(query);

        try {
            stmt.close();
        } catch (SQLException ignored) {
        }

        try {
            conn.close();
        } catch (SQLException ignored) {
        }


    }

}
