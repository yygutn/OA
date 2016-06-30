package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jumy on 16/6/29 15:24.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class Meet extends DataSupport{
    @JsonField
    private String id;
    /**
     * 会议名称
     */
    @JsonField
    private String name;
    /**
     * 会议时间
     */
    @JsonField
    private Date meetTime;

    /**
     * 会议承办单位
     */
    @JsonField
    private String meetCompany;
    /**
     * 接收单位
     */
    @JsonField
    private String department;
    /**
     * 公文等级(1:特急 2:加急 3:平急 4:特提)
     */
    @JsonField
    private Integer level;
    /**
     * 公文编号
     */
    @JsonField
    private String docNo;
    /**
     * 公文标题
     */
    @JsonField
    private String docTitle;
    /**
     * 联系人姓名
     */
    @JsonField
    private String contactName;
    /**
     * 联系人电话
     */
    @JsonField
    private String contactPhone;
    /**
     * 会议地址
     */
    @JsonField
    private String addr;
    /**
     * 签收(0:是  1:否)
     */
    @JsonField
    private String isshow;
    /**
     * 是否启用(0:是  1:否)
     */
    @JsonField
    private Integer isuse;
    /**
     * 备注
     */
    @JsonField
    private String remark;
    /**
     * 创建人员
     */
    @JsonField
    private String cuid;
    /**
     * 修改人员
     */
    @JsonField
    private String uuid;

    // 额外属性

    private String orderBy;
    /**
     * 附件
     */
    private List<Attachment> attachmentList = new ArrayList<>();

}
