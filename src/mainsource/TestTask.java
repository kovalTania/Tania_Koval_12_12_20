/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainsource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 10 груд. 2020 9:58:48
 * @author ktw75702
 */
public class TestTask {

    List<String> list = new ArrayList<>();
    List<String> listToCode = new ArrayList<>();
    List<String> listToCopy = new ArrayList<>();
    String tif = ".tif";
    String mov = ".mov";
    String avi = ".avi";
    String seperator = "/";

    public TestTask(List<String> list) {
        this.list = list;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, "d1/", "d1/f1.mov", "d1/ft.txt", "d1/d3", "d1/d3/78t.mov", "d1/d3/8t.mov", "d1/d3/d4/78t.txt", "d1/d2", "d1/d2/f2.tif", "d1/d2/f-n.tif");
        TestTask test = new TestTask(list);

        System.out.println("Tanya Test Task 1");
        test.sortedFoldersToCopyOrCode();

        List<String> list1 = new ArrayList<>();
        Collections.addAll(list1, "d1/", "d1/f1.mov", "d1/d2", "d1/d2/f2.tif", "d1/d2/f-n.tif");
        TestTask test1 = new TestTask(list1);

        System.out.println("Task 1");
        test1.sortedFoldersToCopyOrCode();

        List<String> list2 = new ArrayList<>();
        Collections.addAll(list2, "d1/", "d1/f1.mov", "d1/f3.txt", "d1/d2", "d1/d2/f2.tif", "d1/d2/f-n.tif", "d1/d2/d3", "d1/d2/d3/f4.pdf");
        TestTask test2 = new TestTask(list2);

        System.out.println("Task 2");
        test2.sortedFoldersToCopyOrCode();

        List<String> list3 = new ArrayList<>();
        Collections.addAll(list3, "d1/", "d1/f3.txt", "d1/d2", "d1/d2/d3", "d1/d2/d3/f4.pdf");
        TestTask test3 = new TestTask(list3);

        System.out.println("Task 3");
        test3.sortedFoldersToCopyOrCode();

        List<String> list4 = new ArrayList<>();
        Collections.addAll(list4, "d1/", "d1/f3.txt", "d1/f3.mov", "d1/d2", "d1/d2/d3", "d1/d2/d3/f4.pdf", "d2/d3/re.mov");
        TestTask test4 = new TestTask(list4);

        System.out.println("Tanye test 4");
        test4.sortedFoldersToCopyOrCode();

        List<String> list5 = new ArrayList<>();
        Collections.addAll(list5, "d1/", "d1/f3.txt", "d1/f3.pdf", "d2/", "d2/n1.mov", "d2/gs.avi");
        TestTask test5 = new TestTask(list5);

