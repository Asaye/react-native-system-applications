# Files
<p style = "text-align: justify">This module is used to open any file from the device for a given file path and to open a file picker dialog so that the user can pick any file from the device.</p>

## Functions

<p style = "text-align: justify">The files module contains the following functions.</p>

``` 
        open(String path)
        getPath()
        pick()
        download(String src, String dest)
        upload(String dest)
```

### Permissions

No permission is required open a file picker dialog with <code>getPath()</code> function and to obtain the path of the selected file as a response. However, to download a file and to save it on the device with <code>download()</code> function, only the first configuration, i.e., adding the storage permission,  is required. In addition, to open a file for a given path from the device with <code>open()</code> function or to pick a file from a dialog and to open it via <code>pick()</code> function, the following  configurations should be made.
For API level 23 and below devices, only the first configuration is required. For API level 24 and above, all the three configurations are mandatory. The third configuration can be changed according to official android [documentation](https://developer.android.com/reference/android/support/v4/content/FileProvider)  regarding <code>FileProvider</code> class.

1. Add the following permission outside the application tage of the AndroidManifest.xml file.

 ```    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/> ```

2. Add the following provider tag within the application tag in the AndroidManifest.xml file.</p>

  ```
        <provider
                 android:name="android.support.v4.content.FileProvider"
                 android:authorities="${applicationId}"
                 android:exported="false"
                 android:grantUriPermissions="true">
                 <meta-data
                     android:name="android.support.FILE_PROVIDER_PATHS"
                     android:resource="@xml/provider_paths">
                 </meta-data>
         </provider>
```

3. Create a folder named <code>xml</code> inside <code>yourAppName\android\app\src\main\res</code> (if there isn't any) and create a file named <code>provider_paths.xml</code> inside the folder and add the following content in the file. As stated above, the contents of this file can be changed to meet your desired accessible file locations.

```
    <?xml version='1.0' encoding='utf-8'?>
        <paths xmlns:android="http://schemas.android.com/apk/res/android">
            <root-path name="root" path=""/>
            <files-path name="files" path="/" />
            <external-files-path name="external_files" path="" />
            <external-path name="external" path="." />
            <cache-path name="cache" path="/" />
        </paths>
```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### &#x1F537; open(String path): 

<p style = "text-align: justify">is used to open a file from a location specified by the <code>path</code> parameter. A number of common file types will be opened by calling this function via the default app on the device. Note that the device should have an installed app to open the given file type. For instance, if you want to open a pdf file, the file will be opened successfully only if there is a pdf viewer installed on the device.</p>

##### Sample code snippet

```
            import { Files } from "react-native-system-applications";
            ....
            ....
            .... 
            _openFile = () => {
                const path = "/storage/emulated/0/DCIM/Camera/myPic.jpg";
                Files.open(path).then((res) => {
                    // do something
                }).catch((err) => {
                  console.log(err);
                });
            } 
```
<p style = "text-align: justify">Call to  <code>_openFile()</code> will open the specified file for successful requests or a promise rejection will be sent if something goes wrong.</p>

#### &#x1F537; getPath(): 

<p style = "text-align: justify">is used to get a path of a file where a  file picker dialog will be opened and the user picks any file from the device. For successful requests, the path of the selected file will be sent as a response or a promise rejection otherwise. </p>

##### Sample code snippet

``` 
            import { Files } from "react-native-system-applications";
            ....
            ....
            .... 
            _getFilePath = () => {
                Files.getPath().then((res) => {
                    console.log(res);  // see the output format below
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

##### Sample result 
<p style = "text-align: justify">Call to  <code>_getFilePath()</code> function opens a file picker dialog, waits for a user to select a file and logs the path of the selected file. A sample response may look like the following:

```
                "file:///storage/emulated/0/Download/myfile.mp4"
```

#### &#x1F537; pick(): 

<p style = "text-align: justify">is used to open  file picker dialog so that the user can pick and open a file from the device. Note that, for API level 24 and above, in order to successfully open the desired file, the correct configuration should be made to grant access to the desired file location as described above. </p>

##### Sample code snippet

``` 
            import { Files } from "react-native-system-applications";
            ....
            ....
            .... 
            _openFileFromPicker = () => {
                Files.pick().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```


<p style = "text-align: justify">Call to  <code>_openFileFromPicker()</code> function opens a file picker dialog, waits for a user to select a file and opens the file if access is rightfully granted.</p>

#### &#x1F537; download(String src, String dest): 

<p style = "text-align: justify">is used to download file from <code>src</code> to <code>dest</code>. The <code>src</code> parameter can be either a file location on the device or a webservice. If it is the former one, the actual file location should be preceded by a protocol, i.e., <code>file://</code>. The <code>dest</code> parameter can be either a presumed complete file path or a directory where the downloaded file will be stored. You use the former one if you know the file mime type before downloading and you want to give your own custom name to the file. Where as the latter is used if you want the downloaded file to take the same name as it has in the source location.</p>

##### Sample code snippet

``` 
            import { Files } from "react-native-system-applications";
            ....
            ....
            .... 
            _downloadFile = () => {
                  const src = "http://www.mywebservice.com/download/myVideo.mp4",
                        dest = "/storage/emulated/0/videos/";
                  Files.download(src, dest).then((res) => {
                      // file downloaded
                  }).catch((err) => {
                      console.log(err);
                  });
            } 
```

<p style = "text-align: justify">Call to  <code>_downloadFile()</code> function downloads a file from the webservice specified by <code>src</code> variable and saves it in a directory specified by <code>dest</code> variable.</p>

#### &#x1F537; upload(String dest): 

<p style = "text-align: justify">is used to upload a file picked from the device on to a webservice specified by <code>dest</code> parameter. Note that, for API level 24 and above, in order to successfully upload the desired file, the correct configuration should be made to grant access to the desired file location as described above. </p>

##### Sample code snippet

``` 
            import { Files } from "react-native-system-applications";
            ....
            ....
            .... 
            _uploadFile = () => {
                const dest = "http://www.mywebservice.com/upload";
                Files.upload(dest).then((res) => {
                     // file uploaded
                }).catch((err) => {
                     console.log(err);
                });
            } 
```

##### Sample result 
<p style = "text-align: justify">Call to  <code>_uploadFile()</code> function opens a file picker dialog, waits for a user to select a file and uploads the selected file to a webservice specified by <code>dest</code> parameter if access is rightfully granted.</p>
