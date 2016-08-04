package com.example.seeker.moneyexchanger;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class BNMFileXML {

    public BNMFileXML(String url, String fileName){
        DownloadFromUrl(url, fileName);
    }

    public void DownloadFromUrl(String DownloadUrl, String fileName) {

        try {
            File root = android.os.Environment.getExternalStorageDirectory();

            File dir = new File (root.getAbsolutePath() + "/xmls");
            if(!dir.exists()) {
                dir.mkdirs();
            }

            URL url = new URL(DownloadUrl); //you can write here any link
            File file = new File(dir, fileName);

            long startTime = System.currentTimeMillis();
            Log.d("DownloadManager", "download begining");
            Log.d("DownloadManager", "download url:" + url);
            Log.d("DownloadManager", "downloaded file name:" + fileName);

           /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

           /*
            * Define InputStreams to read from the URLConnection.
            */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

           /*
            * Read bytes to the Buffer until there is nothing more to read(-1).
            */
            FileOutputStream fos = new FileOutputStream(file);

            int current = 0;
            while ((current = bis.read()) != -1) {
                fos.write(current);
            }

            fos.write(bis.read());
            fos.flush();
            fos.close();
            Log.d("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("DownloadManager", "Error: " + e);
        }

    }
}
