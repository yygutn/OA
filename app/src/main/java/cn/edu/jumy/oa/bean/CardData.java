package cn.edu.jumy.oa.bean;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;

import java.io.Serializable;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.widget.datepicker.view.ContentItemViewAbs;
import io.realm.RealmObject;

/**
 * Created by Jumy on 16/5/25 15:04.
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
public class CardData extends RealmObject implements Serializable{
    private String title;
    private String subTitle;
    private String message;
    private Boolean isRead;
    private int TAG;

    public static ContentItemViewAbs buildItemView(Context context, final CardData cardData){
        return new ContentItemViewAbs(context,cardData.getTitle(),cardData.getSubTitle(),cardData.getMessage());
    }

    public CardData() {
    }

    public CardData(String title, String subTitle, String message) {
        this.title = title;
        this.subTitle = subTitle;
        this.message = message;
    }

    public CardData(String title, String subTitle, String message, int TAG) {
        this.title = title;
        this.subTitle = subTitle;
        this.message = message;
        this.TAG = TAG;
    }

    public int getTAG() {
        return TAG;
    }

    public void setTAG(int TAG) {
        this.TAG = TAG;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
