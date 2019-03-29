package com.homeproject.worker;

import com.homeproject.config.ConnectDB;
import com.homeproject.config.ConstDBName;
import com.homeproject.helper.DataBaseHelper;
import com.homeproject.models.User;
import com.homeproject.processing.GenerateData;

import java.sql.*;
import java.time.LocalDate;

public class DataBaseWorker {

    private ConnectDB connect = new ConnectDB();
    private GenerateData generateData = new GenerateData();
    private DataBaseHelper dataBaseHelper = new DataBaseHelper();

    public void addUserInDB(User user) {
        int addressId;

        String insertIntoPerson = "INSERT INTO " + ConstDBName.USER_TABLE_PERSON + "(" + ConstDBName.USER_NAME + "," +
                ConstDBName.USER_SURNAME + "," + ConstDBName.MIDDLENAME + "," + ConstDBName.INN + "," +
                ConstDBName.GENDER + "," + ConstDBName.BIRTHDAY + "," + ConstDBName.ADDRESS_ID + ")" +
                "VALUE(?,?,?,?,?,?,?)";

        String insertIntoAddress = "INSERT INTO " + ConstDBName.USER_TABLE_ADDRESS + "(" + ConstDBName.POSTCODE + "," +
                ConstDBName.COUNTRY + "," + ConstDBName.REGION + "," + ConstDBName.CITY + "," +
                ConstDBName.STREET + "," + ConstDBName.HOUSE + "," + ConstDBName.FLAT + ")" +
                "VALUE(?,?,?,?,?,?,?)";

        String sqlGetAddressId = "SELECT MAX" + "(" + ConstDBName.ADDRESS_TABLE_ID + ")" + " FROM " + ConstDBName.USER_TABLE_ADDRESS;

        try (PreparedStatement prStPerson = connect.getDbConnection().prepareStatement(insertIntoPerson);
             PreparedStatement prStAddress = connect.getDbConnection().prepareStatement(insertIntoAddress)) {
            // если пользовательского ФИО нет в БД >>> добавляем
            if (!dataBaseHelper.checkContainUserInBD(user.getFirstName(), user.getLastName(), user.getPatronymic())) {

                prStAddress.setString(1, user.getIndex());
                prStAddress.setString(2, user.getCountry());
                prStAddress.setString(3, user.getState());
                prStAddress.setString(4, user.getCity());
                prStAddress.setString(5, user.getStreet());
                prStAddress.setString(6, user.getHouse());
                prStAddress.setString(7, user.getFlat());
                prStAddress.executeUpdate();

                try (ResultSet resultOfSqlGetAddressId = prStPerson.executeQuery(sqlGetAddressId)) {
                    resultOfSqlGetAddressId.next();
                    addressId = resultOfSqlGetAddressId.getInt(1);
                }

                prStPerson.setString(1, user.getFirstName());
                prStPerson.setString(2, user.getLastName());
                prStPerson.setString(3, user.getPatronymic());
                prStPerson.setString(4, user.getInn());
                prStPerson.setString(5, user.getGender());
                prStPerson.setDate(6, Date.valueOf(user.getDateOfBorn()));
                prStPerson.setInt(7, addressId);
                prStPerson.executeUpdate();

            } else {
                //update User
                try (ResultSet resultOfSqlGetAddressId = prStPerson.executeQuery(sqlGetAddressId)) {
                    resultOfSqlGetAddressId.next();
                    addressId = resultOfSqlGetAddressId.getInt(1);

                    updateUserInDB(user.getFirstName(), user.getInn(), user.getLastName(), user.getPatronymic(), user.getDateOfBorn(),
                            user.getGender(), user.getIndex(), user.getCountry(), user.getState(), user.getCity(), user.getStreet(),
                            user.getHouse(), user.getFlat(), String.valueOf(addressId));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateUserInDB(String name, String inn, String surname, String middlename, LocalDate birthday, String gender,
                                String postcode, String country, String region, String city, String street,
                                String house, String flat, String addressId) {

        String updatePerson = "UPDATE " + ConstDBName.USER_TABLE_PERSON + " SET " + ConstDBName.INN + "=? ,"
                + ConstDBName.USER_SURNAME + "=? ," + ConstDBName.MIDDLENAME + "=? ," +
                ConstDBName.BIRTHDAY + "=? ," + ConstDBName.GENDER + "=?" +
                " WHERE " + ConstDBName.USER_NAME + "=?";

        String updateAddress = "UPDATE " + ConstDBName.USER_TABLE_ADDRESS + " SET " + ConstDBName.POSTCODE + "=? ," +
                ConstDBName.COUNTRY + "=? ," + ConstDBName.REGION + "=? ," + ConstDBName.CITY + "=? ," +
                ConstDBName.STREET + "=? ," + ConstDBName.HOUSE + "=? ," + ConstDBName.FLAT + "=?"
                + " WHERE " + ConstDBName.ADDRESS_TABLE_ID + "=?";

        try (PreparedStatement prStPerson = connect.getDbConnection().prepareCall(updatePerson);
             PreparedStatement prStAddress = connect.getDbConnection().prepareCall(updateAddress)) {

            prStPerson.setString(1, inn);
            prStPerson.setString(2, surname);
            prStPerson.setString(3, middlename);
            prStPerson.setDate(4, Date.valueOf(birthday));
            prStPerson.setString(5, gender);
            prStPerson.setString(6, name);
            prStPerson.executeUpdate();
            prStAddress.setString(1, postcode);
            prStAddress.setString(2, country);
            prStAddress.setString(3, region);
            prStAddress.setString(4, city);
            prStAddress.setString(5, street);
            prStAddress.setString(6, house);
            prStAddress.setString(7, flat);
            prStAddress.setString(8, addressId);
            prStAddress.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public User getUserFromDatabase(int randomRecordNumber) {
        User user = new User();
        String name = null;
        String surname = null;
        String middlename = null;
        String inn = null;
        Date birthday = null;
        String gender = null;
        String addressId;
        String postcode = null;
        String country = null;
        String region = null;
        String street = null;
        String city = null;
        String house = null;
        String flat = null;

        String selectPerson = "SELECT" + " " + ConstDBName.USER_NAME + "," + ConstDBName.USER_SURNAME + "," +
                ConstDBName.MIDDLENAME + "," + ConstDBName.INN + "," +
                ConstDBName.BIRTHDAY + "," + ConstDBName.GENDER + "," + ConstDBName.ADDRESS_ID + " " +
                "FROM " + ConstDBName.USER_TABLE_PERSON + " " +
                "WHERE " + ConstDBName.USER_TABLE_ID + "=?";

        String selectAddress = "SELECT" + " " + ConstDBName.POSTCODE + "," + ConstDBName.COUNTRY + "," +
                ConstDBName.REGION + "," + ConstDBName.CITY + "," +
                ConstDBName.STREET + "," + ConstDBName.HOUSE + "," + ConstDBName.FLAT + " " +
                "FROM " + ConstDBName.USER_TABLE_ADDRESS + " " +
                "WHERE " + ConstDBName.ADDRESS_TABLE_ID + "=?";

        try (PreparedStatement prStPerson = connect.getDbConnection().prepareStatement(selectPerson);
             PreparedStatement prStAddress = connect.getDbConnection().prepareStatement(selectAddress)) {

            prStPerson.setInt(1, randomRecordNumber);
            ResultSet rs = prStPerson.executeQuery();
            rs.next();

            name = rs.getString(1);
            surname = rs.getString(2);
            middlename = rs.getString(3);
            inn = rs.getString(4);
            birthday = rs.getDate(5);
            gender = rs.getString(6);
            addressId = rs.getString(7);

            prStAddress.setInt(1, Integer.parseInt(addressId));

            ResultSet rsAddress = prStAddress.executeQuery();
            rsAddress.next();

            postcode = rsAddress.getString(1);
            country = rsAddress.getString(2);
            region = rsAddress.getString(3);
            city = rsAddress.getString(4);
            street = rsAddress.getString(5);
            house = rsAddress.getString(6);
            flat = rsAddress.getString(7);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assert birthday != null;
        LocalDate dateOfBorn = birthday.toLocalDate();
        user.setFirstName(name);
        user.setLastName(surname);
        user.setPatronymic(middlename);
        user.setGender(gender);
        user.setCountry(country);
        user.setState(region);
        user.setCity(city);
        user.setStreet(street);
        user.setInn(inn);
        user.setHouse(house);
        user.setFlat(flat);
        user.setDateOfBorn(dateOfBorn);
        user.setAge(String.valueOf(generateData.getAge(dateOfBorn)));
        user.setIndex(postcode);

        return user;
    }
}
