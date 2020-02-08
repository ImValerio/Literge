import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Classe che si occupa della criptazione o decriptazione di file
 */

public class CryptUtils {
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";

	private static final String KEY = "Very good key!!!"; //Deve essere di 16bit

	public static void encrypt(File inputFile, File outputFile) throws Exception {
		doCrypto(Cipher.ENCRYPT_MODE, inputFile, outputFile);
	}

	public static void decrypt(File inputFile, File outputFile) throws Exception {
		doCrypto(Cipher.DECRYPT_MODE, inputFile, outputFile);
	}

	/**
	 * Cripta o decripta un il file in input generando un file output(a seconda dei
	 * parametri)
	 */
	private static void doCrypto(int cipherMode, File inputFile, File outputFile) throws Exception {
		try {
			Key secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			throw new Exception("Error encrypting/decrypting file", e);
		}
	}
}
