package cn.edu.jumy.oa.safe;

import java.io.File;

/**
 * SimpleCipher.java
 * 加密接口
 *
 * @author zlh
 */
public interface SimpleCipher {
    
    /**
     * 加密操作
     * 
     * @param in 待加密的字节数据
     * @return 加密结果字符串
     */
    public String simpleStringEncrypt(byte[] in);

    /**
     * 解密操作
     * 
     * @param in 已加密的字符串
     * @return 解密后数据的字节数组
     */
    public byte[] simpleStringDecrypt(String in);

    /**
     * 设置密钥文件
     * 
     * @param keyString 密钥字串
     */
    public void setKeyString(String keyString);

}
