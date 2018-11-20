package app.wottrich.securitymanagerlibrary.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import app.wottrich.securitymanagerlibrary.R;
import app.wottrich.securitymanagerlibrary.exception.CancelButtonException;
import app.wottrich.securitymanagerlibrary.fingerprint.FingerprintHelper;
import app.wottrich.securitymanagerlibrary.fingerprint.FingerprintKeys;
import app.wottrich.securitymanagerlibrary.fingerprint.FingerprintSetup;
import app.wottrich.securitymanagerlibrary.fingerprint.FingerprintUtils;
import app.wottrich.securitymanagerlibrary.generics.BaseLockDialog;
import app.wottrich.securitymanagerlibrary.interfaces.AllInterfaceFingerprint;
import app.wottrich.securitymanagerlibrary.interfaces.OnCanceledFingerprint;
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
@SuppressLint("ValidFragment")
public class FingerprintDialog extends BaseLockDialog implements View.OnClickListener, OnSuccessFingerprint, OnErrorFingerprint, OnFailedFingerprint, OnInformationFingerprint, AllInterfaceFingerprint {

    //<editor-folder defaultstate="Collapsed" desc="Widgets Fingerprint">
    private ImageView ivFingerprint;
    private TextView tvInformation;

    private TextView tvTitle;
    private String title = "";

    private TextView tvSubTitle;
    private String subTitle = "";

    private TextView tvMessage;
    private String message = "";

