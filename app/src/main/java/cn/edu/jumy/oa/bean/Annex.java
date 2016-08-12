package cn.edu.jumy.oa.bean;

import com.hyphenate.chat.EMClient;

//import org.apache.commons.io.FileUtils;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.edu.jumy.oa.MyApplication;

/**
 * Created by Jumy on 16/7/21 14:15.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Annex extends DataSupport {
    private String IDS;
    /**
     * 父id 当PID为UID的时候，他是用户头像
     */
    private String pid;
    /**
     * 创建者
     */
    private String cuid;
    /**
     * 1为公文;2为会议;3用户头像
     */
    private Integer type;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件后缀名
     */
    private String suffix;
    @Column(ignore = true)
    private File file;
    private byte byteFile[];

    private String username;

    public Annex(Attachment attachment, File file) {
        this.IDS = attachment.getId();
        this.pid = attachment.getPid();
        this.cuid = attachment.getPid();
        this.type = attachment.getType();
        this.fileName = attachment.getFileName();
        this.suffix = attachment.getSuffix();
        this.username = EMClient.getInstance().getCurrentUser();
        try {
            this.byteFile = File2byte(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Annex() {
    }

    public String getID() {
        return IDS;
    }

    public void setID(String IDS) {
        this.IDS = IDS;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getByteFile() {
        return byteFile;
    }

    public void setByteFile(byte[] byteFile) {
        this.byteFile = byteFile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAnnex(Attachment attachment, File file) {
        this.IDS = attachment.getId();
        this.pid = attachment.getPid();
        this.cuid = attachment.getPid();
        this.type = attachment.getType();
        this.fileName = attachment.getFileName();
        this.suffix = attachment.getSuffix();
        this.username = EMClient.getInstance().getCurrentUser();
        try {
            this.byteFile = File2byte(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getFileByByte(Annex annex) {
        BufferedOutputStream bufferedOutputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            file = new File(MyApplication.getContext().getExternalCacheDir(),annex.fileName);
            fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(annex.getByteFile());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static byte[] File2byte(File file)
    {
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }
}
