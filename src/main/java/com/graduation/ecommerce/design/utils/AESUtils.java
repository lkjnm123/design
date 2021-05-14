package com.graduation.ecommerce.design.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Random;

public class AESUtils {
	private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);
    
    public static final String AES = "AES";
     
    public static final String AES_TYPE = "AES/ECB/PKCS5Padding";
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    public static String getAesKey() {
        Random random = new Random();
        String result = "";
        for(int i = 0; i< 16; i++){
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if( "char".equalsIgnoreCase(charOrNum) ) {
                // int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                result += (char)(random.nextInt(26) + 65);
            } else {
                result += String.valueOf(random.nextInt(10));
            }
        }
        return result;
    }
     
    public static String encryptStr(String content,String aesKey,String iv) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");//"算法/模式/补码方式"
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = content.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(aesKey.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return encoder.encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        /*
        try {
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(),AES );
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String content16Str = CommonUtils.completionCodeFor16Bytes(content);
            byte[] encryptedData = cipher.doFinal(content16Str.getBytes(CommonUtils.CODE_TYPE));
            String hexStr = CommonUtils.parseByte2HexStr(encryptedData);
            return hexStr;
        } catch (Exception e) {
            logger.error("AES加密字符串异常",e);
            throw new Exception("AES加密字符串异常",e);
        }

         */
 
    }
    public static String  decryptStr(String content,String aesKey,String iv) throws Exception {
        try {
            byte[] encrypted1 = decoder.decode(content);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(aesKey.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        /*
        try {
            byte[] bytes = CommonUtils.parseHexStr2Byte(content);
            SecretKeySpec key = new SecretKeySpec(
                    aesKey.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(bytes);
            String result=new String(decryptedData, CommonUtils.CODE_TYPE);
            String orgResult = CommonUtils.resumeCodeOf16Bytes(result);
            return orgResult;
        } catch (Exception e) {
            logger.error("AES解密字符串异常",e);
            throw new Exception("AES解密字符串异常",e);
        }

         */
    }
}
