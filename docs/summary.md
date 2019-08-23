<h1 style = "color:#4285F4">Summary</h1>
<p style = "text-align: justify">In order to get the full functionality of this module, the basic steps are summarized below. This summary doesn't cover all the details and I strongly recommend referring to the detailed documentation if extra information is sought regarding the required permissions and configurations. This summary ,however, is useful as a quick reference concerning the capabilities of the module. To use this module, all you have to do is basically as follows:</p>
<h3 style = "color:#4285F4">Install</h3>
> >```$ npm install react-native-system-applications --save``` 

> or

> > ```$ yarn add react-native-system-applications```

<h3 style = "color:#4285F4">Link</h3>
> > ```react-native link react-native-system-applications```

<h3 style = "color:#4285F4">Import</h3>
> > ```import Sysapps from 'react-native-system-applications';```

<h3 style = "color:#4285F4">Implement</h3>
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./audio.md">Audio</a>

>>> ###### Prepare an audio recording environment so that the output will be saved in music folder. 

>>> > ``` Sysapps.audio.prepare({ "outputFolder": "music/" }); ``` 
>>>
>>> ###### Start audio recording. 

>>> > ``` 	Sysapps.audio.startRecording(); ``` 
>>>
>>> ###### Stop audio recording and prepare for another recording session. 

>>> > ``` 	Sysapps.audio.stopRecording(); ``` 
>>>
>>> ###### Check if there is an ongoing audio recording activity. 

>>> > ``` 	Sysapps.audio.isRecording(); ``` 
>>>
>>> ###### Exit the audio recording and return to the delegating activity. 

>>> > ``` 	Sysapps.audio.exitRecording(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./video.md">Video</a>

>>> ###### Prepare a video recording environment so that the output will be saved in videos folder. 

>>> > ``` Sysapps.video.prepare({ "outputFolder": "videos/" }); ``` 
>>>
>>> ###### Start video recording. 

>>> > ``` 	Sysapps.video.startRecording(); ``` 
>>>
>>> ###### Stop video recording and prepare for another recording session. 

>>> > ``` 	Sysapps.video.stopRecording(); ``` 
>>>
>>> ###### Check if there is an ongoing video recording activity. 

>>> > ``` 	Sysapps.video.isRecording(); ``` 
>>>
>>> ###### Exit the video recording and return to the delegating activity. 

>>> > ``` 	Sysapps.video.exitRecording(); ``` 
>>
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./image.md">Image</a>

>>> ###### Prepare a capture environment using the front camera to save the image in images folder. 

>>> > ``` Sysapps.image.prepare({outputFolder: "images/", "cameraType": "FRONT"}); ``` 

>>> ###### Take pictures. 

>>> > ``` Sysapps.image.capture(); ``` 
>>>
>>> ###### Exit image capturing activity. 

>>> > ``` Sysapps.image.exitCapture(); ``` 
> >
> > <a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./contacts.md">Contacts</a>

>>> ###### Obtain a user picked contact. 

>>> > ``` Sysapps.contacts.pick(); ``` 

>>> ###### Obtain all contacts. 

>>> > ``` Sysapps.contacts.getAll(); ``` 
>>
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./calls.md">Calls</a>

>>> ###### Call phone number +123-456-7890. 

>>> > ``` Sysapps.calls.call("+123-456-7890"); ``` 

>>> ###### Obtain up to 500 call logs. 

>>> > ``` Sysapps.calls.getLog(); ``` 
>>
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./files.md">Files</a>

>>> ###### Open a file from Images folder. 

>>> > ``` Sysapps.files.open("Images/myPic.jpg"); ``` 

>>> ###### Get absolute path of a file from file picker. 

>>> > ``` Sysapps.files.getPath(); ``` 
>
>>> ###### Open a file from file picker. 

>>> > ``` Sysapps.files.pick(); ``` 
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./sms.md">Sms</a>
>>
>>> ###### Send "Hi there" message to phone number +012-345-6789. 

>>> > ``` Sysapps.sms.send("+012-345-6789", "Hi there"); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./volume.md"> Volume</a>

>>> ###### Get the volume index of the system stream. 

>>> > ``` Sysapps.volume.indexOf("system"); ``` 

>>> ###### Set the volume index of alarm to 10. 

>>> > ``` Sysapps.volume.indexTo("alarm", 10); ``` 

>>> ###### Change ringer mode to silent. 

>>> > ``` Sysapps.volume.silence(); ``` 

>>> ###### Change ringer mode to normal. 

>>> > ``` Sysapps.volume.normalize(); ``` 
>
>>> ###### Change ringer mode to vibrate. 

>>> > ``` Sysapps.volume.vibrate(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./bluetooth.md">Bluetooth</a>

>>> ###### Get the status of bluetooth on the device. 

>>> > ``` Sysapps.bluetooth.isEnabled(); ``` 

>>> ###### Enable bluetooth. 

>>> > ``` Sysapps.bluetooth.enable(); ``` 
>
>>> ###### Disable bluetooth. 

>>> > ``` Sysapps.bluetooth.disable(); ``` 
>>
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./wifi.md"> Wifi</a>

>>> ###### Get the status of wifi on the device. 

>>> > ``` Sysapps.wifi.isEnabled(); ``` 

>>> ###### Enable wifi. 

>>> > ``` Sysapps.wifi.enable(); ``` 
>
>>> ###### Disable wifi. 

>>> > ``` Sysapps.wifi.disable(); ``` 
>>
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./brightness.md">Brightness</a>

>>> ###### Obtain the index of system brightness. 

>>> > ``` Sysapps.brightness.index(); ``` 

>>> ###### Set system brightness to 100. 

>>> > ``` Sysapps.brightness.indexTo(100); ``` 
>>
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./alarm.md"> Alarm</a>

>>> ###### Schedule a notification to be posted after one minute. 

>>> > ``` Sysapps.alarm.schedule({ "channelId": "abc123", "time": 60000 }); ``` 

>>> ###### Add title to a scheduled notification having channedId of abc123. 

>>> > ``` Sysapps.alarm.update({"channelId": "abc123", "title": "myTitle"}); ``` 
>>>
>>> ###### Have an overview of a scheduled notification. 

>>> > ``` Sysapps.alarm.refer("abc123"); ``` 

>>> ###### Cancel an alarm notification with channelId of abc123. 

>>> > ``` Sysapps.alarm.cancel("abc123"); ``` 
>>>
>>> ###### Cancel all notifications. 

>>> > ``` Sysapps.alarm.cancelAll(); ``` 


​			 