package it.dpe.igeriv.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtils {
	private static final String MESSAGE_DIGEST_ALGORITHM = "MD5";
	private static final String CYPHER_ALGORITHM = "AES";

	public static Cipher getEncrypter() throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
		Cipher result = null;

		MessageDigest digester = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
		digester.update("password".getBytes("UTF8"));
		byte[] passwordAsBA = digester.digest();

		Key key = new SecretKeySpec(passwordAsBA, CYPHER_ALGORITHM);

		result = Cipher.getInstance(key.getAlgorithm());
		result.init(Cipher.ENCRYPT_MODE, key);

		return result;
	}

	public static Cipher getDecrypter() throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
		Cipher result = null;

		MessageDigest digester = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
		digester.update("password".getBytes("UTF8"));
		byte[] passwordAsBA = digester.digest();

		Key key = new SecretKeySpec(passwordAsBA, CYPHER_ALGORITHM);

		result = Cipher.getInstance(key.getAlgorithm());
		result.init(Cipher.DECRYPT_MODE, key);

		return result;
	}

	public static String encrypt(Cipher cipher, String data) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		String result = null;

		byte[] dataUTF8 = data.getBytes("UTF8");
		byte[] encoded = cipher.doFinal(dataUTF8);
		result = Base64.encodeBase64String(encoded);

		return result;
	}

	public static String decrypt(Cipher cipher, String data) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		String result = null;

		byte[] dataAsBA = Base64.decodeBase64(data);
		byte[] encrypted = cipher.doFinal(dataAsBA);
		result = new String(encrypted, "UTF8");

		return result;
	}

}
