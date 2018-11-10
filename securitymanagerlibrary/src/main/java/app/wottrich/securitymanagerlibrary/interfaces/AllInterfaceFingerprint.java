package app.wottrich.securitymanagerlibrary.interfaces;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */
public interface AllInterfaceFingerprint {
    void onSuccess(FingerprintManagerCompat.AuthenticationResult result);

    void onInformation(String information);

    void onError(String error, boolean dismiss);

    void onCanceled();

    void onFailed();
}
