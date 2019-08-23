# Alarm
<p style = "text-align: justify">This module is used to schedule and manage alarm notifications. The module is accessed by calling the alarm module via <code>Sysapps.alarm</code>.</p> 

## Functions
<p style = "text-align: justify">The alarm module contains the following functions.</p>

``` 
    schedule(Object options)
    update(Object options)
    refer(String id)
    cancel(String id)
    cancelAll()    
```

## Events
<p style = "text-align: justify">An event is emitted when the notification is firing. If you want to handle the event, you have to add <code>onNotification</code> listener to your app. One way to do this is as follows: </p>

``` 
	import { DeviceEventEmitter } from "react-native";

	componentDidMount() {
		DeviceEventEmitter.addListener('onNotification', (e) => {
			const response = JSON.parse(e);
			console.log(response);
		});
  	}
```

The response data sent via the event emitter is the string representation of the original object parameter passed to the <code>schedule()</code> function. Note that, only primitive data will be persisted and sent back as a response. 
<p style = "text-align: justify;">The notification will be posted even after reboot. However, after reboot, the event will be emitted if the implementing app is in the foreground.</p>  
 
### Permissions
<p style = "text-align: justify">In order to schedule and manage notifications, the following service should be added inside <code>application</code> tag of your AndroidManifest.xml file.</p>

```
	<service android:name="com.sysapps.alarm.AlarmService"
    		android:permission="android.permission.BIND_JOB_SERVICE" />
```

<p style = "text-align: justify">Besides, add the following permissions outside the <code>application</code> tag of the AndroidManifest.xml file.</p>

```
	<uses-permission android:name="android.permission.WAKE_LOCK"/>
	<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
	<uses-permission android:name="android.permission.VIBRATE"/>
```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### schedule(Object options): 

<p style = "text-align: justify">is used to schedule an alarm notification. The <code>options</code> parameter can have, but not limited to, the properties shown below. </p>

<table>
<tr><th>Prop</th><th>Required</th><th>Default</th><th style =  "width: 150px">Type</th><th>Description</th></tr>
<tr><td>channelId</td><td>true</td><td>-</td><td>String</td><td style = "text-align: justify">A unique string identifier for the notification and for the channel throught which the notification will be streamed.</td></tr>
<tr><td>time</td><td>true</td><td>-</td><td> long</td><td style = "text-align: justify">A time gap in milli seconds between January 1, 1970 00:00:00 and the time when the notification is desired to fire.</td></tr>
<tr><td>title</td><td> false</td><td> -</td><td>String</td><td style = "text-align: justify">The title of the notification.</td></tr>
<tr><td>content</td><td> false</td><td>-</td><td>String</td><td style = "text-align: justify">The content of the notification.</td></tr>
</table>

You can also add any other key-value pairs in addition to the above so that you can recover them when the event is fired at the time of notification. These key-value pairs should be of primitive data types.

##### Sample code snippet

```
        _scheduleNotification = () => {        	 
        	const params = { 
                "channelId": "abc123", 
                "time": 60000, 
                "title": "my title", 
                "content" : "my content", 
                "key1": "value1", 
                "key2": false, 
                "key3": 14123 
        	};
        	Sysapps.alarm.schedule(params);
        } 
 ```

<p style = "text-align: justify">Invoking the <code>_scheduleNotification()</code> function schedules an alarm to be fired after one minute. When the notification is fired an event will also be emitted and the string representation of the <code>params</code> field defined within the function will be sent back as a response.</code></p>

#### update(Object options): 

<p style = "text-align: justify">is used to update a scheduled notification. All of the parameters passed to <code>schedule()</code> function except the <code>channelId</code> property can be updated. Thus, the <code>options</code> parameter passed to the <code>update()</code> function should contain the <code>channelId</code> property of an already scheduled notification and any other properties either to be modified or added.</p>

##### Sample code snippet

```
        _updateNotif_abc123 = () => {
        	Sysapps.alarm.update({ "channelId": "abc123", "time": 120000 });        	
        } 
```
<p style = "text-align: justify">Invoking the <code>_updateNotif_abc123()</code> function updates a scheduled notification with <code>channelId</code> of <code>abc123</code> by changing the minimum delay when the notification will be posted to two minutes.</p>

#### refer(String channelId): 

<p style = "text-align: justify">is used to refer a scheduled notification so as to have an overview of its properties. The parameter <code>channelId</code> represents the unique identifier associated with the scheduled notification. The response is the string representation of the object parameter passed to the <code>schedule()</code> or <code>update()</code> function. For unsuccessful requests a promise rejection will be sent.</p>

##### Sample code snippet

```
            _referScheduledNotif = async () => {
                const params = await Sysapps.alarm.refer("abc123");
                console.log(params);
            } 
```
<p style = "text-align: justify">Invoking the <code>_referScheduledNotif()</code> function obtains the data associated with a scheduled alarm notification having a <code>channelId</code> of <code>abc123</code> and logs the response.</p>

#### cancel(String channelId): 

<p style = "text-align: justify">is used to cancel an alarm notification scheduled with a unique identifier of <code>channelId</code>. In addition, the data associated with the notification will be deleted.</p>

##### Sample code snippet

```
            _cancelNotification = () => {
                Sysapps.alarm.cancel("abc123");
            } 
```
<p style = "text-align: justify">Invoking the <code>_cancelNotification()</code> cancels an alarm notification having a <code>channelId</code> of <code>abc123</code>.</p>

#### cancelAll(): 

<p style = "text-align: justify">is used to cancel all the notifications scheduled via the <code>schedule()</code> function. The data corresponding to all notifications will also be deleted.</p>

##### Sample code snippet

```
            _cancelAllNotifications = () => {
                Sysapps.alarm.cancelAll();
            } 
```
<p style = "text-align: justify">Invoking the <code>_cancelAllNotifications()</code> function cancels all notifications scheduled via the <code>schedule()</code> function and deletes the corresponding data.</p>

