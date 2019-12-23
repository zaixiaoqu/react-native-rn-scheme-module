import {
  NativeModules,
  Platform,
  DeviceEventEmitter,
  NativeEventEmitter,
  AppState
} from 'react-native';

const RnSchemeModule = {

  didOpenMessage(callbackEvent = () => {}) {
    return new Promise((resolve, reject) => {
      this._addEventListener('OpenSchemeDidReceiveMessage', message => {
        if (typeof  callbackEvent == 'function') {
          callbackEvent(message);
        }
        resolve(message);
      });
      NativeModules.RnSchemeModule.awakenWaitOpenMessage().then((s) => {
      });
    });
  },

  _addEventListener(eventName, handler) {
    if (Platform.OS === 'android') {
      return (new NativeEventEmitter(NativeModules.RnSchemeModule)).addListener(eventName, event => {
        handler(event);
      });
    } else {
      return (new NativeEventEmitter(NativeModules.RnSchemeModule)).addListener(eventName, event => {
        handler(event);
      });
    }
  }
};
export default RnSchemeModule;

