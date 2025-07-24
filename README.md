# react-native-auth-session-plugin

This React Native plugin enables secure **biometric authentication** followed by **web-based login or logout flows**, using native modules on **iOS** and **Android**. It's perfect for OAuth services like Auth0, where biometric verification is required before redirecting the user to an authentication session.

## Features

✅ Biometric authentication (Face ID / Touch ID / Fingerprint)
✅ Secure OAuth login via native browser (ASWebAuthenticationSession / CustomTabsIntent)
✅ Support for custom callback URL schemes
✅ Works on both iOS and Android

---

## Installation

```sh
npm install react-native-auth-session-plugin
```

### 1. Link Native Code

Make sure the native modules are added in both the iOS and Android projects.

If you're not using autolinking, do:

```bash
npx react-native link react-native-auth-session-plugin
```

## Usage

```js
import { NativeModules } from 'react-native';

const { RNAuthSessionPlugin } = NativeModules;

// Login
RNAuthSessionPlugin.login(
  'https://your-domain.auth0.com/authorize?...',
  'myapp' // callback scheme
).then(url => {
  console.log('Redirected back with URL:', url);
}).catch(err => {
  console.error('Login error:', err);
});

// Logout
RNAuthSessionPlugin.logout(
  'https://your-domain.auth0.com/v2/logout?...',
  'myapp'
).then(url => {
  console.log('Logout redirect URL:', url);
}).catch(err => {
  console.error('Logout error:', err);
});

```

### If your plugin is locally added or manually created, ensure:
    1. iOS: RNAuthSessionPlugin.swift is added to your Xcode project.
    2. Android: Add the Java module in your app’s package structure and register it in MainApplication.java.

### 2. iOS Setup
    1. Info.plist
    2. Add your app's custom URL scheme:

```xml
<key>CFBundleURLTypes</key>
<array>
  <dict>
    <key>CFBundleURLSchemes</key>
    <array>
      <string>myapp</string> <!-- Replace with your scheme -->
    </array>
  </dict>
</array>

```
## CocoaPods
Run:

```bash
npx pod-install

```
### 3. Android Setup
    1. AndroidManifest.xml
    2. Add an intent filter in your <activity> (usually inside MainActivity):
```xml

<intent-filter>
  <action android:name="android.intent.action.VIEW" />
  <category android:name="android.intent.category.DEFAULT" />
  <category android:name="android.intent.category.BROWSABLE" />
  <data android:scheme="myapp" /> <!-- Replace with your scheme -->
</intent-filter>

```

## Permissions
No additional permissions are required, but biometric support depends on the device.

## Platform Details
  ### iOS
    1. Uses LocalAuthentication for biometrics
    2. Opens URLs via ASWebAuthenticationSession
    3. The callback scheme is handled securely by the OS
    4. iOS 13+ required

  ### Android
    1. Uses BiometricPrompt for biometric check
    2. Opens URLs using CustomTabsIntent (Chrome Custom Tabs)
    3. Callback is handled through deep linking (intent filters)
  
## Troubleshooting
    1. Ensure biometric hardware is available and enrolled
    2. Ensure your redirect URI is registered in both the Auth0 dashboard and native app config
    3. For Android, ensure Chrome is available (for Custom Tabs)

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