    private Button btnCancel;
    private TextView tvCancel;
    private View.OnClickListener listenerCanceled;
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Context and variables Fingerprint">
    private boolean hasSetCanceled = true;
    private Activity activity;
    private PreferenceManager.OnActivityResultListener activityResult;
    private Fragment fragment;
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Security Key and FingerprintManager">
    private FingerprintSetup setup;
    private FingerprintManagerCompat manager;
    private FingerprintHelper helper;
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Interface Callback Fingerprint">
    private OnSuccessFingerprint success;
    private OnErrorFingerprint error;
    private OnInformationFingerprint information;
    private OnCanceledFingerprint canceled;
    private OnFailedFingerprint failed;
    private AllInterfaceFingerprint all;
	private boolean dismissAfterSuccess = true;
	//</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Constructors Defaults with Activity">
    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public FingerprintDialog(@NonNull Activity activity) {
        this.activity = activity;
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public FingerprintDialog(@NonNull Activity activity, @LayoutRes int layout) {
        this.activity = activity;
        this.layout = layout;
        this.otherLayout = true;
        this.enterAnimation = null;
        this.exitAnimation = null;
        this.hasSetCanceled = true;
        createView(activity);
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public FingerprintDialog(@NonNull Activity activity, @NonNull View.OnClickListener listenerCanceled) {
        this.activity = activity;
        this.listenerCanceled = listenerCanceled;
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public FingerprintDialog(@NonNull Activity activity, @NonNull AllInterfaceFingerprint allCallback) {
        this.activity = activity;
        this.all = allCallback;
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public FingerprintDialog(@NonNull Activity activity, @NonNull OnSuccessFingerprint success, @NonNull OnErrorFingerprint error) {
        this.activity = activity;
        this.success = success;
        this.error = error;
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public FingerprintDialog(@NonNull Activity activity, @NonNull OnSuccessFingerprint success, @NonNull OnErrorFingerprint error, @NonNull OnInformationFingerprint information, @NonNull OnCanceledFingerprint canceled) {
        this.activity = activity;
        this.success = success;
        this.error = error;
        this.information = information;
        this.canceled = canceled;
    }
    //</editor-folder>

    public FingerprintDialog(Fragment fragment , @NonNull View.OnClickListener listenerCanceled) {
        this.fragment = fragment;
        this.listenerCanceled = listenerCanceled;
    }

    @Override
    protected void onLoadComponents(View v) {
        if (otherLayout)
            return;

        root = v.findViewById(R.id.clRootFingerprint);
        content = v.findViewById(R.id.clContent);
        ivFingerprint = v.findViewById(R.id.ivFingerprint);
        tvInformation = v.findViewById(R.id.tvInformation);
        tvTitle = v.findViewById(R.id.tvTitle);
        tvSubTitle = v.findViewById(R.id.tvSubTitle);
        tvMessage = v.findViewById(R.id.tvMessage);
        btnCancel = v.findViewById(R.id.btnCancel);

        this.jumpSerious();
    }

    @Override
    protected void onInitValues() {
        if (!otherLayout) {
            setTextWithDefaultMessage(tvTitle, title, "Fingerprint Confirm");
            setText(tvSubTitle, subTitle);
            setText(tvMessage, message);
            btnCancel.setOnClickListener(this);
        } else if (btnCancel == null && tvCancel == null) throw new CancelButtonException();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (activity.checkSelfPermission(Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                if (! activity.shouldShowRequestPermissionRationale(Manifest.permission.USE_FINGERPRINT))
                    activity.requestPermissions(new String[] {Manifest.permission.USE_FINGERPRINT}, 1);
            } else {
                manager = FingerprintManagerCompat.from(this.activity);
                setup = new FingerprintSetup ();
                initFingerprint();
            }
        else Log.w("WARNING LOCK LIB", "INVALID SDK VERSION");


    }

    //<editor-folder defaultstate="Collapsed" desc="Settings Fingerprint">
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initFingerprint() {
        try {
            if (FingerprintUtils.isFingerprintAvailable(activity)) {
                setupFingerprint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupFingerprint() {
        KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager != null && keyguardManager.isKeyguardSecure()) {
            if (setup.generateKey() && setup.initCipher()) {
                FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(setup.getCipher());

                helper = new FingerprintHelper();
                if (all != null) helper.allCallback(this);
                else helper.attachCallback(this, this, this, this);
                helper.startAuth(manager, cryptoObject);
            }
        } else error.onError("Error to generator fingerprint keyguard manager", true);
    }
    //</editor-folder>

    private void setText(TextView tv, String text) {
        if (text != null && ! text.isEmpty()) {
            if (tv != null)
                tv.setText(text);
        } else if (tv != null) tv.setVisibility(View.GONE);
    }

    private void setTextWithDefaultMessage(TextView tv, String text, String defaultMessage) {
        if (text != null && ! text.isEmpty()) {
            if (tv != null)
                tv.setText(text);
        } else if (tv != null){
            tv.setText(defaultMessage);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clRootFingerprint || v.getId() == R.id.btnCancel || (btnCancel != null && v.getId() == btnCancel.getId()) || (tvCancel != null && v.getId () == tvCancel.getId ())) {
            dismissPattern();
            if (listenerCanceled != null)
                listenerCanceled.onClick(v);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (helper != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            helper.onDismiss();

        super.onDismiss(dialog);
    }

    //<editor-folder defaultstate="Collapsed" desc="Base Interface Callback`s">
    @Override
    public void onSuccess(FingerprintManagerCompat.AuthenticationResult result) {
        if (all != null) all.onSuccess(result);
        else if (success != null) success.onSuccess(result);

        if (dismissAfterSuccess)
        	dismissPattern();
    }

    @Override
    public void onError(String errorMessage, boolean dismiss) {
        if (all != null) all.onError(errorMessage, dismiss);
        else if (error != null) error.onError(errorMessage, dismiss);

        dismissPattern();
    }

    @Override
    public void onCanceled() {
        if (all != null) all.onCanceled();
        else if (canceled != null) canceled.onCanceled();
    }

    @Override
    public void onFailed() {
        if (!otherLayout) {
            if (getActivity() != null && getActivity().getResources() != null)
                ivFingerprint.setColorFilter(getActivity().getResources().getColor(android.R.color.holo_red_dark));

            setText(tvInformation, "Failed. Try again!");
        }
        if (all != null) all.onFailed();
        else if (failed != null) failed.onFailed();
    }

    @Override
    public void onInformation(String informationMessage) {
        setText(tvInformation, informationMessage);
        if (all != null) all.onInformation(informationMessage);
        else if (information != null) information.onInformation(informationMessage);
    }
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Callback`s">
    public void setSuccessCallback(OnSuccessFingerprint success) {
        this.success = success;
    }

    public void setInformationCallback(OnInformationFingerprint information) {
        this.information = information;
    }

    public void setErrorCallback(OnErrorFingerprint error) {
        this.error = error;
    }

    public void setCanceledCallback(OnCanceledFingerprint canceled) {
        this.canceled = canceled;
    }

    public void setFailedCallback(OnFailedFingerprint failed) {
        this.failed = failed;
    }

    public void setAllCallBack(AllInterfaceFingerprint all) {
        this.all = all;
    }
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Methods and Methods with FingerprintDialog instance">
    public void setBtnCancel (Button btnCancel) {
        this.btnCancel = btnCancel;
        this.btnCancel.setOnClickListener(this);
    }

    public void setBtnCancel (TextView tvCancel) {
        this.tvCancel = tvCancel;
        this.tvCancel.setOnClickListener(this);
    }

	public void setDismissAfterSuccess (boolean dismissAfterSuccess) {
		this.dismissAfterSuccess = dismissAfterSuccess;
	}

	public void setListenerCanceled (View.OnClickListener listenerCanceled) {
        this.listenerCanceled = listenerCanceled;
    }

    public FingerprintDialog canceledListener(View.OnClickListener listenerCanceled) {
        this.listenerCanceled = listenerCanceled;
        return this;
    }

	public void setSuccess (OnSuccessFingerprint success) {
		this.success = success;
	}

	public void setError (OnErrorFingerprint error) {
		this.error = error;
	}

    public void setFailed (OnFailedFingerprint failed) {
        this.failed = failed;
    }

    //<editor-folder defaultstate="Collapsed" desc="Pack`s fingerprint callback">
    public void defaultCallback(OnSuccessFingerprint success, OnErrorFingerprint error){
        this.success = success;
        this.error = error;
    }

    public FingerprintDialog defaultCallbackInstance (OnSuccessFingerprint success, OnErrorFingerprint error){
        this.success = success;
        this.error = error;
        return this;
    }

    public void helpCallback (OnInformationFingerprint information, OnFailedFingerprint failed) {
        this.information = information;
        this.failed = failed;
    }

    public FingerprintDialog helpCallbackInstance (OnInformationFingerprint information, OnFailedFingerprint failed) {
        this.information = information;
        this.failed = failed;
        return this;
    }
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Information">
    public FingerprintDialog setTitle (String title) {
        this.title = title;
        return this;
    }

    public FingerprintDialog setSubTitle (String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public FingerprintDialog setMessage (String message) {
        this.message = message;
        return this;
    }

    public FingerprintDialog setInformation (@NonNull String title, @NonNull String subTitle, @NonNull String message) {
        this.title = title;
        this.subTitle = subTitle;
        this.message = message;
        return this;
    }
    //</editor-folder>

	public FingerprintDialog setDismissAfterSuccessInstance (boolean dismissAfterSuccess) {
		this.dismissAfterSuccess = dismissAfterSuccess;
		return this;
	}

    public FingerprintDialog allCallback (AllInterfaceFingerprint all) {
        this.all = all;
        return this;
    }

    public FingerprintDialog setSuccessInstance (OnSuccessFingerprint success) {
        this.success = success;
        return this;
    }

    public FingerprintDialog setInformation(OnInformationFingerprint information) {
        this.information = information;
        return this;
    }

    public FingerprintDialog setErrorInstance (OnErrorFingerprint error) {
        this.error = error;
        return this;
    }

    public FingerprintDialog setCanceled(OnCanceledFingerprint canceled) {
        this.canceled = canceled;
        return this;
    }

    public FingerprintDialog setFailedInstance (OnFailedFingerprint failed) {
        this.failed = failed;
        return this;
    }

    public FingerprintDialog setAll(AllInterfaceFingerprint all) {
        this.all = all;
        return this;
    }

    public FingerprintDialog setOnActivityResult(PreferenceManager.OnActivityResultListener activityResult) {
        this.activityResult = activityResult;
        return this;
    }
    //</editor-folder>

}
