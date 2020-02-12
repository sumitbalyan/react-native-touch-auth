package com.reactlibrary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.biometric.BiometricManager;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

public class BiometricUtils{

    public static boolean isBiometricPromptEnabled(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }

    public static boolean isSDKVersionSupported(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static boolean isHardwareSupported(Context context){
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        return fingerprintManager.isHardwareDetected();
    }

    public static boolean isFingerPrintAvailable(Context context){
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        return fingerprintManager.hasEnrolledFingerprints();
    }

    public static boolean isPermissionGranted(Context context){
        return (
                        (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)== PackageManager.PERMISSION_GRANTED)
                        ||
                        (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC)== PackageManager.PERMISSION_GRANTED));
    }

    public static boolean isBiometricAvailable(Context context){
        BiometricManager biometricManager = BiometricManager.from(context);
        boolean check = false;
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                Toast.makeText(context,"App can authenticate using biometrics.",Toast.LENGTH_LONG).show();
                check =  true;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                Toast.makeText(context,"No biometric features available on this device.",Toast.LENGTH_LONG).show();
                check =  false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                Toast.makeText(context,"Biometric features are currently unavailable.",Toast.LENGTH_LONG).show();
                check =  false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("MY_APP_TAG", "The user hasn't associated " +
                        "any biometric credentials with their account.");
                Toast.makeText(context,"The user hasn't associated any biometric credentials with their account.",Toast.LENGTH_LONG).show();
                check =  false;
                break;
        }
        return  check;
    }

}