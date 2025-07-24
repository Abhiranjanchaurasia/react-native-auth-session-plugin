#import <React/RCTBridgeModule.h>
#import <React/RCTUtils.h>

@interface RCT_EXTERN_MODULE(RNAuthSessionPlugin, NSObject)

RCT_EXTERN_METHOD(login:(NSString *)loginUrl
                  callbackScheme:(NSString *)callbackScheme
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(logout:(NSString *)logoutUrl
                  callbackScheme:(NSString *)callbackScheme
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

@end
