package service.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author HieuPham
 *
 */
public class CryptoUtils {
	
	/**
	 * 
	 * @param secrect
	 * @param data
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String createHMAC(String secret, String data, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec key = new SecretKeySpec(secret.getBytes(),algorithm);
		
		Mac mac = Mac.getInstance(algorithm);
		mac.init(key);
		
		byte[] result = mac.doFinal(data.getBytes());
		Formatter formatter = new Formatter();
		for (byte b : result) {
			formatter.format("%02x", b);
		}

		String expectSignature = formatter.toString();
		formatter.close();
		return expectSignature;
	}
}
