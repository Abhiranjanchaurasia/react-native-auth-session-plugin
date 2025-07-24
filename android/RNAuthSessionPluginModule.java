package com.reactnativeauthsessionplugin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.browser.customtabs.CustomTabsIntent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.concurrent.Executor;

public class RNAuthSessionPluginModule extends ReactContextBaseJavaModule implements ActivityEventListener {
  private static Promise pendingPromise;
  private static String callbackScheme;

  public RNAuthSessionPluginModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(this);
  }

  @NonNull
  @Override
  public String getName() {
    return "RNAuthSessionPlugin";
  }

  @ReactMethod
  public void login(String loginUrl, String scheme, Promise promise) {
    startAuthSession(loginUrl, scheme, promise);
  }

  @ReactMethod
  public void logout(String logoutUrl, String scheme, Promise promise) {
    startAuthSession(logoutUrl, scheme, promise);
  }

  private void startAuthSession(String url, String scheme, Promise promise) {
    Activity activity = getCurrentActivity();
    if (activity == null) {
      promise.reject("no_activity", "No current activity");
      return;
    }
    BiometricManager biometricManager = BiometricManager.from(activity);
    if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_SUCCESS) {
      promise.reject("biometric_unavailable", "Biometric authentication not available");
      return;
    }
    Executor executor = ContextCompat.getMainExecutor(activity);
    BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
      @Override
      public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
          pendingPromise = promise;
          callbackScheme = scheme;
          CustomTabsIntent intent = new CustomTabsIntent.Builder().build();
          intent.launchUrl(activity, Uri.parse(url));
        });
      }
      @Override
      public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
        promise.reject("biometric_failed", errString.toString());
      }
      @Override
      public void onAuthenticationFailed() {
        promise.reject("biometric_failed", "Biometric authentication failed");
      }
    };
    BiometricPrompt prompt = new BiometricPrompt(activity, executor, callback);
    BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
      .setTitle("Authenticate")
      .setSubtitle("Biometric authentication required")
      .setNegativeButtonText("Cancel")
      .build();
    prompt.authenticate(promptInfo);
  }

  @Override
  public void onNewIntent(Intent intent) {
    if (pendingPromise != null && intent != null && intent.getData() != null) {
      Uri uri = intent.getData();
      if (uri != null && uri.toString().startsWith(callbackScheme)) {
        pendingPromise.resolve(uri.toString());
        pendingPromise = null;
        callbackScheme = null;
      }
    }
  }

  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {}
}
