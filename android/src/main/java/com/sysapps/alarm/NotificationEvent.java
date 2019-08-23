package com.sysapps.alarm;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import android.os.PersistableBundle;
import android.content.Context;
import org.json.JSONObject;

public class NotificationEvent extends ReactContextBaseJavaModule {

	private static ReactApplicationContext mReactContext;

	public NotificationEvent(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;      
    }

    @Override
    public String getName() {
        return "NotificationEvent";
    }

	public static void emitEvent(PersistableBundle pBundle) {

		if (mReactContext == null) {
            return;
        }
		
		String response;

		try {
			JSONObject json = new JSONObject();
            for (String key: pBundle.keySet()) {
                if (pBundle.getString(key) != null) {
                    json.put(key, pBundle.getString(key)); 
                } else if (pBundle.get(key) instanceof Double) {
                    json.put(key, pBundle.getDouble(key)); 
                } else if (pBundle.get(key) instanceof Boolean) {
                    json.put(key, pBundle.getBoolean(key)); 
                }                               
            } 
            response = json.toString();
        } catch (Exception ex){
        	response = ex.getMessage();
        }

        mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
  				.emit("onNotification", response);           	
	}
}