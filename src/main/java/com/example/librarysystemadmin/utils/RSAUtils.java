package com.example.librarysystemadmin.utils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author springdoc.cn
 * 生成 RSA 密钥对
 */
public class RSAUtils {

    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private final static int KEY_SIZE = 1024;

    /**
     * 用于封装随机产生的公钥与私钥
     */
    public static Map<String, String> keyMap = new HashMap<>();

    /**
     * 公钥
     */
    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7oYsxOhouLWdA28n7w4jxWlHkVnITwnb4ptFm4i0NYHs2mwpI7ZCfQXmNk3XMf2ZZ+gMTbb3L0AJeFlKTxCDp/Q91gdOFsN9hYeVGN4UhnsRHcz/gQeRFPJhCHkQr90FtoIwuV1rcWrtYh2KB+S40LAUGBjAzEKWscPVpqzwyweRB7tYTnuYFw7qDVQCgUGYzp6LyxLS14SVblKkR6vsUR09D7w27JjJUKEuTP91CEv1xanMPKjEAad9oDOvoDbGa/8DzR2dGVZikOtSA5A+QH2YZZJYrkYcviAbngha0IE+NiVrg6K+fTvRHGNmE9LsDTm5F1ZpLpqupQLE5cKEJwIDAQAB";

    /**
     * 私钥
     */
    public static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDuhizE6Gi4tZ0DbyfvDiPFaUeRWchPCdvim0WbiLQ1gezabCkjtkJ9BeY2Tdcx/Zln6AxNtvcvQAl4WUpPEIOn9D3WB04Ww32Fh5UY3hSGexEdzP+BB5EU8mEIeRCv3QW2gjC5XWtxau1iHYoH5LjQsBQYGMDMQpaxw9WmrPDLB5EHu1hOe5gXDuoNVAKBQZjOnovLEtLXhJVuUqRHq+xRHT0PvDbsmMlQoS5M/3UIS/XFqcw8qMQBp32gM6+gNsZr/wPNHZ0ZVmKQ61IDkD5AfZhlkliuRhy+IBueCFrQgT42JWuDor59O9EcY2YT0uwNObkXVmkumq6lAsTlwoQnAgMBAAECggEAIJEJQ8lHoxCI45MlrKoNkEKTlvDfPItoDkSM+HNtx3B52kiyRUH5SgCoMfnmy9iIPXudUm8MyNLBeEEYuDrU/vWGC9brfogqdzTP0plfzAy5hYwbxo417No7DWEaOii/Qu/7nxN2PAIRbzgBRJqh8TxZQgD/MeRVlufaH+u05MNcNvPPv+yS2K/ll+bR4mKeFBWDVO/garA8zT42NV+d5l+N+n3zpus3IoAE6KsEUihdFCQolwQrnIbGQOPZZGPBGHxdfBGNqF/MOi8tUZ48RCFN3eG/BRPUFEfgAaAqp9VFCwfIglOTvLObx7d0DQ7g23DlCHRdJ/iPwYaX1fe/gQKBgQDxe6FmdtryLPVehMXu7298jrfrw5yvdccLArBSxdSeFOSnQ5EZF5O2uok2BsOY0OkwVvNolDuxNQfw9N1SZaWXkzXxU3uHRiq8jKcUoa3OElQkclXbAGP6gOO8nkgME4h3Eoya13bBW4atqgIQ8maKV/7wiUJkEN9h/V7DYUiDtwKBgQD83QJK/3Frg7Z9dpOEtx0N+10WD2Fi0nGup74ki2bVN197/X5d5J2s6lhNKx0piyK1DaH/jCNMr3zLgYHJI02fGsZiOghYJPPmRlCdB9Vz454CVeTODI6stQ4D3NelI715MpCWtMCWA6D7wXq+oPBjs5PNqoiWVGJjXlUeVw5jEQKBgCCPwBq16h3/q/JbyujGBm1wPSKgVM9f/T/h0+7FWD8cqBxZwwX0JOndy0SqstM3UEpBXz75xfbGJNT736ANrr96jSqGTEDYWEjmWl0w0+PWmtvzQtpt+g08NfkoCqT+OUd9C9KRPSUjd0yRgQfR97ut0+WRTI97qvNmm9GSm5mPAoGBAMNQ+3puwW8ouAdFVkFHJ/wq0wY9Lx4kK4ebLjYQL4g2U1z0u21PL7Pm9/nzeX0jadWsCBvaZVHQo3aWPGffCxAAoIgzB0r+kB6o5Y/GA0yW3vAy5J9796DT4VV+elumta4uLp0aVUAK4YhVNLiWm9iHV32fYg2sdKsIrHrZB9XhAoGBAI/2Zc0s4m5q5CuRnNzOLmO76WMFRTBLWf2EViAz1C3auGz+JGyG1eMnhEOhlNifDNBsF/oba6tgr3qMmM3g6nH1aLHmCvmtZndn0wQk6YvkwyYpMfjJj4ZTM9oYqXsSPpNx5UUkTULXFdldSQXhHkxm2zxVZ3bBwC4yiv2VWDBg";


    /**
     * 随机生成密钥对
     *
     * @throws Exception 异常
     */

    private static void generateKeyPair() throws NoSuchAlgorithmException {
        // 初始化 Key 生成器，指定算法类型为 RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // 密钥长度为 2048 位
        keyPairGenerator.initialize(2048);
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 将密钥存储在 Map 中
        keyMap.put("public", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        keyMap.put("private", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }

    /**
     * RSA 公钥加密
     *
     * @param str 加密字符串
     * @return 密文
     */
    public static String encrypt(String str) {
        try {
            // base64编码的公钥
            byte[] decoded = Base64.getDecoder().decode(publicKey);

            // 获取公钥
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decoded));
            // RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptedBytes = cipher.doFinal(str.getBytes("UTF-8"));

            // 使用字符集编码
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RSA 私钥解密
     *
     * @param str 加密字符串
     * @return 明文
     */
    public static String decrypt(String str) {
        try {
            if (str.length() < 2) return null;

            // 64位解码加密后的字符串
            byte[] inputByte = Base64.getDecoder().decode(str);

            // base64编码的私钥
            byte[] decoded = Base64.getDecoder().decode(privateKey);

            // 获取私钥
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey priKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoded));

            // RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] decryptedBytes = cipher.doFinal(inputByte);

            // 使用字符集解码
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(30);
            sb.append(str.charAt(number));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        try {
            // 生成公钥和私钥
            generateKeyPair();
            System.out.println("公钥: " + keyMap.get("public"));
            System.out.println("私钥: " + keyMap.get("private"));

            // 待加密的字符串
            String originalString = "Hello, World!";

            // 使用公钥加密
            String encryptedString = RSAUtils.encrypt(originalString);
            System.out.println("Encrypted String: " + encryptedString);

            // 使用私钥解密
            String decryptedString = RSAUtils.decrypt(encryptedString);
            System.out.println("Decrypted String: " + decryptedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}