# Contacts
<p style = "text-align: justify">This module is used to obtain list of all contacts or a user picked contact from the device. The module is accessed by calling the contacts module via <code>Sysapps.contacts</code>.</p> 

## Functions
<p style = "text-align: justify">The contacts module contains the following functions.</p>

``` 
    pick()
    getAll()
    print()
```

### Permissions
<p style = "text-align: justify">In order to prompt the user to pick a contact with <code>pick()</code>  function and to retrieve list of all contacts with <code>getAll()</code> function or to print them with <code>print()</code> function, the following permission should be included in the AndroidManifest.xml file.</p>

 ```    <uses-permission android:name="android.permission.READ_PHONE_NUMBERS"/>```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### pick(): 

> ><p style = "text-align: justify">is used to pick a user selected contact from the device.</p>

##### Sample code snippet

``` 
            import { Contacts } from "react-native-system-applications";
            ....
            ....
            ....
            _pickContact = () => {
                Contacts.pick().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">Invoking the <code>_pickContact()</code> function prompts the user to pick a contact and logs the contact details (name and phone number) of the selected contact. If the user cancels the contact picker window, a promise rejection will be sent.</p>


##### Sample result formal
<p style = "text-align: justify">The invocation of <code>_pickContact()</code> function may have the following log output:</p>

``` 
            [{ "Name": "Mr. John Doe", "Number": "+1234567890"}]
```

#### getAll(): 

<p style = "text-align: justify">is used to get list of all contacts from the device.</p>

##### Sample code snippet

``` 
            import { Contacts } from "react-native-system-applications";
            ....
            ....
            ....
            _getAllContacts = () => {
                Contacts.getAll().then((res) => {
                    console.log(res);   // see the output format below
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">Invoking the <code>_getAllContacts()</code> function retrieves all contacts from the device and writes the data on the console. For successful requests, sample output format is shown below. A promise rejection will be sent if something goes wrong during the retrieval.</p>

##### Sample output format

``` 
          [
          	{ "Name": "Mr. X", "Number": "+1234567890"},
          	{ "Name": "Mr. Y", "Number": "+0123456789"},
          	{ "Name": "Mrs. Z", "Number": "+1234567891"},
          ]
```

#### print(): 

<p style = "text-align: justify">is used to print all contacts from the device in pdf format. The user gets the location of the saved document from an alert notification. </p>

##### Sample code snippet

``` 
            import { Contacts } from "react-native-system-applications";
            ....
            ....
            ....
            _printContacts = () => {
                Contacts.print().then((res) => {
                    // do something
                }).catch((err) => {
                    console.log(err);
                });
            } 
```

<p style = "text-align: justify">Invoking the <code>_printContacts()</code> function prints all contacts from the device and saves it in pdf format.</p>