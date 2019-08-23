# Audio
<p style = "text-align: justify">This module is used to record audio using hardwares installed on the device. The module is accessed by calling the audio module via <code>Sysapps.audio</code>.</p> 

## Functions
<p style = "text-align: justify">The audio module contains the following functions.</p>

``` 
    prepare(Object options)
    startRecording()
    stopRecording()
    isRecording()
    exitRecording()
```

### Permissions
<p style = "text-align: justify">In order to record audio, the following permissions should be included in the AndroidManifest.xml file.</p>

```     
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
        <uses-permission android:name="android.permission.RECORD_AUDIO" /
```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### prepare(Object options): 

> ><p style = "text-align: justify">is used to prepare an audio recroding environment. The <code>options</code> parameter can have the properties shown below. 
    
<table>
<tr><th>Prop</th><th>Required</th><th>Default</th><th style =  "width: 150px">Type</th><th>Description</th></tr>
<tr><td>audioSource </td><td> false</td><td>"DEFAULT" </td><td>enum( "MIC" ,  "DEFAULT" , "CAMCORDER" ,  "VOICE_UPLINK",  "VOICE_CALL", "VOICE_DOWNLINK", "VOICE_COMMUNICATION",  "VOICE_RECOGNITION") </td><td style = "text-align: justify">An audio source which will be used for the audio recoding activity.</td></tr>
<tr><td>outputFormat  </td><td> false</td><td>"MPEG_4"</td><td>enum( "MPEG_4" , "3GPP", "WEBM" )</td><td style = "text-align: justify">An output format for the type of the media to be used for the output file.</td></tr>
<tr><td>audioEncoder</td><td> false</td><td>"AMR_NB" </td><td>enum( "AMR_NB" , "DEFAULT_EN",  "AAC",  "AAC_ELD" )</td><td style = "text-align: justify">An audio encoder used to process the audio data.</td></tr>
<tr><td>outputFolder </td><td>true</td><td>-</td><td>String</td><td style = "text-align: justify">The path to the folder where the recorded audio will be saved.</td></tr>
<tr><td>maxDuration</td><td> false</td><td>-</td><td>int</td><td style = "text-align: justify">The maximum duration (in milliseconds) of the recording.</td></tr>
<tr><td>maxDuration</td><td> false</td><td>-</td><td>int</td><td style = "text-align: justify">The maximum file size (in bytes) of the recoded file.</td></tr>
</table>

All the above properties except the <code>outputFile</code> are optional. If you  don't specify them, default values ,as described above, will be assigned to those properties during runtime.

    ##### Sample code snippet
``` 
        _prepareMediaRecorder = () => {
        	const params = { "outputFile": "Music/" };
        	Sysapps.audio.prepare(params);
        } 
```

<p style = "text-align: justify">Invoking the <code>_prepareMediaRecorder()</code> function prepares the media recorder for the audio recording activity so that the output will be saved in <code>Music</code> folder (Don't forget the last backslash). This makes the media recorder to be ready for recording and a user interface with a START/STOP button appears on the screen of the device. After this, there are two options to start recording. The first one is to call the <code>startRecording()</code> function programmatically. And the second option is to manually press the START button on the created user interface.</p>

#### startRecording(): 

> ><p style = "text-align: justify">is used to start audio recroding activity. Calling this function is equivalent to pressing the START button. After calling the <code>prepare()</code> function and before calling this one, I recommend to provide a few milli seconds gap to make sure that the recording environment is prepared and ready to record. Once the recording is started, it can be terminated by:</p>

> >>* explicitly calling <code>stopRecording()</code> function.
> >>* pressing the <code>STOP</code> button which appears on the audio recording view.
> >>* setting <code>maxDuration</code> property in the arguments passed to <code>prepare()</code> function.
> >>* setting <code>maxFileSize</code> property in the arguments passed to <code>prepare()</code> function.
> >>* explicitly calling <code>exitRecording()</code> function.
> >>* pressing the back button.

> ><p style = "text-align: justify">Note that if the audio recording is terminated by the first four ways, another session of recording can be started again by calling the <code>startRecording()</code> method or by pressing the START button and the loop continues until the recording is terminated by pressing the back button or calling the <code>exitRecording()</code> function.</p>
    
    ##### Sample code snippet
``` 
        _startAudioRecording = () => {
        	setTimeout(() => {
        		Sysapps.audio.startRecording();
        	}, 1000);        	
        } 
```

>>>><p style = "text-align: justify">Invoking the <code>_startAudioRecording()</code> function starts audio recording after 1000ms.</p>

#### stopRecording(): 

> ><p style = "text-align: justify">is used to terminate an ongoing audio recording activity. Calling this function is equivalent to pressing the STOP button. After calling this function, the media recording evnvironment is still set up and another session of audio recording can be started by calling <code>startRecording()</code> function or by pressing the START button.</p>

    ##### Sample code snippet
``` 
            _stopAudioRecording = () => {
                Sysapps.audio.stopRecording();
            } 
```
<p style = "text-align: justify">Invoking the <code>_stopAudioRecording()</code> function terminates an ongoing recording activity.</p>

#### isRecording(): 

> ><p style = "text-align: justify">is used to check whether there is an ongoing recording activity or not. This might be useful if you want to call <code>startRecording()</code> function while there is no any ongoing recording activity or to call <code>stopRecording()</code> function while there is a checked ongoing recording activity.</p>

    ##### Sample code snippet
``` 
            _checkAudioRecording = async () => {
                const recording = await Sysapps.audio.isRecording();
                if (recording) {
                	Sysapps.audio.stopRecording();
                }
            } 
```
>>>><p style = "text-align: justify">Invoking the <code>_checkAudioRecording()</code> checks if there is an ongoing recording activity and the <code>stopRecording()</code> function is called if the response is <code>true</code>.</p>

#### exitRecording(): 

<p style = "text-align: justify">is used to exit the recording activity. Calling this function is equivalent to pressing the back button. No further audio recording session can be started after calling this funtion.</p>

    ##### Sample code snippet
``` 
            _exitAudioRecording = () => {
                Sysapps.audio.exitRecording();
            } 
```

<p style = "text-align: justify">Invoking the <code>_exitAudioRecording()</code> function aborts an ongoing recording activity and closes audio recording view.</p>
>>
