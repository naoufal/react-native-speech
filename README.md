# React Native Speech

[![npm version](https://img.shields.io/npm/v/react-native-speech.svg?style=flat-square)](https://www.npmjs.com/package/react-native-speech)
[![npm downloads](https://img.shields.io/npm/dm/react-native-speech.svg?style=flat-square)](https://www.npmjs.com/package/react-native-speech)
[![Code Climate](https://img.shields.io/codeclimate/github/naoufal/react-native-speech.svg?style=flat-square)](https://codeclimate.com/github/naoufal/react-native-speech)

React Native Speech is a text-to-speech library for [React Native](https://facebook.github.io/react-native/).

## Documentation
- [Install](https://github.com/naoufal/react-native-speech#install)
- [Usage](https://github.com/naoufal/react-native-speech#usage)
- [Example](https://github.com/naoufal/react-native-speech#example)
- [Methods](https://github.com/naoufal/react-native-speech#methods)
- [License](https://github.com/naoufal/react-native-speech#license)

## Install
```shell
npm i --save react-native-speech
```

## Usage
### Linking the Library
In order to use Speech, you must first link the library your project.  There's excellent documentation on how to do this in the [React Native Docs](https://facebook.github.io/react-native/docs/linking-libraries.html#content).

### Speaking an Utterance
Once you've linked the library, you'll want to make it available to your app by requiring it:

```js
var Speech = require('react-native-speech');
```

Speaking an utterance is as simple as calling:
```js
Speech.speak({
  text: 'React Native Speech is awesome!  I\'m going to use it in my next project.',
  voice: 'en-US'
});
```

## Example
Using Speech in your app will usually look like this:
```js
var Speech = require('react-native-speech');

var YourComponent = React.createClass({
  _startHandler() {
    Speech.speak({
      text: 'Aujourd\'hui, Maman est morte. Ou peut-être hier, je ne sais pas.',
      voice: 'fr-FR'
    })
    .then(started => {
      console.log('Speech started');
    })
    .catch(error => {
      console.log('You\'ve already started a speech instance.');
    });
  },

  _pauseHandler() {
    Speech.pause();
  },

  _resumeHandler() {
    Speech.resume();
  },

  _stopHandler() {
    Speech.stop();
  },

  render() {
    return (
      <View>
        ...
        <Button onPress={this._startHandler}>
          Speak
        </Button>
        <Button onPress={this._pauseHandler}>
          Pause
        </Button>
        <Button onPress={this._resumeHandler}>
          Resume
        </Button>
        <Button onPress={this._stopHandler}>
          Stop
        </Button>
      </View>
    );
  }
});
```

## Methods

### speak(utterance)
Initializes the speech instance and speaks the utterance provided.

__Arguments__
- `utterance` - An `Object` containing the following keys: `text`, `voice` and, optionally, `rate`. `rate` is a float where lower numbers indicate slower speech.

__Examples__
```js
Speech.speak({
  text: 'I was runnin\' through the 6 with my woes',
  voice: 'en-US',
  rate: 0.4
})
.then(started => {
  // Success code
})
.catch(error => {
  // Failure code
});
```

```js
Speech.speak({
  text: 'I was runnin\' through the 6 with my woes',
  voice: 'en-US'
});
```

### pause()
Pauses the speech instance.

__Example__
```js
Speech.pause();
```

### resume()
Resumes the speech instance.

__Example__
```js
Speech.resume();
```

### stop()
Stops and destroys the speech instance.

__Example__
```js
Speech.stop();
```

### isSpeaking()
Indicates whether speech is in progress.

__Example__
```js
Speech.isSpeaking()
  .then(speaking => {
    console.log(speaking); // true or false
  });
```

### isPaused()
Indicates whether speech is paused.

__Example__
```js
Speech.isPaused()
  .then(paused => {
    console.log(paused); // true or false
  });
```

### supportedVoices()
Indicates which speech voices are available.

__Example__
```js
Speech.supportedVoices()
  .then(locales => {
    console.log(locales); // ["ar-SA", "en-ZA", "nl-BE", "en-AU", "th-TH", ...]
  });
```

## License
Copyright (c) 2015, [Naoufal Kadhom](http://naoufal.com)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
