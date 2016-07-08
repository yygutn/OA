package cn.edu.jumy.oa.Utils;

import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/5/24 15:03.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * *****************************************************
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #                佛祖保佑         永无BUG
 * #                                                   #
 * *****************************************************
 */
public class CardGenerator {
    public static String generateNotifyString(int tag, SparseArray<String> list) {
        String message = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = sdf.format(new Date());
        if (tag == 0) {
            message += "发文单位: " + list.get(0, "省办公厅") + "\n";
            message += "发文时间: " + list.get(1, time) + "\n";
            message += "承办单位: " + list.get(2, "省办公厅") + "\n";
            message += "会议时间: " + list.get(3, time) + "\n";
            message += "会议地点: " + list.get(4, "省办公厅11楼" + 110 + new Random().nextInt(9)) + "\n";
//            message += "附件： " + list.get(5,"");
        } else if (tag == 1) {
            message += "发文单位: " + list.get(0, "浙江省人民代表大会常务委员会") + "\n";
            message += "发文时间: " + list.get(1, time) + "\n";
//            message += "附件： " + list.get(2,"");
        } else if (tag == 2) {
            message += "发文单位: " + list.get(0, "浙江省人民政府") + "\n";
            message += "发文时间: " + list.get(1, time) + "\n";
            message += "文件文号: " + list.get(2, "浙政函〔2016〕63号") + "\n";
//            message += "附件： " + list.get(3,"");
        }
        return message;
    }

    public static Node getStringFromArray(List<String> strings,int type) {
        String ret = "";
        for (int i = 1; i < strings.size() - 1; i++) {
            ret += strings.get(i) + "\n";
        }
        return new Node(strings.get(0), ret, type);
    }
}
