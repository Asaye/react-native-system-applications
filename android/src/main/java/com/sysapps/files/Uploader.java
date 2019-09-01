package com.sysapps.files;

import com.facebook.react.bridge.Promise;

import java.lang.Exception;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.File;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.lang.Runnable;

public class Uploader implements Runnable {

	private static final String TAG = "FILE_UPLOAD_ERROR";

	private String mDestination;
	private File mFile;
	private Promise mPromise;

	public Uploader(File file, String dest, Promise promise) {
		mFile = file;
		mDestination = dest;
		mPromise = promise;
	}

	@Override
	public void run() {
		
        HttpURLConnection conn = null;
        DataOutputStream output = null;  
        FileInputStream input = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String fileName = mFile.getName();           
        
        try { 

        	URL url = new URL(mDestination);

			input = new FileInputStream(mFile);		

			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); 
			conn.setDoOutput(true); 
			conn.setUseCaches(false); 
			conn.setChunkedStreamingMode(0);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("file", fileName); 

			output = new DataOutputStream(conn.getOutputStream());

			output.writeBytes(twoHyphens + boundary + lineEnd); 
			output.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
			                         + fileName + "\"" + lineEnd);
			output.writeBytes(lineEnd);

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}           

			output.writeBytes(lineEnd);
			output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			output.flush();
			
			String responseMessage = conn.getResponseMessage();
			int responseCode = conn.getResponseCode();
			mPromise.resolve("" + responseCode + " : " + responseMessage);	

		} catch (Exception ex) {
			mPromise.reject(TAG, ex.getMessage());
		} finally {
			if (conn != null) conn.disconnect();
			try {
				if (input != null) input.close();		
				if (output != null) output.close();
			} catch (Exception e) {
				mPromise.reject(TAG, e.getMessage());
			}
		}
	}
}