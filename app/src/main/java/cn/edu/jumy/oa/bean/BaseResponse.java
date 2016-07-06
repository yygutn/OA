package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jumy on 16/7/6 16:32.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class BaseResponse {

    /**
     * msg :
     * code : 0
     * data : [{"id":"1","name":"管理员单位","typeid":null,"pid":"0","sort":null,"level":0,"contact1":null,"contact2":null,"contact3":null,"isdef":0,"isuse":0,"remark":null,"cuid":null,"uuid":null,"createTime":null,"updataTime":null,"orderBy":null,"organizationList":[]},{"id":"2","name":"省委办公厅","typeid":null,"pid":"0","sort":null,"level":0,"contact1":null,"contact2":null,"contact3":null,"isdef":1,"isuse":0,"remark":null,"cuid":"1","uuid":"1","createTime":1467104287000,"updataTime":1467695810000,"orderBy":null,"organizationList":null},{"id":"de8df8b0f7784d47b2ce930cc26f96f7","name":"省信访局","typeid":null,"pid":"0","sort":null,"level":0,"contact1":null,"contact2":null,"contact3":null,"isdef":1,"isuse":0,"remark":null,"cuid":"1","uuid":null,"createTime":1467523584000,"updataTime":null,"orderBy":null,"organizationList":null}]
     */

    private String msg;
    private int code;

    private List<String> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

}
