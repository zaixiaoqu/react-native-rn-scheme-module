#import "RnSchemeModule.h"

static NSString * const OpenSchemeDidReceiveMessage = @"OpenSchemeDidReceiveMessage";


// 如果通知已经注册了，告诉系统，我的通知已经注册 (注册一个全局变量来保存时间注册状态)
static BOOL IsAddObserverSchemeEvent = NO;
// 最后Scheme的信息(还未被RN读取到的信息)
static NSDictionary * LastSchemeWaitMessage = NULL;


@implementation RnSchemeModule {
    bool hasListeners;
}
RCT_EXPORT_MODULE();

// Will be called when this module's first listener is added.
-(void)startObserving {
    hasListeners = YES;
    // Set up any upstream listeners or background tasks as necessary
}

// Will be called when this module's last listener is removed, or on dealloc.
-(void)stopObserving {
    hasListeners = NO;
    // Remove upstream listeners, stop unnecessary background tasks
}

//rn代理, 导出事件
- (NSArray<NSString *> *)supportedEvents
{
    return @[OpenSchemeDidReceiveMessage];
}

-(instancetype)init{
    self = [super init];
    if (self) {
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(openSchemeDidReceiveMessageEvent:)
                                                     name:OpenSchemeDidReceiveMessage
                                                   object:nil];
        
        // 告诉系统，我的通知已经注册
        IsAddObserverSchemeEvent = YES;
    }
    return self;
}

-(void)openSchemeDidReceiveMessageEvent:(NSNotification *)notification{
    
    if (hasListeners) {
        [self sendEventWithName:OpenSchemeDidReceiveMessage body:notification.userInfo];
    }
}

+ (void)sendOpenLinkMessage:(NSURL *)url{
    if (nil == url || NULL == url || [url isEqual:[NSNull null]]) {
        return;
    }
    NSString * scheme = [url scheme];
    NSString * encodedPath = [[[url host] stringByRemovingPercentEncoding] stringByAppendingString: [[url path] stringByRemovingPercentEncoding]];
    NSString * queryString = [[url query] stringByRemovingPercentEncoding];
    if (nil == scheme || [scheme isEqual:[NSNull null]] || [scheme isEqualToString:@""]) {
        return;
    }
    if (nil == encodedPath || NULL == encodedPath || [encodedPath isEqual:[NSNull null]] || [encodedPath isEqualToString:@""]) {
        return;
    }
    if (nil == queryString || NULL == queryString || [queryString isEqual:[NSNull null]]) {
        queryString = @"";
    }
    // 创建词典对象，初始化长度为10
    NSDictionary *dictionary = @{
                                 @"scheme": scheme,
                                 @"encodedPath": encodedPath,
                                 @"queryString": queryString
                                 };
    if (YES == IsAddObserverSchemeEvent) {
        [[NSNotificationCenter defaultCenter]postNotificationName:OpenSchemeDidReceiveMessage object:nil userInfo:dictionary];
        return;
    }
    LastSchemeWaitMessage = dictionary;
}

// 唤醒首次打开的等待消息
RCT_EXPORT_METHOD(awakenWaitOpenMessage:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSDictionary *waitMessage = LastSchemeWaitMessage;
    if (nil == waitMessage || NULL == waitMessage || [waitMessage isEqual:[NSNull null]]) {
        resolve(@"0");
        return;
    }
    @try {
        IsAddObserverSchemeEvent = YES;
        [[NSNotificationCenter defaultCenter]postNotificationName:OpenSchemeDidReceiveMessage object:nil userInfo:waitMessage];
        LastSchemeWaitMessage = NULL;
        
        resolve(@"1");
        return;
    } @catch (NSException *exception) {
    } @finally {
    }
    resolve(@"0");
    return;
}

RCT_EXPORT_METHOD(sampleMethod:(NSString *)stringArgument numberParameter:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)
{
    // TODO: Implement some actually useful functionality
    callback(@[[NSString stringWithFormat: @"numberArgument: %@ stringArgument: %@", numberArgument, stringArgument]]);
}

@end
