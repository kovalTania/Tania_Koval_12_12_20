/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Class with methods to get information from DataBase
 *
 * @author ktw757057
 */
public class TableDAO extends AbstractDAO {

    public TableDAO(HttpSession session) {
        super(session);
    }

    //запрос для получения одной колонки из таблицы
    public ArrayList<String> getOneColumnFromDB(String sqlQuery, String columnName) throws SQLException {
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
            log.info("error--" + e);
        }
        connectionClose();
        return returnColumn;
    }

    //запрос для получения одного результата с таблицы (1 колонка, 1 строка)
    public String getOneResult(String sqlQuery, String columnName) throws SQLException {
        String result = null;
        try {
            //create connection and statement to work with DB
            Statement st = getStatement();
            // execute the query
            ResultSet resultSet = st.executeQuery(sqlQuery);
            while (resultSet.next()) {
                result = resultSet.getString(columnName);
            }
        } catch (Exception e) {
            log.info("error--" + e);
        }
        connectionClose();
        return result;
    }

    /**
     * метод для получения таблицы данных из БД
     *
     * @param sqlQuery - запрос типа селект
     * @param paramNames - список имен столбцов
     * @return - LinkedHashMap<Название столбца, ArrayList<Данные С БД>> @throws
     * SQLException
     */
    public LinkedHashMap<String, ArrayList<String>> getDataFromTable(String sqlQuery, List<String> paramNames) throws SQLException {
        LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<String, ArrayList<String>>();
        ResultSet resultSet = getDataTable(sqlQuery);
        try {
            while (resultSet.next()) {
                String param;
                for (int i = 0; i < paramNames.size(); i++) {
                    if (!result.containsKey(paramNames.get(i))) {
                        result.put(paramNames.get(i), new ArrayList<String>());
                    }
                    param = resultSet.getString(paramNames.get(i));
                    result.get(result.get(paramNames.get(i)).add(param));
                }
            }
        } catch (Exception e) {
            log.info("error--" + e);
        }
        connectionClose();
        return result;
    }

    /**
     * метод для получения таблицы данных из БД с автоматическим получаем имена
     * столбцов с запроса
     *
     * @param sqlQuery - запрос типа селект
     * @return - LinkedHashMap<Название столбца, ArrayList<Данные С БД>> @throws
     * SQLException
     */
    public LinkedHashMap<String, ArrayList<String>> getDataFromTable(String sqlQuery) throws SQLException {
        LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<String, ArrayList<String>>();
        ResultSet resultSet = getDataTable(sqlQuery);
        try {
            //получаем имена столбцов и записываем в лист
            ResultSetMetaData meta = resultSet.getMetaData();
            ArrayList<String> columNames = new ArrayList<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                columNames.add(meta.getColumnName(i));
            }
            //формируем map с полученных данных с БД
            while (resultSet.next()) {
                String data;
                for (int i = 0; i < columNames.size(); i++) {
                    if (!result.containsKey(columNames.get(i))) {
                        result.put(columNames.get(i), new ArrayList<String>());
                    }
                    data = resultSet.getString(columNames.get(i));
                    result.get(result.get(columNames.get(i)).add(data));
                }
            }
        } catch (Exception e) {
            log.info("error--" + e);
        }
        connectionClose();
        return result;
    }

    /**
     * метод для получения строки данных из БД
     *
     * @param sqlQuery - запрос типа селект
     * @param paramNames -список имен столбцов, которые получим из запроса
     * @return - LinkedHashMap< Название столбца, Данные С БД>
     * @throws SQLException
     */
    public LinkedHashMap<String, String> getOneLineFromTable(String sqlQuery, List<String> paramNames) throws SQLException {
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        ResultSet resultSet = getDataTable(sqlQuery);

        while (resultSet.next()) {
            String param;
            for (int i = 0; i < paramNames.size(); i++) {
                param = resultSet.getString(paramNames.get(i)).toString();
                result.put(paramNames.get(i), param);
            }
        }
        connectionClose();
        return result;
    }

    /**
     * метод для получения строки данных из БД с автоматическим получаем имена
     * столбцов с запроса
     *
     * @param sqlQuery - запрос типа селект
     * @return - LinkedHashMap< Название столбца, Данные С БД>
     * @throws SQLException
     */
    public LinkedHashMap<String, String> getOneLineFromTable(String sqlQuery) throws SQLException {
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        ResultSet resultSet = getDataTable(sqlQuery);

        //получаем имена столбцов и записываем в лист
        ResultSetMetaData meta = resultSet.getMetaData();
        ArrayList<String> columNames = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            columNames.add(meta.getColumnName(i));
        }

        while (resultSet.next()) {
            String data;
            for (int i = 0; i < columNames.size(); i++) {
                data = resultSet.getString(columNames.get(i));
                result.put(columNames.get(i), data);
            }
        }
        connectionClose();
        return result;
    }

    private ResultSet getDataTable(String sqlQuery) throws SQLException {
        ResultSet resultSet = null;
        try {
            //create connection and statement to work with DB
            Statement st = getStatement();
            // execute the query
            resultSet = st.executeQuery(sqlQuery);
        } catch (Exception e) {
            log.info("error--" + e);
        }
        return resultSet;
    }

    public void insertDataToTable(String tableName, List<String> dataToUpdate) throws SQLException {
        String sql = "INSERT INTO [" + getDataBase() + "].[dbo].[" + tableName + "]\n"
                + "VALUES\n";
        for (int i = 0; i < dataToUpdate.size(); i++) {
            if (sql.contains("('")) {
                sql = sql + ",'" + dataToUpdate.get(i) + "'";
            } else {
                sql = sql + "('" + dataToUpdate.get(i) + "'";
            }
        }
        sql = sql + ")\n";
        System.out.println("sql-->" + sql);
        executeWithoutReturnStatement(sql);
        connectionClose();
    }

    /**
     * метод который используется для выполнения запросов возвращать результат
     * которых не нужно
     *
     * @param sqlQuery - запрос типа delete
     * @throws SQLException
     */
    public void executeWithoutReturnStatement(String sqlQuery) throws SQLException {
        try {
            Statement st = getStatement();
            // execute the query
            st.executeUpdate(sqlQuery);
        } catch (Exception e) {
            System.out.println("error--" + e);
        }
        connectionClose();
    }

    /**
     * метод который используется для выполнения запросов возвращать результат
     * которых не нужно
     *
     * @param sqlQuery - запрос типа delete
     * @throws SQLException
     */
    public String executeWithoutReturnStatementRes(String sqlQuery) throws SQLException {
        String res = "";
        try {
            Statement st = getStatement();
            // execute the query
            st.executeUpdate(sqlQuery);
            res = "ok";
        } catch (Exception e) {
            System.out.println("error--" + e);
            res = e.getMessage();
        }
        connectionClose();
        return res;
    }

}
