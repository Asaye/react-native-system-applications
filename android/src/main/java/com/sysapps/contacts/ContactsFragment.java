package com.sysapps.contacts;

import com.sysapps.utils.Table;

import android.content.Context;
import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ContactsFragment extends Table {
	
	private int[] mColIndices = null;

	public ContactsFragment(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
		getCursor();
	}

	public static ContactsFragment newInstance(Context context, Activity activity) {		
		return new ContactsFragment(context, activity);
	}

    private void getCursor() {  

    	this.fileName = "contacts.pdf";
	    this.titles = new String[] { "No.", "Name", "Phone Number" };
	    this.cursor = this.context.getContentResolver()
	                        .query(Phone.CONTENT_URI, null, null, null, null);

	    if (this.cursor != null) {
	    	int nameIndex = this.cursor.getColumnIndex(Contacts.DISPLAY_NAME);
	    	int numberIndex = this.cursor.getColumnIndex(Phone.NUMBER);
	    	mColIndices = new int[] { nameIndex, numberIndex };
	    }
    }

    @Override
    protected void getRowData(Cursor cursor, List rowData) {
    	for (int index: mColIndices) {
           	rowData.add(cursor.getString(index));
        }
    }
}
