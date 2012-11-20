/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.io.*;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author manfred.fischer
 */
public class sicherheit {

    public sicherheit() {
        writeKey();
    }
    private Key pubKey, privKey;

    public void writeKey() {
        try {
            String algo = "RSA";
            String pubKeyPath = "Daten/netzGS." + algo;
            String privKeyPath = "Daten/netzGSW." + algo;
            privKey = readObj(privKeyPath);
            pubKey = readObj(pubKeyPath);

        } catch (Exception ex) {
        }

    }

    private Key readObj(String path) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
        Key key = (Key) in.readObject();
        in.close();
        return key;
    }

    private void writeObj(Key key, String path) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(key);
        out.close();

    }
    private static KeyPair keyPair;

    /**
     * Erzeugt ein RSA Schluesselpaar
     *
     * @return RSA Schluesselpaar
     * @throws Exception
     */
    public KeyPair getKeyPair() throws Exception {
        if (keyPair == null) {
            KeyPairGenerator kpg;
            try {
                kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                keyPair = kpg.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                throw new Exception(
                        "Fehler beim Erzeugen des Schluesselpaars: "
                        + e.getMessage());
            }
        }

        return keyPair;
    }

    protected SecretKey getKey() {
        try {
            String ss = "79aasfe3adf45khi";
            byte[] raw = ss.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            return skeySpec;
        } catch (Exception ex) {
            return null;
        }
    }

    public OutputStream encryptOutputStream(OutputStream os)
            throws Exception {
        try {

            // temporaeren AES Key erzeugen
            SecretKey key = getKey();

            // mit RSA verschluesseln und an Empfaenger senden
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.WRAP_MODE, pubKey);
            byte[] encryptedAesKey = cipher.wrap(key);
            os.write(encryptedAesKey);

            // eigentliche Nachricht mit AES verschluesseln
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            os = new CipherOutputStream(os, cipher);
        } catch (Exception e) {
            throw new Exception("Fehler beim Verschluesseln: " + e.getMessage());
        }

        return os;
    }

    public String verschluesseln(String file, String Daten) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os = encryptOutputStream(os);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(Daten);
            osw.close();
            os.close();
            return readFile(file);
        } catch (Exception ex) {
            System.out.println(ex);
            return "";
        }
    }

    public String entschluesseln(String file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            is = decryptInputStream(is);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader isrb = new BufferedReader(isr);
            String decryptedPlain = isrb.readLine();
            isrb.close();
            is.close();
            return decryptedPlain;
        } catch (Exception ex) {
            System.out.println(ex);
            return "";
        }
    }

    public String readFile(String file) throws Exception {
        InputStream is = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader isrb = new BufferedReader(isr);
        String daten = isrb.readLine();
        is.close();
        isr.close();
        isrb.close();
        return daten;
    }

    /**
     * Wrapped einen InputStream in einen entschlüsselnden CipherInputStream
     *
     * @param is InputStream
     * @return entschlüsselnder Inputstream
     * @throws Exception
     */
    public InputStream decryptInputStream(InputStream is)
            throws Exception {
        try {
            // AES Key lesen
            byte[] wrappedKey = new byte[256];
            is.read(wrappedKey, 0, 256);

            // AES Key mit RSA entschluesseln
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.UNWRAP_MODE, privKey);
            Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

            // Daten mit AES entschluesseln
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            is = new CipherInputStream(is, cipher);
        } catch (Exception e) {
            throw new Exception("Fehler beim Entschluesseln: " + e.getMessage());
        }

        return is;
    }
}
