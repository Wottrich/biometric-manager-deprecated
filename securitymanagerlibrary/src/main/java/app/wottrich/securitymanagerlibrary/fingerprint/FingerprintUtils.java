package app.wottrich.securitymanagerlibrary.fingerprint;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintUtils {

    public static boolean isHardwareDetected (Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    public static boolean hasEnrolledFingerprints (Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    public static boolean isFingerprintAvailable (Context context) {
        return isHardwareDetected(context) && hasEnrolledFingerprints(context);
    }
}
