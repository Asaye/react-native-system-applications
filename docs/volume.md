# Volume
<p style = "text-align: justify">This module is used to change or obtain the existing volume states of the device.</p> 

## Functions
<p style = "text-align: justify">The volume module contains the following functions.</p>

``` 
    indexOf(String type)
    indexTo(String type, int index)
    silence()
    normalize()
    vibrate() 
```

### Permissions

<p style = "text-align: justify">For API levels below 23, no permission is required to use all the functions in this module. In addition, to call <code>indexOf()</code> and <code>indexTo()</code>  functions of the module, no permission is required for all API levels released till date (API level 28). However, for API level 23 and above, users should grant access to Do Not Disturb configuration in order to make a successful calls to the <code>silence()</code>, <code>normalize()</code> and <code>vibrate()</code> functions. As a result, the following permission should be added in the AndroidManifest.xml file outside the application tag.</p>

```     <uses-permission android:name="android.permission.ACCESS_NOTIFICATION_POLICY" /> ```


### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### &#x1F537; indexOf(String type): 

<p style = "text-align: justify">is used to obtain the current, minimum and maximum volume indices of the audio stream of the device as specified by <code>type</code> parameter. The possible values for <code>type</code> are: <code>"alarm"</code> , <code>"music"</code> , <code>"notification"</code> , <code>"ring"</code> , <code>"voicecall"</code> and  <code>"system"</code>.</p>

##### Sample code snippet
``` 
            import { Volume } from "react-native-system-applications";
            ...
            ...
            ...
            _getAudioVolume = () => {
                Volume.indexOf("system").then((res) => {
                    console.log(res); // see the output format below
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

##### Sample result 
<p style = "text-align: justify">Call to  <code>_getAudioVolume()</code> may result in the following log output depending on the type of the divice used and the current volume state of the <code>system</code> stream of the device. For unseccessful requests, a promise rejection will be sent back.</p>

```
                {
                  "minimum": 0, 
                  "maximum": 15, 
                  "current": 5 
                }
```

#### &#x1F537; indexTo(String type, int index): 

<p style = "text-align: justify"> is used to set the current index of the audio stream of the device as specified by the <code>type</code> parameter. The possible values for <code>type</code> are: <code>"alarm"</code> , <code>"music"</code> , <code>"notification"</code> , <code>"ring"</code> , <code>"voicecall"</code> and  <code>"system"</code>. Note that if the value of <code>index</code> parameter is not between the minimum and maximum audio indices  of the device, the intended outcome may not be achieved. The minimum and maximum indices can be requested by calling the above function. </p>

##### Sample code snippet

``` 
            import { Volume } from "react-native-system-applications";
            ...
            ...
            ...
            _setAudioVolume = () => {
                Volume.indexTo("music", 7).then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```
<p style = "text-align: justify">Call to  <code>_setAudioVolume()</code> sets the audio index of the music stream to 7 for successful requests or a promise rejection if something goes wrong.</p>

#### &#x1F537; silence(): 

<p style = "text-align: justify"> is used to set the ringer mode of the device to silent with no vibration. </p>

##### Sample code snippet

``` 
            import { Volume } from "react-native-system-applications";
            ...
            ...
            ...
            _silenceRinger = () => {
                Volume.silence().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```
<p style = "text-align: justify">Call to  <code>_silenceRinger()</code> sets the ringer mode of the device to silent with no vibration for successful requests or a promise rejection if something goes wrong.</p>

#### &#x1F537; vibrate(): 

<p style = "text-align: justify"> is used to set the ringer mode of the device to a mode that will be silent and will vibrate. </p>

##### Sample code snippet

``` 
            import { Volume } from "react-native-system-applications";
            ...
            ...
            ...
            _vibrateRinger = () => {
                Volume.vibrate().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```
<p style = "text-align: justify">Call to  <code>_vibrateRinger()</code> sets the ringer mode of the device to silent but with vibration for successful requests or a promise rejection if something goes wrong.</p>

#### &#x1F537; normalize(): 

<p style = "text-align: justify"> is used to set the ringer mode of the device to a mode that may be audible and may vibrate. It will be audible if the volume before changing out of this mode was audible. It will vibrate if the vibrate setting is on. </p>

##### Sample code snippet

```
            import { Volume } from "react-native-system-applications";
            ...
            ...
            ...
            _normalizeRinger = () => {
                Volume.normalize().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">Call to  <code>_normalizeRinger()</code> sets the ringer mode of the device to normal for successful requests or a promise rejection if something goes wrong.</p>
