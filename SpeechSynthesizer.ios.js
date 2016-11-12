/**
 * @providesModule SpeechSynthesizer
 * @flow
 */
'use strict';

var React = require('react-native');
var { NativeModules, NativeEventEmitter } = React;
var NativeSpeechSynthesizer = NativeModules.SpeechSynthesizer;

const { SpeechEventEmitter } = NativeModules;
/**
 * High-level docs for the SpeechSynthesizer iOS API can be written here.
 * To implement event emitter of didFinishSpeechUtterance event use in your code:
 *  componentWillMount() {
 *    if (Platform.OS === 'ios') {
 *      this.eventEmitter = new NativeEventEmitter(SpeechEventEmitter);
 *      this.unsubscribeSpeechEvents = this.eventEmitter.addListener(SpeechEventEmitter.SPEECH_FINISH_EVENT, (result) =>
 *        console.log('Speech Finished! Text spoken: ', result.payload)
 *      );
 *    }
 *  }
 *
 *  componentWillUnmount() {
 *    // prevent leaking
 *    if (Platform.OS === 'ios') {this.unsubscribeSpeechEvents()}
 *
 *  }
 *
 * The default pitch is 1.0. Allowed values are in the range from 0.5 (for lower pitch) to 2.0 (for higher pitch).
 * Allowed values are in the range from 0.0 (silent) to 1.0 (loudest). The default volume is 1.0.
 * beforeInterval and afterInterval are in seconds, e.g. afterInterval: 1, will wait after the speech before playing the next ßspeech.
 * e.g.:
 * options = {
 *    text: 'Hello World',
 *    voice: 'en-US',
 *    rate: 0.35,
 *    pitch: 1,
 *    beforeInterval: 0.5,
 *    afterInterval: 0.5,
 *    volume: 1
 * }
 */

var SpeechSynthesizer = {

  speak(options) {
    return new Promise(function(resolve, reject) {
      NativeSpeechSynthesizer.speakUtterance(options, function(error, success) {
        if (error) {
          return reject(error);
        }

        resolve(true);
      });
    });
  },

  stop: NativeSpeechSynthesizer.stopSpeakingAtBoundary,

  pause: NativeSpeechSynthesizer.pauseSpeakingAtBoundary,

  resume: NativeSpeechSynthesizer.continueSpeakingAtBoundary,

  isPaused() {
    return new Promise(function(resolve, reject) {
      NativeSpeechSynthesizer.paused(function(error, paused) {
        if (error) {
          return reject(error);
        }

        if (paused === 1) {
          resolve(true);
        } else {
          resolve(false);
        }
      });
    });
  },

  isSpeaking() {
    return new Promise(function(resolve, reject) {
      NativeSpeechSynthesizer.speaking(function(error, speaking) {
        if (error) {
          return reject(error);
        }

        if (speaking === 1) {
          resolve(true);
        } else {
          resolve(false);
        }
      });
    });
  },

  supportedVoices() {
    return new Promise(function(resolve, reject) {
      NativeSpeechSynthesizer.speechVoices(function(error, locales) {
        if (error) {
          return reject(error);
        }

        resolve(locales);
      });
    });
  }
};

module.exports = SpeechSynthesizer;
