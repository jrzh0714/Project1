package com.ex.ers.connection;

import org.postgresql.Driver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgreSQLDBConnector implements DBConnector{
    private String username;
    private String password;
    private String url;

    private static Properties props = new Properties();

    static{
        try {
            props.load(ClassLoader.getSystemClassLoader().getResourceAsStream("db.properties"));


        } catch (FileNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException("db.properties not found",e);
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("couldnt parse db.properties",e);
        }  catch (ClassCastException e){
            throw new RuntimeException("Driver class is not java.sql.Driver",e);
        }
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public PostgreSQLDBConnector() {
        this.username= props.getProperty("db.username");
        this.password= props.getProperty("db.password");
        this.url= props.getProperty("db.url");
    }

    @Override
    public Connection newConnection(String url, String username, String password) throws SQLException{
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Connection newConnection() throws SQLException{
        return this.newConnection(this.url, this.username, this.password);

    }
}

