package com.example.tasks.service.helper;

import android.content.Context;
import android.os.Build;

import androidx.biometric.BiometricManager;

public class FingerprintHelper {

    public static boolean isAvailable(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        String s;

        BiometricManager biometricManager = BiometricManager.from(context);
        int canAuthenticate = biometricManager.canAuthenticate();
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }
}
