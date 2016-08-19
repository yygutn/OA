package cn.edu.jumy.oa.Utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.SimpleDateFormat;

import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/5/24 15:03.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class CardGenerator {

    public static String getContentString(@NonNull Node node) {
        String message = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            switch (node.type) {
                case 0: {//会议
                    message += "发文单位: " + (TextUtils.isEmpty(node.dispatchUnit)?"":node.dispatchUnit) + "\n";
                    message += "发布时间: " + (TextUtils.isEmpty(node.dispatchTime)?"":node.dispatchTime) + "\n";
                    message += "承办单位: " + (TextUtils.isEmpty(node.undertakingUnit)?"":node.undertakingUnit) + "\n";
                    message += "会议时间: " + (TextUtils.isEmpty(node.meetingTime)?"":node.meetingTime) + "\n";
                    message += "发文编号: " + (TextUtils.isEmpty(node.documentNumber)?"":node.documentNumber) + "\n";
                    message += "会议地点: " + (TextUtils.isEmpty(node.meetingLocation)?"":node.meetingLocation);
                    break;
                }
                case 1: {//公文
                    message += "发文单位: " + (TextUtils.isEmpty(node.dispatchUnit)?"":node.dispatchUnit) + "\n";
                    message += "发布时间: " + (TextUtils.isEmpty(node.dispatchTime)?"":node.dispatchTime) + "\n";
                    message += "文件文号: " + (TextUtils.isEmpty(node.documentNumber)?"":node.documentNumber);
                    break;
                }
                case 2: {//公告
                    message += "接收单位: " + (TextUtils.isEmpty(node.dispatchUnit)?"":node.dispatchUnit) + "\n";
                    message += "发布时间: " + (TextUtils.isEmpty(node.dispatchTime)?"":node.dispatchTime);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    public static String getZFContentString(@NonNull Node node) {
        String message = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            switch (node.type) {
                case 0: {//会议
                    message += "发文单位: " + (TextUtils.isEmpty(node.dispatchUnit)?"":node.oldSendDepartment) + "\n";
                    message += "发布时间: " + (TextUtils.isEmpty(node.dispatchTime)?"":node.dispatchTime) + "\n";
                    message += "承办单位: " + (TextUtils.isEmpty(node.undertakingUnit)?"":node.undertakingUnit) + "\n";
                    message += "会议时间: " + (TextUtils.isEmpty(node.meetingTime)?"":node.meetingTime) + "\n";
                    message += "发文编号: " + (TextUtils.isEmpty(node.documentNumber)?"":node.documentNumber) + "\n";
                    message += "会议地点: " + (TextUtils.isEmpty(node.meetingLocation)?"":node.meetingLocation);
                    break;
                }
                case 1: {//公文
                    message += "发文单位: " + (TextUtils.isEmpty(node.dispatchUnit)?"":node.oldSendDepartment) + "\n";
                    message += "发布时间: " + (TextUtils.isEmpty(node.dispatchTime)?"":node.dispatchTime) + "\n";
                    message += "文件文号: " + (TextUtils.isEmpty(node.documentNumber)?"":node.documentNumber);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
