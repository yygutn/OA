package cn.edu.jumy.oa.bean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jumy on 16/6/29 15:22.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Doc extends DataSupport{
    
    private String id;
    /**
     * 公文接收单位
     */
    
    private String department;
    /**
     * 公文接收人员
     */
    
    private String personnel;

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
     * 公文摘要
     */
    
    private String docSummary;

    /**
     * 签收人数
     */
    
    private Integer signNum;
    /**
     * 签收状态(0:是 1:否)
     */
    
    private Integer signStatus;
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

    /**
     * 已签收人员
     */
    private String signUid;

    private String orderBy;
    /**
     * 附件
     */
    private List<Attachment> attachmentList = new ArrayList<>();
}
