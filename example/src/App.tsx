import React, { useState } from 'react';
import { Text, View, StyleSheet, Button } from 'react-native';
import AuthSessionPlugin from 'react-native-auth-session-plugin';

export default function App() {
  const [result, setResult] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleLogin = async () => {
    setError(null);
    try {
      const callbackUrl = await AuthSessionPlugin.login(
        'https://example-login-url',
        'custom scheme'
      );
      setResult(callbackUrl);
    } catch (e: any) {
      setError(e?.message || 'Login failed');
    }
  };

  const handleLogout = async () => {
    setError(null);
    try {
      const callbackUrl = await AuthSessionPlugin.logout(
        'https://example-logout-url',
        'com.lsportal.migration'
      );
      setResult(callbackUrl);
    } catch (e: any) {
      setError(e?.message || 'Logout failed');
    }
  };

  return (
    <View style={styles.container}>
      <Button title="Login with Auth0" onPress={handleLogin} />
      <Button title="Logout" onPress={handleLogout} />
      {result && <Text>Callback URL: {result}</Text>}
      {error && <Text style={styles.errorText}>{error}</Text>}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  errorText: {
    color: 'red',
  },
});
