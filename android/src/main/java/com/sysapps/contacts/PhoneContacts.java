package com.sysapps.contacts;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import android.net.Uri;
import android.os.Process;
import android.database.Cursor;
import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class PhoneContacts extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "READ_CONTACTS_ERROR";
    private static final int PERMISSIONS_REQUEST_CODE = 10040; 
    private static final int REQUEST_READ_CONTACTS = 10041;

    private static final String[] PERMISSIONS = 
                        new String[] {   
                            Manifest.permission.READ_CONTACTS,
                        };

    private boolean mIsPermissionGranted = false;

    private Promise mContactsPromise;
    private String mType;

    private final ActivityEventListener contactsEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == REQUEST_READ_CONTACTS) {
                if (mContactsPromise != null && resultCode == Activity.RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri == null) {
                        mContactsPromise.reject(TAG, "No contact data found");
                        mContactsPromise = null;
                    } else {
                        getContacts(uri);
                    }
                } else if (mContactsPromise != null && resultCode == Activity.RESULT_CANCELED) {                    
                    mContactsPromise.reject(TAG, "User canceled the contact picker window.");                        
                    mContactsPromise = null;
                }
            } 
        } 
    };  

    public PhoneContacts(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(contactsEventListener);
    }

    @Override
    public String getName() {
        return "Contacts";
    }

    @Override
    public boolean onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && 
            grantResults.length == PERMISSIONS.length) {
            mIsPermissionGranted = true;
            if (mType.equals("picker")) {
                pickContacts();
            } else if (mType.equals("all")) {
                getContacts();
            }
        } else {
            mContactsPromise.reject(TAG, "User canceled permission request."); 
        }
        return true;
    } 

    @ReactMethod
    public void pick(Promise promise) {
        Activity activity = getCurrentActivity();

        assert activity != null;
        mType = "picker";
        mContactsPromise = promise;
        try {
            checkPermission();         
        } catch(Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    @ReactMethod
    public void getAll(Promise promise) {
        Activity activity = getCurrentActivity(); 
        
        assert activity != null;
        mType = "all";        
        mContactsPromise = promise; 
        try { 
            checkPermission();            
        } catch(Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    private void checkPermission() {        
        int pid = Process.myPid();
        int uid = Process.myPid();
        int status_ok = PackageManager.PERMISSION_GRANTED;
        Activity activity = getCurrentActivity();
        Context context = getReactApplicationContext().getBaseContext();

        mIsPermissionGranted = true;
        for (String permission : PERMISSIONS) {
            if (context.checkPermission(permission, pid, uid) != status_ok) {
                mIsPermissionGranted = false;
                ((PermissionAwareActivity) activity)
                        .requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE, this);
                break;
            }
        }

        if (mIsPermissionGranted && mType.equals("picker")) {
            pickContacts();
        } else if (mIsPermissionGranted && mType.equals("all")) {
            getContacts();
        }        
    }
    private void pickContacts() {
        Activity activity = getCurrentActivity();

        Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        intent.setType(Phone.CONTENT_TYPE);  
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, REQUEST_READ_CONTACTS);
        }
    }

    private void getContacts() {
        Cursor cursor = getReactApplicationContext().getContentResolver()
                        .query(Phone.CONTENT_URI, null, null, null, null);
        sendContacts(cursor);
    }

    private void getContacts(Uri uri) {
        String[] projection = {Contacts.DISPLAY_NAME, Phone.NUMBER};   
        Cursor cursor = getReactApplicationContext().getContentResolver()
                        .query(uri, projection, null, null, null);
        sendContacts(cursor);
    }

    private void sendContacts(Cursor cursor) {

        WritableMap map = null;
        WritableArray array = new WritableNativeArray();

        int name = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
        int number = cursor.getColumnIndex(Phone.NUMBER);

        if (cursor.moveToFirst()) {
            do {
                map = new WritableNativeMap();
                map.putString("Number", cursor.getString(number));
                map.putString("Name", cursor.getString(name));                
                array.pushMap(map);
            } while (cursor.moveToNext());
            mContactsPromise.resolve(array);
        } else {
            mContactsPromise.reject(TAG, "No contact could be retrieved.");
        }

        if (cursor != null) {
            cursor.close();
        }
        mContactsPromise = null;
    }
}
