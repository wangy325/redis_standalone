package com.zc.redis.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/5 / 21:00
 */
public class AesUtil {
    private static final String AES_CBC = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";
    private static final String SKEY = "MTIzNDU2Nzg5MDEyMzQ1Ng==";
    private static final String IV = "MTIzNDU2Nzg5MDk4NzY1NA==";

    public static String encrypt(String encData) throws Exception {
        return encrypt(encData, Base64.getDecoder().decode(SKEY), Base64.getDecoder().decode(IV));
    }

    /**
     * 加密
     *
     * @param encData 加密数据
     * @param aesKey  密钥
     * @param aesIv   偏移向量
     * @return 加密后的字符串
     */
    public static String encrypt(String encData, byte[] aesKey, byte[] aesIv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(aesKey, AES);
        IvParameterSpec iv = new IvParameterSpec(aesIv);
        Cipher cipher = Cipher.getInstance(AES_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes(StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(encrypted);

    }


    public static String decrypt(String decData) throws Exception {
        return decrypt(decData, Base64.getDecoder().decode(SKEY), Base64.getDecoder().decode(IV));
    }

    /**
     * 解密
     *
     * @param decData 解密数据
     * @param aesKey  密钥
     * @param aesIv   偏移向量
     * @return 解密后的字符串
     */
    public static String decrypt(String decData, byte[] aesKey, byte[] aesIv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(aesKey, AES);
        IvParameterSpec iv = new IvParameterSpec(aesIv);
        Cipher cipher = Cipher.getInstance(AES_CBC);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(decData));
        return new String(original, StandardCharsets.UTF_8);

    }
}