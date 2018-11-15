package app.wottrich.securitymanagerlibrary.fingerprint;

import android.support.annotation.NonNull;

import app.wottrich.securitymanagerlibrary.exception.EqualKeyException;

/**
 * @author lucas.wottrich
 * @since 10/11/2018.
 */
public class FingerprintKeys {

    public static String KEY_ALIAS_DEFAULT_FINGERPRINT_LIB = "FINGERPRINT_ALIAS_LOCK_LIB";
    public static String KEY_ALIAS_DEFAULT_ENCODE = "FINGERPRINT_ALIAS_ENCODE";

    public static void setKeyFingerprint (@NonNull String key) {
        FingerprintKeys.KEY_ALIAS_DEFAULT_FINGERPRINT_LIB = key;
    }

    public static void setKeyEncode (@NonNull String key) {
        FingerprintKeys.KEY_ALIAS_DEFAULT_ENCODE = key;
    }

    public static void setKeys (@NonNull String fingerprintKey, @NonNull String encodeKey) {
        if (!fingerprintKey.isEmpty () && !encodeKey.isEmpty () && !fingerprintKey.equals (encodeKey)) {
            FingerprintKeys.KEY_ALIAS_DEFAULT_FINGERPRINT_LIB = fingerprintKey;
            FingerprintKeys.KEY_ALIAS_DEFAULT_ENCODE = encodeKey;
        } else throw new EqualKeyException ("Keys Securities can't be equals or empty.");
    }

}
