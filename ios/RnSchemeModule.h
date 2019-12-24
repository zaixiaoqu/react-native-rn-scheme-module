#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

// 模拟器测试
// xcrun simctl openurl booted 'lanlingtong://test1/test2?id=1&a=2&b=3'

NS_ASSUME_NONNULL_BEGIN

@interface RnSchemeModule :  RCTEventEmitter<RCTBridgeModule>

+ (void)sendOpenLinkMessage:(NSURL *)msg;

@end

NS_ASSUME_NONNULL_END
