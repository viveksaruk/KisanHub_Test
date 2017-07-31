package com.company;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class CountryWeather {

    private File weatherCsv = new File("Weather.csv");
    private BufferedWriter csvBufferedWriter;

    private String[] countryList;
    private String[] tempTypeList;

    private String preLink = "http://www.metoffice.gov.uk/pub/data/weather/uk/climate/datasets/";

    CountryWeather(String[] countryList, String[] tempTypeList) throws IOException {
        this.countryList = countryList;
        this.tempTypeList = tempTypeList;
    }

    public void downloadFilesAndWriteCsv () throws IOException {

        File dir = new File("downloads");
        dir.mkdirs();

        if(!weatherCsv.exists()) {
        weatherCsv.createNewFile();
         }
        FileWriter writer = new FileWriter(weatherCsv);
        csvBufferedWriter = new BufferedWriter(writer);

        String header = "region_code, weather_param, year, key, value";

        csvBufferedWriter.write(header);
        csvBufferedWriter.newLine();

        for (String country : this.countryList) {

            System.out.println("Country is :" + country);

            for (String aTempType : tempTypeList) {

                String weatherDataUrl = preLink + aTempType + "/ranked/" + country + ".txt";
                String weatherDataFileName = aTempType + "_" + country;
                File file = new File(dir, weatherDataFileName);
                file.createNewFile();
                DownloadURL.downloadFile(weatherDataUrl, file, aTempType, country);
                csvWrite(weatherDataUrl, aTempType, country);
            }
        }
        csvBufferedWriter.flush();

    }

        private void csvWrite(String weatherDataUrl, String tempType, String country) throws IOException {

            URL url = new URL(weatherDataUrl);
            URLConnection con = url.openConnection();
            InputStream inputStream = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);

            String line = null;
            String[] columns = null;
            String[] columns3 = new String[3000];
            String[] splited = null;

            while (null != (line = br.readLine())) {

                if (line.contains("ANN")) {

                    line = line.trim().replaceAll("\\s{2,}", " ");
                    columns = line.split("[\\s]+");

                    for (int i=0; i<columns.length; i++) {

                        String ss;

                        if ( !columns[i].equals("Year"))  {
                            ss = columns[i];
                            columns3[i] = ss;
                        }
                    }
                    System.out.println();

                }
                else if (null != columns) {

                    line = line.trim().replaceAll("\\s{2,}+", " ");
                    splited = line.split("[\\s]+");

                        for (int j = 0; j < splited.length ; j++) {

                            String csvString1 = null;
                            if (splited.length == 14) {

                                if (columns3[j].equals("JUN")) {
                                    csvString1 = country + ", " + tempType + ", " + splited[j + 1] + ", " + columns3[j] + ", " + splited[j];
                                    csvBufferedWriter.append(csvString1);
                                    csvBufferedWriter.newLine();

                                    csvString1 = country + ", " + tempType + ", " + splited[j + 2] + ", " + "SPR" + ", " + splited[j+1];
                                    csvBufferedWriter.append(csvString1);
                                    csvBufferedWriter.newLine();
                                    break;
                                }
                                else {
                                    csvString1 = country + ", " + tempType + ", " + splited[j + 1] + ", " + columns3[j] + ", " + splited[j];
                                    j = j + 1;
                                    csvBufferedWriter.append(csvString1);
                                    csvBufferedWriter.newLine();
                                }
                            }
                            else {
                                csvString1 = country + ", " + tempType + ", " + splited[j + 1] + ", " + columns3[j] + ", " + splited[j];
                                j = j + 1;
                                csvBufferedWriter.append(csvString1);
                                csvBufferedWriter.newLine();
                            }
                        }
                }
            }
            System.out.println();
            br.close();
            isr.close();
        }
}