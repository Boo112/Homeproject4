package com.homeproject;

import com.homeproject.helper.PathToFiles;
import com.homeproject.helper.StorageUsers;
import com.homeproject.models.User;
import com.homeproject.worker.DataBaseWorker;
import com.homeproject.worker.ExcelWorker;
import com.homeproject.worker.PdfWorker;



public class Main {

    public static void main(String[] args) {

        PathToFiles filePath=new PathToFiles();
        StorageUsers storageUsers = new StorageUsers();
        ExcelWorker excelWorker =new ExcelWorker();
        DataBaseWorker dataBaseWorker =new DataBaseWorker();

        storageUsers.populate();
        excelWorker.createExcelFile(filePath.fileNameXls);

        for (User user : storageUsers.get()) {
            excelWorker.reWriteExcel(filePath.fileNameXls,user);// добавляем юзера в эксель
            dataBaseWorker.addUserInDB(user);// Записываем данные в БД
        }
        new PdfWorker().writePDF(filePath.filenamePdf, storageUsers.get()); // создаем pdf
    }
}