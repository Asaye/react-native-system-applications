# Wifi
<p style = "text-align: justify">This module is used to change or obtain the current wifi state of the device. The module is accessed by calling the wifi module via <code>Sysapps.wifi</code>.</p> 

## Functions
<p style = "text-align: justify">The wifi module contains the following functions.</p>

``` 
    enable()
    disable()
    isEnabled()
```

### Permissions
<p style = "text-align: justify">In order to change the wifi state of the device with <code>enable()</code> and <code>disable()</code> functions, the following permission should be included in the AndroidManifest.xml file.</p>

 ```        <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>```

<p style = "text-align: justify">Besides,  to access the wifi state of the device with <code>isEnabled()</code> function, the following permission should be included in the AndroidManifest.xml file.</p>

​```        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### &#x1F537; enable(): 

<p style = "text-align: justify">is used to turn on the wifi state of the device.</p>

##### Sample code snippet

``` 
            import { Wifi } from "react-native-system-applications";
            ...
            ...
            ...
            _turnOnWifi = () => {
                Wifi.enable().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">Call to  <code>_turnOnWifi()</code> will turn on the wifi state of the device for successful requests or will result in a promise rejection if something goes wrong.</p>

#### &#x1F537; disable(): 

<p style = "text-align: justify">is used to turn off the wifi state of the device.</p>

##### Sample code snippet

```
            import { Wifi } from "react-native-system-applications";
            ...
            ...
            ... 
            _turnOffWifi = () => {
                Wifi.disable().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">After the  <code>_turnOffWifi()</code> function is called, the wifi state of the device will be turned off for successful requests or a promise rejection will be sent if something goes wrong.</p>

#### &#x1F537; isEnabled(): 

<p style = "text-align: justify">is used to obtain the current wifi state of the device, i.e., whether the wifi state is enabled or disabled.</p>

##### Sample code snippet

``` 
            import { Wifi } from "react-native-system-applications";
            ...
            ...
            ...
            _getWifiState = () => {
                Wifi.isEnabled().then((res) => {
                    if (res) {
                       // do something 
                   }                    
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">Call to  <code> _getWifiState()</code> will attempt to get the current wifi state of the device and the task within the if-statement will be executed if wifi is enabled. A promise rejection will be sent for unsuccessful requests.</p>
