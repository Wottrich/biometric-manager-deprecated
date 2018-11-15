package app.wottrich.securitymanagerlibrary.generics;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import app.wottrich.securitymanagerlibrary.R;
import app.wottrich.securitymanagerlibrary.exception.CancelButtonException;


/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */
public abstract class BaseLockDialog extends DialogFragment {

    //<editor-folder defaultstate="Collapsed" desc="View's">
    protected ViewGroup root;
    protected ViewGroup content;

    protected boolean otherLayout = false;
    protected int layout = R.layout.dialog_fingerprint;
    protected View v;
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Default animations">
    protected TranslateAnimation enterAnimation = defaultTranslateAnimation(false);
    protected TranslateAnimation exitAnimation = defaultTranslateAnimation(true);
    protected AlphaAnimation alphaEnterAnimation = defaultAlphaAnimation(false);
    protected AlphaAnimation alphaExitAnimation = defaultAlphaAnimation(true);
    //</editor-folder>

    public void showSecurityDialog(@NonNull FragmentManager manager) {
        show(manager, "Fingerprint Security Manager Library");
    }

    //<editor-folder defaultstate="Collapsed" desc="Enter and Exit">

    /**
     * This method is used to dismiss dialog with default or custom animation
     */
    public void dismissPattern() {
        if (root != null)
            root.startAnimation(alphaExitAnimation);

        if (content != null && exitAnimation != null)
            content.startAnimation(exitAnimation);
        else BaseLockDialog.this.dismiss();
    }

    /**
     * This method is used to show dialog with default or custom animation
     */
    public void jumpSerious() {
        if (root != null)
            root.startAnimation(alphaEnterAnimation);

        if (content != null && enterAnimation != null)
            content.startAnimation(enterAnimation);
    }
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Animations">

    /**
     * @param dismiss used to control type animation
     * @return return default animation
     */
    public TranslateAnimation defaultTranslateAnimation(boolean dismiss) {
        TranslateAnimation animation;
        if (! dismiss) {
            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0f,
                    Animation.RELATIVE_TO_PARENT, 0f,
                    Animation.RELATIVE_TO_PARENT, 1f,
                    Animation.RELATIVE_TO_PARENT, 0f);
            animation.setRepeatMode(0);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(350);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    content.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0f,
                    Animation.RELATIVE_TO_PARENT, 0f,
                    Animation.RELATIVE_TO_PARENT, 0f,
                    Animation.RELATIVE_TO_PARENT, 1f);

            animation.setRepeatMode(0);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(350);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    BaseLockDialog.this.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return animation;
    }

    /**
     * @param dismiss used to control type animation
     * @return return default animation
     */
    public AlphaAnimation defaultAlphaAnimation(boolean dismiss) {
        AlphaAnimation alpha;
        if (! dismiss) {
            alpha = new AlphaAnimation(0, 1);
            alpha.setRepeatCount(0);
            alpha.setDuration(350);
            alpha.setInterpolator(new AccelerateDecelerateInterpolator());
        } else {
            alpha = new AlphaAnimation(1, 0);
            alpha.setRepeatCount(0);
            alpha.setDuration(350);
            alpha.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return alpha;
    }

    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Override Method's">
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.dimAmount = 0;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null)
            v = inflater.inflate(layout, container, false);


        if (! otherLayout)
            this.onLoadComponents(v);

        new Handler().post(this::onInitValues);
        return v;
    }
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Controller View's">

    /**
     * Create {@link LayoutInflater} and inflate a custom layout
     *
     * @param context used to create LayoutInflater
     */
    public void createView(Context context) {
        if (v == null)
            v = LayoutInflater.from(context).inflate(layout, null, false);
    }

    /**
     * @return custom layout
     */
    public View getLayoutView() {
        return v;
    }
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Set Method's">
    public void setContent(ViewGroup view) {
        this.content = view;
    }

    public void setRoot(ViewGroup view) {
        this.root = view;
    }

    public void setEnterAnimation(TranslateAnimation enterAnimation) {
        this.enterAnimation = enterAnimation;
    }

    public void setExitAnimation(TranslateAnimation exitAnimation) {
        this.exitAnimation = exitAnimation;
    }

    public void setEnterDefaultAnimation() {
        this.enterAnimation = defaultTranslateAnimation(false) ;
    }

    public void setExitDefaultAnimation() {
        this.exitAnimation = defaultTranslateAnimation(true);
    }

    public void setAlphaEnterAnimation(AlphaAnimation alphaEnterAnimation) {
        this.alphaEnterAnimation = alphaEnterAnimation;
    }

    public void setAlphaExitAnimation(AlphaAnimation alphaExitAnimation) {
        this.alphaExitAnimation = alphaExitAnimation;
    }
    //</editor-folder>

    //<editor-folder defaultstate="Collapsed" desc="Abstract Method's">
    protected abstract void onLoadComponents(View v);

    protected abstract void onInitValues() throws CancelButtonException;
    //</editor-folder>

}
