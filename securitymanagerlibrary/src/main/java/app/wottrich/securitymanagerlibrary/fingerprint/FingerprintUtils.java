package app.wottrich.securitymanagerlibrary.fingerprint;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintUtils {

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public static boolean isHardwareDetected (Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public static boolean hasEnrolledFingerprints (Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public static boolean isFingerprintAvailable (Context context) {
        return isHardwareDetected(context) && hasEnrolledFingerprints(context);
    }
}
