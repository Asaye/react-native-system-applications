package com.sysapps;

import com.sysapps.volume.Volume;
 import com.sysapps.files.Files;
 import com.sysapps.wifi.Wifi;
 import com.sysapps.bluetooth.Bluetooth;
 import com.sysapps.brightness.Brightness;
 import com.sysapps.alarm.Alarm;
 import com.sysapps.alarm.NotificationEvent;
 import com.sysapps.calls.PhoneCalls;
 import com.sysapps.contacts.PhoneContacts;
import com.sysapps.sms.Sms;
 import com.sysapps.image.ImageCapturer;
 import com.sysapps.audio.AudioRecorder;
 import com.sysapps.video.VideoRecorder;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SysappsPackage implements ReactPackage {

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Collections.emptyList();
  }

  @Override
  public List<NativeModule> createNativeModules(
                              ReactApplicationContext reactContext) {
    List<NativeModule> modules = new ArrayList<>();

    modules.add(new Volume(reactContext));
     modules.add(new Files(reactContext));
     modules.add(new Wifi(reactContext));
     modules.add(new Bluetooth(reactContext));
     modules.add(new Brightness(reactContext));
     modules.add(new Alarm(reactContext));
     modules.add(new NotificationEvent(reactContext));
    modules.add(new PhoneCalls(reactContext));
     modules.add(new Sms(reactContext));
     modules.add(new PhoneContacts(reactContext));
     modules.add(new ImageCapturer(reactContext));
     modules.add(new AudioRecorder(reactContext));
     modules.add(new VideoRecorder(reactContext));

    return modules;
  }

}