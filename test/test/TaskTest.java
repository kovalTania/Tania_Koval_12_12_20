/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import mainsource.Task;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Таня Коваль
 */
public class TaskTest {

    @Test
    //тест примера 1
    public void task1ShouldHaveRightAnswear() {
        List<String> list1 = new ArrayList<>();
        Collections.addAll(list1, "d1/", "d1/f1.mov", "d1/d2", "d1/d2/f2.tif", "d1/d2/f-n.tif");
        Task test1 = new Task(list1);

        test1.sortedFoldersToCopyOrCode();

        List<String> listToCode = test1.getListToCode();
        List<String> listToCopy = test1.getListToCopy();

        List<String> rightAnswearCode = Arrays.asList("d1/d2", "d1");
        Assert.assertTrue(listToCode.equals(rightAnswearCode) && listToCopy.isEmpty());
    }

    @Test
    //тест примера 2
    public void task2ShouldHaveRightAnswear() {
        List<String> list2 = new ArrayList<>();
        Collections.addAll(list2, "d1/", "d1/f1.mov", "d1/f3.txt", "d1/d2", "d1/d2/f2.tif", "d1/d2/f-n.tif", "d1/d2/d3", "d1/d2/d3/f4.pdf");
        Task test2 = new Task(list2);

        test2.sortedFoldersToCopyOrCode();

        List<String> listToCode = test2.getListToCode();
        List<String> listToCopy = test2.getListToCopy();

        List<String> rightAnswearCode = Arrays.asList("d1/d2", "d1/f1.mov");
        List<String> rightAnswearCopy = Arrays.asList("d1/f3.txt");
        Assert.assertTrue(listToCode.equals(rightAnswearCode) && listToCopy.equals(rightAnswearCopy));
    }

    @Test
    //тест примера 3
    public void task3ShouldHaveRightAnswear() {
        List<String> list3 = new ArrayList<>();
        Collections.addAll(list3, "d1", "d1/f3.txt", "d1/d2", "d1/d2/d3", "d1/d2/d3/f4.pdf");
        Task test3 = new Task(list3);

        test3.sortedFoldersToCopyOrCode();

        List<String> listToCode = test3.getListToCode();
        List<String> listToCopy = test3.getListToCopy();

        List<String> rightAnswearCopy = Arrays.asList("d1");
        Assert.assertTrue(listToCode.isEmpty() && listToCopy.equals(rightAnswearCopy));
    }

    @Test
    //тест примера 4
    public void task4ShouldHaveRightAnswear() {
        List<String> list4 = new ArrayList<>();
        Collections.addAll(list4, "d0/d1", "d0/d1/f1.mov", "d0/d1/f3.txt", "d0/d1/d2/fn.tif", "d0/d1/d2/f2.tif", "d0/d1/d2/d3/f4.pdf", "d0/d4", "d0/d4/f5.xml", "d0/d4/d5", "d0/d4/d5/d6", "d0/d4/d5/d6/f6.xml", "d0/d7", "d0/d7/d8", "d0/d7/d8/f7.xml");
        Task test4 = new Task(list4);

        test4.sortedFoldersToCopyOrCode();

        List<String> listToCode = test4.getListToCode();
        List<String> listToCopy = test4.getListToCopy();

        List<String> rightAnswearCode = Arrays.asList("d0/d1/d2", "d0/d1/f1.mov");
        List<String> rightAnswearCopy = Arrays.asList("d0/d4", "d0/d7", "d0/d1/f3.txt");
        Assert.assertTrue(listToCode.equals(rightAnswearCode) && listToCopy.equals(rightAnswearCopy));
    }

}
