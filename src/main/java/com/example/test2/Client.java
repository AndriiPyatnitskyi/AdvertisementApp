package com.example.test2;

import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static Logger logger = Logger.getLogger(com.example.test2.Client.class.getName());

    private static String MAIN_URL = "http://localhost:8080/";

    public static void main(String[] args) throws IOException {
        List<String> filesList = getFilesList("http://localhost:8080/list");
        for (String fileName : filesList) {
            downloadAndSaveFile(MAIN_URL + "getfiles/" + fileName, fileName);
        }
    }

    public static List<String> getFilesList(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        List<String> resultList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                String substring = inputLine.substring(1, inputLine.length() - 1);
                String[] split = substring.split("\"");
                for (int i = 0; i < split.length; i++) {
                    if (!split[i].equals(",") && !split[i].equals("")) {
                        resultList.add(split[i]);
                    }
                }
                logger.log(Level.INFO, "Files:");
                for (String s : resultList) {
                    logger.log(Level.INFO, s);
                }
            }
        }
        return resultList;
    }

    public static void downloadAndSaveFile(String stringUrl, String fileName) throws IOException {
        URL url = new URL(stringUrl);
        URLConnection urlConnection = url.openConnection();
        logger.info("Connected to: " + stringUrl);

        InputStream inputStream = urlConnection.getInputStream();
        byte[] bytes = getBytes(inputStream);

        try (FileOutputStream fos = new FileOutputStream("c:\\folder1\\" + fileName)) {
            fos.write(bytes);
        }
        logger.info("File: " + fileName + " was saved successfully");
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }
}
