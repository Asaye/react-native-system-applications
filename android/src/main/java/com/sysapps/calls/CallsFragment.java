package com.sysapps.calls;

import com.sysapps.utils.Table;

import android.content.Context;
import android.app.Activity;
import android.database.Cursor;
import android.provider.CallLog.Calls;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CallsFragment extends Table {

	private int[] mColIndices = null;

	public CallsFragment(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
		this.ROWS_PER_PAGE = 15;
		this.PAGE_WIDTH = 842 - 2*72;
		this.PAGE_HEIGHT = 595 - 2*72;
		getCursor();
	}

	public static CallsFragment newInstance(Context context, Activity activity) {		
		return new CallsFragment(context, activity);
	}

    private void getCursor() {  

    	this.fileName = "calls.pdf";
	    this.titles = new String[] { "No.", "Phone Number", "Type", "Date", "Duration" };
	    this.cursor = this.context.getContentResolver()
              .query(Calls.CONTENT_URI, null, null, null, null);

	    if (this.cursor != null) {
	    	int number = cursor.getColumnIndex(Calls.NUMBER);
	        int type = cursor.getColumnIndex(Calls.TYPE);
	        int date = cursor.getColumnIndex(Calls.DATE);
	        int duration = cursor.getColumnIndex(Calls.DURATION);

		    mColIndices = new int[] { number, type, date, duration };
	    }
    }

    @Override
    protected void getRowData(Cursor cursor, List rowData) {
    	int i = 0;    	 
    	for (int index: mColIndices) {
    		if (i == 1) {
    			String type = getType(cursor, index);
    			rowData.add(type);
    		} else if (i == 2) {
    			String str = cursor.getString(index);
    			long millis = Long.parseLong(str);
    			Date date = new Date(millis);
    			String day = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);    			
    			rowData.add(day);
    		} else {
           		rowData.add(cursor.getString(index));
            }
           	i++;
        }
    }

    private String getType(Cursor cursor, int index) {
    	int callType_int = Integer.parseInt(cursor.getString(index));
        String callType = "";

        if (Calls.OUTGOING_TYPE == callType_int) callType = "OUTGOING";
        else if (Calls.INCOMING_TYPE == callType_int) callType = "INCOMING";
        else if (Calls.MISSED_TYPE == callType_int) callType = "MISSED"; 

        return callType;
    }
}
