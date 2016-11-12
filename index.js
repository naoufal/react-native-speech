import { Platform } from 'react-native';

let SpeechSynthesizer = null;

if(Platform.OS === 'ios') {
  SpeechSynthesizer = require('./SpeechSynthesizer.ios.js');
} else {
  SpeechSynthesizer = require('./SpeechSynthesizer.android.js');
}

export default SpeechSynthesizer;