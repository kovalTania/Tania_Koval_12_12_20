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
public class Task {

    private List<String> list = new ArrayList<>();
    private List<String> listToCode = new ArrayList<>();
    private List<String> listToCopy = new ArrayList<>();
    private final String TIF = ".tif";
    private final String MOV = ".mov";
    private final String AVI = ".avi";
    private final String SEPERATOR = "/";

    public Task(List<String> list) {
        this.list = list;
        list.parallelStream();
    }

    public void sortedFoldersToCopyOrCode() {
        List<String> listOfTifElements = new ArrayList<>();
        //получаем все едементы с расширением TIF
        listOfTifElements = list.parallelStream().filter(a -> a.contains(TIF)).map(a -> a.substring(0, a.lastIndexOf(SEPERATOR))).distinct().collect(Collectors.toList());
        //удаляем из общего списка все файли с TIF
        list.removeIf(a -> a.contains(TIF));
        //работаем со списком фалов TIF
        if (!listOfTifElements.isEmpty()) {
            for (String s : listOfTifElements) {
                TIFFoldersToResult(s);
                String[] elementsOfFolder = s.split(SEPERATOR);
                //список глубины поиска и кодирования других папок
                int maxDeep = elementsOfFolder.length;
                //по корням папок проверяем файли и при необходимости добавляем в результат
                findFoldersByTifCopy(elementsOfFolder, maxDeep);
                //обработываем оставшиеся файли
                List<String> copyOfGeneralList = new ArrayList<>(list.parallelStream().filter(a -> a.split(SEPERATOR).length <= maxDeep + 1).collect(Collectors.toList()));
                addFilesToResultByFileType(copyOfGeneralList.parallelStream().filter(a -> a.contains(".")).collect(Collectors.toList()));
            }

        } else {
            //проверка можно ли записать файли не по елементно а сразу папку если все ее фали соответствуют определенным типам
            comparatorByLengthOfEl(list);
            //получаем список всех корней папок
            List<String> copyList = list.parallelStream().filter(a -> !a.contains(".")).collect(Collectors.toList());
            for (String folder : copyList) {
                if (folder == null) {
                    continue;
                }
                //получае все файли в данном корне
                List<String> allFilesInFolder = list.parallelStream().filter(a -> a.contains(folder)).filter(a -> a.contains(".")).collect(Collectors.toList());
                parseFolderToAddInResult(allFilesInFolder, folder, copyList);
            }
            //обработываем оставшиеся файли
            List<String> copyOfGeneralList = new ArrayList<>(list);
            addFilesToResultByFileType(copyOfGeneralList.parallelStream().filter(a -> a.contains(".")).collect(Collectors.toList()));
        }
        if (!listToCopy.isEmpty() && !listToCode.isEmpty()) {
            checkDoubleDirectories();
        }
    }

    private void checkDoubleDirectories() {
        List<String> onlyFoldersCopy = listToCopy.parallelStream().map(a -> a.substring(0, a.lastIndexOf(SEPERATOR))).collect(Collectors.toList());
        List<String> onlyFoldersCode = listToCode.parallelStream().map(a -> a.substring(0, a.lastIndexOf(SEPERATOR))).collect(Collectors.toList());
        findSame(onlyFoldersCode, onlyFoldersCopy, listToCode);
        findSame(onlyFoldersCopy, onlyFoldersCode, listToCopy);
    }

    private void findSame(List<String> findIn, List<String> otherList, List<String> listToChange) {
        for (String folder : findIn) {
            long qnt = findIn.parallelStream().filter(a -> a.equals(folder)).count();
            if (qnt != 0) {
                long qntInOther = otherList.parallelStream().filter(a -> a.contains(folder)).count();
                if (qntInOther == 0) {
                    listToChange.removeIf(a -> a.contains(folder));
                    listToChange.add(folder);
                }
            }
        }
    }

    private void addFilesToResultByFileType(List<String> folderContainsFile) {
        for (String otherElementsInSameF : folderContainsFile) {
            if (otherElementsInSameF.contains(MOV) || otherElementsInSameF.contains(AVI)) {
                listToCode.add(otherElementsInSameF);
            } else {
                listToCopy.add(otherElementsInSameF);
            }
            list.remove(otherElementsInSameF);
        }
    }

