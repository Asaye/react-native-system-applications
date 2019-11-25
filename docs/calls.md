# Calls
<p style = "text-align: justify">This module is used to call a specific phone number or to retrieve call log from the device. The module is accessed by calling the calls module via <code>Sysapps.calls</code>.</p> 

## Functions
<p style = "text-align: justify">The calls module contains the following functions.</p>

``` 
    call(String num)
    getLog()
    print()
```

### Permissions
<p style = "text-align: justify">In order to call a number with <code>call()</code>  function, the following permission should be included in the AndroidManifest.xml file.</p>

```	<uses-permission android:name="android.permission.CALL_PHONE" />```

<p style = "text-align: justify">Besides, to retrieve the call logs from the device with <code>getLog()</code>  function or to print call logs using <code>print()</code> function, the following permission should be included in the AndroidManifest.xml file.</p>

```	<uses-permission android:name="android.permission.READ_CALL_LOG"/>```

### Description

<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### &#x1F537; call(String number): 

<p style = "text-align: justify">is used to call a phone number specified by <code>number</code> parameter.</p>

##### Sample code snippet

``` 
            import { Calls } from "react-native-system-applications";
            ....
            ....
            ....
            _callNumber = () => {
                Calls.call("+123456789").then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">Invoking the <code>_callNumber()</code> function makes a call to the arbitrary number passed as a parameter. A promise rejection will be sent for unsuccessful requests.</p>

#### &#x1F537; getLog(): 

<p style = "text-align: justify">is used to retrieve upto 500 call data from the device.</p>

##### Sample code snippet

``` 
            import { Calls } from "react-native-system-applications";
            ....
            ....
            ....
            _getCallLog = () => {
                Calls.getLog().then((res) => {
                    console.log(res);  // see the output format below
                }).catch((err) => {
                    console.log(err);
                });                
            } 
```

<p style = "text-align: justify">Invoking the <code>_getCallLog()</code> function retrieves call logs from the device and writes the data on the console. For successful requests, sample output format is shown below. A promise rejection will be sent if something goes wrong during the retrieval.</p>

##### Sample output format

``` 
          [
              {
				"Type": "OUTGOING",
				"Duration": 242
				"Date": "1557222126017",
				"Number": "+1234567890"
              },
              {
				"Type": "MISSED",
				"Duration": 0
				"Date": "1557221887573",
				"Number": "+0123456789"
              },
              {
				"Type": "INCOMING",
				"Duration": 453
				"Date": "1557210000345",
				"Number": "+1234567891"
              }
          ] 
```

<p style = "text-align: justify">Note that the <code>Duration</code> values are the call durations in seconds and <code>Date</code> values are string values of the number of milliseconds between the call time and January 1,1970 00:00:00. It is possible to convert it into human understandable date using the javascript <code>Date</code> object.</p>

#### &#x1F537; print(): 

<p style = "text-align: justify">is used to print upto 500 call data from the device in pdf format. The user gets the location of the saved document from an alert notification. </p>

##### Sample code snippet

``` 
            import { Calls } from "react-native-system-applications";
            ....
            ....
            ....
            _printCallLog = async () => {
                Calls.print().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">Invoking the <code>_printCallLog()</code> function prints call logs from the device and saves it in pdf format.</p>
