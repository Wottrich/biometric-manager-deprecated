package app.wottrich.securitymanagerlibrary.fingerprint;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import app.wottrich.securitymanagerlibrary.interfaces.AllInterfaceFingerprint;
import app.wottrich.securitymanagerlibrary.interfaces.OnDismissFingerprintHelper;
import app.wottrich.securitymanagerlibrary.interfaces.OnErrorFingerprint;
import app.wottrich.securitymanagerlibrary.interfaces.OnFailedFingerprint;
import app.wottrich.securitymanagerlibrary.interfaces.OnInformationFingerprint;
import app.wottrich.securitymanagerlibrary.interfaces.OnSuccessFingerprint;


/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHelper extends FingerprintManagerCompat.AuthenticationCallback implements OnDismissFingerprintHelper {

    private CancellationSignal cancellationSignal;
    private OnSuccessFingerprint onSuccessFingerprint;
    private OnInformationFingerprint onInformationFingerprint;
    private OnErrorFingerprint onErrorFingerprint;
    private OnFailedFingerprint onFailedFingerprint;

    private AllInterfaceFingerprint allInterfaceFingerprint;

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public FingerprintHelper () {}

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public void attachCallback(OnSuccessFingerprint onSuccessFingerprint, OnInformationFingerprint onInformationFingerprint, OnErrorFingerprint onErrorFingerprint, OnFailedFingerprint onFailedFingerprint) {
        this.onSuccessFingerprint = onSuccessFingerprint;
        this.onInformationFingerprint = onInformationFingerprint;
        this.onErrorFingerprint = onErrorFingerprint;
        this.onFailedFingerprint = onFailedFingerprint;
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public void allCallback(AllInterfaceFingerprint allInterfaceFingerprint) {
        this.allInterfaceFingerprint = allInterfaceFingerprint;
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public void startAuth (FingerprintManagerCompat managerCompat, FingerprintManagerCompat.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        managerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null);
    }

    public FingerprintHelper getDismissListener () {
        return this;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (this.allInterfaceFingerprint != null)
            this.allInterfaceFingerprint.onError(errString.toString(), errMsgId == 7 || errMsgId == 5);
        else if (this.onErrorFingerprint != null)
            this.onErrorFingerprint.onError(errString.toString(), errMsgId == 7 || errMsgId == 5);

        super.onAuthenticationError(errMsgId, errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        if (this.allInterfaceFingerprint != null)
            this.allInterfaceFingerprint.onInformation(helpString.toString());
        else if (this.onInformationFingerprint != null)
            this.onInformationFingerprint.onInformation(helpString.toString());

        super.onAuthenticationHelp(helpMsgId, helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        if (this.allInterfaceFingerprint != null)
            this.allInterfaceFingerprint.onSuccess(result);
        else if (this.onSuccessFingerprint != null)
            this.onSuccessFingerprint.onSuccess(result);

        super.onAuthenticationSucceeded(result);
    }

    @Override
    public void onAuthenticationFailed() {
        if (this.allInterfaceFingerprint != null)
            this.allInterfaceFingerprint.onFailed();
        else if (this.onFailedFingerprint != null)
            this.onFailedFingerprint.onFailed();

        super.onAuthenticationFailed();
    }

    @Override
    public void onDismiss() {
        if (cancellationSignal != null)
            cancellationSignal.cancel();
    }
}
