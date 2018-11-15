package app.wottrich.securitymanagerlibrary;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
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
            FingerprintDialog dialog = new FingerprintDialog(this, R.layout.test_dialog_fingerprint);
            View myView = dialog.getLayoutView();

            //required
            dialog.setBtnCancel(myView.findViewById(R.id.btnCancel));

            //optional
            dialog.setRoot(myView.findViewById(R.id.root));
            dialog.setContent(myView.findViewById(R.id.clContent));

            //optional
            dialog.setEnterDefaultAnimation();
            dialog.setExitDefaultAnimation();

            dialog.showSecurityDialog(this.getSupportFragmentManager());
        }
    }
}
