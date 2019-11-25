# Bluetooth

<p style = "text-align: justify">This module is used to change or obtain the current bluetooth state of the device. The module is accessed by calling the bluetooth module via <code>Sysapps.bluetooth</code>.</p>

## Functions

<p style = "text-align: justify">The bluetooth module contains the following functions.</p>

```
    enable()
    disable()
    isEnabled()
```

### Permissions

<p style = "text-align: justify">To access the bluetooth state of the device with <code>isEnabled()</code> function, no permission is required. However, in order to change the bluetooth state of the device with <code>enable()</code> and <code>disable()</code> functions, the following permissions should be included in the AndroidManifest.xml file.</p>

 ```
        <uses-permission android:name="android.permission.BLUETOOTH"/>
        <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
        <uses-feature android:name="android.hardware.bluetooth" />
 ```


### Description

<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### enable(): 

<p style = "text-align: justify">is used to turn on the bluetooth state of the device.</p>

##### Sample code snippet

 ```
            import { Bluetooth } from "react-native-system-applications";
            ....
            ....
            ....
            _turnOnBluetooth = () => {
                Bluetooth.enable().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
 ```

<p style = "text-align: justify">Call to  <code>_turnOnBluetooth()</code> will turn on the bluetooth state of the device for successful requests or will result in a promise rejection if something goes wrong.</p>

#### disable(): 

<p style = "text-align: justify">is used to turn off the bluetooth state of the device.</p>

##### Sample code snippet

```
            import { Bluetooth } from "react-native-system-applications";
            ....
            ....
            ....
            _turnOffBluetooth = () => {
                Bluetooth.disable().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">After the  <code>_turnOffBluetooth()</code> function is called, the bluetooth state of the device will be turned off for successful requests or a promise rejection will be sent if something goes wrong.</p>

#### isEnabled(): 

<p style = "text-align: justify">is used to obtain the current bluetooth state of the device, i.e., whether the bluetooth state is enabled or disabled.</p>

##### Sample code snippet

```
            import { Bluetooth } from "react-native-system-applications";
            ....
            ....
            ....
            _getBluetoothState = () => {
                Bluetooth.isEnabled().then((res) => {
                    if (res) {
                        // bluetooth is enabled
                    }
                }).catch((err) => {
                    console.log(err);
                });                
            } 
```

<p style = "text-align: justify">Call to  <code> _getBluetoothState()</code> will attempt to get the current bluetooth state of the device and the task within the if-statement will be executed if bluetooth is enabled. A promise rejection will be sent for unsuccessful requests.</p>
