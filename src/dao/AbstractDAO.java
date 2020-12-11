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
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * General class to work with DataBase
 * @author ktw757057
 */
abstract class AbstractDAO {

    protected final Logger log = Logger.getLogger(this.getClass());
    private String myDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String server = mon.MainMon.getMainServerName();
    private String dataBase = mon.MainMon.getMainDatabaseName();
    private String myUrlDriver;
    private String user;
    private String password;
    private Connection connection;

    protected AbstractDAO(HttpSession session) {
        user = (String) session.getAttribute("j_username");
        password = (String) session.getAttribute("j_password");
        createUrlDriver(server, dataBase);
    }

    public Logger getLog() {
        return log;
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
        myUrlDriver = "jdbc:sqlserver://" + server + ";database=" + dB;
        System.out.println("driver=" + myUrlDriver);
        //private String myUrl = "jdbc:sqlserver://o757;database=TDB1";
    }

    private Connection getConnection() {
        try {
            Class.forName(myDriver);
            connection = DriverManager.getConnection(myUrlDriver, user, password);
        } catch (Exception e) {
            log.info("error--" + e);
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
