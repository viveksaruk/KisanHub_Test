package com.company;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

final class DownloadURL {


    public static void downloadFile(String weatherDataUrl, File weatherDataFileName, String tempType, String country) throws IOException{

        URL url = new URL(weatherDataUrl);
        System.out.println("Downloading:" + url);
        URLConnection con = url.openConnection();
        InputStream inputStream = con.getInputStream();
        BufferedInputStream bufferedInputStream = null;

        ByteArrayOutputStream out = null;

        try {
        bufferedInputStream = new BufferedInputStream(inputStream);
        out = new ByteArrayOutputStream();

        final byte[] data = new byte[1024];
        int count;
        while ((count = bufferedInputStream.read(data, 0, 1024)) != -1) {
            out.write(data, 0, count);
        }
        }
        finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (out != null) {
                out.close();
            }
        }
        byte[] responseReceived = out.toByteArray();
        FileOutputStream fos = new FileOutputStream(weatherDataFileName);
        fos.write(responseReceived);

        fos.close();

        System.out.println(country + " " +tempType + " is downloaded !");
    }
}