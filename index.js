import { NativeModules } from 'react-native';

module.exports = {
		"volume": NativeModules.Volume,
		"wifi": NativeModules.Wifi,
		"bluetooth": NativeModules.Bluetooth,
		"brightness": NativeModules.Brightness,
		"files": NativeModules.Files,
		"alarm": NativeModules.Alarm,
		"image": NativeModules.Image,
		"video": NativeModules.Video,
		"audio": NativeModules.Audio,
		"sms": NativeModules.Sms,
		"calls": NativeModules.Calls,
		"contacts": NativeModules.Contacts,
};