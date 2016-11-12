//
//  SpeechEventEmitter.h
//  Ola Mundo PA
//
//  Created by Alex Pavtoulov on 10/11/2016.
//  Copyright © 2016 Ola Mundo Ltd. All rights reserved.
//

#ifndef SpeechEventEmitter_h
#define SpeechEventEmitter_h

#import "RCTEventEmitter.h"

@interface SpeechEventEmitter : RCTEventEmitter

+ (BOOL)application:(UIApplication *)application speechFinished:(NSString *)speechID;

@end

#endif /* SpeechEventEmitter_h */
