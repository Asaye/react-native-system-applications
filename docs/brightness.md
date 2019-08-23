 # Brightness

 <p style = "text-align: justify">This module is used to set or get the current brightness index of the device. The module is accessed by calling the brightness module via <code>Sysapps.brightness</code>.</p> 
 
 ## Functions

<p style = "text-align: justify">The brightness module contains the following functions.</p>

 ```
         index()
         indexTo(int index)
 ```
 

### Permissions
 <p style = "text-align: justify">To access the current brightness index of the screen with <code>index()</code> function, no permission is required. However, in order to change the bluetooth state of the device with <code>enable()</code> and <code>disable()</code> functions, the following permissions should be included in the AndroidManifest.xml file.</p>
 
 ```
      <uses-permission android:name="android.permission.BLUETOOTH"/>
      <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
      <uses-feature android:name="android.hardware.bluetooth" />
```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### index(): 

<p style = "text-align: justify">is used to get the current brightness index of the screen.</p>
 ##### Sample code snippet
 
 ```
      _getBrightness = async () => {
           const brightness = await Sysbluetooth.brightness.index();
             console.log(brightness);
          } 
 ```
 <p style = "text-align: justify">Call to  <code>_getBrightness()</code> will attempt to obtain the brightness index of the screen and the result will be logges as below.If something goes wrong during the request, a promise rejection will be sent.</p>
 
##### Sample result
```
        {
        	"brightness": 100
        }
```

#### indexTo(int index): 
<p style = "text-align: justify">is used to set the brightness index of the device to an index spacified by <code>index</code> parameter. The value of <code>index</code> parameter should be between between 0 and 255 inclusive. If not, a promise rejection will be sent.</p>

##### Sample code snippet
```
            _setBrightnessValue = async () => {
                 await Sysapps.brightness.indexTo(100);
             } 
```
<p style = "text-align: justify">After the  <code>_setBrightnessValue()</code> function is called, the brightness of the screen will be set to <code>0.3</code> for successful requests or a promise rejection will be sent if something goes wrong.</p>
