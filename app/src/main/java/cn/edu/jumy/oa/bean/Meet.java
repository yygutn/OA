package cn.edu.jumy.oa.bean;

import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jumy on 16/6/29 15:24.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Meet extends DataSupport{
    
    private String id;
    /**
     * 会议名称
     */
    
    private String name;
    /**
     * 会议时间
     */
    
    private Date meetTime;

    /**
     * 会议承办单位
     */
    
    private String meetCompany;
    /**
     * 接收单位
     */
    
    private String department;
    /**
     * 公文等级(1:特急 2:加急 3:平急 4:特提)
     */
    
    private Integer level;
    /**
     * 公文编号
     */
    
    private String docNo;
    /**
     * 公文标题
     */
    
    private String docTitle;
    /**
     * 联系人姓名
     */
    
    private String contactName;
    /**
     * 联系人电话
     */
    
    private String contactPhone;
    /**
     * 会议地址
     */
    
    private String addr;
    /**
     * 签收(0:是  1:否)
     */
    
    private String isshow;
    /**
     * 是否启用(0:是  1:否)
     */
    
    private Integer isuse;
    /**
     * 备注
     */
    
    private String remark;
    /**
     * 创建人员
     */
    
    private String cuid;
    /**
     * 修改人员
     */
    
    private String uuid;

    // 额外属性

    private String orderBy;
    /**
     * 附件
     */
    private List<Attachment> attachmentList = new ArrayList<>();

}
