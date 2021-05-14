package com.graduation.ecommerce.design.utils;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAUtils {
	
	private static final String RSA="RSA";
	private static final String MD5WITHRSA="MD5withRSA";
	private static final Logger logger =  LoggerFactory.getLogger(RSAUtils.class);
	private static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();
	private static final java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
	private static final java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
	public static KeyPair getKey() throws Exception {
		try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA,PROVIDER);
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;
        } catch (Exception e) {
            logger.error("获取RSA密钥对异常",e);
            throw new Exception("获取RSA密钥对异常",e);
        }
	}
	public static String encryptStr(RSAPublicKey publicKey, String str) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(RSA, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int key_len = publicKey.getModulus().bitLength()/8;
            String  data = new String(str.getBytes(),"ISO-8859-1");
            String[] datas = splitString(data,key_len-11);
            String result = "";
            for(String s:datas){
                result+=bcd2Str(cipher.doFinal(s.getBytes("ISO-8859-1")));
            }
            return result;
        } catch (Exception e) {
            logger.error("RSA公钥加密异常",e);
            throw new Exception("RSA公钥加密异常",e);
        }
    }
    //byte 数组十进制转十六进制字符串
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }
    //切分字符串
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i=0; i<x+z; i++) {
            if (i==x+z-1 && y!=0) {
                str = string.substring(i*len, i*len+y);
            }else{
                str = string.substring(i*len, i*len+len);
            }
            strings[i] = str;
        }
        return strings;
    }
    public static String decryptJSStr(RSAPrivateKey privateKey,String str) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int key_len = privateKey.getModulus().bitLength()/8;
        byte[] real_bytes = decoder.decode(str.getBytes("UTF-8"));
        String result = "";
        byte[][] arrays =splitArray(real_bytes,key_len);
        for(byte[] arr:arrays){
            result+=new String(cipher.doFinal(arr),"UTF-8");
        }
        return new String(result.getBytes("UTF-8"));
    }
	 public static String decryptStr(RSAPrivateKey privateKey, String str) throws Exception {
	        try {
	            Cipher cipher = Cipher.getInstance(RSA, PROVIDER);
	            cipher.init(Cipher.DECRYPT_MODE, privateKey);
	            int key_len = privateKey.getModulus().bitLength()/8;
	            byte[] bytes = str.getBytes("ISO-8859-1");
	            byte[] bcd =ASCII_To_BCD(bytes,bytes.length);
                String result = "";
                byte[][] arrays =splitArray(bcd,key_len);
                for(byte[] arr:arrays){
                    result+=new String(cipher.doFinal(arr),"ISO-8859-1");
                }
                return new String(result.getBytes("ISO-8859-1"));
	        } catch (Exception e) {
	            logger.error("RSA私钥解密异常",e);
	            throw new Exception("RSA私钥解密异常",e);
	        }
	    }
    public static byte[][] splitArray(byte[] data,int len){
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if(y!=0){
            z = 1;
        }
        byte[][] arrays = new byte[x+z][];
        byte[] arr;
        for(int i=0; i<x+z; i++){
            arr = new byte[len];
            if(i==x+z-1 && y!=0){
                System.arraycopy(data, i*len, arr, 0, y);
            }else{
                System.arraycopy(data, i*len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }
    public static byte asc_to_bcd(byte asc) {
        byte bcd;
        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }
    public static PrivateKey getPrivateKey(String key) throws Exception {
	    byte[] privateKeyCode = decoder.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyCode);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    public static PublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyCode = decoder.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyCode);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    //TODO:私钥加密 验签
	public static String sign(byte[] data, PrivateKey privateKey, String signType) throws Exception {
        Signature signature = Signature.getInstance(signType);
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signByte = signature.sign();
        return new String(Base64.encode(signByte));
	}
	public static String signMD5withRSA(byte[] data, PrivateKey privateKey) throws Exception {
        return sign(data, privateKey, MD5WITHRSA);
    }
	public static boolean verify(byte[] data, byte[] sign, PublicKey publicKey, String signType) {
        try {
            Signature signature = Signature.getInstance(signType);
            signature.initVerify(publicKey);
            signature.update(data);
            byte[] keyByte = Base64.decode(sign);
            return signature.verify(keyByte);
        } catch (Exception e) {
            logger.error("RSA公钥验签异常", e);
            return false;
        }
    }
	public static void verifyMD5withRSA(byte[] data, byte[] sign, PublicKey publicKey) throws Exception {
        if(!verify(data, sign, publicKey, MD5WITHRSA)) {
            throw new Exception("MD5withRSA公钥验签异常");
        }
    }
}