        System.out.println("Tanye test 5");
        test5.sortedFoldersToCopyOrCode();

    }

    public void sortedFoldersToCopyOrCode() {
        List<String> listOfTifElements = new ArrayList<>();
        //получаем все едементы с расширением tif
        listOfTifElements = list.stream().filter(a -> a.contains(tif)).map(a -> a.substring(0, a.lastIndexOf(seperator))).distinct().collect(Collectors.toList());
        //удаляем из общего списка все файли с tif
        list.removeIf(a -> a.contains(tif));
        //работаем со списком фалов tif
        if (!listOfTifElements.isEmpty()) {
            for (String s : listOfTifElements) {
                tifFoldersToResult(s);
                String[] elementsOfFolder = s.split(seperator);
                //список глубины поиска и кодирования других папок
                int maxDeep = elementsOfFolder.length;
                //по папкам проверяем файли и при необходимости добавляем в результат
                for (String folder : elementsOfFolder) {
                    if (list.contains(folder + seperator)) {
                        //список файлов записанных по данному пути
                        List<String> folderContainsFile = list.stream().filter(a -> a.contains(folder + seperator) && a.contains(".")).filter(a -> a.split(seperator).length <= maxDeep + 1).collect(Collectors.toList());
                        //-------System.out.println("folderCont-> " + folderContainsFile);
                        //получаем только пути папок для проверки если все файли в них с расширением подходящим для кодирования 
                        //то записиваем только путь к папке
                        List<String> folders = folderContainsFile.stream().map(a -> a.substring(0, a.lastIndexOf(seperator))).distinct().collect(Collectors.toList());

                        //цыкл по папкам 
                        folderContainsFile = parsePathToFoldersAndAddOnlyPathToFolred(folders, folderContainsFile);
                        //--------------System.out.println("list->" + list);
                        //проверка на то что папка хранит только мови ави
                        addFilesWhithoutByPath(folderContainsFile);
                    }
                }
            }
        } else {
            //повтор функции поиска файлов с разширением мов и ави
            //стр 62
            List<String> allFilesToWork = list.stream().filter(a -> a.contains(".")).collect(Collectors.toList());
            //файли для копирования
            List<String> allFilesToCopy = allFilesToWork.stream().filter(a -> !a.contains(mov) && !a.contains(avi)).collect(Collectors.toList());
            List<String> allFilesToCode = allFilesToWork.stream().filter(a -> a.contains(mov) || a.contains(avi)).collect(Collectors.toList());

            //работа с типами для копирвоания
            //добавляем самую котороткую папку пути
            /*comparatorByLengthOfEl(allFilesToCopy);
            comparatorByLengthOfEl(allFilesToCode);

            //новый список что бы сохранить занчения  папок
            List<String> folderToCopy = allFilesToCopy.stream().map(a -> a.substring(0, a.lastIndexOf("/"))).distinct().collect(Collectors.toList());
            List<String> folderToCode = allFilesToCode.stream().map(a -> a.substring(0, a.lastIndexOf("/"))).distinct().collect(Collectors.toList());

            if (!folderToCode.isEmpty() && !folderToCopy.isEmpty()) {
                folderToCode.retainAll(folderToCopy);
                System.out.println(folderToCode);
                System.out.println(folderToCopy);
                System.out.println("retrain->" + folderToCopy);
                //удаляем из списков подлежащих к сопированию файлов 
                //те папки в которых разнотивные файли
                for (String elToDelete : folderToCode) {
                    allFilesToCode.removeAll(allFilesToCode.stream().filter(a -> a.contains(elToDelete)).collect(Collectors.toList()));
                    allFilesToCopy.removeAll(allFilesToCopy.stream().filter(a -> a.contains(elToDelete)).collect(Collectors.toList()));
                }
                System.out.println(allFilesToCode);
                System.out.println(allFilesToCopy);
            }
            //если пришоло пустое рабюоатем дальше если нет удаляем из списка ол
            //здесь работаем c файлами с разширением */
            //нужно реализовать работу с папками
            if (!allFilesToCopy.isEmpty() && !list.isEmpty()) {
                System.out.println("here here");

                comparatorByLengthOfEl(list);
                String rootCopy = list.get(0);
                //что бы не уходило далеко в глубь если файл с корня не должен быть записан
                if (listToCopy.isEmpty()) {
                    //TO METHOD
                    listToCopy.add(rootCopy);
                    list.removeAll(list.stream().filter(a -> a.contains(rootCopy)).collect(Collectors.toList()));

                } else if (rootCopy.split(seperator).length <= listToCopy.get(listToCopy.size() - 1).split(seperator).length + 1) {
                    listToCopy.add(rootCopy);
                    list.removeAll(list.stream().filter(a -> a.contains(rootCopy)).collect(Collectors.toList()));
                }

            }
            if (!allFilesToCode.isEmpty() && !list.isEmpty()) {
                System.out.println("here here");
                //работа с кодированием
                comparatorByLengthOfEl(list);
                String rootCode = list.get(0);
                //что бы не уходило далеко в глубь если файл с корня не должен быть записан
                if (listToCode.isEmpty()) {
                    listToCode.add(rootCode);
                    list.removeAll(list.stream().filter(a -> a.contains(rootCode)).collect(Collectors.toList()));
                } else if (rootCode.split(seperator).length + 1 <= listToCode.get(listToCode.size() - 1).split(seperator).length + 1) {
                    listToCode.add(rootCode);
                    list.removeAll(list.stream().filter(a -> a.contains(rootCode)).collect(Collectors.toList()));
                }
                List<String> copyOfGeneralList = new ArrayList<>(list);
                //addFilesWhithoutByPath(copyOfGeneralList.stream().filter(a -> a.contains(".")).collect(Collectors.toList()));
            }

            resultToConsole();

        }
    }

    public List<String> addFolderPathToResult(String pathToFolder, List<String> listToResult, List<String> sameFolderPath, List<String> folderContainsFile) {
        if (list.contains(pathToFolder)) {
            //записываем путь папки в список подлежащих к кодированию
            listToResult.add(pathToFolder);
            //удаляем из общего списка
            list.remove(pathToFolder);
            list.removeAll(sameFolderPath);
            folderContainsFile.removeAll(sameFolderPath);
        }
        return folderContainsFile;
    }

    public void addFilesWhithoutByPath(List<String> folderContainsFile) {
        for (String otherElementsInSameF : folderContainsFile) {
            if (otherElementsInSameF.contains(mov) || otherElementsInSameF.contains(avi)) {
                listToCode.add(otherElementsInSameF);
            } else {
                listToCopy.add(otherElementsInSameF);
            }
            list.remove(otherElementsInSameF);
        }
    }

    public List<String> parsePathToFoldersAndAddOnlyPathToFolred(List<String> folders, List<String> folderContainsFile) {
        for (String elPath : folders) {
            //получаем файли в данных папках для дальнейшей проверки на наличие только файлов мов и ави
            List<String> sameFolderPath = list.stream().filter(a -> a.contains(elPath) && a.split(seperator).length == elPath.split(seperator).length + 1).collect(Collectors.toList());
            //---------System.out.println(sameFolderPath);
            //если файли найдены
            if (!sameFolderPath.isEmpty()) {
                boolean flag = true;
                //поеементно проверяем на соответствие расширения у файлов
                for (String path : sameFolderPath) {
                    //проверяем только пути с файлами
                    if (path.contains(".")) {
                        //если файл не мов и не ави то выходим из цыкла данная папка не подходит 
                        if (!path.contains(mov) && !path.contains(avi)) {
                            flag = false;
                            break;
                        }
                    }
                }
                //если все файли совпали по расширениям
                if (flag = true) {
                    //проверка наличия только пути в начальних данных
                    folderContainsFile = addFolderPathToResult(elPath, listToCode, sameFolderPath, folderContainsFile);
                } //tif втянул и папку где только другое расширение и можно скопировать просто путь к папке а не к файлу
                /*else {
                                    folderContainsFile = addFolderPathToResult(elPath, listToCopy, sameFolderPath, folderContainsFile);
                                }*/
            }
        }
        return folderContainsFile;
    }

    public void tifFoldersToResult(String s) {
        //запиываем в список на кодирование
        listToCode.add(s);
        list.removeIf(a -> a.equals(s));
    }

    public void resultToConsole() {
        System.out.println("Didn't need elements->" + list);
        System.out.println("To code->" + listToCode);
        System.out.println("To copy->" + listToCopy);
    }

    public void comparatorByLengthOfEl(List<String> listOfEl) {
        Collections.sort(listOfEl, (String o1, String o2) -> {
            if (o1.length() > o2.length()) {
                return 1;
            } else if (o1.length() < o2.length()) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    public void parseFilesToCopy() {
        //comparatorByLengthOfEl();
        String rootCopy = list.get(0);

        listToCopy.add(rootCopy);
        list.removeAll(list.stream().filter(a -> a.contains(rootCopy)).collect(Collectors.toList()));
    }
}
