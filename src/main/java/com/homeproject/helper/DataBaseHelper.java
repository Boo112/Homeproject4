package com.homeproject.helper;

import com.homeproject.config.ConnectDB;
import com.homeproject.config.ConstDBName;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class DataBaseHelper {

    private ConnectDB connect = new ConnectDB();

    //есть ли хотябы одна запись в таблице
    boolean checkExistRecordInBD() {
        boolean isRecordsExists = false;
        String select = "SELECT * FROM " + ConstDBName.USER_TABLE_PERSON;

        try (PreparedStatement prSt = connect.getDbConnection().prepareStatement(select);
             ResultSet resultSet = prSt.executeQuery()) {

            if (resultSet.next()) {
                isRecordsExists = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return isRecordsExists;
    }

    // есть ли в БД пользователь с определенным ФИО
    public boolean checkContainUserInBD(String name, String surname, String middlename) {
        boolean userContainsInBD = false;
        int counter = 0;
        String select = "SELECT * FROM " + ConstDBName.USER_TABLE_PERSON + " WHERE " +
                ConstDBName.USER_NAME + "=? AND " +
                ConstDBName.USER_SURNAME + "=? AND " + ConstDBName.MIDDLENAME + "=?";

        try (PreparedStatement prSt = connect.getDbConnection().prepareStatement(select)) {
            prSt.setString(1, name);
            prSt.setString(2, surname);
            prSt.setString(3, middlename);

            try (ResultSet resultSet = prSt.executeQuery()) {
                while (resultSet.next()) {
                    counter++;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (counter >= 1) userContainsInBD = true;
        return userContainsInBD;
    }

    // количество записей в таблице
    int countRecordsInTable(String tableName) {
        String sqlSelect = "SELECT id FROM " + tableName;
        ResultSet resultSet;
        int countRecord = 0;

        try (PreparedStatement prSt = connect.getDbConnection().prepareStatement(sqlSelect)) {
            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                countRecord++;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return countRecord;
    }

    // Id номера записей в таблице
    private int[] getIdNumber(int limit) {
        String sqlSelect = "SELECT id FROM " + ConstDBName.USER_TABLE_PERSON;
        int[] id = new int[limit];

        try (PreparedStatement prSt = connect.getDbConnection().prepareStatement(sqlSelect);
             ResultSet rs = prSt.executeQuery()) {

            int counter = 0;
            while (rs.next()) {
                id[counter] = Integer.parseInt(rs.getString(1));
                counter++;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return id;
    }

    //массив ID записей в бд отсортированный случайном поредке
    int[] getShuffleRecordsNumbers(int limit) {
        ArrayList<Integer> listRecordsNumbers = new ArrayList<>();
        int[] arrayRandomNumbers = new int[limit];
        int[] arrayIdNumbers = getIdNumber(limit);

        for (int i = 0; i < limit; i++) {
            listRecordsNumbers.add(arrayIdNumbers[i]);
        }
        Collections.shuffle(listRecordsNumbers);

        for (int i = 0; i < listRecordsNumbers.size(); i++) {
            arrayRandomNumbers[i] = listRecordsNumbers.get(i);
        }
        return arrayRandomNumbers;
    }
}
