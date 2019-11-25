# Image
<p style = "text-align: justify">This module is used to capture images using hardwares installed on the device. The module is accessed by calling the image module via <code>Sysapps.image</code>.</p> 

## Functions

<p style = "text-align: justify">The image module contains the following functions.</p>

``` 
    prepare(Object options)
    capture()
    exitCapture()
```

### Permissions

<p style = "text-align: justify">In order to capture images, the following permissions should be included in the AndroidManifest.xml file.</p>

```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-feature android:name="android.hardware.camera" android:required="true" />
```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### &#x1F537; prepare(Object options): 

<p style = "text-align: justify">is used to prepare an environment for image capturing and to display a preview. The <code>options</code> parameter can have the properties shown below. 
    
<table>
<tr><th>Prop</th><th>Required</th><th>Default</th><th style =  "width: 150px">Type</th><th>Description</th></tr>
<tr><td>width</td><td>false</td><td>-</td><td>int</td><td style = "text-align: justify">The width of the captured images.</td></tr>
<tr><td>height</td><td>false</td><td>-</td><td>int</td><td style = "text-align: justify">The height of the captured images.</td></tr>
<tr><td>outputFolder </td><td>true</td><td>-</td><td>String</td><td style = "text-align: justify">The path to the folder where the captured images will be saved.</td></tr>
<tr><td>cameraType</td><td> false</td><td>"BACK"</td><td>enum( "BACK", "FRONT")</td><td style = "text-align: justify">The type of camera to be used for capturing.</td></tr>
</table>

All the above properties except the <code>outputFolder</code> are optional. If you  don't specify them, default values ,as described above, will be assigned to those properties during runtime.

##### Sample code snippet
 ```
        import { Image } from "react-native-system-applications";
        ...
        ...
        ...
        _prepareCaptureEnv = () => {
        	const params = { "outputFolder": "images/", "cameraType": "FRONT" };
        	Image.prepare(params);
        } 
 ```
<p style = "text-align: justify">Invoking the <code>_prepareCaptureEnv()</code> function prepares environment for image capturing using the front camera so that the output will be saved in <code>images</code> folder (don't forget the last backslash). This makes the device to be ready for capturing and a user interface with a preview having a CAPTURE button appears on the screen of the device. After this, there are two options to take pictures. The first one is to call the <code>capture()</code> function programmatically. And the second option is to manually press the CAPTURE button on the created user interface.</p>

#### &#x1F537; capture(): 

<p style = "text-align: justify">is used to capture images. Calling this function is equivalent to pressing the CAPTURE button. After calling the <code>prepare()</code> function and before calling this one, I recommend to provide at least three seconds (3000ms) gap to make sure that the capturing environment is prepared and ready to take pictures. Once the preview is started, the image capturing can be terminated by:</p>

* explicitly calling <code>exitCapture()</code> function.
* pressing the back button.

<p style = "text-align: justify">Note that once the preview starts, multiple pictures can be taken by either calling the <code>capture()</code> function or by pressing the CAPTURE until the capturing is terminated by pressing the back button or calling the <code>exitCapture()</code> function.</p>

##### Sample code snippet
```
        import { Image } from "react-native-system-applications";
        ...
        ...
        ...
        _takePictures = () => {
        	setTimeout(() => {
        		Image.capture()
        	}, 3000);        	
        } 
```

<p style = "text-align: justify">Invoking the <code>_takePictures()</code> function captures an image after 3000ms.</p>


#### &#x1F537; exitCapture(): 

<p style = "text-align: justify">is used to exit the image capturing activity. Calling this function is equivalent to pressing the back button. No further pictures can be taken after calling this funtion.</p>

##### Sample code snippet
```
            import { Image } from "react-native-system-applications";
            ...
            ...
            ...
            _exitImageCapture = () => {
                Image.exitCapture();
            } 
```
<p style = "text-align: justify">Invoking the <code>_exitImageCapture()</code> function aborts an ongoing image capturing activity and closes preview.</p>
