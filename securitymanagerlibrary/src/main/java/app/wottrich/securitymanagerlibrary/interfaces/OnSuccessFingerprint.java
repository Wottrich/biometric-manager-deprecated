package app.wottrich.securitymanagerlibrary.interfaces;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */
public interface OnSuccessFingerprint {
    void onSuccess(FingerprintManagerCompat.AuthenticationResult result);
}
