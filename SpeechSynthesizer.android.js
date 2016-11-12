/**
 * @providesModule SpeechSynthesizer
 * @flow
 */
'use strict';

var React = require('react-native');
var { NativeModules } = React;
var NativeSpeechSynthesizer = NativeModules.SpeechSynthesizer;

/**
 * High-level docs for the SpeechSynthesizer Android API can be written here.
 */

var SpeechSynthesizer = {
  test () {
    return NativeSpeechSynthesizer.reactNativeSpeech();
  },

  supportedVoices() {
    return NativeSpeechSynthesizer.supportedVoices();
  },

  isSpeaking() {
    return NativeSpeechSynthesizer.isSpeaking();
  },

  isPaused() {
    return NativeSpeechSynthesizer.isPaused();
  },

  resume() {
    return NativeSpeechSynthesizer.resume();
  },

  pause() {
    return NativeSpeechSynthesizer.pause();
  },

  stop() {
    return NativeSpeechSynthesizer.stop();
  },

  speak(options) {
    return NativeSpeechSynthesizer.speak(options);
  }
};

module.exports = SpeechSynthesizer;
