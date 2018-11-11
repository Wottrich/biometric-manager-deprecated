package app.wottrich.securitymanagerlibrary.generics;

import android.app.Dialog;
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
import app.wottrich.securitymanagerlibrary.annotations.ContentView;


/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */
public abstract class BaseLockDialog extends DialogFragment {

    protected ViewGroup root;
    protected ViewGroup content;

    public void showSecurityDialog (@NonNull FragmentManager manager) {
        show(manager, "Fingerprint Security Manager Library");
    }

    public void dismissPattern () {
        AlphaAnimation alpha = null;
        TranslateAnimation animation = null;
        if (content != null) {
            if (root != null){
                alpha = new AlphaAnimation(1, 0);
                alpha.setRepeatCount(0);
                alpha.setDuration(350);
                alpha.setInterpolator(new AccelerateDecelerateInterpolator());
            }
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
            if (root != null && alpha != null)
                root.startAnimation(alpha);
            content.startAnimation(animation);
        }
    }

    public void jumpSerious () {
        AlphaAnimation alpha = null;
        TranslateAnimation animation = null;
        if (content != null){
            if (root != null) {
                alpha = new AlphaAnimation(0, 1);
                alpha.setRepeatCount(0);
                alpha.setDuration(350);
                alpha.setInterpolator(new AccelerateDecelerateInterpolator());
            }
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
            if (root != null && alpha != null)
                root.startAnimation(alpha);
            content.startAnimation(animation);
        }
    }

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
        //ContentView annotation = getClass().getAnnotation(ContentView.class);
        View v = inflater.inflate(R.layout.dialog_fingerprint, container, false);
        this.onLoadComponents(v);
        new Handler().post(this::onInitValues);
        return v;
    }

    protected abstract void onLoadComponents(View v);

    protected abstract void onInitValues();

}
