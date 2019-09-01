package com.sysapps.files;

import com.facebook.react.bridge.Promise;

import java.lang.Exception;
import java.lang.Runnable;
import java.io.File;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.List;

public class Downloader implements Runnable {

	private static final String TAG = "FILE_DOWNLOAD_ERROR";

	private String mDestination;
	private String mSource;
	private Promise mPromise;

	public Downloader(String src, String dest, Promise promise) {
		mSource = src;
		mDestination = dest;
		mPromise = promise;
	}

	@Override
	public void run() {
		if (mSource == null) return;
        if (mDestination == null) return;

        DataInputStream input = null;
        FileOutputStream output = null;
        HttpURLConnection conn = null;
        File destFile = null;
       
        try {
            
            URL url = new URL(mSource);
            
            destFile = new File(mDestination);

            if (destFile.isDirectory()) {
                conn = (HttpURLConnection) url.openConnection(); 
                conn.setDoInput(true); 
                conn.setUseCaches(false); 
                conn.setChunkedStreamingMode(0);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                Map map = conn.getHeaderFields();
                List col = (List) map.get("Content-Disposition");
                if (col != null) {
                    String fileName = null;
                    for (Object key: col) {
                        String data = (String) key;
                        if (data.indexOf("fileName") != -1) {
                            fileName =  data.split("=")[1];
                            break;
                        }                    
                    }
                    destFile = new File(mDestination + fileName);
                }                
            }

            input = new DataInputStream(url.openStream());
            output = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }              
            
            output.flush();
            mPromise.resolve("File downloaded successfully.");

        } catch (Exception ex) {
            mPromise.reject(TAG, ex.getMessage());
        } finally {
            if (conn != null) conn.disconnect();            
            try {
                if (input != null) input.close();
                if (output != null) output.close();
            } catch(Exception e) {
                mPromise.reject(TAG, e.getMessage());
            }
        }
	}
}