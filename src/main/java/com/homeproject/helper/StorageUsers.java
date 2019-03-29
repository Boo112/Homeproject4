package com.homeproject.helper;

import com.homeproject.config.ConstDBName;
import com.homeproject.models.User;
import com.homeproject.processing.GenerateUser;
import com.homeproject.processing.GenerateUserApi;
import com.homeproject.worker.DataBaseWorker;
import com.homeproject.worker.HttpWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StorageUsers {

    private Random random = new Random();
    private List<User> users = new ArrayList();
    private GenerateUser generateUser = new GenerateUser();
    private GenerateUserApi generateUserApi = new GenerateUserApi();
    private DataBaseWorker dataBaseWorker = new DataBaseWorker();
    private DataBaseHelper dataBaseHelper = new DataBaseHelper();

    public void populate() {

        int numberRecords = dataBaseHelper.countRecordsInTable(ConstDBName.USER_TABLE_PERSON);
        int[] randomRecordNumber = dataBaseHelper.getShuffleRecordsNumbers(numberRecords);
        int count = 1 + random.nextInt(30);

        for (int i = 0; i < count; i++) {
            StringBuffer httpResponse = new HttpWorker().getResponse();

            if (httpResponse != null) {
                users.add(generateUserApi.getUserFromJSON(httpResponse, i));// добавляем из randomuser/api
            } else {
                if (!dataBaseHelper.checkExistRecordInBD()) {
                    users.add(dataBaseWorker.getUserFromDatabase(randomRecordNumber[i]));// добавляем пользователей из Mysql BD
                } else {
                    users.add(generateUser.getUserFromLocalDatabase(i)); // добавляем пользователя из txt файликов
                }
            }
        }
    }

    public List<User> get() {
        return users;
    }
}
