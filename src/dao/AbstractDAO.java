/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * General class to work with DataBase
 *
 * @author ktw757057
 */
abstract class AbstractDAO {

    private String myDriver = "com.mysql.cj.jdbc.Driver";
    private String server = "localhost";
    private String dataBase = "testdb";
    private String myUrlDriver;
    private String user = "root";
    private String password = "vova060659";
    private Connection connection;

    protected AbstractDAO() {
        createUrlDriver(server, dataBase);
    }

    public String getDataBase() {
        return dataBase;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private void createUrlDriver(String server, String dB) {
        myUrlDriver = "jdbc:mysql://" + server + "/" + dataBase + "?serverTimezone=UTC";
        System.out.println("driver=" + myUrlDriver);
    }

    private Connection getConnection() {
        try {
            Class.forName(myDriver);
            connection = DriverManager.getConnection(myUrlDriver, user, password);
        } catch (Exception e) {
            System.out.println("error--" + e);
        }
        return connection;
    }

    protected Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    protected void connectionClose() throws SQLException {
        connection.close();
    }

}
