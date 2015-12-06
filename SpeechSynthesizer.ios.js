/**
 * @providesModule SpeechSynthesizer
 * @flow
 */
'use strict';

var React = require('react-native');
var { NativeModules } = React;
var NativeSpeechSynthesizer = NativeModules.SpeechSynthesizer;

/**
 * High-level docs for the SpeechSynthesizer iOS API can be written here.
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
