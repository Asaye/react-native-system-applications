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
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class PhoneContacts extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "READ_CONTACTS_ERROR";
    private static final int PERMISSIONS_REQUEST_READ = 10040; 
    private static final int PERMISSIONS_REQUEST_PRINT = 10041; 
    private static final int REQUEST_READ_CONTACTS = 10042;

    private static final String[] PERMISSIONS_READ = 
                        new String[] {   
                            Manifest.permission.READ_CONTACTS,
                        };

    private static final String[] PERMISSIONS_PRINT = 
                        new String[] {   
                            Manifest.permission.READ_CONTACTS
                        };

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
        if (requestCode == PERMISSIONS_REQUEST_READ && 
            grantResults.length == PERMISSIONS_READ.length) {
            if (mType.equals("picker")) {
                pickContacts();
            } else if (mType.equals("all")) {
                getContacts();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_PRINT && 
            grantResults.length == PERMISSIONS_PRINT.length) {
            printContacts();
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

        if (checkPermission(PERMISSIONS_READ)) {
            pickContacts();
        } else {
            requestPermissions(PERMISSIONS_READ, PERMISSIONS_REQUEST_READ);
        }
    }

    @ReactMethod
    public void getAll(Promise promise) {
        Activity activity = getCurrentActivity(); 
        
        assert activity != null;
        mType = "all";        
        mContactsPromise = promise; 

        if (checkPermission(PERMISSIONS_READ)) {
            getContacts();
        } else {
            requestPermissions(PERMISSIONS_READ, PERMISSIONS_REQUEST_READ);
        }
    }

    @ReactMethod
    public void print(Promise promise) {
        if (checkPermission(PERMISSIONS_PRINT)) {
            printContacts();
        } else {
            requestPermissions(PERMISSIONS_PRINT, PERMISSIONS_REQUEST_PRINT);
        }        
    }

    private boolean checkPermission(String[] permissions) {        
        int pid = Process.myPid();
        int uid = Process.myUid();
        int status_ok = PackageManager.PERMISSION_GRANTED;    

        Context context = getReactApplicationContext().getBaseContext();
        
        for (String permission : permissions) {
            if (context.checkPermission(permission, pid, uid) != status_ok) {
                return false;
            }
        }

        return true;               
    }

    private void requestPermissions(String[] permissions, int requestCode) {
        Activity activity = getCurrentActivity();
        if (activity == null) return;

        ((PermissionAwareActivity) activity)
                        .requestPermissions(permissions, requestCode, this);
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

    private void printContacts() {
        Activity activity = getCurrentActivity();
        Context context = getReactApplicationContext();

        assert activity == null;          

        FragmentManager mFragmentManager = activity.getFragmentManager();
        ContactsFragment fragment = ContactsFragment.newInstance(context, activity);                  
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
 
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
}
