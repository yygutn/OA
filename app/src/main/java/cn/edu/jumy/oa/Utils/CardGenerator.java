package cn.edu.jumy.oa.Utils;

import android.support.annotation.NonNull;

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
                    message += "发文单位: " + node.dispatchUnit + "\n";
                    message += "发文时间: " + node.dispatchTime + "\n";
                    message += "承办单位: " + node.undertakingUnit + "\n";
                    message += "会议时间: " + node.meetingTime + "\n";
                    message += "会议地点: " + node.meetingLocation + "\n";
                    break;
                }
                case 1: {//公文
                    message += "发文单位: " + node.dispatchUnit + "\n";
                    message += "发文时间: " + node.dispatchTime + "\n";
                    message += "文件文号: " + node.documentNumber + "\n";
                    break;
                }
                case 2: {//公告
                    message += "接收单位: " + node.dispatchUnit + "\n";
                    message += "发文时间: " + node.dispatchTime + "\n";
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
