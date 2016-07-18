package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Meet;

/**
 * Created by Jumy on 16/6/1 17:48.
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
public class MeetingCardAdapter extends CommonAdapter<Meet>{
    public MeetingCardAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Meet node) {
        ((CardView)holder.getView(R.id.cardView)).setCardBackgroundColor(Color.parseColor("#30DDDDDD"));
        holder.setText(R.id.subtitle,node.docTitle);
        String message = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        message += "发文单位: " + node.sendDepartmentInfo + "\n";
        message += "发文时间: " + sdf.format(new Date(node.createTime)) + "\n";
        message += "承办单位: " + node.meetCompanyName + "\n";
        message += "会议时间: " + sdf.format(new Date(node.meetTime)) + "\n";
        message += "会议地点: " + node.addr + "\n";
        holder.setText(R.id.supportingText,message);
    }
    public void setList(ArrayList<Meet> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }
}
