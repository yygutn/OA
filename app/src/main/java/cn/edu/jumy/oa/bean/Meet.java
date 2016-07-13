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
    
    public String id;
    /**
     * 会议名称
     */
    
    public String name;
    /**
     * 会议时间
     */
    
    public long meetTime;

    /**
     * 会议承办单位ID
     */
    
    public String meetCompany;
    /**
     * 接收单位
     */
    
    public String department;
    /**
     * 等级(1:特急 2:加急 3:平急 4:特提)
     */
    
    public Integer level;
    /**
     * 编号
     */
    
    public String docNo;
    /**
     * 标题
     */
    
    public String docTitle;
    /**
     * 摘要
     */
    public String docSummary;
    /**
     * 联系人姓名
     */
    
    public String contactName;
    /**
     * 联系人电话
     */
    
    public String contactPhone;
    /**
     * 会议地址
     */
    
    public String addr;
    /**
     * 签收人数
     */

    public Integer signNum;
    /**
     * 签收状态(0:是 1:否)
     */

    public Integer signStatus;
    /**
     * 签收(0:是  1:否)
     */
    
    public Integer isuse;
    /**
     * 备注
     */
    
    public String remark;
    /**
     * 创建人员
     */
    
    public String cuid;
    /**
     * 修改人员
     */
    
    public String uuid;

    // 额外属性

    public String orderBy;
    /**
     * 发文时间
     */
    public long createTime;
    /**
     * 附件
     */
    public List<Attachment> attachmentList = new ArrayList<>();
    /**
     * 会议时间-转化
     */
    public String meetTimeString;
    /**
     * 发送单位名称-self
     */
    public String sendDepartmentInfo;
    /**
     * 接收单位多个以,隔开
     */
    public String departmentInfo;
    /**
     * 会议承办单位名称
     */
    public String meetCompanyName;
    /**
     * tid 任务ID
     */
    public String tid;
    /**
     * didtask
     */
    public String didtask;
}
