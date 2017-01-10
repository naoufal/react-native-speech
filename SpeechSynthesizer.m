#import "SpeechSynthesizer.h"
#import "RCTUtils.h"
#import "RCTLog.h"

@implementation SpeechSynthesizer

RCT_EXPORT_MODULE()

// Speak
RCT_EXPORT_METHOD(speakUtterance:(NSDictionary *)args callback:(RCTResponseSenderBlock)callback)
{
    // Error if self.synthesizer was already initialized
    if (self.synthesizer) {
        return callback(@[RCTMakeError(@"There is a speech in progress.  Use the `paused` method to know if it's paused.", nil, nil)]);
    }

    // Set args to variables
    NSString *text = args[@"text"];
    NSString *voice = args[@"voice"];
    NSNumber *rate = args[@"rate"];

    // Error if no text is passed
    if (!text) {
        RCTLogError(@"[Speech] You must specify a text to speak.");
        return;
    }

    // Set default voice
    NSString *voiceLanguage;

    // Set voice if provided
    if (voice) {
        voiceLanguage = voice;

    // Fallback to en-US
    } else {
        voiceLanguage = @"en-US";
    }

    // Setup utterance and voice
    AVSpeechUtterance *utterance = [[AVSpeechUtterance alloc] initWithString:text];

    utterance.voice = [AVSpeechSynthesisVoice voiceWithLanguage:voiceLanguage];

    if (rate) {
      utterance.rate = [rate doubleValue];
    }

    self.synthesizer = [[AVSpeechSynthesizer alloc] init];
    self.synthesizer.delegate = self;

    // Speak
    [self.synthesizer speakUtterance:utterance];

    // Return that the speach has started
    callback(@[[NSNull null], @true]);
}

// Stops synthesizer
RCT_EXPORT_METHOD(stopSpeakingAtBoundary)
{
    if (self.synthesizer) {
        [self.synthesizer stopSpeakingAtBoundary:AVSpeechBoundaryImmediate];
        self.synthesizer = nil;
    }
}

// Pauses synthesizer
RCT_EXPORT_METHOD(pauseSpeakingAtBoundary)
{
    if (self.synthesizer) {
        [self.synthesizer pauseSpeakingAtBoundary:AVSpeechBoundaryImmediate];
    }
}

// Resumes synthesizer
RCT_EXPORT_METHOD(continueSpeakingAtBoundary)
{
    if (self.synthesizer) {
        [self.synthesizer continueSpeaking];
    }
}

// Returns false if synthesizer is paued
RCT_EXPORT_METHOD(paused:(RCTResponseSenderBlock)callback)
{
    if (self.synthesizer.paused) {
        callback(@[@true]);
    } else {
        callback(@[@false]);
    }
}

// Returns true if synthesizer is speaking
RCT_EXPORT_METHOD(speaking:(RCTResponseSenderBlock)callback)
{
    if (self.synthesizer.speaking) {
        callback(@[@true]);
    } else {
        callback(@[@false]);
    }
}

// Returns available voices
RCT_EXPORT_METHOD(speechVoices:(RCTResponseSenderBlock)callback)
{
    NSArray *speechVoices = [AVSpeechSynthesisVoice speechVoices];
    NSArray *locales = [speechVoices valueForKey:@"language"];

    callback(@[[NSNull null], locales]);
}

// Delegate

// Finished Handler
-(void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didFinishSpeechUtterance:(AVSpeechUtterance *)utterance
{
    NSLog(@"Speech finished");
    self.synthesizer = nil;
}

// Started Handler
-(void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didStartSpeechUtterance:(AVSpeechUtterance *)utterance
{
    NSLog(@"Speech started");
}

// Paused Handler
-(void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didPauseSpeechUtterance:(AVSpeechUtterance *)utterance
{
    NSLog(@"Speech paused");
}

// Resumed Handler
-(void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didContinueSpeechUtterance:(AVSpeechUtterance *)utterance
{
    NSLog(@"Speech resumed");
}

// Word Handler
-(void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer willSpeakRangeOfSpeechString:(NSRange)characterRange utterance:(AVSpeechUtterance *)utterance
{
    NSLog(@"Started word");
}

// Cancelled Handler
-(void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didCancelSpeechUtterance:(AVSpeechUtterance *)utterance
{
    NSLog(@"Speech cancelled");
}

@end
