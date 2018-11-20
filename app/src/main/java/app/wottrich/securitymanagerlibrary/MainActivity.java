package app.wottrich.securitymanagerlibrary;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.wottrich.securitymanagerlibrary.dialog.FingerprintDialog;
import app.wottrich.securitymanagerlibrary.generics.BaseLockDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void defaultFingerprint(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            FingerprintDialog dialog = new FingerprintDialog(this);
            dialog.setSuccess(result -> Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show());
            dialog.setError((error, dismiss) -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
            dialog.showSecurityDialog(this.getSupportFragmentManager());
        }
    }

    public void customFingerprint(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            FingerprintDialog dialog = new FingerprintDialog (this, R.layout.dialog_fingerprint_pattern);
            View v = dialog.getLayoutView ();

            dialog.setDismissAfterSuccess (false);
            dialog.setBtnCancel ((TextView) v.findViewById (R.id.tvCancelAction));

            TextView tvPassword = v.findViewById (R.id.tvUsePasswordAction);
            tvPassword.setOnClickListener (viewButton -> dialog.dismissPattern ());

            dialog.setSuccess (result -> {
                Toast.makeText (this, "Success", Toast.LENGTH_SHORT).show ();

                LinearLayout llContainer = v.findViewById (R.id.llContainer);
				TextView tvInformation = v.findViewById (R.id.tvSensor);
				ImageView ivFingerprint = v.findViewById (R.id.ivFingerprint);

				ivFingerprint.setColorFilter (this.getResources ().getColor (R.color.mid_green, this.getTheme ()));
                llContainer.setBackgroundResource (R.drawable.shape_fingprint_success);
				tvInformation.setText ("Success!");

				llContainer.postDelayed (dialog::dismissPattern, 250);
            });

            dialog.setError (((error, dismiss) -> {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

                LinearLayout llContainer = v.findViewById (R.id.llContainer);
                ImageView ivFingerprint = v.findViewById (R.id.ivFingerprint);

                ivFingerprint.setColorFilter (this.getResources ().getColor (R.color.mid_red, this.getTheme ()));
                llContainer.setBackgroundResource (R.drawable.shape_fingeprint_error);

                if (dismiss)
                    llContainer.postDelayed (dialog::dismissPattern, 250);
            }));

            dialog.setFailed (() -> {

                LinearLayout llContainer = v.findViewById (R.id.llContainer);
                TextView tvInformation = v.findViewById (R.id.tvSensor);
                ImageView ivFingerprint = v.findViewById (R.id.ivFingerprint);

                ivFingerprint.setColorFilter (this.getResources ().getColor (R.color.mid_red, this.getTheme ()));
                llContainer.setBackgroundResource (R.drawable.shape_fingeprint_error);
                tvInformation.setText ("Failed. Try Again!");
            });

            dialog.showSecurityDialog (this.getSupportFragmentManager ());
        }
    }
}
