package cn.edu.jumy.oa.safe;

import com.orhanobut.logger.Logger;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.crypto.SecretKey;


/**
 * SimpleCipherDES.java
 * 加密实现类
 *
 * @author zlh
 */
public class SimpleCipherDES implements SimpleCipher {

    private SecretKey key = null;

    public SimpleCipherDES() {

    }

    /**
     * 读取密钥文件
     *
     * @param keyString
     */
    public SimpleCipherDES(String keyString) {
        try {
            key = TripleDES.readKey(keyString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    public void setKeyString(String keyString) {
        try {
            key = TripleDES.readKey(keyString);
        } catch (InvalidKeyException e) {
            Logger.e("Cipher set key", e);
        } catch (NoSuchAlgorithmException e) {
            Logger.e("Cipher set key", e);
        } catch (InvalidKeySpecException e) {
            Logger.e("Cipher set key", e);
        } catch (Exception e) {
            Logger.e("Cipher set key", e);
        }

    }

    /**
     * 加密操作
     *
     * @param in 待加密的字节数据
     * @return 加密结果字符串
     */
    public String simpleStringEncrypt(byte[] in) {
        try {
            return new String(Hex.encodeHex(TripleDES.simpleEncrypt(key, in)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密操作
     *
     * @param in 待加密的字节数据
     * @return 加密结果字符串
     */
    public byte[] simpleStringDecrypt(String in) {
        try {
            byte[] bin = Hex.decodeHex(in.toCharArray());
            return TripleDES.simpleDecrypt(key, bin);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public void main2(String[] args) {
//        // some dirty test code for this Encrypt Wrapper Class
//        long time = new Date().getTime();
//        System.out.println("time=" + time);
//        String token = "12851)(*&#0)";
//        System.out.println("token=" + token);
//        byte[] bt = token.getBytes();
//        int l = 8 + bt.length;
//        ByteBuffer bbf = ByteBuffer.allocate(l);
//        bbf.putLong(time);
//        bbf.put(bt);
//        bbf.flip();
//        byte[] in = new byte[bbf.remaining()];
//        bbf.get(in);
//
//        SimpleCipherDES des = new SimpleCipherDES();
//        des.setKeyString("cskc_cisencdis-fesafclcsa");
//        String sen = des.simpleStringEncrypt(in);
//        System.out.println(sen);
//
//        byte[] bd = des.simpleStringDecrypt(sen);
//        bbf = ByteBuffer.allocate(bd.length);
//        bbf.put(bd);
//        bbf.flip();
//        System.out.println("time=" + bbf.getLong());
//        byte[] bs = new byte[bbf.remaining()];
//        bbf.get(bs);
//        String s = new String(bs);
//        System.out.println("token=" + s);
//    }
}
