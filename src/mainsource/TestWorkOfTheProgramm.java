/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainsource;

import dao.TaskDAO;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author Таня Коваль
 */
public class TestWorkOfTheProgramm {

    public static void main(String[] args) {
        TaskDAO dao = new TaskDAO();

        ArrayList<String> fileNames = dao.getOneColumnFromDB("FileName");
        fileNames = fileNames.parallelStream().map(a -> a.replaceAll("\\\\", "/")).collect(Collectors.toCollection(ArrayList::new));

        Task task = new Task(fileNames);
        task.sortedFoldersToCopyOrCode();
        task.replaceSperator("\\\\");
        task.resultToConsole();
    }

}
