package app.wottrich.securitymanagerlibrary.fingerprint;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintEncrypt {

    private static String KEY_ENCRYPT = FingerprintKeys.KEY_ALIAS_DEFAULT_ENCODE;


    public static String encodeString (String encode) {
        try {
            Cipher cipher = encodedForm();
            if (cipher == null) return null;
            byte [] bytes = cipher.doFinal(encode.getBytes());
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decodeMyPassword(String encode) {
        try {
            Cipher cipher = loadMyCipher();
            if (cipher == null) return null;

            initDecodeCipher(cipher);
            byte[] bytes = Base64.decode(encode, Base64.NO_WRAP);
            return new String(cipher.doFinal(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Cipher encodedForm() {
        try {
            Cipher cipher = loadMyCipher();
            KeyStore key = loadMyKey();
            if (key == null || cipher == null)
                return null;
            if (!key.containsAlias(KEY_ENCRYPT))
                generateKey();
            initEncodedCipher(cipher, key, KEY_ENCRYPT);
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void initEncodedCipher(Cipher cipher, KeyStore myKey, String alias) throws Exception {
        try {
            PublicKey key = myKey.getCertificate(alias).getPublicKey();
            PublicKey unrestricted = KeyFactory.getInstance(key.getAlgorithm()).generatePublic(
                    new X509EncodedKeySpec(key.getEncoded())
            );
            OAEPParameterSpec spec = new OAEPParameterSpec("SHA-256", "MGF1",
                    MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
            cipher.init(Cipher.ENCRYPT_MODE, unrestricted, spec);
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | InvalidKeySpecException
                | InvalidAlgorithmParameterException
                | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private static void generateKey() {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            keyGenerator.initialize(new KeyGenParameterSpec.Builder(KEY_ENCRYPT,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                    .setUserAuthenticationRequired(false)
                    .build());
            keyGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }
    }


    private static void initDecodeCipher(Cipher cipher) throws Exception {
        try {
            KeyStore myKeyStore = loadMyKey();
            if (myKeyStore == null) return;

            if (!myKeyStore.containsAlias(KEY_ENCRYPT))
                generateKey();

            if (myKeyStore.getKey(KEY_ENCRYPT, null) instanceof PrivateKey) {
                PrivateKey key = (PrivateKey) myKeyStore.getKey(KEY_ENCRYPT, null);
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                SecretKey key = (SecretKey) myKeyStore.getKey(KEY_ENCRYPT, null);
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | UnrecoverableKeyException
                | InvalidKeyException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

    }

    public static void deleteKey() {
        KeyStore key = loadMyKey();
        try {
            if (key != null && key.containsAlias(KEY_ENCRYPT))
                key.deleteEntry(KEY_ENCRYPT);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private static KeyStore loadMyKey() {
        try {
            KeyStore key = KeyStore.getInstance("AndroidKeyStore");
            key.load(null);
            return key;
        } catch (KeyStoreException
                | IOException
                | NoSuchAlgorithmException
                | CertificateException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Cipher loadMyCipher() throws Exception {
        try {
            return Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

}
