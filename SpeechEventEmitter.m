//
//  SpeechEventEmitter.m
//  Ola Mundo PA
//
//  Created by Alex Pavtoulov on 10/11/2016.


#import <Foundation/Foundation.h>
#import "SpeechEventEmitter.h"
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"

NSString *const speechFinishEvent = @"speech-finished";
NSString *const kSpeechFinishNotification = @"SpeechFinishNotification";


@implementation SpeechEventEmitter

RCT_EXPORT_MODULE();

- (NSDictionary<NSString *, NSString *> *)constantsToExport {
    return @{@"SPEECH_FINISH_EVENT": speechFinishEvent};
}

- (NSArray<NSString *> *)supportedEvents {
    return @[speechFinishEvent];
}

- (void)startObserving {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleSpeechFinishNotification:)
                                                 name:speechFinishEvent object:nil];
}

- (void)stopObserving {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

+ (BOOL)application:(UIApplication *)application speechFinished:(NSString *)speechID {
    NSDictionary<NSString *, id> *payload = @{@"payload": speechID};
    [[NSNotificationCenter defaultCenter] postNotificationName:speechFinishEvent
                                                        object:self
                                                      userInfo:payload];
  return YES;
}

- (void)handleSpeechFinishNotification:(NSNotification *)notification {
  [self sendEventWithName:speechFinishEvent body:notification.userInfo];
}

@end
