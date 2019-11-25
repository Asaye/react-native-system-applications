# Alarm
<p style = "text-align: justify">This module is used to schedule and manage alarm notifications.</p> 

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
<p style = "text-align: justify">An event is emitted when the notification is firing whether the app in the foreground or background. If you want to handle the event while the app is in the foreground, you have to add <code>onNotification</code> listener to your app. One way to do this is as follows: </p>

``` 
	import { DeviceEventEmitter } from "react-native";

	componentDidMount() {
		DeviceEventEmitter.addListener('onNotification', (e) => {
			const response = JSON.parse(e);
			console.log(response);
		});
  	}
```

If you want to handle the event while the app is in the background, you have to add the following at the bottom of your javascript file. You should also register the <code>EventEmitter</code> service in your AndroidManifest.xml file as described in the <a href="#eventEmitter">Permissions</a> section.</p>

``` 
    import { AppRegistry } from "react-native";

    AppRegistry.registerHeadlessTask('SysappsEventNotification', () => async (e) => {
        console.log(taskData);
    });
```

The response data sent via the event emitter is the string representation of the original object parameter passed to the <code>schedule()</code> function. Note that, only primitive data will be persisted and sent back as a response. 
 
### Permissions
<p style = "text-align: justify">In order to schedule and manage notifications, the following service should be added inside <code>application</code> tag of your AndroidManifest.xml file.</p>

```
	<service android:name="com.sysapps.alarm.AlarmService"
    		android:permission="android.permission.BIND_JOB_SERVICE" />
```

<p id = "eventEmitter">To handle events when the app in the background, the following service should be registered in the AndroidManifest.xml file:</p>

 ```
    <service android:name="com.sysapps.alarm.EventEmitter"/>
```

<p style = "text-align: justify">Besides, add the following permissions outside the <code>application</code> tag of the AndroidManifest.xml file.</p>

```
	<uses-permission android:name="android.permission.WAKE_LOCK"/>
	<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
	<uses-permission android:name="android.permission.VIBRATE"/>
```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### &#x1F537; schedule(Object options): 

<p style = "text-align: justify">is used to schedule an alarm notification. The <code>options</code> parameter can have, but not limited to, the properties shown below. </p>

<table>
<tr><th>Prop</th><th>Required</th><th>Default</th><th style =  "width: 150px">Type</th><th>Description</th></tr>
<tr><td>channelId</td><td>true</td><td>-</td><td>String</td><td style = "text-align: justify">A unique string identifier for the notification and for the channel throught which the notification will be streamed.</td></tr>
<tr><td>date</td><td>true</td><td>-</td><td>long</td><td style = "text-align: justify">The number of milli seconds between the date and time when the notification is desired to be posted and January 1, 1970 00:00:00. The value can easily be obtained by calling <code>getTime()</code> function on a javascript date object. </td></tr>
<tr><td>title</td><td> false</td><td> -</td><td>String</td><td style = "text-align: justify">The title of the notification.</td></tr>
<tr><td>content</td><td> false</td><td>-</td><td>String</td><td style = "text-align: justify">The content of the notification.</td></tr>
</table>

You can also add any other key-value pairs in addition to the above so that you can recover them when the event is fired at the time of notification. These key-value pairs should be of primitive data types.

##### Sample code snippet

```
        import { Alarm } from "react-native-system-applications";
        ....
        ....
        ....
        _scheduleNotification = () => {
            const date = new Date(2019, 8, 1, 8, 30, 0); // Sep 01 2019 @ 8:30:00 AM        	 
        	const params = { 
                "channelId": "abc123", 
                "date": date.getTime(), 
                "title": "my title", 
                "content" : "my content", 
                "key1": "value1", 
                "key2": false, 
                "key3": 14123 
        	};
        	Alarm.schedule(params);
        } 
 ```

<p style = "text-align: justify">Invoking the <code>_scheduleNotification()</code> function schedules an alarm to be fired on Sep 01 2019 @ 8:30:00 AM local time. When the notification is fired an event will also be emitted and the string representation of the <code>params</code> field defined within the function will be sent back as a response.</code></p>

#### &#x1F537; update(Object options): 

<p style = "text-align: justify">is used to update a scheduled notification. All of the parameters passed to <code>schedule()</code> function except the <code>channelId</code> property can be updated. Thus, the <code>options</code> parameter passed to the <code>update()</code> function should contain the <code>channelId</code> property of an already scheduled notification and any other properties either to be modified or added.</p>

##### Sample code snippet

```
        import { Alarm } from "react-native-system-applications";
        ....
        ....
        ....
        _updateNotif_abc123 = () => {
            const date = new Date(2019, 8, 2, 8, 30, 0); // Sep 02 2019 @ 8:30:00 AM  
        	Alarm.update({ "channelId": "abc123", "date": date.getTime() });        	
        } 
```
<p style = "text-align: justify">Invoking the <code>_updateNotif_abc123()</code> function updates a scheduled notification with <code>channelId</code> of <code>abc123</code> by changing the date when the notification will be posted to Sep 02 2019 @ 8:30:00 AM local time.</p>

#### &#x1F537; refer(String channelId): 

<p style = "text-align: justify">is used to refer a scheduled notification so as to have an overview of its properties. The parameter <code>channelId</code> represents the unique identifier associated with the scheduled notification. The response is the string representation of the object parameter passed to the <code>schedule()</code> or <code>update()</code> function. For unsuccessful requests a promise rejection will be sent.</p>

##### Sample code snippet

```
            import { Alarm } from "react-native-system-applications";
            ....
            ....
            ....
            _referScheduledNotif = () => {
                Alarm.refer("abc123").then((res) => {
                    console.log(res);
                }).catch((err) => {
                    console.log(err);
                });
            } 
```
<p style = "text-align: justify">Invoking the <code>_referScheduledNotif()</code> function obtains the data associated with a scheduled alarm notification having a <code>channelId</code> of <code>abc123</code> and logs the response.</p>

#### &#x1F537; cancel(String channelId): 

<p style = "text-align: justify">is used to cancel an alarm notification scheduled with a unique identifier of <code>channelId</code>. In addition, the data associated with the notification will be deleted.</p>

##### Sample code snippet

```
            import { Alarm } from "react-native-system-applications";
            ....
            ....
            ....
            _cancelNotification = () => {
                Alarm.cancel("abc123");
            } 
```
<p style = "text-align: justify">Invoking the <code>_cancelNotification()</code> cancels an alarm notification having a <code>channelId</code> of <code>abc123</code>.</p>

#### &#x1F537; cancelAll(): 

<p style = "text-align: justify">is used to cancel all the notifications scheduled via the <code>schedule()</code> function. The data corresponding to all notifications will also be deleted.</p>

##### Sample code snippet

```
            import { Alarm } from "react-native-system-applications";
            ....
            ....
            ....
            _cancelAllNotifications = () => {
                Alarm.cancelAll();
            } 
```
<p style = "text-align: justify">Invoking the <code>_cancelAllNotifications()</code> function cancels all notifications scheduled via the <code>schedule()</code> function and deletes the corresponding data.</p>

