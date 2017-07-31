package com.company;


import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        String[] countryList = {"UK", "England", "Wales", "Scotland"};
        String[] tempTypeList = {"Tmax", "Tmin", "Tmean", "Sunshine", "Rainfall"};

        CountryWeather countryWeather = new CountryWeather(countryList, tempTypeList);
        countryWeather.downloadFilesAndWriteCsv();
    }

}


