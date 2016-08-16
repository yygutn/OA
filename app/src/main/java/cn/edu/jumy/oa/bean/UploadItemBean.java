package cn.edu.jumy.oa.bean;

public class UploadItemBean {
    public String fileName = "";
    public String filePath = "";

    public UploadItemBean(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public UploadItemBean() {
    }
}