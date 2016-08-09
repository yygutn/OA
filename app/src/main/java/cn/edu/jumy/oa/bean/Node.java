package cn.edu.jumy.oa.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jumy on 16/6/20 15:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Node implements Serializable {
    /**
     * 类型 0-会议 1-公文 2－公告
     */

    public int type = 0;

    public String id = "";//主键
    public String name = "";
    /**
     * 创建人员
     * --发文单位
     */
    public String dispatchUnit = "";
    /**
     * 发文时间
     */
    public String dispatchTime = "";
    /**
     * 承办单位
     */
    public String undertakingUnit = "";
    /**
     * 会议时间
     */
    public String meetingTime = "";
    /**
     * 会议地点
     */
    public String meetingLocation = "";
    /**
     * 文件文号
     */
    public String documentNumber = "";
    /**
     * 标题
     */
    public String title = "";
    /**
     * 内容-标题(eg:经XX研究决定:)
     */
    public String contentHead = "";
    /**
     * 内容--正文摘要
     */
    public String content = "";
    /**
     * 接收单位
     */

    public String department = "";
    /**
     * 接收人员
     */

    public String personnel = "";
    /**
     * 附件
     */

    public List<Attachment> attachmentList;
    /**
     * 联系人姓名
     */

    public String contactName = "";
    /**
     * 联系人电话
     */

    public String contactPhone = "";
    /**
     * 等级:(1.特急2.加急3.平急4.特提)
     */

    public Integer level = 0;
    /**
     * 已签收人员
     */
    public String issuer = "";//签发人
    /**
     * 签收人数
     */

    public Integer signNum = 0;
    public String other = "";// 其他
    /**
     * 签收状态(0:是 1:否)
     */

    public Integer signStatus = 1;

    /**
     * 创建人员
     */

    public String cuid = "";
    /**
     * 修改人员
     */
    public String uuid = "";
    public String tid = "";
    public String summary = "";
    public String remark = "";

    public Node() {

    }
    public Node(Notify notify){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.type = 2;
        this.id = notify.id;
        this.title = notify.title;
        this.department = notify.department;
        this.content = notify.summary;
        this.summary = notify.summary;
        this.cuid = notify.cuid;
        this.tid = notify.tid;
        this.dispatchUnit = notify.departmentName;
        this.dispatchTime = sdf.format(new Date(notify.createTime));
    }
    public Node(Doc doc) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.type = 1;
        this.attachmentList = doc.attachmentList;
        this.id = doc.id;
        this.level = doc.level;
        this.content = doc.docSummary;
        this.department = doc.departmentInfo;
        this.documentNumber = doc.docNo;
        this.attachmentList = doc.attachmentList;
        this.title = doc.docTitle;
        this.dispatchTime = sdf.format(new Date(doc.createTime));
        this.tid = doc.tid;
        this.signStatus = doc.signStatus;
        this.cuid = doc.cuid;
        this.uuid = doc.uuid;
        this.dispatchUnit = doc.sendDepartmentInfo;
    }

    public Node(Meet meet) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.contactName = meet.contactName;
        this.type = 0;
        this.attachmentList = meet.attachmentList;
        this.id = meet.id;
        this.meetingLocation = meet.addr;
        this.meetingTime = sdf.format(new Date(meet.meetTime));
        this.undertakingUnit = meet.meetCompanyName;
        this.dispatchTime = sdf.format(new Date(meet.createTime));
        this.dispatchUnit = meet.sendDepartmentInfo;
        this.department = meet.departmentInfo;
        this.level = meet.level;
        this.content = meet.docSummary;
        this.signStatus = meet.signStatus;
        this.cuid = meet.cuid;
        this.uuid = meet.uuid;
        this.title = meet.docTitle;
        this.tid = meet.tid;
        this.name = meet.name;
    }

    public Node(Relay node) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.contactName = node.contactName;
        this.type = node.type == 2 ? 0 : 1;
        this.attachmentList = node.attachmentList;
        this.id = node.id;
        this.meetingLocation = node.addr;
        this.meetingTime = sdf.format(new Date(node.meetTime));
        this.undertakingUnit = node.meetCompanyName;
        this.dispatchTime = sdf.format(new Date(node.createTime));
        this.dispatchUnit = node.sendDepartmentInfo;
        this.department = node.departmentInfo;
        this.level = node.level;
        this.content = node.docSummary;
        this.signStatus = node.signStatus;
        this.cuid = node.cuid;
        this.uuid = node.uuid;
        this.title = node.docTitle;
        this.tid = node.tid;
        this.documentNumber = node.docNo;
        this.remark = node.remark;
        this.signNum = node.signNum;
        this.name = node.name;
    }
}
