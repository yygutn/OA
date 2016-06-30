package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jumy on 16/6/29 15:22.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class Doc extends DataSupport{
    @JsonField
    private String id;
    /**
     * 公文接收单位
     */
    @JsonField
    private String department;
    /**
     * 公文接收人员
     */
    @JsonField
    private String personnel;

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
     * 公文摘要
     */
    @JsonField
    private String docSummary;

    /**
     * 签收人数
     */
    @JsonField
    private Integer signNum;
    /**
     * 签收状态(0:是 1:否)
     */
    @JsonField
    private Integer signStatus;
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
