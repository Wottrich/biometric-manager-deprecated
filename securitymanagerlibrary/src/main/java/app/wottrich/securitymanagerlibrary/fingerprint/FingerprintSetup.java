package app.wottrich.securitymanagerlibrary.fingerprint;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * @author lucas.wottrich
 * @since 12/11/2018
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintSetup {

	private Cipher cipher;
	private KeyStore keyStore;

	public FingerprintSetup () {}

	@RequiresApi(api = Build.VERSION_CODES.M)
	public boolean generateKey() {
		try {
			keyStore = KeyStore.getInstance("AndroidKeyStore");
			KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
			keyStore.load(null);
			keyGenerator.init(new KeyGenParameterSpec.Builder(FingerprintKeys.KEY_ALIAS_DEFAULT_FINGERPRINT_LIB, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
					.setBlockModes(KeyProperties.BLOCK_MODE_CBC)
					.setUserAuthenticationRequired(true)
					.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
			);
			keyGenerator.generateKey();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	public boolean initCipher () {
		try {
			cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES
					+ "/" + KeyProperties.BLOCK_MODE_CBC
					+ "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);

			keyStore.load(null);
			SecretKey key = (SecretKey) keyStore.getKey(FingerprintKeys.KEY_ALIAS_DEFAULT_FINGERPRINT_LIB, null);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return true;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
