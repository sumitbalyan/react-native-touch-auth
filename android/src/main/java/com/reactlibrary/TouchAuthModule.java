package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import android.widget.Toast;
import androidx.biometric.BiometricPrompt.PromptInfo;
import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;
import java.util.concurrent.Executor;

public class TouchAuthModule extends ReactContextBaseJavaModule {
    private Executor executor = new MainThreadExecutor();
    private BiometricPrompt biometricPrompt;
    private final ReactApplicationContext reactContext;
    private Callback succesCallback;
    private Callback errorCallback;
    private BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            if (errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                //biometricPrompt.cancelAuthentication();
            toast(errString.toString());
            errorCallback.invoke(errString.toString());
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            toast("Authentication succeed");
            succesCallback.invoke("success");
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            toast("Application did not recognize the placed finger print. Please try again!");
            errorCallback.invoke("Application did not recognize the placed finger print. Please try again");
        }
    };

    public TouchAuthModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
       // reactContext.addLifecycleEventListener(this);
    }
   
    @Override
    public String getName() {
        return "TouchAuth";
    }
    // @Override
    // public void onHostResume() {
    //     biometricPrompt = null;
    // }
    
    // @Override
    // public void onHostPause() {
    //     biometricPrompt = null;
    // }
    
    // @Override
    // public void onHostDestroy() {
    //     biometricPrompt = null;
    // }
    @ReactMethod
    public void auth(Callback success, Callback error) {
        succesCallback = success;
        errorCallback = error;
        final boolean check = BiometricUtils.isBiometricAvailable(this.reactContext);
        if (check && biometricPrompt == null) {
            biometricPrompt = new BiometricPrompt(this.reactContext, executor, callback);
            PromptInfo promptInfo = buildBiometricPrompt();
            biometricPrompt.authenticate(promptInfo);
        }
        toast("No biometric features available on this device.");
    }
    private PromptInfo buildBiometricPrompt() {
        return new PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("Login into your account")
                .setDescription("Touch your finger on the finger print sensor to authorise your account.")
                .setNegativeButtonText("Cancel")
                .build();
    }
    private void toast(String text) {
        Toast.makeText(this.reactContext, text, Toast.LENGTH_SHORT).show();
    }
}
