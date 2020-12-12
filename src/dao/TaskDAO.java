/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class with methods to get information from DataBase
 *
 * @author ktw757057
 */
public class TaskDAO extends AbstractDAO {

    public TaskDAO() {
        super();
    }

    //запрос для получения одной колонки из таблицы
    public ArrayList<String> getOneColumnFromDB(String columnName) {
        String sqlQuery = "SELECT " + columnName + " FROM `san_content_999_calculated`";
        ArrayList<String> returnColumn = null;
        try {
            //create connection and statement to work with DB
            Statement st = getStatement();
            // execute the query
            ResultSet resultSet = st.executeQuery(sqlQuery);
            returnColumn = new ArrayList<String>();
            while (resultSet.next()) {
                String ww = resultSet.getString(columnName);
                returnColumn.add(ww);
            }
        } catch (Exception e) {
            System.out.println("error--" + e);
        }
        try {
            connectionClose();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnColumn;
    }

}
