package app.wottrich.securitymanagerlibrary;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import app.wottrich.securitymanagerlibrary.dialog.FingerprintDialog;
import app.wottrich.securitymanagerlibrary.generics.BaseLockDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //BaseLockDialog.setLayout (R.layout.test_dialog_fingerprint);
            FingerprintDialog dialog = new FingerprintDialog(this);
            dialog.showSecurityDialog(this.getSupportFragmentManager());
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder (this);
        AlertDialog alertDialog = dialogBuilder.create ();
        //alertDialog.findViewById ()
    }

}
