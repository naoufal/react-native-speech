package com.omega.speech;

import java.util.Locale;
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.facebook.common.logging.FLog;

import com.facebook.react.common.ReactConstants;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;


class SpeechSynthesizerModule extends ReactContextBaseJavaModule {
    private Context context;
    private static TextToSpeech tts;
    private Promise ttsPromise;

    public SpeechSynthesizerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        this.init();
    }

    /**
     * @return the name of this module. This will be the name used to {@code require()} this module
     * from javascript.
     */
    @Override
    public String getName() {
        return "SpeechSynthesizer";
    }

    /**
     * Intialize the TTS module
     */
     public void init(){
        tts = new TextToSpeech(getReactApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.ERROR){
                    FLog.e(ReactConstants.TAG,"Not able to initialized the TTS object");
                }
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String utteranceId) {
                WritableMap map = Arguments.createMap();
                map.putString("utteranceId", utteranceId);
                getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)
                    .emit("FinishSpeechUtterance", map);
                ttsPromise.resolve(true);
            }

            @Override
            public void onError(String utteranceId) {
                WritableMap map = Arguments.createMap();
                map.putString("utteranceId", utteranceId);
                getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)
                    .emit("ErrorSpeechUtterance", map);
                ttsPromise.reject("error");
            }

            @Override
            public void onStart(String utteranceId) {
                WritableMap map = Arguments.createMap();
                map.putString("utteranceId", utteranceId);
                getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)
                    .emit("StartSpeechUtterance", map);
            }
        });
    }

    @ReactMethod
    public void supportedVoices(final Promise promise) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                try{
                    if(tts == null){
                        init();
                    }
                    Locale[] locales = Locale.getAvailableLocales();
                    WritableArray data = Arguments.createArray();
                    for (Locale locale : locales) {
                        int res = tts.isLanguageAvailable(locale);
                        if(res == TextToSpeech.LANG_COUNTRY_AVAILABLE){
                            data.pushString(locale.getLanguage());
                        }
                    }
                    promise.resolve(data);
                } catch (Exception e) {
                    promise.reject(e.getMessage());
                }
            }
        }.execute();
    }

    @ReactMethod
    public void isSpeaking(final Promise promise) {
        new GuardedAsyncTask<Void,Void>(getReactApplicationContext()){
            @Override
            protected  void doInBackgroundGuarded(Void... params){
                try {
                    if (tts.isSpeaking()) {
                        promise.resolve(true);
                    } else {
                        promise.resolve(false);
                    }
                } catch (Exception e){
                    promise.reject(e.getMessage());
                }
            }
        }.execute();
    }

    @ReactMethod
    public void isPaused(final Promise promise) {
        promise.reject("This function doesn\'t exists on android !");
    }

    @ReactMethod
    public void resume(final Promise promise) {
        promise.reject("This function doesn\'t exists on android !");
    }

    @ReactMethod
    public void pause(final Promise promise) {
        promise.reject("This function doesn\'t exists on android !");
    }

    @ReactMethod
    public void stop(final Promise promise) {
        new GuardedAsyncTask<Void,Void>(getReactApplicationContext()){
            @Override
            protected  void doInBackgroundGuarded(Void... params){
                try {
                    tts.stop();
                    promise.resolve(true);

                } catch (Exception e){
                    promise.reject(e.getMessage());
                }
            }
        }.execute();
    }

    @ReactMethod
    public void speak(final ReadableMap args, final Promise promise) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                if(tts == null){
                    init();
                }
                String text = args.hasKey("text") ? args.getString("text") : null;
                String voice = args.hasKey("voice") ? args.getString("voice") : null;
                Boolean forceStop = args.hasKey("forceStop") ?  args.getBoolean("forceStop") : null;
                Float rate = args.hasKey("rate") ? (float)  args.getDouble("rate") : null;
                if(tts.isSpeaking()){
                    //Force to stop and start new speech
                    if(forceStop != null && forceStop){
                        tts.stop();
                    } else {
                        promise.reject("TTS is already speaking something , Please shutdown or stop  TTS and try again");
                    }
                }
                if(args.getString("text") == null || text == ""){
                    promise.reject("Text cannot be blank");
                }
                try {
                    if (voice != null && voice != "") {
                        tts.setLanguage(new Locale(voice));
                    } else {
                        //Setting up default voice
                        tts.setLanguage(new Locale("en"));
                    }
                    //Set the rate if provided by the user
                    if(rate != null){
                        tts.setPitch(rate);
                    }

                    int speakResult = 0;
                    if(Build.VERSION.SDK_INT >= 21) {
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence(Engine.KEY_PARAM_UTTERANCE_ID, "");
                        ttsPromise = promise;
                        speakResult = tts.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, UUID.randomUUID().toString());
                    } else {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(Engine.KEY_PARAM_UTTERANCE_ID, UUID.randomUUID().toString());
                        ttsPromise = promise;
                        speakResult = tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
                    }

                    if(speakResult < 0) {
                        throw new Exception("Speak failed, make sure that TTS service is installed on you device");
                    }


                    promise.resolve(true);
                } catch (Exception e) {
                    promise.reject(e.getMessage());
                }
            }
        }.execute();
    }
}
