# react-native-system-applications

<p style = "text-align: justify">This is a simple multi-purpose react-native module which is designed to perform various activities on android phones. The following tasks can be performed with the help of this module.</p>

* Record audio and video
* Take pictures
* Call a number
* Send SMS
* Get or print call logs
* Get or print contacts
* Schedule an alarm notification.
* Download , upload or open files
* Control device settings.

<p style = "text-align: justify">To perform these tasks, the module offers the following twelve components.</p>

<table>
<tr><td>[Audio](./docs/audio.md)</td><td>[Video](./docs/video.md)</td><td>[Image](./docs/image.md)</td></tr>
<tr><td>[Contacts](./docs/contacts.md)</td><td>[Calls](./docs/calls.md)</td><td>[Files](./docs/files.md)</td></tr>
<tr><td>[Sms](./docs/sms.md)</td><td>[Volume](./docs/volume.md)</td><td>[Bluetooth](./docs/bluetooth.md)</td></tr>
<tr><td>[Wifi](./docs/wifi.md)</td><td>[Brightness](./docs/brightness.md)</td><td>[Alarm](./docs/alarm.md)</td></tr>
</table>

The links to the above sub-modules (we call them modules from now on) leads to the documentation for each module. The documentation contains detailed information about the usage and required configurations of each module. If you find going through all the documentation is time consuming, you can also find a brief summary in just one page [here](./docs/summary.md).

# Getting Started

### Installation

Using npm

```	$ npm install react-native-system-applications --save```

Using yarn

```	$ yarn add react-native-system-applications```

### Linking
There are two options for linking:
##### 1. Automatic

```	react-native link react-native-system-applications```
##### 2. Manual

If the automatic linking fails for some reason, you can do the linking manually as follows:
 * add the following to <code>yourAppName/android/settings.gradle</code> file:
 
 ```
 	include ':react-native-system-applications'
 	project(':react-native-system-applications').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-system-applications/android')
 ```

 * add the following inside the dependencies closure of  <code>yourAppName/android/app/build.gradle</code> file:
 ```
 	implementation project(':react-native-system-applications')
```

* add the following to your <code>MainApplication.java</code> file:
 ```
 	import com.sysapps.SysappsPackage;
 ```
 and also,
 ```
	@Override
	protected List<ReactPackage> getPackages() {
		return Arrays.<ReactPackage>asList(
			new MainReactPackage(),
			....
			new SysappsPackage()    <== Add this
		);
	}
 ```


### Usage
The module is simple to use. Just import the component that you want to work with and invoke the methods. For instance, the 
<code>Files</code> component is imported like so:

```   import { Files } from 'react-native-system-applications';```

Then, if you want to pick a file with a file picker, you can invoke the <code>pick()</code> function on the 
<code>Files</code> object as follows:

```    Files.pick()    ```

For versions below 2.0.0, you can achieve the same outcome as above using:

```   import Sysapps from 'react-native-system-applications';```

```   Sysapps.files.pick()    ```

This approach is still feasible with the recent versions and there is no issue regarding backward compatibility.

## Issues or suggestions?
In some cases, the module might work only for devices with higher API levels. You might have issues if you are working with older API levels. For such and other  issues or if you want to suggest something , you can write it [here](https://github.com/Asaye/react-native-system-applications/issues).
