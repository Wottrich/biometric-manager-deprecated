package app.wottrich.securitymanagerlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import app.wottrich.securitymanagerlibrary.dialog.FingerprintDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            FingerprintDialog dialog = new FingerprintDialog(this);
            dialog.showSecurityDialog(this.getSupportFragmentManager());
        }
    }

}
