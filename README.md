# react-native-system-applications

<p style = "text-align: justify">This is a simple multi-purpose react-native module which is designed to perform various activities on android phones. The following tasks can be performed with the help of this module.</p>

* Record audio and video
* Take multiple instantaneous pictures
* Call a number or get call logs from a device
* Send SMS
* Get a user picked contact or all contacts from a device
* Schedule an alarm notification and get notified.
* Open files through a file picker and url or obtain a given file's path
* Access and control the device's volume, ringer modes, wifi, bluetooth and brightness.

<p style = "text-align: justify">All these tasks can be performed by importing just one field from the module. However, one may not be interested in performing all these tasks and the module is designed keeping this in mind. As a result, the module is comprised of twelve loosely coupled sub-modules. This, to the least, reduces the number of required permissions which would be added if the sub-modules weren't loosely coupled. You just need to call the sub-module you are intersted in and add only the required permssions (if any) by that sub-module and you can achieve the intended task.</p>

# Getting Started

### Installation

>>Using npm
>>
>>>>```$ npm install react-native-system-applications --save```

>>Using yarn

>>>>```$ yarn add react-native-system-applications```

### Linking
There are two options for linking:
##### 1. Automatic

```react-native link react-native-system-applications```
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
The module is simple to use. Just import the main module, call one of the twelve submodules and invoke the methods. The main module is imported like so:

```import Sysapps from 'react-native-system-applications';```

You can also use your custom name for the main module without any loss of functionality as below:

```import mySystemApp from 'react-native-system-applications';```

<p style = "text-align: justify">For simplicity and modularity reasons, the module is comprised of the following loosely coupled sub-modules. Each submodule is independent of the other, and you just need to call the module which you are interested in via the above imported field.</p>

 * [audio](./docs/audio.md)
 * [video](./docs/video.md)
 * [images](./docs/images.md)
 * [contacts](./docs/contacts.md)
 * [calls](./docs/calls.md)
 * [files](./docs/files.md)
 * [sms](./docs/sms.md)
 * [volume](./docs/volume.md)
 * [bluetooth](./docs/bluetooth.md)
 * [wifi](./docs/wifi.md)
 * [brightness](./docs/brightness.md)
 * [alarm](./docs/alarm.md)


<p style = "text-align: justify">For instance, if you want to use the <code>wifi</code> module, call it like so:

 ```Sysapps.wifi```
<p style = "text-align: justify">Then, invoke the methods which the resulting object contains. For instance, to enable wifi on the device, what you have to do is: </p>
 ```Sysapps.wifi.enable()```

<p style = "text-align: justify">The links to the above sub-modules (we call them modules from now on) leads to the documentation for each module. The documentation contains detailed information about the usage and required configurations of each module. If you find going through all the documentation is time consuming, you can also find a brief summary in just one page [here](./docs/summary.md).</p>

## Issues or suggestions?
In some cases, the module might work only for devices with higher API levels. You might have issues if you are working with older API levels. For such and other  issues or if you want to suggest something , you can write it [here](https://github.com/Asaye/react-native-system-applications/issues).
