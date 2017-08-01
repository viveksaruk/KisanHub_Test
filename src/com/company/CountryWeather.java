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

                String weatherDataUrl = preLink + aTempType + "/date/" + country + ".txt";
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
            String[] months = null;
            String[] values = null;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("Year")) {

                    line = line.trim().replaceAll("\\s{2,}", " ");
                    months = line.split("[\\s]+");

                }

                else if (months != null) {

                    line = line.trim().replaceAll("\\s{2,}+", " ");
                    values = line.split("[\\s]+");

                        for (int j = 0; j < values.length ; j++) {

                            if( j != 0) {

                                String csvString = null;

                            if (values.length == 9) {

                                if (months[j].equals("JUN")) {
                                    csvString = country + ", " + tempType + ", " + values[0] + ", " + months[j] + ", " + values[j];
                                    csvBufferedWriter.append(csvString);
                                    csvBufferedWriter.newLine();

                                    for ( int k= 7 ; k < 13; k++ ) {

                                        csvString = country + ", " + tempType + ", " + values[0] + ", " + months[k] + ", " + "N/A";
                                        csvBufferedWriter.append(csvString);
                                        csvBufferedWriter.newLine();
                                    }

                                    csvString = country + ", " + tempType + ", " + values[0] + ", " + months[13] + ", " + values[j+1];
                                    csvBufferedWriter.append(csvString);
                                    csvBufferedWriter.newLine();

                                    csvString = country + ", " + tempType + ", " + values[0] + ", " + months[14] + ", " + values[j+2];
                                    csvBufferedWriter.append(csvString);
                                    csvBufferedWriter.newLine();

                                    for ( int m= 15 ; m < 18; m++ ) {

                                        csvString = country + ", " + tempType + ", " + values[0] + ", " + months[m] + ", " + "N/A";
                                        csvBufferedWriter.append(csvString);
                                        csvBufferedWriter.newLine();
                                    }
                                    break;

                                }
                                else {
                                    csvString = country + ", " + tempType + ", " + values[0] + ", " + months[j] + ", " + values[j];
                                    csvBufferedWriter.append(csvString);
                                    csvBufferedWriter.newLine();
                                }
                            }
                            else {

                                if (values[j].equals("---")) {

                                    csvString = country + ", " + tempType + ", " + values[0] + ", " + months[j] + ", " + "N/A";
                                    csvBufferedWriter.append(csvString);
                                    csvBufferedWriter.newLine();

                                } else {
                                    csvString = country + ", " + tempType + ", " + values[0] + ", " + months[j] + ", " + values[j];
                                    csvBufferedWriter.append(csvString);
                                    csvBufferedWriter.newLine();

                                }
                            }
                            }
                        }
                }
            }
            System.out.println();
            br.close();
            isr.close();
        }
}
