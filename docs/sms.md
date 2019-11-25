# Sms
<p style = "text-align: justify">This module is used to send an SMS message to a specific phone number. The module is accessed by calling the sms module via <code>Sysapps.sms</code>.</p> 

## Functions
<p style = "text-align: justify">The sms module contains the following function.</p>

``` 
    send(String number, String message)
```

### Permissions
<p style = "text-align: justify">In order to send an SMS message via <code>send()</code>  function, the following permission should be included in the AndroidManifest.xml file.</p>

 ```        <uses-permission android:name="android.permission.SEND_SMS"/>```


### Description
<p style = "text-align: justify">The above function is used to perform the following activities.</p>

#### send(String number, String message): 

<p style = "text-align: justify">is used to send an SMS message to a phone number specified by <code>number</code> parameter having a content specified by <code>message</code> parameter.</p>

##### Sample code snippet

``` 
			import { Sms } from "react-native-system-applications";
            ...
            ...
            ....
            _sendSms = () => {
                Sms.send("+123456789", "Hi, how are you?").then((res) => {
                	// do something
                }).catch((err) => {
                	console.log(err);
                });
            } 
```

<p style = "text-align: justify">Call to  <code>_sendSms()</code> function sends an SMS message to the number passed as the first parameter where the content of the message is passed as the second parameter. A promise rejection will be sent for unsuccessful requests.</p>

