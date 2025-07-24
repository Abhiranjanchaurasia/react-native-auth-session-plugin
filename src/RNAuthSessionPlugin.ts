import { NativeModules } from 'react-native';

const { RNAuthSessionPlugin } = NativeModules;

export default {
  /**
   * Starts a login session with biometric authentication.
   * @param loginUrl The login URL to open.
   * @param callbackScheme The callback scheme to listen for.
   * @returns Promise<string> with the callback URL.
   */
  login(loginUrl: string, callbackScheme: string): Promise<string> {
    return RNAuthSessionPlugin.login(loginUrl, callbackScheme);
  },

  /**
   * Starts a logout session with biometric authentication.
   * @param logoutUrl The logout URL to open.
   * @param callbackScheme The callback scheme to listen for.
   * @returns Promise<string> with the callback URL.
   */
  logout(logoutUrl: string, callbackScheme: string): Promise<string> {
    return RNAuthSessionPlugin.logout(logoutUrl, callbackScheme);
  },
};
