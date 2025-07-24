package com.reactnativeauthsessionplugin;

import android.content.Intent;
import android.os.Bundle;
import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        RNAuthSessionPluginModule module = new RNAuthSessionPluginModule(getReactInstanceManager().getCurrentReactContext());
        module.onNewIntent(intent);
    }
}