    private void findFoldersByTifCopy(String[] elementsOfFolder, int maxDeep) {
        for (String folder : elementsOfFolder) {
            //список файлов записанных по данному пути
            List<String> folderContainsFile = list.parallelStream().filter(a -> a.contains(folder + SEPERATOR) && a.contains(".")).filter(a -> a.split(SEPERATOR).length <= maxDeep + 1).collect(Collectors.toList());
            //получаем только пути папок для проверки если все файли в них с расширением подходящим для кодирования 
            //записиваем только путь к папке
            List<String> folders = folderContainsFile.parallelStream().map(a -> a.substring(0, a.lastIndexOf(SEPERATOR))).distinct().collect(Collectors.toList());
            //проверка на корень выше
            for (String theFolder : folders) {
                if (theFolder.contains(SEPERATOR)) {
                    String rootUpper = theFolder.substring(0, theFolder.lastIndexOf(SEPERATOR));
                    int lenOfFolders = theFolder.split(SEPERATOR).length;
                    long count = list.parallelStream().filter(a -> a.contains(rootUpper)).filter(a -> a.split(SEPERATOR).length <= lenOfFolders).count();
                    if (count == 2) {
                        int index = folders.indexOf(theFolder);
                        folders.set(index, rootUpper);
                    }
                }
            }
            //цыкл по папкам 
            parsePathToFoldersAndAddOnlyPathToFolderTif(folders, folderContainsFile);
        }
    }

    private void parsePathToFoldersAndAddOnlyPathToFolderTif(List<String> folders, List<String> folderContainsFile) {
        comparatorByLengthOfEl(folders);
        List<String> copyFolders = new ArrayList<>(folders);
        for (String folder : copyFolders) {
            if (folder == null) {
                continue;
            }
            //получае все файли в данном корне
            List<String> allFilesInFolder = folderContainsFile.parallelStream().filter(a -> a.contains(folder)).collect(Collectors.toList());
            parseFolderToAddInResult(allFilesInFolder, folder, copyFolders);
        }

    }

    private List<String> parseFolderToAddInResult(List<String> allFilesInFolder, String folder, List<String> copyList) {
        //запоминаем начальное количество файлов в корне  
        int qntOfAllFiles = allFilesInFolder.size();
        if (!allFilesInFolder.isEmpty()) {
            List<String> copyAllFilesInFolder = new ArrayList<>(allFilesInFolder);
            for (String file : allFilesInFolder) {
                //если не подлежт кодированию 
                //удалем елемент из списка
                if (!file.contains(MOV) && !file.contains(AVI)) {
                    copyAllFilesInFolder.remove(file);
                }
            }
            //после проверки типов файла
            //запоминаем количесвто файлов подлежащих содированию после обработки
            int qntFileNotToCode = copyAllFilesInFolder.size();
            //если ни один елемент не удалили 
            //корень подходит под кодирование
            if (qntOfAllFiles == qntFileNotToCode) {
                listToCode.add(folder);
                copyList.parallelStream().filter(a -> a.contains(folder)).map(a -> a = null).collect(Collectors.toList());
                list.removeAll(list.parallelStream().filter(a -> a.contains(folder)).collect(Collectors.toList()));
            } else if (qntFileNotToCode == 0) {
                //если все удалили
                //корень подходит копированию
                listToCopy.add(folder);
                copyList.parallelStream().filter(a -> a.contains(folder)).map(a -> a = null).collect(Collectors.toList());
                list.removeAll(list.parallelStream().filter(a -> a.contains(folder)).collect(Collectors.toList()));
            }
        }
        return copyList;
    }

    private void TIFFoldersToResult(String s) {
        //запиываем в список на кодирование
        listToCode.add(s);
        list.removeIf(a -> a.equals(s));
    }

    public void resultToConsole() {
        String strCode = "Список файлов\\директорий на кодирование:";
        String strCopy = "Список файлов\\директорий на копирование:";
        if (listToCode.isEmpty()) {
            System.out.println(strCode + "-------");
        } else {
            System.out.println(strCode);
            listToCode.forEach(a -> System.out.println(a));
        }
        if (listToCopy.isEmpty()) {
            System.out.println(strCopy + "-------");
        } else {
            System.out.println(strCopy);
            listToCopy.forEach(a -> System.out.println(a));
        }
    }

    //сортировка по длине елементов
    private void comparatorByLengthOfEl(List<String> listOfEl) {
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

    public void replaceSperator(String sepFromDB) {
        listToCode = listToCode.parallelStream().map(a -> a.replaceAll(SEPERATOR, sepFromDB)).collect(Collectors.toList());
        listToCopy = listToCopy.parallelStream().map(a -> a.replaceAll(SEPERATOR, sepFromDB)).collect(Collectors.toList());
    }

    public List<String> getListToCopy() {
        return this.listToCopy;
    }

    public List<String> getListToCode() {
        return this.listToCode;
    }

}
