package com.hj.selenium.framework.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


/**
 * Created by David.Zheng on 30/04/2014.
 */
public class PasswordUtil
{
	private static String algorithm = "AES";

	private static byte[] keyValue = new byte[]
	  { 'P', 'A', 'C', 'T', 'E', 'R', 'D', 'A', 'V', 'I', 'D', 'Z', 'H', 'E', 'N', 'G' };

	public static String encrypt(String plainText) throws Exception
	{
		Key key = generateKey();
		Cipher chiper = Cipher.getInstance(algorithm);
		chiper.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = chiper.doFinal(plainText.getBytes());
		String encryptedValue = Base64.encodeBase64String(encVal);

		return encryptedValue;
	}

	public static String decrypt(String encryptedText) throws Exception
	{
		Key key = generateKey();
		Cipher chiper = Cipher.getInstance(algorithm);
		chiper.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.decodeBase64(encryptedText);
		byte[] decValue = chiper.doFinal(decordedValue);
		String decryptedValue = new String(decValue);

		return decryptedValue;
	}

	private static Key generateKey() throws Exception
	{
		Key key = new SecretKeySpec(keyValue, algorithm);
		return key;
	}
}
