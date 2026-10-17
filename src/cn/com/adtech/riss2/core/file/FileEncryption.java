package cn.com.adtech.riss2.core.file;


import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA生成私钥公钥加解密文件工具类
 * @author MaChao
 */
public class FileEncryption {

	public static KeyPairGenerator keyPairGen;
	public static KeyPair keyPair;
	public static RSAPrivateKey privateKey;
	public static RSAPublicKey publicKey;
    static{
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(512);
            // 生成一个密钥对，保存在keyPair中
            keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            publicKey = (RSAPublicKey) keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密文件
     * @param file 原始文件
     * @param newFile 加密后文件
     */
    public static void encryptFile(File file, File newFile) {
        try {
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(newFile);
            byte[] bytes = new byte[53];
            while (is.read(bytes) > 0) {
                byte[] e = encrypt(FileEncryption.publicKey, bytes);
                bytes = new byte[53];
                os.write(e, 0, e.length);
            }
            os.close();
            is.close();
            System.out.println("write success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密文件
     * @param file 加密文件
     * @param newFile 解密后文件
     */
    public static void decryptFile(File file, File newFile) {
        try {
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(newFile);
            byte[] bytes = new byte[64];
            while (is.read(bytes) > 0) {
                byte[] de = decrypt(FileEncryption.privateKey, bytes);
                bytes = new byte[64];
                os.write(de, 0, de.length);
            }
            os.close();
            is.close();
            System.out.println("write success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据公钥加密文件
     * @param publicKey 公钥
     * @param obj 加密内容
     */
    protected static byte[] encrypt(RSAPublicKey publicKey, byte[] obj) {
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据私钥解密文件
     * @param privateKey 私钥
     * @param obj 解密内容
     */
    protected static byte[] decrypt(RSAPrivateKey privateKey, byte[] obj) {
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    
    public static void main(String[] args) {
		decryptFile(new File("D:/Downloads/1608875987815_data.pdf"), new File("D:/Downloads/1608875987815_data_jiemi.pdf"));
	}

}
