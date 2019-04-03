package com.homeproject.config;

import com.homeproject.helper.PathToFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class СonfigurationDB {
    String dbHost = getParametersForConnectDb(0);
    String dbPort = getParametersForConnectDb(1);
    String dbUser = getParametersForConnectDb(2);
    String dbPassword = getParametersForConnectDb(3);
    String dbName = getParametersForConnectDb(4);


    private String getParametersForConnectDb(int index) {
        PathToFiles fileParameters = new PathToFiles();
        String dbParameters = null;
        try {
            dbParameters = Files.readAllLines(Paths.get(fileParameters.fileParametresForDB)).get(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dbParameters.substring(dbParameters.lastIndexOf("=") + 1).trim();
    }
}
