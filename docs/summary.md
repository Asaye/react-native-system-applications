<h1 style = "color:#4285F4">Summary</h1>
<p style = "text-align: justify">In order to get the full functionality of this module, the basic steps are summarized below. This summary doesn't cover all the details and I strongly recommend referring to the detailed documentation if extra information is sought regarding the required permissions and configurations. This summary ,however, is useful as a quick reference concerning the capabilities of the module. To use this module, all you have to do is basically as follows:</p>

### Install
``` $ npm install react-native-system-applications --save``` 

 or

``` $ yarn add react-native-system-applications```

### Link
```  react-native link react-native-system-applications```

### Import

```  
		import { Audio, Alarm, Video, Image, 
	             Files, Sms, Volume, Wifi, Calls, 
	             Contacts, Bluetooth, Brightness} from 'react-native-system-applications';  
```

### Implement

>> <a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./audio.md">Audio</a>

 ###### Prepare an audio recording environment so that the output will be saved in music folder. 

 > ``` Audio.prepare({ "outputFolder": "music/" }); ``` 

 ###### Start audio recording. 

 > ``` 	Audio.startRecording(); ``` 

 ###### Stop audio recording and prepare for another recording session. 

 > ``` 	Audio.stopRecording(); ``` 

 ###### Check if there is an ongoing audio recording activity. 

 > ``` 	Audio.isRecording(); ``` 

 ###### Exit the audio recording and return to the delegating activity. 

 > ``` 	Audio.exitRecording(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./video.md">Video</a>

 ###### Prepare a video recording environment so that the output will be saved in videos folder. 

 > ``` Video.prepare({ "outputFolder": "videos/" }); ``` 

 ###### Start video recording. 

 > ``` 	Video.startRecording(); ``` 

 ###### Stop video recording and prepare for another recording session. 

 > ``` 	Video.stopRecording(); ``` 

 ###### Check if there is an ongoing video recording activity. 

 > ``` 	Video.isRecording(); ``` 

 ###### Exit the video recording and return to the delegating activity. 

 > ``` 	Video.exitRecording(); ``` 
>>
>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./image.md">Image</a>

 ###### Prepare a capture environment using the front camera to save the image in images folder. 

 > ``` Image.prepare({outputFolder: "images/", "cameraType": "FRONT"}); ``` 

 ###### Take pictures. 

 > ``` Image.capture(); ``` 

 ###### Exit image capturing activity. 

 > ``` Image.exitCapture(); ``` 
> >
> > <a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./contacts.md">Contacts</a>

 ###### Obtain a user picked contact. 

 > ``` Contacts.pick(); ``` 

 ###### Obtain all contacts. 

 > ``` Contacts.getAll(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./calls.md">Calls</a>

 ###### Call phone number +123-456-7890. 

 > ``` Calls.call("+123-456-7890"); ``` 

 ###### Obtain up to 500 call logs. 

 > ``` Calls.getLog(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./files.md">Files</a>

 ###### Open a file from Images folder. 

 > ``` Files.open("Images/myPic.jpg"); ``` 

 ###### Get absolute path of a file from file picker. 

 > ``` Files.getPath(); ``` 
>
 ###### Open a file from file picker. 

 > ``` Files.pick(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./sms.md">Sms</a>

 ###### Send "Hi there" message to phone number +012-345-6789. 

 > ``` Sms.send("+012-345-6789", "Hi there"); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./volume.md"> Volume</a>

 ###### Get the volume index of the system stream. 

 > ``` Volume.indexOf("system"); ``` 

 ###### Set the volume index of alarm to 10. 

 > ``` Volume.indexTo("alarm", 10); ``` 

 ###### Change ringer mode to silent. 

 > ``` Volume.silence(); ``` 

 ###### Change ringer mode to normal. 

 > ``` Volume.normalize(); ``` 
>
 ###### Change ringer mode to vibrate. 

 > ``` Volume.vibrate(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./bluetooth.md">Bluetooth</a>

 ###### Get the status of bluetooth on the device. 

 > ``` Bluetooth.isEnabled(); ``` 

 ###### Enable bluetooth. 

 > ``` Bluetooth.enable(); ``` 
>
 ###### Disable bluetooth. 

 > ``` Bluetooth.disable(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./wifi.md"> Wifi</a>

 ###### Get the status of wifi on the device. 

 > ``` Wifi.isEnabled(); ``` 

 ###### Enable wifi. 

 > ``` Wifi.enable(); ``` 
>
 ###### Disable wifi. 

 > ``` Wifi.disable(); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./brightness.md">Brightness</a>

 ###### Obtain the index of system brightness. 

 > ``` Brightness.index(); ``` 

 ###### Set system brightness to 100. 

 > ``` Brightness.indexTo(100); ``` 

>><a style = "color:#4285F4;font-size:20px;font-weight:bold" href = "./alarm.md"> Alarm</a>

 ###### Schedule a notification to be posted after one minute. 

 > ``` Alarm.schedule({ "channelId": "abc123", "date": (new Date()).getTime() + 60000 }); ``` 

 ###### Add title to a scheduled notification having channedId of abc123. 

 > ``` Alarm.update({"channelId": "abc123", "title": "myTitle"}); ``` 

 ###### Have an overview of a scheduled notification. 

 > ``` Alarm.refer("abc123"); ``` 

 ###### Cancel an alarm notification with channelId of abc123. 

 > ``` Alarm.cancel("abc123"); ``` 

 ###### Cancel all notifications. 

 > ``` Alarm.cancelAll(); ``` 


​			 
