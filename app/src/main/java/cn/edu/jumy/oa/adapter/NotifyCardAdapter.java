package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Node;

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
public class NotifyCardAdapter extends CommonAdapter<Node>{
    public NotifyCardAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Node cardData) {
        holder.setText(R.id.subtitle,cardData.getTitle());
        holder.setText(R.id.supportingText,cardData.getContent());
        ((CardView)holder.getView(R.id.cardView)).setCardBackgroundColor(Color.parseColor("#30DDDDDD"));
    }
}
